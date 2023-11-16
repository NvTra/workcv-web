package com.tranv.workcv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.CategoryDAO;
import com.tranv.workcv.entity.Category;
import com.tranv.workcv.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	// Service handles operations related to the Category object

	@Autowired
	private CategoryDAO categoryDAO;

	// Retrieve all categories from the database.
	@Override
	@Transactional
	public List<Category> getCategories() {
		return categoryDAO.getCategories();
	}

	// Retrieve a specific category from the database based on the given ID.
	@Override
	@Transactional
	public Category getCategoryById(int theId) {
		return categoryDAO.getCategoryById(theId);
	}

	// Retrieve the top 4 categories with the highest number of chooses.
	@Override
	@Transactional
	public List<Category> getTop4Categorys() {
		return categoryDAO.getTop4Categorys();
	}

}
