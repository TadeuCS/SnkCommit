/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Tadeu-PC
 */
public class CommitPojo {
    private String hash;
    private String author;
    private String message;
    private List<String> changedFiles;

    public CommitPojo(String hash, String author, String message) {
        this.hash = hash;
        this.author = author;
        this.message = message;
        this.changedFiles=new ArrayList<>();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getChangedFiles() {
        return changedFiles;
    }

    public void setChangedFiles(List<String> changedFiles) {
        this.changedFiles = changedFiles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.hash);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommitPojo other = (CommitPojo) obj;
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return "CommitPojo{" + "hash=" + hash + ", author=" + author + ", message=" + message + '}';
    }

}
