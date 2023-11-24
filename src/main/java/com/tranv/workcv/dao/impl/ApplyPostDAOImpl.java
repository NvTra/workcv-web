package com.tranv.workcv.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.ApplyPostDAO;
import com.tranv.workcv.entity.ApplyPost;

@Repository
public class ApplyPostDAOImpl implements ApplyPostDAO {
	// DAO handles operations related to the ApplyPost object

	@Autowired
	private SessionFactory sessionFactory;

	// Save or update the ApplyPost object in the database
	@Override
	public void saveOrUpdateApplyPost(ApplyPost theApplyPost) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theApplyPost);
	}

	// Method to get a list of ApplyPosts associated with the Recruitment ID
	@Override
	public List<ApplyPost> listApplyPostByRecruitmentId(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<ApplyPost> theQuery = currentSession.createQuery(
				"SELECT a FROM ApplyPost a JOIN FETCH a.recruitment r JOIN FETCH a.user u WHERE r.id = :theId",
				ApplyPost.class);
		theQuery.setParameter("theId", theId);

		List<ApplyPost> applyPosts = theQuery.getResultList();
		return applyPosts;
	}

	// Method confirm that the company has approved the job for the applicant
	@Override
	public void confirmPost(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		ApplyPost applyPost = currentSession.get(ApplyPost.class, theId);
		applyPost.setStatus(1);
		currentSession.update(applyPost);
	}

	// Method to get list of ApplyPosts associated with the Company ID
	@Override
	public ApplyPost getApplyPostbyId(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		ApplyPost applyPost = currentSession.get(ApplyPost.class, theId);
		return applyPost;
	}

	// Method to get list of ApplyPosts associated with the Company ID
	@Override
	public List<ApplyPost> listApplyPostsByCompany(int companyId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<ApplyPost> theQuery = currentSession.createQuery("SELECT a FROM ApplyPost a JOIN FETCH a.recruitment r "
				+ "JOIN FETCH a.user u JOIN FETCH r.company c " + "WHERE c.id = :companyId", ApplyPost.class);
		theQuery.setParameter("companyId", companyId);
		List<ApplyPost> applyPosts = theQuery.getResultList();
		return applyPosts;
	}

	// Method to get list of ApplyPosts associated with the User ID
	@Override
	public List<ApplyPost> listApplyPostsByUser(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<ApplyPost> theQuery = currentSession.createQuery(
				"SELECT a FROM Recruitment r JOIN r.applyPosts a JOIN a.user u WHERE u.id = :userId", ApplyPost.class);
		theQuery.setParameter("userId", theId);
		List<ApplyPost> applyPosts = theQuery.getResultList();
		return applyPosts;
	}

	@Override
	public void deleteJob(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = currentSession.createQuery("delete from ApplyPost where id= :applyPostId");
		theQuery.setParameter("applyPostId", theId);
		theQuery.executeUpdate();

	}

}
