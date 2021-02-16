/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.pojo;

import org.ini4j.Ini;

/**
 *
 * @author Tadeu-PC
 */
public class ParamettersPojo {
    private String userName;
    private String password;
    private String url;
    private String projectName;

    public ParamettersPojo() {
    }
    
    public ParamettersPojo(Ini ini) {
        this.url=ini.get("GITLAB", "URL");
        this.userName=ini.get("GITLAB", "USERNAME");
        this.password=ini.get("GITLAB", "PASSWORD");
        this.projectName=ini.get("GITLAB", "PROJECT_NAME");
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "ParamettersPojo{" + "userName=" + userName + ", password=" + password + ", url=" + url + '}';
    }
    
}
