package com.fabbi.springbootmobileStore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.CategoryDTO;
import com.fabbi.springbootmobileStore.service.CategoryService;

@CrossOrigin
@RestController
@RequestMapping(value = Constants.API_PATH + Constants.CATEGORY_PATH)
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> createCategory(@Valid @RequestBody CategoryDTO request) {
		ResultBean resultBean = categoryService.insertCategory(request);
		return new ResponseEntity<>(resultBean, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> updateCategory(@Valid @RequestBody CategoryDTO request,
			@PathVariable(Constants.CATEGORY_PROP_ID) Integer id) {
		ResultBean resultBean = categoryService.updateCategory(id, request);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> deleteCategory(@PathVariable(Constants.CATEGORY_PROP_ID) Integer id) {
		ResultBean resultBean = categoryService.deleteCategory(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResultBean> getAllCategory() {
		ResultBean resultBean = categoryService.getAllCategory();
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResultBean> getCategoryById(@PathVariable(Constants.CATEGORY_PROP_ID) Integer id) {
		ResultBean resultBean = categoryService.getCategoryById(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}
}
