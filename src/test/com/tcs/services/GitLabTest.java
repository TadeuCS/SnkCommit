package com.tcs.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.List;

import org.gitlab4j.api.models.Branch;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tcs.pojo.CommitPojo;
import com.tcs.pojo.ParametterPojo;

class GitLabTest {
	private static ParametterPojo parametros;
	private static GitLab gitLabService;
	
	@BeforeEach
	void setUp() throws Exception {
		parametros=new ParametterPojo();
		gitLabService = new GitLab();
		autentica();
	}
	
	private void autentica() throws Exception{
		parametros.setUserName("tadeu.sousa");
		parametros.setPassword("88015736t");
		gitLabService.auth(parametros);
	}

	@Test
	void deveriaConseguirAutenticar() {
		assertDoesNotThrow(()-> autentica());
	}
	
	@Test
	void naoDeveriaConseguirAutenticar() throws Exception {
		parametros.setUserName("blablabla");
		assertThrowsExactly(Exception.class, ()-> gitLabService.auth(parametros));
	}
	
	@Test
	void deveriaRetornarUmaListaDeProjetos() throws Exception {
		autentica();
	}
	
	@Test
	void deveriaRetornarIdDoProjeto() throws Exception {
		assertNotNull(getProjectID());
	}

	private Long getProjectID() throws Exception {
		Long projectID = gitLabService.getProjectID(parametros.getProjectName());
		return projectID;
	}
	
	@Test
	void naoRetornarNullAoProcurarUmProjetoInexistente() throws Exception {
		parametros.setProjectName("blablabla");
		Object project = gitLabService.getProjectID(parametros.getProjectName());
		assertNull(project);
	}
	
	@Test
	void deveriaRetornarUmaBranch() throws Exception {
		List branchs = gitLabService.listBranchs(parametros.getProjectName(), "3808322-4.16-DEV");
		assertFalse(branchs.isEmpty());
	}
	
	@Test
	void deveriaRetornarUmaListaVasiaAoBuscarBranchInexistente() throws Exception {
		List branchs = gitLabService.listBranchs(parametros.getProjectName(), "1234567-1.1-DEV");
		assertTrue(branchs.isEmpty());
	}
	
	@Test
	void deveriaRetornarUmaListaDeBranchsAoBuscarBranchExistente() throws Exception {
		Long projectID = getProjectID();
		List commits = gitLabService.listCommits(projectID, "3808322-4.16-DEV", 4, "");
		assertFalse(commits.isEmpty());
	}
	
}
