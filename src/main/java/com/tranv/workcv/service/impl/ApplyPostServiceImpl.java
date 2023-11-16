package com.tranv.workcv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.ApplyPostDAO;
import com.tranv.workcv.entity.ApplyPost;
import com.tranv.workcv.service.ApplyPostService;

@Service
public class ApplyPostServiceImpl implements ApplyPostService {
	// Service handles operations related to the ApplyPost object

	@Autowired
	private ApplyPostDAO applyPostDAO;

	// Save or update the ApplyPost object in the database
	@Override
	@Transactional
	public void saveOrUpdateApplyPost(ApplyPost applyPost) {
		applyPostDAO.saveOrUpdateApplyPost(applyPost);

	}

	// Method to get a list of ApplyPosts associated with the Recruitment ID
	@Override
	@Transactional
	public List<ApplyPost> listApplyPostByRecruitmentId(int theId) {
		return applyPostDAO.listApplyPostByRecruitmentId(theId);

	}

	// Method confirm that the company has approved the job for the applicant
	@Override
	@Transactional
	public void confirmPost(int theId) {
		applyPostDAO.confirmPost(theId);

	}

	// Method to get list of ApplyPosts associated with the Company ID
	@Override
	@Transactional
	public ApplyPost getApplyPostbyId(int theId) {
		return applyPostDAO.getApplyPostbyId(theId);
	}

	// Method to get list of ApplyPosts associated with the Company ID
	@Override
	@Transactional
	public List<ApplyPost> listApplyPostsByCompany(int companyId) {
		return applyPostDAO.listApplyPostsByCompany(companyId);
	}

	// Method to get list of ApplyPosts associated with the User ID
	@Override
	@Transactional
	public List<ApplyPost> listApplyPostsByUser(int theId) {
		return applyPostDAO.listApplyPostsByUser(theId);
	}

	@Override
	@Transactional
	public void deleteJob(int theId) {
		applyPostDAO.deleteJob(theId);

	}

}
