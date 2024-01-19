package com.tranv.workcv.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.CategoryDAO;
import com.tranv.workcv.entity.Category;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
	// DAO handles operations related to the Category object

	@Autowired
	SessionFactory sessionFactory;

	// Retrieve all categories from the database.
	@Override
	public List<Category> getCategories() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Category> theQuery = currentSession.createQuery("from Category", Category.class);
		List<Category> categories = theQuery.getResultList();
		return categories;
	}

	// Retrieve a specific category from the database based on the given ID.
	@Override
	public Category getCategoryById(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Category category = currentSession.get(Category.class, theId);
		return category;
	}

	// Retrieve the top 4 categories with the highest number of chooses.
	@Override
	public List<Category> getTop4Categorys() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Category> theQuerry = currentSession.createQuery("SELECT c FROM Category c ORDER BY c.numberChoose DESC",
				Category.class);
		theQuerry.setMaxResults(4);
		List<Category> Categorys = theQuerry.getResultList();
		return Categorys;
	}
}
