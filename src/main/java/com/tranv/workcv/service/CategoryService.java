package com.tranv.workcv.service;

import java.util.List;

import com.tranv.workcv.entity.Category;

public interface CategoryService {
	public List<Category> getCategories();

	public Category getCategoryById(int theId);

	public List<Category> getTop4Categorys();
}
