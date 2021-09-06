package com.fabbi.springbootmobileStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.ProductOrderDTO;
import com.fabbi.springbootmobileStore.entity.OrderDetailEntity;
import com.fabbi.springbootmobileStore.entity.OrderEntity;
import com.fabbi.springbootmobileStore.entity.ProductEntity;
import com.fabbi.springbootmobileStore.exception.LogicErrorException;
import com.fabbi.springbootmobileStore.repository.OrderDetailRepository;
import com.fabbi.springbootmobileStore.repository.OrderRepository;
import com.fabbi.springbootmobileStore.repository.ProductRepository;
import com.fabbi.springbootmobileStore.service.OrderDetailService;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	private ResultBean resultBean;

	@Synchronized
	@Override
	public ResultBean insertItem(ProductOrderDTO request) {
		resultBean = new ResultBean();
		log.info("######## Begin insert item into order ########");

		OrderDetailEntity orderDetailEntity = orderDetailRepository.findOneByOrderIdAndProductId(request.getOrderId(),
				request.getProductId());

		if (orderDetailEntity != null) {
			log.error("Cannot insert this item into order because item is already exist");
			throw new LogicErrorException(Constants.ORDER_PROP_ORDER_AND_PRODUCT_ID,
					Constants.ORDER_PROP_ORDER_AND_PRODUCT_ID_ERROR, Constants.CREATE_FAIL);
		}

		OrderEntity orderEntity = orderRepository.findOneById(request.getOrderId());

		if (orderEntity == null) {
			log.error("Cannot get order with id: " + request.getOrderId());
			throw new LogicErrorException(Constants.ORDER_PROP_ID_DETAIL, Constants.ORDER_NOT_EXIST_ERROR, Constants.GET_FAIL);
		}

		ProductEntity productEntity = productRepository.findOneById(request.getProductId());

		if (productEntity == null) {
			log.error("Product not found");
			throw new LogicErrorException(Constants.PRODUCT_PROP_ID, Constants.PRODUCT_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		Integer stockQuantity = productEntity.getQuantity();

		if (stockQuantity - request.getQuantity() < 0) {
			log.error("Product quantity out of stock");
			throw new LogicErrorException(Constants.PRODUCT_PROP_QUANTITY, Constants.PRODUCT_QUANTITY_NOT_ENOUGH_ERROR,
					Constants.CREATE_FAIL);
		}

		orderDetailEntity = new OrderDetailEntity();
		orderDetailEntity.setOrder(orderEntity);
		orderDetailEntity.setProduct(productEntity);
		orderDetailEntity.setQuantity(request.getQuantity());
		orderDetailEntity.setPrice(productEntity.getPrice());
		orderDetailEntity.setAmount(request.getQuantity() * productEntity.getPrice());

		try {
			orderDetailRepository.save(orderDetailEntity);
		} catch (Exception e) {
			log.error("Error to insert item");
			throw new LogicErrorException(e.getMessage(), Constants.CREATE_FAIL);
		}

		productEntity.setQuantity(stockQuantity - request.getQuantity());

		try {
			productRepository.save(productEntity);
		} catch (Exception e) {
			log.error("Error to update product quantity");
			throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
		}

		Float orderAmount = orderEntity.getAmount() + orderDetailEntity.getAmount();
		orderEntity.setAmount(orderAmount);

		try {
			orderRepository.save(orderEntity);
		} catch (Exception e) {
			log.error("Error to update order amount");
			throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
		}

		resultBean.setMessage(Constants.CREATE_SUCCESS);

		log.info("######## End insert item into order ########");

		return resultBean;
	}

	@Synchronized
	@Override
	public ResultBean updateItem(Integer id, ProductOrderDTO request) {
		resultBean = new ResultBean();
		log.info("######## Begin update item into order ########");

		validateId(id);

		OrderEntity orderEntity = orderRepository.findOneById(id);

		if (orderEntity == null) {
			log.error("Cannot get order with id: [" + id + "]");
			throw new LogicErrorException(Constants.ORDER_PROP_ID_DETAIL, Constants.ORDER_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		if (orderEntity.getIsProcess() == true) {
			log.error("Cannot update order with id: [" + id + "] because it had been processed");
			throw new LogicErrorException(Constants.ORDER_PROP_ID_DETAIL, Constants.ORDER_HAD_BEEN_PROCESSED_ERROR,
					Constants.DELETE_FAIL);
		}

		Integer itemId = request.getId();

		if (itemId == null) {
			log.error("Order detail id is mandatory");
			throw new LogicErrorException(Constants.ORDER_DETAIL_ID, Constants.ORDER_DETAIL_ID_NULL_ERROR,
					Constants.ORDER_DETAIL_ID_NULL_ERROR);
		}

		OrderDetailEntity orderDetailEntity = orderDetailRepository.findOneById(itemId);

		if (orderDetailEntity == null) {
			log.error("Cannot get item with id: [" + itemId + "]");
			throw new LogicErrorException(Constants.ORDER_DETAIL_ID, Constants.ORDER_DETAIL_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		ProductEntity productEntity = productRepository.findOneById(request.getProductId());

		if (productEntity == null) {
			log.error("Product not found");
			throw new LogicErrorException(Constants.PRODUCT_PROP_ID, Constants.PRODUCT_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		Integer oldQuantity = orderDetailEntity.getQuantity();
		Integer stockQuantity = productEntity.getQuantity();

		if ((stockQuantity + oldQuantity) - request.getQuantity() < 0) {
			log.error("Product quantity out of stock");
			throw new LogicErrorException(Constants.PRODUCT_PROP_QUANTITY, Constants.PRODUCT_QUANTITY_NOT_ENOUGH_ERROR,
					Constants.CREATE_FAIL);
		}

		Float oldProductAmount = orderDetailEntity.getAmount();
		orderDetailEntity.setQuantity(request.getQuantity());
		orderDetailEntity.setAmount(orderDetailEntity.getQuantity() * orderDetailEntity.getPrice());

		try {
			orderDetailRepository.save(orderDetailEntity);
		} catch (Exception e) {
			log.error("Error to insert item");
			throw new LogicErrorException(e.getMessage(), Constants.CREATE_FAIL);
		}

		productEntity.setQuantity((stockQuantity + oldQuantity) - request.getQuantity());

		try {
			productRepository.save(productEntity);
		} catch (Exception e) {
			log.error("Error to update product quantity");
			throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
		}

		Float orderAmount = (orderEntity.getAmount() - oldProductAmount) + orderDetailEntity.getAmount();
		orderEntity.setAmount(orderAmount);

		try {
			orderRepository.save(orderEntity);
		} catch (Exception e) {
			log.error("Error to update order amount");
			throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
		}

		resultBean.setMessage(Constants.UPDATE_SUCCESS);

		log.info("######## End update item into order ########");

		return resultBean;
	}

	@Synchronized
	@Override
	public ResultBean removeItem(Integer orderId, Integer itemId) {
		resultBean = new ResultBean();
		log.info("######## Begin remove item into order ########");

		validateId(itemId);
		validateId(orderId);

		OrderDetailEntity orderDetailEntity = orderDetailRepository.findOneById(itemId);

		if (orderDetailEntity == null) {
			log.error("Cannot get item with id: [" + itemId + "]");
			throw new LogicErrorException(Constants.ID_PATH, Constants.ORDER_DETAIL_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		Integer productId = orderDetailEntity.getProduct().getId();

		OrderEntity orderEntity = orderRepository.findOneById(orderId);

		if (orderEntity == null) {
			log.error("Cannot get order with id: " + orderId);
			throw new LogicErrorException(Constants.ORDER_PROP_ID_DETAIL, Constants.ORDER_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		if (orderEntity.getIsProcess() == true) {
			log.error("Cannot delete order with id: [" + orderId + "] because it had been processed");
			throw new LogicErrorException(Constants.ORDER_PROP_ID_DETAIL, Constants.ORDER_HAD_BEEN_PROCESSED_ERROR,
					Constants.DELETE_FAIL);
		}

		try {
			orderDetailRepository.deleteById(itemId);
		} catch (Exception e) {
			log.error("Error to remove product in order");
			throw new LogicErrorException(e.getMessage(), Constants.DELETE_FAIL);
		}

		ProductEntity productEntity = productRepository.findOneById(productId);

		if (productEntity == null) {
			log.error("Cannot get product with id: " + productId);
			throw new LogicErrorException(Constants.PRODUCT_PROP_ID, Constants.PRODUCT_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		productEntity.setQuantity(productEntity.getQuantity() + orderDetailEntity.getQuantity());

		try {
			productRepository.save(productEntity);
		} catch (Exception e) {
			log.error("Error to update product quantity after remove in order");
			throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
		}

		Float orderAmount = orderEntity.getAmount() - orderDetailEntity.getAmount();
		orderEntity.setAmount(orderAmount);

		try {
			orderRepository.save(orderEntity);
		} catch (Exception e) {
			log.error("Error to update order amount");
			throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
		}

		resultBean.setMessage(Constants.DELETE_SUCCESS);

		log.info("######## End remove item into order ########");

		return resultBean;
	}

	private void validateId(Integer id) {

		if (id <= 0 || id > Constants.MAX_VALUE_NUMBER) {
			log.error("######## Error with limit id ########");
			throw new LogicErrorException(Constants.PROP_ID, Constants.ID_ERROR, Constants.ID_ERROR);
		}
	}
}
