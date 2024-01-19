package com.tranv.workcv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.FollowCompanyDAO;
import com.tranv.workcv.entity.Company;
import com.tranv.workcv.service.FollowCompanyService;

@Service
public class FollowCompanyServiceImpl implements FollowCompanyService {
	// Service handles operations related to the Follow Company object
	@Autowired
	private FollowCompanyDAO followCompanyDAO;

	// Follow a company by associating it with a user.
	@Override
	@Transactional
	public void followCompany(int userId, int companyId) {
		followCompanyDAO.followCompany(userId, companyId);

	}

	// Unfollow a company by disassociating it from a user.
	@Override
	@Transactional
	public void unFollowCompany(int userId, int companyId) {
		followCompanyDAO.unFollowCompany(userId, companyId);

	}

	// Retrieve the list of companies followed by the user with the given ID
	@Override
	@Transactional
	public List<Company> listCompanyFollow(int userId) {
		return followCompanyDAO.listCompanyFollow(userId);
	}

}
