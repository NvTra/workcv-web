package com.tranv.workcv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.RecruitmentDAO;
import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.service.RecruitmentService;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {
	// Service handles operations related to the Recruitment object

	@Autowired
	private RecruitmentDAO recruitmentDAO;

	// This method returns a list of Recruitment objects from the database
	@Override
	@Transactional
	public List<Recruitment> getListRecruitments() {
		return recruitmentDAO.getListRecruitments();
	}

	// Get a recruitment by its ID.
	@Override
	@Transactional
	public Recruitment getRecruitmentById(int theId) {
		return recruitmentDAO.getRecruitmentById(theId);
	}

	// Save a recruitment.
	@Override
	@Transactional
	public void saveRecruitment(Recruitment theRecruitment) {
		recruitmentDAO.saveRecruitment(theRecruitment);

	}

	// Update a recruitment.
	@Override
	@Transactional
	public void update(Recruitment theRecruitment) {
		recruitmentDAO.update(theRecruitment);

	}

	// Delete a recruitment by its ID.
	@Override
	@Transactional
	public void deleteRecruitment(int theId) {
		recruitmentDAO.deleteRecruitment(theId);

	}

	// Get a list of recruitments sorted by salary, creation date, and type.
	@Override
	@Transactional
	public List<Recruitment> getResultRecruitmentByCompany(int theId) {
		return recruitmentDAO.getResultRecruitmentByCompany(theId);
	}

	// This method returns a list of Recruitment by salary objects from the database
	@Override
	@Transactional
	public List<Recruitment> getResultRecruitmentBySalary() {
		return recruitmentDAO.getResultRecruitmentBySalary();
	}

	// Search recruitments by a given title.
	@Override
	@Transactional
	public List<Recruitment> getResultRecruitment(String searchTerm) {
		return recruitmentDAO.getResultRecruitment(searchTerm);
	}

	// Search recruitments by a given address.
	@Override
	@Transactional
	public List<Recruitment> getResultAdress(String searchTerm) {
		return recruitmentDAO.getResultAdress(searchTerm);
	}

	// Search recruitments by a given company.
	@Override
	@Transactional
	public List<Recruitment> getResultCompany(String searchTerm) {
		return recruitmentDAO.getResultCompany(searchTerm);
	}

}
