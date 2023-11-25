package com.tranv.workcv.dao;

import java.util.List;

import com.tranv.workcv.entity.ApplyPost;


public interface ApplyPostDAO {
	public void saveOrUpdateApplyPost(ApplyPost applyPost);

	public ApplyPost getApplyPostbyId(int theId);

	public List<ApplyPost> listApplyPostByRecruitmentId(int theId);

	public void approve(int theId);

	public List<ApplyPost> listApplyPostsByCompany(int companyId);

	public List<ApplyPost> listApplyPostsByUser(int theId);

	public void deleteJob(int theId);
}
