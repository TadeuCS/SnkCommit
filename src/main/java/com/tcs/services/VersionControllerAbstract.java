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

    public List<BranchPojo> getBranchs() {
        return branchs;
    }

    public List<CommitPojo> getCommits() {
        List<CommitPojo> commitsPojo = new ArrayList<>();
        for (List<CommitPojo> commits : branchs.stream().map(b -> b.getCommits()).collect(Collectors.toList())) {
            commitsPojo.addAll(commits);
        }
        return commitsPojo;
    }

    public VersionControllerAbstract() {
    }

    public void init() {
        branchs = new ArrayList<>();
    }

    public String buildLog() {
        StringBuilder textLog = new StringBuilder();
        LinkedHashSet<String> branchsNames = new LinkedHashSet<>();
        LinkedHashSet<String> commitMesages = new LinkedHashSet<>();
        LinkedHashSet<String> changedFiles = new LinkedHashSet<>();
        for (BranchPojo branch : branchs) {
            String textStart = "OS: " + getBranchNumber(branch.getName());
            for (CommitPojo commit : branch.getCommits()
                    .stream()
                    .filter(c -> (branch.getName().length()!=3 && branch.getName().length()!=6) ? c.getMessage().startsWith(textStart) : true)
                    .collect(Collectors.toList())) {
                branchsNames.add(branch.getName());
                commitMesages.add(commit.getMessage().replace(textStart, "").replace("\n\n", "\n").trim());
                for (String changedFile : commit.getChangedFiles()) {
                    changedFiles.add(changedFile.trim());
                }
            }
        }

        if (!branchsNames.isEmpty()) {
            textLog.append("### Tipo de Retorno").append("\n");
            textLog.append("Implementação").append("\n\n");
            textLog.append("### Problema/Solução").append("\n");
            for (String commitMessage : commitMesages) {
                textLog.append(commitMessage);
            }
            textLog.append("\n\n");
            for (String branchName : branchsNames) {
                textLog.append("### Implementado na BRANCH: [").append(branchName).append("]\n");
            }
            textLog.append("\n");
            textLog.append("### Fontes Alterados\n");
            for (String filePath : changedFiles) {
                textLog.append(filePath).append("\n");
            }
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
