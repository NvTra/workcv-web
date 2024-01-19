package com.tranv.workcv.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.FollowCompanyDAO;
import com.tranv.workcv.entity.Company;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.CompanyService;
import com.tranv.workcv.service.UserService;

@Repository
public class FollowCompanyDAOImpl implements FollowCompanyDAO {
	// DAO handles operations related to the Follow Company object

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	// Follow a company by associating it with a user.
	@Override
	public void followCompany(int userId, int companyId) {
		Session currentSession = sessionFactory.getCurrentSession();
		User theUser = userService.getUserById(userId);
		Company theCompany = companyService.getCompanyById(companyId);
		theUser.getCompanies().add(theCompany);
		theCompany.getUsers().add(theUser);
		currentSession.merge(theUser);
	}

	// Unfollow a company by disassociating it from a user.
	@Override
	public void unFollowCompany(int userId, int companyId) {
		Session currentSession = sessionFactory.getCurrentSession();
		User theUser = userService.getUserById(userId);
		Company theCompany = companyService.getCompanyById(companyId);
		theUser.getCompanies().remove(theCompany);
		theCompany.getUsers().remove(theUser);
		currentSession.merge(theUser);
	}

	// Retrieve the list of companies followed by the user with the given ID
	@Override
	public List<Company> listCompanyFollow(int userId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Company> theQuery = currentSession
				.createQuery("SELECT c FROM User u JOIN u.companies c WHERE u.id = :userId", Company.class);
		theQuery.setParameter("userId", userId);
		List<Company> companies = theQuery.getResultList();
		return companies;
	}

}
