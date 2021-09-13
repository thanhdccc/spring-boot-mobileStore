package com.fabbi.springbootmobileStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.CategoryDTO;
import com.fabbi.springbootmobileStore.entity.CategoryEntity;
import com.fabbi.springbootmobileStore.entity.ProductEntity;
import com.fabbi.springbootmobileStore.exception.LogicErrorException;
import com.fabbi.springbootmobileStore.repository.CategoryRepository;
import com.fabbi.springbootmobileStore.repository.ProductRepository;
import com.fabbi.springbootmobileStore.service.CategoryService;
import com.fabbi.springbootmobileStore.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	private ResultBean resultBean;

	@Override
	public ResultBean insertCategory(CategoryDTO request) {
		resultBean = new ResultBean();
		log.info("######## Begin insert category ########");

		CategoryEntity categoryEntity = ObjectMapperUtils.map(request, CategoryEntity.class);

		if (!categoryRepository.existsByName(categoryEntity.getName())) {
			try {
				categoryRepository.save(categoryEntity);
			} catch (Exception e) {
				log.error("Error to insert category");
				throw new LogicErrorException(e.getMessage(), "category.insert.error");
			}
		} else {
			log.error("Cannot insert category because the name is existed");
			throw new LogicErrorException(Constants.CATEGORY_PROP_NAME, Constants.CATEGORY_EXIST_ERROR,
					"category.name.isexisted.error");
		}

		resultBean.setMessage(Constants.CREATE_SUCCESS);

		log.info("######## End insert category ########");

		return resultBean;
	}

	@Override
	public ResultBean updateCategory(Integer id, CategoryDTO request) {
		resultBean = new ResultBean();
		String name = request.getName();
		log.info("######## Begin update category ########");

		validateCategoryId(id);

		if (id == null) {
			log.error("Cannot update category because category id is not exist");
			throw new LogicErrorException(Constants.CATEGORY_PROP_ID, Constants.CATEGORY_ID_NULL_ERROR,
					"category.id.null.error");
		} else {
			CategoryEntity categoryEntity = categoryRepository.findOneById(id);

			if (categoryEntity == null) {
				log.error("Cannot update category because category is not exist");
				throw new LogicErrorException(Constants.CATEGORY_PROP_ID, Constants.CATEGORY_NOT_EXIST_ERROR,
						"category.notexist.error");
			}

			if (categoryRepository.existsByNameAndIdNot(name, id)) {
				log.error("Cannot update category because there is exist category with same name");
				throw new LogicErrorException(Constants.CATEGORY_PROP_NAME, Constants.CATEGORY_EXIST_ERROR,
						"category.name.exist.error");
			}

			categoryEntity = ObjectMapperUtils.map(request, CategoryEntity.class);
			categoryEntity.setId(id);
			
			try {
				categoryRepository.save(categoryEntity);
			} catch (Exception e) {
				log.error("Error to update category");
				throw new LogicErrorException(e.getMessage(), "category.update.error");
			}
		}

		resultBean.setMessage(Constants.UPDATE_SUCCESS);

		log.info("######## End update category ########");

		return resultBean;
	}

	@Override
	public ResultBean getCategoryById(Integer id) {
		resultBean = new ResultBean();
		CategoryEntity categoryEntity = null;
		log.info("######## Begin get category by id ########");

		validateCategoryId(id);

		try {
			categoryEntity = categoryRepository.findOneById(id);
		} catch (Exception e) {
			log.error("Error to get category");
			throw new LogicErrorException(e.getMessage(), "category.get.error");
		}

		if (categoryEntity != null) {
			CategoryDTO categoryDTO = ObjectMapperUtils.map(categoryEntity, CategoryDTO.class);
			resultBean.setData(categoryDTO);
		} else {
			log.error("Cannot get category with id: " + id);
			throw new LogicErrorException(Constants.CATEGORY_PROP_ID, Constants.CATEGORY_NOT_EXIST_ERROR,
					"category.notexist.error");
		}

		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get category by id ########");

		return resultBean;
	}

	@Override
	public ResultBean getAllCategory() {
		resultBean = new ResultBean();
		List<CategoryEntity> categoryList = null;
		log.info("######## Begin get all category ########");

		try {
			categoryList = categoryRepository.findAll();
		} catch (Exception e) {
			log.error("Error to get category");
			throw new LogicErrorException(e.getMessage(), "category.get.error");
		}

		List<CategoryDTO> categoryDtoList = ObjectMapperUtils.mapAll(categoryList, CategoryDTO.class);

		resultBean.setData(categoryDtoList);
		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get all category ########");

		return resultBean;
	}

	@Override
	public ResultBean deleteCategory(Integer id) {
		resultBean = new ResultBean();
		CategoryEntity categoryEntity = null;
		log.info("######## Begin delete category by id ########");

		validateCategoryId(id);

		try {
			categoryEntity = categoryRepository.findOneById(id);
		} catch (Exception e) {
			log.error("Error to get category");
			throw new LogicErrorException(e.getMessage(), "category.get.error");
		}

		if (categoryEntity != null) {		
			ProductEntity productEntity = productRepository.findOneByCategoryId(id);

			if (productEntity != null) {
				log.error("Cannot delete category with id: " + id + " because there is product belong to");
				throw new LogicErrorException(Constants.CATEGORY_PROP_ID, Constants.CATEGORY_PRODUCT_BELONG_ERROR,
						"product.belongto.category.error");
			} else {
				try {
					categoryRepository.deleteById(id);
				} catch (Exception e) {
					log.error("Error to delete category");
					throw new LogicErrorException(e.getMessage(), "category.delete.error");
				}
			}
		} else {
			log.error("Cannot delete category with id: " + id);
			throw new LogicErrorException(Constants.CATEGORY_PROP_ID, Constants.CATEGORY_NOT_EXIST_ERROR,
					"category.notexist.error");
		}

		resultBean.setMessage(Constants.DELETE_SUCCESS);

		log.info("######## End delete category by id ########");

		return resultBean;
	}

	private void validateCategoryId(Integer id) {

		if (id <= 0 || id > Constants.MAX_VALUE_NUMBER) {
			log.error("######## Error with limit category id ########");
			throw new LogicErrorException(Constants.CATEGORY_PROP_ID, Constants.CATEGORY_ID_ERROR,
					"category.id.out.range.error");
		}
	}
}
