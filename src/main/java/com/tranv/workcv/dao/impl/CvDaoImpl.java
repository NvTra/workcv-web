package com.tranv.workcv.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.CvDAO;
import com.tranv.workcv.entity.Cv;

@Repository
public class CvDaoImpl implements CvDAO {
	// DAO handles operations related to the Cv object
	@Autowired
	private SessionFactory sessionFactory;

	// Retrieve all CVs from the database
	@Override
	public List<Cv> getListCvs() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Cv> theQuery = currentSession.createQuery("from Cv", Cv.class);
		List<Cv> cvs = theQuery.getResultList();
		return cvs;
	}

	// Retrieve a specific CV from the database based on the given ID
	@Override
	public Cv getCvById(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Cv cv = currentSession.get(Cv.class, theId);
		return cv;
	}

	// Save or update the CV object in the database
	@Override
	public void saveCv(Cv theCv) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theCv);
	}

	// Delete the CV with the given ID from the database
	@Override
	public void deleteCv(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = currentSession.createQuery("delete from Cv where id= :theId");
		theQuery.setParameter("theId", theId);
		theQuery.executeUpdate();
	}

	// Retrieve the CV associated with the given User ID
	@Override
	public Cv getCvByUserId(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Cv> theQuery = currentSession.createQuery("SELECT c FROM Cv c JOIN c.user u WHERE u.id = :theId",
				Cv.class);
		theQuery.setParameter("theId", theId);
		Cv Cv = theQuery.getSingleResult();
		return Cv;
	}
}
