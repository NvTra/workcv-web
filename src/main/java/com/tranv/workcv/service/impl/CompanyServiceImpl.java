package com.tranv.workcv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.CompanyDAO;
import com.tranv.workcv.entity.Company;
import com.tranv.workcv.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	// Service handles operations related to the Company object
	@Autowired
	private CompanyDAO companyDAO;

	// Retrieve all companies from the database
	@Override
	@Transactional
	public List<Company> getListCompanys() {
		return companyDAO.getListCompanys();
	}

	// Retrieve the top companies with the highest number of distinct users who
	// applied to their recruitments
	@Override
	@Transactional
	public List<Company> getCompanyTop() {
		return companyDAO.getCompanyTop();
	}

	// Retrieve the company associated with the given User ID
	@Override
	@Transactional
	public Company getCompanyById(int theId) {
		return companyDAO.getCompanyById(theId);
	}

	// Retrieve a specific company from the database based on the given ID
	@Override
	@Transactional
	public Company getCompanyByUserId(int theId) {
		return companyDAO.getCompanyByUserId(theId);
	}

	// Save or update the company object in the database
	@Override
	@Transactional
	public void saveOrUpdateCompany(Company theCompany) {
		companyDAO.saveOrUpdateCompany(theCompany);
	}

	@Override
	public int numberOfApplicants(int companyId) {
		return companyDAO.numberOfApplicants(companyId);
	}

}
