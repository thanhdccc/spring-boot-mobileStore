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
import com.fabbi.springbootmobileStore.dto.ProductDTO;
import com.fabbi.springbootmobileStore.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping(value = Constants.API_PATH + Constants.PRODUCT_PATH)
public class ProductController {

	@Autowired
	ProductService productService;

	@RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> createProduct(@Valid @RequestBody ProductDTO request) {
		ResultBean resultBean = productService.insertProduct(request);
		return new ResponseEntity<>(resultBean, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> updateProduct(@Valid @RequestBody ProductDTO request,
			@PathVariable(Constants.PRODUCT_PROP_ID) Integer id) {
		ResultBean resultBean = productService.updateProduct(id, request);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> updateProduct(@PathVariable(Constants.PRODUCT_PROP_ID) Integer id) {
		ResultBean resultBean = productService.deleteProduct(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResultBean> getProductById(@PathVariable(Constants.PRODUCT_PROP_ID) Integer id) {
		ResultBean resultBean = productService.getProductById(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResultBean> getAllProduct() {
		ResultBean resultBean = productService.getAllProduct();
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}
}
