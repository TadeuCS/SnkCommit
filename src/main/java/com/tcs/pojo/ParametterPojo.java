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
public class ParametterPojo {
    private String userName;
    private String password;
    private String url;
    private String projectName;

    public ParametterPojo() {
    }
    
    public ParametterPojo(Ini ini) {
    }
    
    public String getUserName() {
        if(userName==null){
            userName="";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        if(password==null){
            password="";
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        if(url==null){
            url="https://git.sankhya.com.br";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectName() {
        if(projectName==null){
            projectName="Sankhyaw";
        }
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "ParamettersPojo{" + "userName=" + getUserName() + ", password=" + getPassword() + ", url=" + getUrl() + '}';
    }
    
}
