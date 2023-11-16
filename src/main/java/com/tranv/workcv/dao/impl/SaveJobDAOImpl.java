package com.tranv.workcv.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.SaveJobDAO;
import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.UserService;

@Repository
public class SaveJobDAOImpl implements SaveJobDAO {
	// DAO handles operations related to the Save Job object

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserService userService;

	@Autowired
	private RecruitmentService recruitmentService;

	// Save a job by associating it with a user.
	@Override
	public void saveJob(int recruitmentId, int userId) {
		Session currentSession = sessionFactory.getCurrentSession();
		User theUser = userService.getUserById(userId);
		Recruitment theRecruitment = recruitmentService.getRecruitmentById(recruitmentId);

		theUser.getRecruitments().add(theRecruitment);
		theRecruitment.getUsers().add(theUser);

		currentSession.merge(theUser);
	}

	// unSave a job by associating it with a user.
	@Override
	public void unSaveJob(int recruitmentId, int userId) {
		Session currentSession = sessionFactory.getCurrentSession();
		User theUser = userService.getUserById(userId);
		Recruitment theRecruitment = recruitmentService.getRecruitmentById(recruitmentId);

		theUser.getRecruitments().remove(theRecruitment);
		theRecruitment.getUsers().remove(theUser);

		currentSession.merge(theUser);
	}

	// Retrieve the list of recruitment save by the user with the given ID
	@Override
	public List<Recruitment> listSaveJobByUser(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuery = currentSession
				.createQuery("SELECT r FROM User u JOIN u.recruitments r WHERE u.id = :userId", Recruitment.class);
		theQuery.setParameter("userId", theId);
		List<Recruitment> recruitments = theQuery.getResultList();
		return recruitments;
	}

}
