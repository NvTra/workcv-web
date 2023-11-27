package com.tranv.workcv.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.RecruitmentDAO;
import com.tranv.workcv.entity.Category;
import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.service.CategoryService;

@Repository
public class RecruitmentDAOImpl implements RecruitmentDAO {
	// DAO handles operations related to the Recruitment object

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	CategoryService categoryService;

	// This method returns a list of Recruitment objects from the database
	@Override
	public List<Recruitment> getListRecruitments() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuerry = currentSession.createQuery("from Recruitment", Recruitment.class);
		List<Recruitment> recruitments = theQuerry.getResultList();
		return recruitments;
	}

	// Get a recruitment by its ID.
	@Override
	public Recruitment getRecruitmentById(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Recruitment recruitment = currentSession.get(Recruitment.class, theId);
		return recruitment;
	}

	// Save a recruitment.
	@Override
	public void saveRecruitment(Recruitment theRecruitment) {
		Session currentSession = sessionFactory.getCurrentSession();
		Category theCategory = categoryService.getCategoryById(theRecruitment.getCategory().getId());
		theCategory.addNumberChoose();
		theRecruitment.setCategory(theCategory);
		theRecruitment.setStatus(1);
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		theRecruitment.setCreatedAt(formater.format(new Date()));
		currentSession.save(theRecruitment);
	}

	// Update a recruitment.
	@Override
	public void update(Recruitment theRecruitment) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.update(theRecruitment);

	}

	// Delete a recruitment by its ID.
	@Override
	public void deleteRecruitment(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = currentSession.createQuery("delete from Recruitment where Id=:recruitmentId");
		theQuery.setParameter("recruitmentId", theId);
		theQuery.executeUpdate();
	}

	// Get a list of recruitments sorted by salary, creation date, and type.
	@Override
	public List<Recruitment> getResultRecruitmentByCompany(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuery = currentSession
				.createQuery("SELECT r FROM Recruitment r join r.company c WHERE c.id = :companyId", Recruitment.class);
		theQuery.setParameter("companyId", theId);
		List<Recruitment> recruitments = theQuery.getResultList();
		return recruitments;
	}

	// This method returns a list of Recruitment by salary objects from the database
	@Override
	public List<Recruitment> getResultRecruitmentBySalary() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuery = currentSession.createQuery(
				"SELECT r FROM Recruitment r ORDER BY r.createdAt DESC, r.type ASC, r.salary DESC", Recruitment.class);
		theQuery.setMaxResults(5);
		List<Recruitment> recruitments = theQuery.getResultList();
		return recruitments;
	}

	// Search recruitments by a given title.
	@Override
	public List<Recruitment> getResultRecruitment(String searchTerm) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuery = currentSession.createQuery("FROM Recruitment r WHERE r.title LIKE :searchTerm",
				Recruitment.class);

		theQuery.setParameter("searchTerm", "%" + searchTerm + "%");
		List<Recruitment> recruitments = theQuery.getResultList();
		return recruitments;
	}

	// Search recruitments by a given address.
	@Override
	public List<Recruitment> getResultAdress(String searchTerm) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuery = currentSession.createQuery("FROM Recruitment r WHERE r.address LIKE :searchTerm",
				Recruitment.class);

		theQuery.setParameter("searchTerm", "%" + searchTerm + "%");
		List<Recruitment> recruitments = theQuery.getResultList();
		return recruitments;
	}

	// Search recruitments by a given company.
	@Override
	public List<Recruitment> getResultCompany(String searchTerm) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuery = currentSession.createQuery(
				"SELECT r FROM Recruitment r JOIN r.company c WHERE c.nameCompany LIKE :searchTerm", Recruitment.class);
		theQuery.setParameter("searchTerm", "%" + searchTerm + "%");
		List<Recruitment> recruitments = theQuery.getResultList();
		return recruitments;
	}

	@Override
	public List<Recruitment> getListRecruitmentsbyCategory(int categoryId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recruitment> theQuery = currentSession.createQuery(
				"SELECT r FROM Recruitment r JOIN r.category c WHERE c.id = :categoryId", Recruitment.class);
		theQuery.setParameter("categoryId", categoryId);
		List<Recruitment> recruitments = theQuery.getResultList();
		return recruitments;
	}

}
