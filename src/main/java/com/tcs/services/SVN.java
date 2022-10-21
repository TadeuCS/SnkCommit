/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.services;

import java.util.List;

import com.tcs.pojo.BranchPojo;
import com.tcs.pojo.CommitPojo;
import com.tcs.pojo.ParametterPojo;

/**
 *
 * @author Tadeu-PC
 */
public class SVN implements IVersionControler{

	@Override
	public void auth() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void auth(ParametterPojo params) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getProjectID(String projectName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchPojo> listBranchs(String projectName, String branchName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommitPojo> listCommits(BranchPojo branch, int days, String author) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommitPojo> listCommits(Long projectID, String branchName, int dtIniCommits, String author)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listDiffs(BranchPojo branch, CommitPojo commit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listDiffs(Long projectID, String commitHash) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    

}
