/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.controllers;

import com.tcs.pojo.BranchPojo;
import com.tcs.pojo.CommitPojo;
import com.tcs.util.MessageUtils;
import com.tcs.util.OUtils;
import com.tcs.util.SessionUtils;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

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

    private Thread searchCommitThread;

    private BooleanProperty runThread;
    @FXML
    private Button btnSearch;
    @FXML
    private ProgressIndicator progressIndicator;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDefaultPatternToField();
        runThread=new SimpleBooleanProperty(false);
        btnSearch.disableProperty().bind(
                iptBranch.textProperty().isEmpty().or(
                        iptDays.textProperty().isEmpty()).or(
                                runThread.not().not()
                        )
                );
        progressIndicator.visibleProperty().bind(runThread);
    }

    @FXML
    private void searchCommits(ActionEvent event) {
        validateParametters();
    }

    private void setDefaultPatternToField() {
        OUtils.onlyDigitsValue(iptDays, 2);
    }

    private void validateParametters() {
        if (iptBranch.getText().trim().length() < 5) {
            MessageUtils.alertDialog("Informe um número de BRANCH válido!");
            iptBranch.requestFocus();
            return;
        }

        if (iptDays.getText().trim().isEmpty()) {
            MessageUtils.alertDialog("Informe a quantidade de DIAS dos COMMITs!");
            iptDays.requestFocus();
            return;
        }
        search();
    }

    private void search() {
        if (searchCommitThread == null) {
            createThread();
            runThread.setValue(Boolean.TRUE);
            System.out.println("Thread Iniciada!");
            searchCommitThread.start();
        } else {
            runThread.setValue(Boolean.TRUE);
            System.out.println("Thread reiniciada...");
            searchCommitThread.resume();
        }
    }

    private void createThread() {
    	Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (runThread.getValue()) {
                    try {
                        System.out.println("Verificando Commits...");
                        updateMessage("Verificando Commits...");
                        List<BranchPojo> listBranchs = SessionUtils.getInstance().versionController.listBranchs(SessionUtils.getInstance().parameters.getProjectName(), iptBranch.getText().trim());
                        for (BranchPojo branch : listBranchs) {
                            List<CommitPojo> listCommits = SessionUtils.getInstance().versionController.listCommits(branch, Integer.parseInt(iptDays.getText().trim()), iptAuthor.getText().trim());
                            branch.setCommits(listCommits);
                        }
                        if (listBranchs.stream().map(BranchPojo::getCommits).collect(Collectors.toList()).isEmpty()) {
                            Platform.runLater(() -> {
                                threadSuspend("");
                                iptDays.requestFocus();
                                MessageUtils.alertDialog("Nenhum commit encontrado!");
                            });
                        } else {
                            Platform.runLater(() -> {
                                threadSuspend(buildLog(listBranchs));
                            });
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            e.printStackTrace();
                            threadSuspend("");
                            MessageUtils.errorDialog(e.getMessage());
                        });
                    }
                    Thread.sleep(1000);
                }
                return null;
            }

            private void threadSuspend(String text) {
                runThread.setValue(Boolean.FALSE);
                searchCommitThread.suspend();
                updateMessage(text);
                System.out.println("Thread Interrompida...");
            }
        };
        searchCommitThread = new Thread(task);
        searchCommitThread.setName("Thread List-Commits");
        searchCommitThread.setDaemon(true);
        runThread.setValue(Boolean.TRUE);
        iptLog.textProperty().bind(task.messageProperty());
        onCloseScreen();
    }

    private void onCloseScreen() {
        SessionUtils.getInstance().screenUtils.getStage().
                setOnCloseRequest((WindowEvent event) -> {
                    runThread.setValue(Boolean.FALSE);
                    searchCommitThread.interrupt();
                    System.gc();
                });
    }
    
    private String buildLog(List<BranchPojo> branchs) {
        StringBuilder textLog = new StringBuilder();
        LinkedHashSet<String> branchsNames = new LinkedHashSet<>();
        LinkedHashSet<String> commitMesages = new LinkedHashSet<>();
        LinkedHashSet<String> changedFiles = new LinkedHashSet<>();
        for (BranchPojo branch : branchs) {
            String textStart = "OS: " + getBranchNumber(branch.getName());
            for (CommitPojo commit : branch.getCommits()
                    .stream()
                    .filter(c -> (branch.getName().length()!=3 && branch.getName().length()!=6) ? c.getMessage().startsWith(textStart) : true)
                    .collect(Collectors.toList())) {
                branchsNames.add(branch.getName());
                commitMesages.add(commit.getMessage().replace(textStart, "").trim());
                for (String changedFile : commit.getChangedFiles()) {
                    changedFiles.add(changedFile.trim());
                }
            }
        }

        if (!branchsNames.isEmpty()) {
            textLog.append("### Tipo de Retorno").append("\n");
            textLog.append("Implementação").append("\n\n");
            textLog.append("### Problema/Solução").append("\n");
            String msg = commitMesages.stream().collect(Collectors.joining("\n"));
            textLog.append(msg);
            textLog.append("\n\n");
            for (String branchName : branchsNames) {
                textLog.append("### Implementado na BRANCH: [").append(branchName).append("]\n");
            }
            textLog.append("\n\n");
            textLog.append("### Fontes Alterados\n");
            String filePath = changedFiles.stream().sorted().collect(Collectors.joining("\n"));
            textLog.append(filePath);
        }

        return textLog.toString();
    }

    private String getBranchNumber(String branchName) {
        return branchName.contains("-") ? branchName.substring(0, branchName.indexOf("-")) : branchName;
    }

}
