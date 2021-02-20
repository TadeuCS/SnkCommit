/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.util;

import java.io.IOException;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Tadeu-PC
 */
public class ScreenUtils {
    
    private final Stage stage;

    public ScreenUtils(Stage stage) {
        this.stage=stage;
    }
    
    public Stage getStage(){
        return stage;
    }
    
    public void toHomePage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/HomeFXML.fxml"));
            setUserAgentStylesheet(STYLESHEET_MODENA);
            stage.setTitle("Sankhya - "+SessionUtils.APP_NAME+" v" + SessionUtils.APP_VERSION+"");
            stage.setResizable(false);
            stage.getIcons().clear();
            stage.getIcons().add(
                    new Image("/img/logosankhya-only.png"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            MessageUtils.exceptionDialog("Erro ao abrir a Tela Principal!", e);
            e.printStackTrace();
        }
    }

    public void toSignInPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginFXML.fxml"));
            setUserAgentStylesheet(STYLESHEET_MODENA);
            stage.setTitle("Sign in - GitLab");
            stage.setResizable(false);
            stage.getIcons().clear();
            stage.getIcons().add(
                    new Image("/img/gitlab-only.png"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            MessageUtils.exceptionDialog("Erro ao abrir a Tela de Login!", e);
            e.printStackTrace();
        }
    }
    
    public void closeWindow(Scene scene) {
        try {
            Stage s = (Stage) scene.getWindow();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.exceptionDialog("Erro ao fechar a janela", e);
        }
    }
}
