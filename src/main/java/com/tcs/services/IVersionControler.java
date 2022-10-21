package com.tcs.services;

import java.util.List;

import com.tcs.pojo.BranchPojo;
import com.tcs.pojo.CommitPojo;
import com.tcs.pojo.ParametterPojo;

public interface IVersionControler {
	
	public void auth() throws Exception;
    
    public void auth(ParametterPojo params) throws Exception;

    public Object getProjectID(String projectName) throws Exception;

    public List<BranchPojo> listBranchs(String projectName, String branchName) throws Exception;

    public List<CommitPojo> listCommits(BranchPojo branch, int days, String author) throws Exception;
    
    public List<CommitPojo> listCommits(Long projectID, String branchName, int dtIniCommits, String author) throws Exception;

    public List<String> listDiffs(BranchPojo branch, CommitPojo commit) throws Exception;
    
    public List<String> listDiffs(Long projectID, String commitHash) throws Exception;
}
