package com.fabbi.springbootmobileStore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.ProductDTO;
import com.fabbi.springbootmobileStore.entity.CategoryEntity;
import com.fabbi.springbootmobileStore.entity.ProductEntity;
import com.fabbi.springbootmobileStore.exception.LogicErrorException;
import com.fabbi.springbootmobileStore.repository.CategoryRepository;
import com.fabbi.springbootmobileStore.repository.ProductRepository;
import com.fabbi.springbootmobileStore.service.ProductService;
import com.fabbi.springbootmobileStore.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	private ResultBean resultBean;

	@Override
	public ResultBean insertProduct(ProductDTO request) {
		resultBean = new ResultBean();
		Integer quantity = request.getQuantity();
		Float price = request.getPrice();
		Integer categoryId = request.getCategoryId();
		log.info("######## Begin insert product ########");
		
		validateId(categoryId);
		validateQuantity(quantity);
		validatePrice(price);

		ProductEntity productEntity = ObjectMapperUtils.map(request, ProductEntity.class);
		CategoryEntity categoryEntity = categoryRepository.findOneById(categoryId);

		if (categoryEntity == null) {
			log.error("Cannot insert product because not exist category with id: " + categoryId);
			throw new LogicErrorException(Constants.PRODUCT_PROP_NAME, Constants.CATEGORY_EXIST_ERROR,
					"category.id.notexist.error");
		}
		
		productEntity.setCategory(categoryEntity);
		
		boolean result = productRepository.existsByNameAndCategoryId(request.getName(), categoryId);

		if (!result) {

			try {
				productRepository.save(productEntity);
			} catch (Exception e) {
				log.error("Error to insert product");
				throw new LogicErrorException(e.getMessage(), "product.insert.error");
			}
		} else {
			log.error("Cannot insert product because the name and category are existed");
			throw new LogicErrorException(Constants.PRODUCT_PROP_NAME + " or " + Constants.PRODUCT_PROP_CATEGORY,
					Constants.PRODUCT_EXIST_ERROR, "product.name.existed.error");
		}

		resultBean.setMessage(Constants.CREATE_SUCCESS);

		log.info("######## End insert product ########");

		return resultBean;
	}

	@Override
	public ResultBean updateProduct(Integer id, ProductDTO request) {
		resultBean = new ResultBean();
		String name = request.getName();
		Integer quantity = request.getQuantity();
		Float price = request.getPrice();
		Integer categoryId = request.getCategoryId();
		log.info("######## Begin update product by id ########");

		validateId(id);
		validateId(categoryId);
		validateQuantity(quantity);
		validatePrice(price);

		if (id == null) {
			log.error("Cannot update product because product id is not exist");
			throw new LogicErrorException(Constants.PRODUCT_PROP_ID, Constants.PRODUCT_ID_NULL_ERROR,
					"product.id.null.error");
		} else {
			try {
				ProductEntity productEntity = productRepository.findOneById(id);
				

				if (productEntity == null) {
					log.error("Cannot update product because product id is not exist");
					throw new LogicErrorException(Constants.PRODUCT_PROP_ID, Constants.PRODUCT_NOT_EXIST_ERROR,
							"product.notexist.error");
				}

				if (productRepository.existsByNameAndCategoryIdAndIdNot(name, categoryId, id)) {
					log.error("Cannot update product because exist a product with same name and category");
					throw new LogicErrorException(Constants.PRODUCT_PROP_NAME + " or " + Constants.PRODUCT_PROP_CATEGORY,
							Constants.PRODUCT_EXIST_ERROR, "product.name.existed.error");
				}

				Long version = productEntity.getVersion();
				productEntity = ObjectMapperUtils.map(request, ProductEntity.class);
				CategoryEntity categoryEntity = categoryRepository.findOneById(categoryId);
				productEntity.setCategory(categoryEntity);
				productEntity.setVersion(version++);
				productEntity.setId(id);

				try {
					productRepository.save(productEntity);
				} catch (Exception e) {
					log.error("Error to update product");
					throw new LogicErrorException(e.getMessage(), "product.update.error");
				}
				
			} catch (Exception e) {
				log.warn("---- Product information had been changed...");
				throw new LogicErrorException(e.getMessage(), "product.update.error");
			}
		}

		resultBean.setMessage(Constants.UPDATE_SUCCESS);

		log.info("######## End update product by id ########");

		return resultBean;
	}

	@Override
	public ResultBean getProductById(Integer id) {
		resultBean = new ResultBean();
		ProductEntity productEntity = null;
		log.info("######## Begin get product by id and category ########");

		validateId(id);

		try {
			productEntity = productRepository.findOneById(id);
		} catch (Exception e) {
			log.error("Error to get product");
			throw new LogicErrorException(e.getMessage(), "product.get.error");
		}

		if (productEntity != null) {
			ProductDTO productDTO = ObjectMapperUtils.map(productEntity, ProductDTO.class);
			productDTO.setCategoryId(productEntity.getCategory().getId());
			resultBean.setData(productDTO);
		} else {
			log.error("Cannot get product with id: [" + id + "]");
			throw new LogicErrorException(Constants.PRODUCT_PROP_ID, Constants.PRODUCT_NOT_EXIST_ERROR,
					"product.notexist.error");
		}

		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get product by id and category ########");

		return resultBean;
	}

	@Override
	public ResultBean getAllProduct() {
		resultBean = new ResultBean();
		List<ProductEntity> productList = null;
		List<ProductDTO> productDTO = new ArrayList<>();
		log.info("######## Begin get all product ########");

		try {
			productList = productRepository.findAll();
		} catch (Exception e) {
			log.error("Error to get product");
			throw new LogicErrorException(e.getMessage(), "product.get.error");
		}
		for (ProductEntity item : productList) {
			ProductDTO tmp = ObjectMapperUtils.map(item, ProductDTO.class);
			tmp.setCategoryId(item.getCategory().getId());
			productDTO.add(tmp);
		}

		resultBean.setData(productDTO);
		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get all product ########");

		return resultBean;
	}

	@Override
	public ResultBean deleteProduct(Integer id) {
		resultBean = new ResultBean();
		ProductEntity productEntity = null;
		log.info("######## Begin delete product by id and category ########");

		validateId(id);

		try {
			productEntity = productRepository.findOneById(id);
		} catch (Exception e) {
			log.error("Error to get product");
			throw new LogicErrorException(e.getMessage(), "product.get.error");
		}

		if (productEntity != null) {
			try {
				productRepository.deleteById(id);
			} catch (Exception e) {
				log.error("Error to delete product");
				throw new LogicErrorException(e.getMessage(), "product.delete.error");
			}
		} else {
			log.error("Cannot get product with id: [" + id + "]");
			throw new LogicErrorException(Constants.PRODUCT_PROP_ID, Constants.PRODUCT_NOT_EXIST_ERROR,
					"product.notexist.error");
		}

		resultBean.setMessage(Constants.DELETE_SUCCESS);

		log.info("######## End delete product by id and category ########");

		return resultBean;
	}

	private void validateQuantity(Integer quantity) {

		if (quantity < 0 || quantity > Constants.MAX_VALUE_QUANTITY) {
			log.error("######## Error with limit integer number ########");
			throw new LogicErrorException(Constants.PRODUCT_PROP_QUANTITY, "Quantity is out of range",
					"product.quantity.out.range.error");
		}
	}
	
	private void validatePrice(Float price) {

		if (price < 0 || price > Constants.MAX_VALUE_PRICE) {
			log.error("######## Error with limit number ########");
			throw new LogicErrorException(Constants.PRODUCT_PROP_QUANTITY, "Price is out of range",
					"product.price.out.range.error");
		}
	}

	private void validateId(Integer id) {

		if (id <= 0 || id > Constants.MAX_VALUE_NUMBER) {
			log.error("######## Error with limit integer number ########");
			throw new LogicErrorException("Product id or Category id", "Product id or Category id is out of range",
					"id.out.range.error");
		}
	}
}
