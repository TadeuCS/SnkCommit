/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.controllers;

import com.tcs.pojo.BranchPojo;
import com.tcs.util.MessageUtils;
import com.tcs.util.OUtils;
import com.tcs.util.SessionUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private boolean runThread;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDefaultPatternToField();
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
        search();
    }

    private void search() {
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
                        System.out.println("Verificando Commits...");
                        updateMessage("Verificando Commits...");
                        SessionUtils.getInstance().versionController.init();
                        SessionUtils.getInstance().versionController.listBranchs(SessionUtils.getInstance().parameters.getProjectName(), iptBranch.getText().trim());
                        for (BranchPojo branch : SessionUtils.getInstance().versionController.getBranchs()) {
                            SessionUtils.getInstance().versionController.listCommits(branch, OUtils.addDays(-Integer.parseInt(iptDays.getText().trim())), iptAuthor.getText().trim());
                        }
                        if (SessionUtils.getInstance().versionController.getCommits().isEmpty()) {
                            Platform.runLater(() -> {
                                MessageUtils.alertDialog("Nenhum commit encontrado!");
                                threadSuspend("");
                                iptDays.requestFocus();
                            });
                        } else {
                            String log = SessionUtils.getInstance().versionController.buildLog();
                            Platform.runLater(() -> {
                                threadSuspend(log);
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
        searchCommitThread.setName("Thread List-Commits");
        searchCommitThread.setDaemon(true);
        runThread = true;
        iptLog.textProperty().bind(task.messageProperty());
        onCloseScreen();
    }

    private void onCloseScreen() {
        SessionUtils.getInstance().screenUtils.getStage().
                setOnCloseRequest((WindowEvent event) -> {
                    runThread = false;
                    searchCommitThread.interrupt();
                    System.gc();
                });
    }
    
}
