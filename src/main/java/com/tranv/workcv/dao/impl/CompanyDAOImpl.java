package com.tranv.workcv.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.CompanyDAO;
import com.tranv.workcv.entity.Company;


@Repository
public class CompanyDAOImpl implements CompanyDAO {
	// DAO handles operations related to the Company object

	@Autowired
	private SessionFactory sessionFactory;

	// Retrieve all companies from the database
	@Override
	public List<Company> getListCompanys() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Company> theQuery = currentSession.createQuery("from Company", Company.class);
		List<Company> companys = theQuery.getResultList();
		return companys;
	}

	// Retrieve the top companies with the highest number of distinct users who
	// applied to their recruitments
	@Override
	public List<Company> getCompanyTop() {
		Session currentSession = sessionFactory.getCurrentSession();
		String hql = "SELECT c FROM Company c LEFT JOIN c.recruitments r LEFT JOIN r.applyPosts a GROUP BY c.id "
				+ "ORDER BY COUNT(DISTINCT a.user) DESC";
		Query<Company> theQuery = currentSession.createQuery(hql, Company.class);
		theQuery.setMaxResults(3);
		List<Company> companies = theQuery.getResultList();
		return companies;

	}

	// Retrieve the company associated with the given User ID
	@Override
	public Company getCompanyByUserId(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Company> theQuery = currentSession
				.createQuery("SELECT c FROM Company c JOIN c.user u WHERE u.id = :theId", Company.class);
		theQuery.setParameter("theId", theId);
		Company company = theQuery.getSingleResult();
		return company;
	}

	// Retrieve a specific company from the database based on the given ID
	@Override
	public Company getCompanyById(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Company company = currentSession.get(Company.class, theId);
		return company;
	}

	// Save or update the company object in the database
	@Override
	public void saveOrUpdateCompany(Company theCompany) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theCompany);

	}


}
