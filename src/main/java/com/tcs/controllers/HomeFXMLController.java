/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.controllers;

import com.tcs.pojo.ParamettersPojo;
import com.tcs.util.GitUtils;
import com.tcs.util.MessageUtils;
import com.tcs.util.OUtils;
import com.tcs.util.SessionUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.Project;
import org.ini4j.Ini;
import org.ini4j.Profile;

/**
 * FXML Controller class
 *
 * @author Tadeu-PC
 */
public class HomeFXMLController implements Initializable {

    @FXML
    private TextField iptBranch;
    @FXML
    private TextField iptAuthor;
    @FXML
    private TextArea iptLog;
    @FXML
    private TextField iptDays;

    private String branchName;
    private String days;
    private String author;

    private Thread searchCommitThread;

    private boolean runThread;

    private LinkedHashSet<Branch> branchs;
    private LinkedHashSet<Commit> commits;
    private LinkedHashSet<String> listImplementations;
    private LinkedHashSet<String> listFiles;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initVariables();
        setDefaultPatternToField();
        initParammeters();
        connectToGit();
    }

    @FXML
    private void searchCommits(ActionEvent event) {
        validateParametters();
    }

    private void setDefaultPatternToField() {
        OUtils.onlyDigitsValue(iptDays, 2);
        OUtils.fieldToLOWER(iptBranch);
        OUtils.fieldToLOWER(iptAuthor);
    }

    private void validateParametters() {
        if (iptBranch.getText().trim().length() < 7) {
            MessageUtils.alertDialog("Informe um número de BRANCH válido!");
            iptBranch.requestFocus();
            return;
        }

        if (iptDays.getText().trim().isEmpty()) {
            MessageUtils.alertDialog("Informe a quantidade de DIAS dos COMMITs!");
            iptDays.requestFocus();
            return;
        }
        branchName = iptBranch.getText().trim();
        days = iptDays.getText().trim();
        author = iptAuthor.getText().trim();
        search();
    }

    private void search() {
        initVariables();
        if (searchCommitThread == null) {
            createThread();
            runThread = true;
            System.out.println("Thread Iniciada!");
            searchCommitThread.start();
        } else {
            runThread = true;
            System.out.println("Thread reiniciada...");
            searchCommitThread.resume();
        }
    }

    private void createThread() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (runThread) {
                    try {
                        StringBuilder textLog = new StringBuilder();
                        System.out.println("Verificando Commits...");
                        updateMessage("Verificando Commits...");
                        Project project = buildCommits(SessionUtils.git);
                        if (commits.isEmpty()) {
                            Platform.runLater(() -> {
                                MessageUtils.alertDialog("Nenhum commit encontrado!");
                                threadSuspend("");
                                iptDays.requestFocus();
                            });
                        } else {
                            processCommits(project);
                            buildLog(textLog);
                            Platform.runLater(() -> {
                                threadSuspend(textLog.toString());
                            });
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            MessageUtils.errorDialog(e.getMessage());
                            threadSuspend("");
                            e.printStackTrace();
                        });
                    }
                    Thread.sleep(1000);
                }
                return null;
            }

            private void threadSuspend(String text) {
                runThread = false;
                searchCommitThread.suspend();
                updateMessage(text);
                System.out.println("Thread Interrompida...");
            }
        };
        searchCommitThread = new Thread(task);
        searchCommitThread.setName(
                "Thread de Busca dos Commits");
        searchCommitThread.setDaemon(true);
        runThread = true;
        iptLog.textProperty().bind(task.messageProperty());
        onCloseScreen();
    }

    private void processCommits(Project project) throws GitLabApiException {
        for (Commit commit : commits) {
            listImplementations.add(commit.getMessage());
            for (Diff diff : SessionUtils.git.gitLabApi.getCommitsApi().getDiff(project.getId(), commit.getId())) {
                listFiles.add(diff.getOldPath());
            }
        }
    }

    private Project buildCommits(GitUtils git) throws Exception {
        Project project = SessionUtils.git.getProject(SessionUtils.parametters.getProjectName());
        branchs.addAll(git.listBranchs(project, branchName));
        for (Branch branch : branchs) {
            List<Commit> commitsByBranch = git.listCommits(project, branch.getName(), OUtils.addDays(-Integer.parseInt(days), new Date()), author);
            commits.addAll(commitsByBranch);
        }
        return project;
    }

    private void buildLog(StringBuilder textLog) {
        textLog.append("### Tipo de Retorno").append("\n");
        textLog.append("Implementação").append("\n\n");
        textLog.append("### Problema/Solução").append("\n");
        for (String implementation : listImplementations) {
            textLog.append(implementation);
        }
        textLog.append("\n");
        for (Branch branch : branchs) {
            textLog.append("### Implementado na BRANCH: [").append(branch.getName()).append("]\n");
        }
        textLog.append("\n");
        textLog.append("### Fontes Alterados\n");
        for (String filePath : listFiles) {
            textLog.append(filePath).append("\n");
        }
    }

    private void onCloseScreen() {
        SessionUtils.stage.
                setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        runThread = false;
                        searchCommitThread.interrupt();
                        System.gc();
                    }
                });
    }

    private void initParammeters() {
        File paramettersFile = new File("snk-easy-commit.ini");
        try {
            if (!paramettersFile.exists()) {
                paramettersFile.createNewFile();
            }
            SessionUtils.ini = new Ini(paramettersFile);
            if (SessionUtils.ini.isEmpty()) {
                throw new Exception();
            }
            SessionUtils.parametters = new ParamettersPojo(SessionUtils.ini);
        } catch (Exception e) {
            e.printStackTrace();
            createIniFile(paramettersFile);
            MessageUtils.errorDialog("Não foi possível carregar os parâmetros do arquivo: " + paramettersFile.getAbsolutePath());
            System.exit(0);
        }
    }

    private void createIniFile(File paramettersFile) {
        Profile.Section section = SessionUtils.ini.add("GITLAB");
        section.put("URL", "https://git.sankhya.com.br");
        section.put("PROJECT_NAME", "Sankhyaw");
        section.put("USERNAME", "");
        section.put("PASSWORD", "");
        try {
            SessionUtils.ini.store();
        } catch (IOException ex) {
            MessageUtils.errorDialog("Erro ao gravar o arquivo INI: " + paramettersFile.getAbsolutePath());
            ex.printStackTrace();
        }
    }

    private void connectToGit() {
        try {
            SessionUtils.git = new GitUtils();
        } catch (Exception e) {
            MessageUtils.errorDialog(e.getMessage());
        }
    }

    private void initVariables() {
        branchs = new LinkedHashSet<>();
        commits = new LinkedHashSet<>();
        listImplementations = new LinkedHashSet<>();
        listFiles = new LinkedHashSet<>();
    }

}
