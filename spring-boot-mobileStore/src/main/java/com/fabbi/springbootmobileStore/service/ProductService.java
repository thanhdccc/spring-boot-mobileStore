package com.fabbi.springbootmobileStore.service;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.dto.ProductDTO;

public interface ProductService {

	ResultBean insertProduct(ProductDTO request);

	ResultBean updateProduct(ProductDTO request);

	ResultBean getProductById(Integer id);

	ResultBean getAllProduct();

	ResultBean deleteProduct(Integer id);
}
