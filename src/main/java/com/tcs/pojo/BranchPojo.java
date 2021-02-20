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
public class BranchPojo {
    private String name;
    private Integer projectId;
    private List<CommitPojo> commits;

    public BranchPojo(String name, Integer projectId) {
        this.name = name;
        this.projectId=projectId;
        this.commits=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<CommitPojo> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitPojo> commits) {
        this.commits = commits;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.name);
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
        final BranchPojo other = (BranchPojo) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "BranchPojo{" + "name=" + name + ", commits=" + commits + '}';
    }
}
