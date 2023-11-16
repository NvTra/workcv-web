package com.tranv.workcv.dao;

import java.util.List;

import com.tranv.workcv.entity.Company;

public interface FollowCompanyDAO {
	public void followCompany(int userId, int companyId);

	public void unFollowCompany(int userId, int companyId);

	public List<Company> listCompanyFollow(int userId);
}
