/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Project;

/**
 *
 * @author Tadeu-PC
 */
public class GitUtils {

    public GitLabApi gitLabApi;

    public GitUtils() throws Exception {
        try {
            gitLabApi = GitLabApi.oauth2Login(SessionUtils.parametters.getUrl(), SessionUtils.parametters.getUserName(), SessionUtils.parametters.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erro ao autenticar no servidor GIT: " + SessionUtils.parametters.getUrl());
        }
    }

    public Project getProject(String projectName) throws Exception {
        try {
            return gitLabApi.getProjectApi().getProjects(projectName).stream().findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erro ao carregar o Projeto: " + projectName);
        }
    }

    public List<Branch> listBranchs(Project project, String branchName) throws Exception {
        try {
            if (branchName.contains("-")) {
                return Arrays.asList(gitLabApi.getRepositoryApi().getBranch(project.getId(), branchName));
            }
            return gitLabApi.getRepositoryApi().getBranches(project.getId(), branchName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erro ao listar as BRANCH do GIT com esta descrição: " + branchName);
        }
    }

    public List<Commit> listCommits(Project project, String ref, Date dtIniCommits) throws Exception {
        try {
            return gitLabApi.getCommitsApi().getCommits(project.getId(), ref, dtIniCommits, new Date());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erro ao listar os COMMITs do GIT.");
        }
    }

    public List<Commit> listCommits(Project project, String branchName, Date dtIniCommits, String author) throws Exception {
        try {
            List<Commit> commits = listCommits(project, branchName, dtIniCommits);
            return commits.stream()
                    .filter(c -> c.getMessage().startsWith("OS: " + getBranchNumber(branchName)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erro ao listar os COMMITs do GIT.");
        }
    }

    private String getBranchNumber(String branchName) {
        return branchName.contains("-") ? branchName.substring(0, branchName.indexOf("-")) : branchName;
    }
}
