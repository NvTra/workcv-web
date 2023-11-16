package com.tranv.workcv.service;

import java.util.List;

import com.tranv.workcv.entity.ApplyPost;

public interface ApplyPostService {

	public void saveOrUpdateApplyPost(ApplyPost applyPost);

	public ApplyPost getApplyPostbyId(int theId);

	public List<ApplyPost> listApplyPostByRecruitmentId(int theId);

	public void confirmPost(int theId);

	public List<ApplyPost> listApplyPostsByCompany(int companyId);

	public List<ApplyPost> listApplyPostsByUser(int theId);

	public void deleteJob(int theId);
}
