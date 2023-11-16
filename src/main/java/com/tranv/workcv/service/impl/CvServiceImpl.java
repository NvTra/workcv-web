package com.tranv.workcv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.CvDAO;
import com.tranv.workcv.entity.Cv;
import com.tranv.workcv.service.CvService;

@Service
public class CvServiceImpl implements CvService {
	// Service handles operations related to the Cv object
	@Autowired
	private CvDAO cvDAO;

	// Retrieve all CVs from the database
	@Override
	@Transactional
	public List<Cv> getListCvs() {
		return cvDAO.getListCvs();
	}

	// Retrieve a specific CV from the database based on the given ID
	@Override
	@Transactional
	public Cv getCvById(int theId) {
		return cvDAO.getCvById(theId);
	}

	// Save or update the CV object in the database
	@Override
	@Transactional
	public void saveCv(Cv theCv) {
		cvDAO.saveCv(theCv);

	}

	// Delete the CV with the given ID from the database
	@Override
	@Transactional
	public void deleteCv(int theId) {
		cvDAO.deleteCv(theId);

	}

	// Retrieve the CV associated with the given User ID
	@Override
	@Transactional
	public Cv getCvByUserId(int theId) {
		return cvDAO.getCvByUserId(theId);
	}

}
