/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.services;

import com.tcs.pojo.BranchPojo;
import com.tcs.pojo.CommitPojo;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Tadeu-PC
 */
public abstract class VersionControllerAbstract {

    protected List<BranchPojo> branchs;
    protected List<CommitPojo> commits;

    public List<BranchPojo> getBranchs() {
        return branchs;
    }

    public List<CommitPojo> getCommits() {
        return commits;
    }

    public VersionControllerAbstract() {
    }

    public void init() {
        branchs = new ArrayList<>();
        commits = new ArrayList<>();
    }

    public String buildLog() {
        StringBuilder textLog = new StringBuilder();
        textLog.append("### Tipo de Retorno").append("\n");
        textLog.append("Implementação").append("\n\n");
        textLog.append("### Problema/Solução").append("\n");
        LinkedHashSet<String> commitMesages = new LinkedHashSet<>();
        LinkedHashSet<String> changedFiles = new LinkedHashSet<>();
        for (BranchPojo branch : branchs) {
            String textStart = "OS: " + getBranchNumber(branch.getName());
            for (CommitPojo commit : branch.getCommits()
                    .stream()
                    .filter(c -> c.getMessage().startsWith(textStart))
                    .collect(Collectors.toList())) {
                commitMesages.add(commit.getMessage().replace(textStart, ""));
                for (String changedFile : commit.getChangedFiles()) {
                    changedFiles.add(changedFile);
                }
            }
        }

        for (String commitMessage : commitMesages) {
            textLog.append(commitMessage);
        }
        textLog.append("\n");
        for (BranchPojo branch : branchs) {
            textLog.append("### Implementado na BRANCH: [").append(branch.getName()).append("]\n");
        }
        textLog.append("\n");
        textLog.append("### Fontes Alterados\n");
        for (String filePath : changedFiles) {
            textLog.append(filePath).append("\n");
        }
        return textLog.toString();
    }

    private String getBranchNumber(String branchName) {
        return branchName.contains("-") ? branchName.substring(0, branchName.indexOf("-")) : branchName;
    }
    
    
    public abstract void auth() throws Exception;
    public abstract Object getProject(String projectName) throws Exception;
    public abstract List listBranchs(String projectName, String branchName) throws Exception;
    public abstract List listCommits(BranchPojo branch, Date dtIniCommits, String author) throws Exception;
    public abstract List listDiffs(BranchPojo branch, CommitPojo commit) throws Exception;
}
