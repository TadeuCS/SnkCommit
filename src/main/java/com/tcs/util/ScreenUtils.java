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
import javafx.stage.Screen;

/**
 *
 * @author Tadeu-PC
 */
public class ScreenUtils {

    public Screen screen;
    public Parent root;

    public void toHome() {
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/HomeFXML.fxml"));
            setUserAgentStylesheet(STYLESHEET_MODENA);
            SessionUtils.stage.setTitle("Sankhya - Easy Commit v" + SessionUtils.version+"");
            SessionUtils.stage.setResizable(false);
            SessionUtils.stage.getIcons().add(
                    new Image("/img/logosankhya-only.png"));
            SessionUtils.stage.setScene(new Scene(root));
            SessionUtils.stage.show();
        } catch (IOException e) {
            MessageUtils.exceptionDialog("Erro ao abrir a Tela Principal!", e);
            e.printStackTrace();
        }
    }
}
