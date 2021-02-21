/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.services;

import com.tcs.pojo.BranchPojo;
import com.tcs.pojo.CommitPojo;
import com.tcs.pojo.ParametterPojo;
import com.tcs.util.OUtils;
import com.tcs.util.SessionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.Project;

/**
 *
 * @author Tadeu-PC
 */
public class GitLab extends VersionControllerAbstract {

    private GitLabApi gitLabApi;
    
    @Override
    public void auth() throws Exception {
        ParametterPojo params = SessionUtils.getInstance().parameters;
        try {
            gitLabApi = GitLabApi.oauth2Login(params.getUrl(), params.getUserName(), params.getPassword());
        } catch (Exception e) {
            throw new Exception("Erro ao autenticar no servidor GIT: " + params.getUrl());
        }
    }

    @Override
    public Object getProject(String projectName) throws Exception {
        try {
            return gitLabApi.getProjectApi().getProjects(projectName).stream().findFirst().get();
        } catch (Exception e) {
            throw new Exception("Erro ao carregar o Projeto: " + projectName);
        }
    }

    @Override
    public List listBranchs(String projectName, String branchName) throws Exception {
        List<Branch> branchsGitLab = new ArrayList<>();
        Project project = (Project) getProject(projectName);
        try {
            System.out.println("Branch: "+branchName);
            if (branchName.contains("-") || branchName.length()==3 || branchName.length()==6) {
                branchsGitLab = Arrays.asList(gitLabApi.getRepositoryApi().getBranch(project.getId(), branchName));
            } else {
                branchsGitLab = gitLabApi.getRepositoryApi().getBranches(project.getId(), branchName);
            }
        } catch (Exception e) {
            throw new Exception("Erro ao listar as BRANCH do GIT com esta descrição: " + branchName);
        } finally {
            this.branchs.addAll(branchsGitLab.stream().map(b -> new BranchPojo(b.getName(), project.getId())).collect(Collectors.toList()));
        }
        return branchsGitLab;
    }

    @Override
    public List listCommits(BranchPojo branch, Date dtIniCommits, String author) throws Exception {
        List<Commit> commitsGitLab = new ArrayList<>();
        try {
            Date since = OUtils.getStartOfDay(dtIniCommits);
            Date until = new Date();
            System.out.println("Período: "+since+" até "+until);
            commitsGitLab = gitLabApi.getCommitsApi().getCommits(branch.getProjectId(), branch.getName(), since, until);
        } catch (Exception e) {
            throw new Exception("Erro ao listar os COMMITs do GIT.\n");
        } finally {
            if (author != null && !author.trim().isEmpty()) {
                System.out.println("Autor: "+ author);
                commitsGitLab = commitsGitLab.stream().filter(c -> c.getAuthorName().equals(author)).collect(Collectors.toList());
            }
            for (Commit commitGitLab : commitsGitLab) {
                CommitPojo commit = new CommitPojo(commitGitLab.getId(), commitGitLab.getAuthorName(), commitGitLab.getMessage());
                listDiffs(branch, commit);
                branch.getCommits().add(commit);
            }
        }
        return commitsGitLab;
    }

    @Override
    public List listDiffs(BranchPojo branch, CommitPojo commit) throws Exception {
        List<Diff> differences = new ArrayList<>();
        try {
            differences= gitLabApi.getCommitsApi().getDiff(branch.getProjectId(), commit.getHash());
        } catch (Exception e) {
            throw new Exception("Erro ao listar o que foi alterado no commit: " + commit.getHash());
        }finally{
            commit.setChangedFiles(differences.stream().map(d->d.getOldPath()).collect(Collectors.toList()));
        }
        return differences;
    }

}
