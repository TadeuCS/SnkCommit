/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.services;

import com.tcs.pojo.BranchPojo;
import com.tcs.pojo.CommitPojo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tadeu-PC
 */
public class SVN extends VersionControllerAbstract{

    @Override
    public void auth() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getProject(String projectName) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List listBranchs(String projectName, String branchName) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List listCommits(BranchPojo branch, Date dtIniCommits, String author) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List listDiffs(BranchPojo branch, CommitPojo commit) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
