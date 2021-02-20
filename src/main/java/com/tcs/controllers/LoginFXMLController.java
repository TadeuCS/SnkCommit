/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.controllers;

import com.tcs.util.OUtils;
import com.tcs.util.SessionUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Tadeu-PC
 */
public class LoginFXMLController implements Initializable {

    @FXML
    private TextField iptUserName;
    @FXML
    private PasswordField iptPassword;
    @FXML
    private CheckBox chxRememberMe;
    @FXML
    private Button btnSignIn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        OUtils.fieldToLOWER(iptUserName);
        btnSignIn.disableProperty().bind(iptUserName.textProperty().isEmpty().or(iptPassword.textProperty().isEmpty()));
    }    

    @FXML
    private void signIn(ActionEvent event) {
        try {
            putParammetersInSession();
            SessionUtils.getInstance().gitUtils.auth();
            SessionUtils.getInstance().screenUtils.toHomePage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putParammetersInSession() throws Exception {
        SessionUtils.getInstance().parameters.setUserName(iptUserName.getText().trim());
        SessionUtils.getInstance().parameters.setPassword(iptPassword.getText());
        if(chxRememberMe.isSelected()){
            SessionUtils.getInstance().storeParammetersInIniFile();
        }
    }

    
}
