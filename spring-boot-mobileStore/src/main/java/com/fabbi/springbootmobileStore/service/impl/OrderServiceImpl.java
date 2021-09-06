package com.fabbi.springbootmobileStore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.OrderDetailDTO;
import com.fabbi.springbootmobileStore.dto.OrderResponseDTO;
import com.fabbi.springbootmobileStore.entity.OrderDetailEntity;
import com.fabbi.springbootmobileStore.entity.OrderEntity;
import com.fabbi.springbootmobileStore.entity.ProductEntity;
import com.fabbi.springbootmobileStore.exception.LogicErrorException;
import com.fabbi.springbootmobileStore.repository.OrderDetailRepository;
import com.fabbi.springbootmobileStore.repository.OrderRepository;
import com.fabbi.springbootmobileStore.repository.ProductRepository;
import com.fabbi.springbootmobileStore.service.OrderService;
import com.fabbi.springbootmobileStore.util.ObjectMapperUtils;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private ProductRepository productRepository;

	private ResultBean resultBean;

	@Override
	public ResultBean insertOrder() {
		resultBean = new ResultBean();
		log.info("######## Begin insert order ########");

		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setAmount(0f);
		orderEntity.setIsProcess(false);

		try {
			orderRepository.save(orderEntity);
		} catch (Exception e) {
			log.error("Error to insert order");
			throw new LogicErrorException(e.getMessage(), Constants.CREATE_FAIL);
		}

		resultBean.setMessage(Constants.CREATE_SUCCESS);

		log.info("######## End insert order ########");

		return resultBean;
	}

	@Override
	public ResultBean updateOrder(Integer id) {
		resultBean = new ResultBean();
		log.info("######## Begin update order by id ########");

		validateId(id);

		OrderEntity orderEntity = orderRepository.findOneById(id);

		if (orderEntity != null) {
			try {
				orderEntity.setIsProcess(true);
				orderRepository.save(orderEntity);
			} catch (Exception e) {
				log.error("Error to update order");
				throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
			}
		} else {
			log.error("Cannot get order with id: " + id);
			throw new LogicErrorException(Constants.ORDER_ID_ERROR, Constants.ORDER_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		resultBean.setMessage(Constants.UPDATE_SUCCESS);

		log.info("######## End insert order ########");

		return resultBean;
	}

	@Synchronized
	@Override
	public ResultBean deleteOrder(Integer id) {
		resultBean = new ResultBean();
		log.info("######## Begin delete order by id ########");

		validateId(id);

		OrderEntity orderEntity = orderRepository.findOneById(id);

		if (orderEntity == null) {
			log.error("Cannot get order with id: " + id);
			throw new LogicErrorException(Constants.ORDER_ID_ERROR, Constants.ORDER_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		if (orderEntity.getIsProcess() == true) {
			log.error("Cannot delete order with id: [" + id + "] because it had been processed");
			throw new LogicErrorException(Constants.ORDER_ID_ERROR, Constants.ORDER_HAD_BEEN_PROCESSED_ERROR,
					Constants.DELETE_FAIL);
		}

		List<OrderDetailEntity> itemList = orderDetailRepository.findAllByOrderId(id);

		if (itemList.size() == 0) {

			try {
				orderRepository.deleteById(id);
			} catch (Exception e) {
				log.error("Error to delete order");
				throw new LogicErrorException(e.getMessage(), Constants.DELETE_FAIL);
			}
		} else {

			for (OrderDetailEntity item : itemList) {

				try {
					orderDetailRepository.deleteById(item.getId());
				} catch (Exception e) {
					log.error("Error to delete product with id: [" + item.getProduct().getId() + "]");
					throw new LogicErrorException(e.getMessage(), Constants.DELETE_FAIL);
				}

				ProductEntity productEntity = productRepository.findOneById(item.getProduct().getId());
				productEntity.setQuantity(productEntity.getQuantity() + item.getQuantity());

				try {
					productRepository.save(productEntity);
				} catch (Exception e) {
					log.error("Error to update quantity with product id: [" + item.getProduct().getId() + "]");
					throw new LogicErrorException(e.getMessage(), Constants.UPDATE_FAIL);
				}
			}

			try {
				orderRepository.deleteById(id);
			} catch (Exception e) {
				log.error("Error to delete order");
				throw new LogicErrorException(e.getMessage(), Constants.DELETE_FAIL);
			}
		}

		resultBean.setMessage(Constants.DELETE_SUCCESS);

		log.info("######## End delete order by id ########");

		return resultBean;
	}

	@Override
	public ResultBean getAllOrder() {
		resultBean = new ResultBean();
		log.info("######## Begin get all order ########");

		List<OrderEntity> orderList = orderRepository.findAll();
		List<OrderResponseDTO> orderDTOList = ObjectMapperUtils.mapAll(orderList, OrderResponseDTO.class);

		for (OrderResponseDTO item : orderDTOList) {
			List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.findAllByOrderId(item.getId());
			List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();

			for (OrderDetailEntity detailEntity : orderDetailEntity) {
				OrderDetailDTO detailDTO = new OrderDetailDTO();
				detailDTO.setId(detailEntity.getId());
				detailDTO.setOrderId(detailEntity.getOrder().getId());
				detailDTO.setProductId(detailEntity.getProduct().getId());
				detailDTO.setQuantity(detailEntity.getQuantity());
				detailDTO.setPrice(detailEntity.getPrice());
				detailDTO.setAmount(detailEntity.getAmount());
				
				orderDetailDTOList.add(detailDTO);
			}

			item.setOrderItems(orderDetailDTOList);
		}

		resultBean.setData(orderDTOList);
		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get all order ########");

		return resultBean;
	}

	@Override
	public ResultBean getOrderById(Integer id) {
		resultBean = new ResultBean();
		log.info("######## Begin get order by id ########");

		validateId(id);

		OrderEntity orderEntity = orderRepository.findOneById(id);

		if (orderEntity == null) {
			log.error("Cannot get order with id: " + id);
			throw new LogicErrorException(Constants.ORDER_ID_ERROR, Constants.ORDER_NOT_EXIST_ERROR,
					Constants.GET_FAIL);
		}

		OrderResponseDTO orderDTO = ObjectMapperUtils.map(orderEntity, OrderResponseDTO.class);
		List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.findAllByOrderId(id);
		List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
		
		for (OrderDetailEntity detailEntity : orderDetailEntity) {
			OrderDetailDTO detailDTO = new OrderDetailDTO();
			detailDTO.setId(detailEntity.getId());
			detailDTO.setOrderId(detailEntity.getOrder().getId());
			detailDTO.setProductId(detailEntity.getProduct().getId());
			detailDTO.setQuantity(detailEntity.getQuantity());
			detailDTO.setPrice(detailEntity.getPrice());
			detailDTO.setAmount(detailEntity.getAmount());
			
			orderDetailDTOList.add(detailDTO);
		}

		orderDTO.setOrderItems(orderDetailDTOList);

		resultBean.setData(orderDTO);
		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get order by id ########");

		return resultBean;
	}

	private void validateId(Integer id) {

		if (id <= 0 || id > Constants.MAX_VALUE_NUMBER) {
			log.error("######## Error with limit order id ########");
			throw new LogicErrorException(Constants.ORDER_PROP_ID, Constants.ORDER_ID_ERROR, Constants.ORDER_ID_ERROR);
		}
	}

}
