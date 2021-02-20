/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.util;

import com.tcs.services.GitLab;
import com.tcs.pojo.ParametterPojo;
import java.io.File;
import org.ini4j.Ini;

/**
 *
 * @author Tadeu-PC
 */
public final class SessionUtils {

    public static final String APP_VERSION = "1.0.5";
    public static final String APP_NAME = "Easy Commit";
    public ParametterPojo parameters;
    public final GitLab gitUtils;
    public ScreenUtils screenUtils;
    private final Ini ini;
    

    private static final SessionUtils instance = new SessionUtils();

    public SessionUtils() {
        gitUtils = new GitLab();
        ini = new Ini();
        try {
            loadParametersByIniFile();
        } catch (Exception e) {
            MessageUtils.errorDialog(e.getMessage());
        }
    }

    public static SessionUtils getInstance() {
        return instance;
    }

    public void loadParametersByIniFile() throws Exception {
        File paramettersFile = new File("snk-easy-commit.ini");
        try {
            if (!paramettersFile.exists()) {
                paramettersFile.createNewFile();
            }
            ini.load(paramettersFile);
            parameters = new ParametterPojo();
            if (!ini.isEmpty()) {
                parameters.setUrl(ini.get("GITLAB", "URL"));
                parameters.setProjectName(ini.get("GITLAB", "PROJECT_NAME"));
                parameters.setUserName(ini.get("GITLAB", "USERNAME"));
                parameters.setPassword(CriptoUtils.decriptografaSenha(ini.get("GITLAB", "PASSWORD")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erro ao carregar os parâmetros do arquivo .INI: " + paramettersFile.getAbsolutePath());
        }
    }

    public void storeParammetersInIniFile() throws Exception {
        File paramettersFile = new File("snk-easy-commit.ini");
        try {
            if (ini.isEmpty()) {
                ini.add("GITLAB");
            }
            ini.add("GITLAB", "URL", parameters.getUrl());
            ini.add("GITLAB", "PROJECT_NAME", parameters.getProjectName());
            ini.add("GITLAB", "USERNAME", parameters.getUserName());
            ini.add("GITLAB", "PASSWORD", CriptoUtils.criptografaSenha(parameters.getPassword()));
            ini.store(paramettersFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erro ao gravar os parâmetros de conexão com GITLAB no arquivo .INI: " + paramettersFile.getAbsolutePath());
        }
    }

}
