package com.fabbi.springbootmobileStore.service;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.dto.CategoryDTO;

public interface CategoryService {

	ResultBean insertCategory(CategoryDTO request);
	
	ResultBean updateCategory(Integer id, CategoryDTO request);
	
	ResultBean getCategoryById(Integer id);
	
	ResultBean getAllCategory();
	
	ResultBean deleteCategory(Integer id);
}
