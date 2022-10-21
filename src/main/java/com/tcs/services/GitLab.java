/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.Project;

import com.tcs.pojo.BranchPojo;
import com.tcs.pojo.CommitPojo;
import com.tcs.pojo.ParametterPojo;
import com.tcs.util.OUtils;
import com.tcs.util.SessionUtils;

/**
 *
 * @author Tadeu-PC
 */
public class GitLab implements IVersionControler {

    private GitLabApi gitLabApi;
    
    public GitLabApi getInstance() {
    	return gitLabApi;
    }
    
    @Override
    public void auth() throws Exception {
        auth(SessionUtils.getInstance().parameters);
    }
    
    @Override
    public void auth(ParametterPojo params) throws Exception {
    	try {
            gitLabApi = GitLabApi.oauth2Login(params.getUrl(), params.getUserName(), params.getPassword());
         // Set the connect timeout to 1 second and the read timeout to 5 seconds
            gitLabApi.setRequestTimeout(1000, 5000);
        } catch (Exception e) {
            throw new Exception("Erro ao autenticar no servidor GIT: " + params.getUrl(), e);
        }
    }

    @Override
    public Long getProjectID(String projectName) throws Exception {
    	Optional<Project> project;
        try {
        	project = gitLabApi.getProjectApi().getProjects(projectName)
					.stream().filter(p->p.getHttpUrlToRepo().trim().toLowerCase().endsWith(projectName.concat(".git").toLowerCase())).findFirst();
        } catch (Exception e) {
            throw new Exception("Erro ao carregar o Projeto: " + projectName, e);
        }
        return project.isPresent() ? project.get().getId() : null; 
    }

    @Override
    public List<BranchPojo> listBranchs(String projectName, String branchName) throws Exception {
    	System.out.println("Branch: "+branchName);
    	List<Branch> branchsGitLab = new ArrayList<>();
        Long projectID = getProjectID(projectName);
        try {
            branchsGitLab = gitLabApi.getRepositoryApi().getBranches(projectID, branchName);
        } catch (Exception e) {
            throw new Exception("Erro ao listar as BRANCH do GIT com esta descrição: " + branchName, e);
        }
        return branchsGitLab.stream().map(b-> new BranchPojo(b.getName(), projectID)).collect(Collectors.toList());
    }

    @Override
    public List<CommitPojo> listCommits(BranchPojo branch, int days, String author) throws Exception {
        return listCommits(branch.getProjectId(), branch.getName(), days, author);
    }

    @Override
    public List<String> listDiffs(BranchPojo branch, CommitPojo commit) throws Exception {
        return listDiffs(branch.getProjectId(), commit.getHash());
    }
    
	@Override
	public List<CommitPojo> listCommits(Long projectID, String branchName, int days, String author) throws Exception {
		List<Commit> commitsGitLab = new ArrayList<>();
		List<CommitPojo> commits = new ArrayList<CommitPojo>();
        try {
            Date since = OUtils.getStartOfDay(OUtils.addDays(- days));
            Date until = new Date();
            System.out.println("Período: "+since+" até "+until);
            commitsGitLab = gitLabApi.getCommitsApi().getCommits(projectID, branchName, since, until);
        } catch (Exception e) {
            throw new Exception("Erro ao listar os COMMITs do GIT.\n", e);
        } finally {
            if (author != null && !author.trim().isEmpty()) {
                System.out.println("Autor: "+ author);
                commitsGitLab = commitsGitLab.stream().filter(c -> c.getAuthorName().equals(author)).collect(Collectors.toList());
            }
            for (Commit commitGitLab : commitsGitLab) {
                CommitPojo commit = new CommitPojo(commitGitLab.getId(), commitGitLab.getAuthorName(), commitGitLab.getMessage());
                List<String> differences = listDiffs(projectID, commit.getHash());
                commit.setChangedFiles(differences);
                commits.add(commit);
            }
        }
		return commits;
	}

	@Override
	public List<String> listDiffs(Long projectID, String commitHash) throws Exception {
		List<Diff> differences = new ArrayList<>();
        try {
            differences= gitLabApi.getCommitsApi().getDiff(projectID, commitHash);
        } catch (Exception e) {
            throw new Exception("Erro ao listar o que foi alterado no commit: " + commitHash, e);
		}
		return differences.stream().map(d->d.getOldPath()).collect(Collectors.toList());
	}

}
