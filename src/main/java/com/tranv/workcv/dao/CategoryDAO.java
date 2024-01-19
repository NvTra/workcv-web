package com.tranv.workcv.dao;

import java.util.List;

import com.tranv.workcv.entity.Category;

public interface CategoryDAO {
	public List<Category> getCategories();

	public Category getCategoryById(int theId);

	public List<Category> getTop4Categorys();
}
