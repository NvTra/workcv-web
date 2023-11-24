package com.tranv.workcv.dao;

import java.util.List;

import com.tranv.workcv.entity.Company;

public interface CompanyDAO {
	public List<Company> getListCompanys();

	public List<Company> getCompanyTop();

	public Company getCompanyByUserId(int theId);

	public void saveOrUpdateCompany(Company theCompany);

	public Company getCompanyById(int theId);
	
	public int numberOfApplicants(int companyId);
}
