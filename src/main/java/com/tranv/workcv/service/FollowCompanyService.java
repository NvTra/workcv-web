package com.tranv.workcv.service;

import java.util.List;

import com.tranv.workcv.entity.Company;

public interface FollowCompanyService {
	public void followCompany(int userId, int companyId);

	public void unFollowCompany(int userId, int companyId);

	public List<Company> listCompanyFollow(int userId);
}
