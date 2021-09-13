package com.fabbi.springbootmobileStore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.OrderDTO;
import com.fabbi.springbootmobileStore.dto.OrderItemDTO;
import com.fabbi.springbootmobileStore.dto.UserDetailsImpl;
import com.fabbi.springbootmobileStore.entity.OrderDetailEntity;
import com.fabbi.springbootmobileStore.entity.OrderEntity;
import com.fabbi.springbootmobileStore.entity.ProductEntity;
import com.fabbi.springbootmobileStore.exception.LogicErrorException;
import com.fabbi.springbootmobileStore.repository.OrderDetailRepository;
import com.fabbi.springbootmobileStore.repository.OrderRepository;
import com.fabbi.springbootmobileStore.repository.ProductRepository;
import com.fabbi.springbootmobileStore.service.OrderService;
import com.fabbi.springbootmobileStore.util.ObjectMapperUtils;

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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ResultBean insertOrder(OrderDTO request) {
		resultBean = new ResultBean();
		Float totalAmount = 0f;
		List<OrderDetailEntity> itemEntityList = new ArrayList<>();

//		try {
//			log.info("******** Sleep ********");
//			Thread.sleep(5000);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}

		log.info("######## Begin insert order ########");

		UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		request.setUserId(userDetail.getId());
		request.setIsProcess(false);

		OrderEntity orderEntity = ObjectMapperUtils.map(request, OrderEntity.class);

		List<OrderItemDTO> items = request.getOrderItems();

		if (items.size() == 0) {
			log.error("There must be at least one product in the order");
			throw new LogicErrorException(Constants.CREATE_FAIL, "order.empty.error");
		}

		for (OrderItemDTO item : items) {
			validateId(item.getProductId());

			try {
				ProductEntity productEntity = productRepository.findOneById(item.getProductId());

				Integer stockQuantity = productEntity.getQuantity();

				if (stockQuantity - item.getQuantity() < 0) {
					log.error("Product with id: [" + item.getProductId() + "] quantity out of stock");
					throw new LogicErrorException(Constants.PRODUCT_PROP_QUANTITY,
							Constants.PRODUCT_QUANTITY_NOT_ENOUGH_ERROR, "product.quantity.out.stock.error");
				}

				item.setPrice(productEntity.getPrice());
				item.setAmount(item.getQuantity() * item.getPrice());

				totalAmount += item.getAmount();

				OrderDetailEntity itemEntity = ObjectMapperUtils.map(item, OrderDetailEntity.class);
				itemEntity.setOrder(orderEntity);
				itemEntity.setProduct(productEntity);

				itemEntityList.add(itemEntity);

				productEntity.setQuantity(stockQuantity - item.getQuantity());

				try {
					productRepository.save(productEntity);
				} catch (Exception e) {
					log.error("Error to update product quantity in stock");
					throw new LogicErrorException(e.getMessage(), "product.update.error");
				}

			} catch (Exception e) {
				log.warn("---- Product information had been changed...");
				throw new LogicErrorException(e.getMessage(), "order.create.error");
			}
		}

		orderEntity.setItems(itemEntityList);
		orderEntity.setAmount(totalAmount);

		try {
			orderRepository.save(orderEntity);
		} catch (Exception e) {
			log.error("Error to insert order");
			throw new LogicErrorException(e.getMessage(), "order.insert.error");
		}

		for (OrderDetailEntity item : itemEntityList) {
			try {
				orderDetailRepository.save(item);
			} catch (Exception e) {
				log.error("Error to insert item into order");
				throw new LogicErrorException(e.getMessage(), "item.insert.error");
			}
		}

		resultBean.setMessage(Constants.CREATE_SUCCESS);

		log.info("######## End insert order ########");

		return resultBean;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ResultBean updateOrder(Integer id, OrderDTO request) {
		resultBean = new ResultBean();
		Float totalAmount = 0f;
		List<OrderDetailEntity> itemEntityList = null;
		List<OrderDetailEntity> items = new ArrayList<>();

		log.info("######## Begin update order ########");

		validateId(id);

		UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		List<OrderItemDTO> itemDTOList = request.getOrderItems();

		if (itemDTOList.size() == 0) {
			log.error("There must be at least one product in the order");
			throw new LogicErrorException(Constants.CREATE_FAIL, "order.empty.error");
		}

		OrderEntity orderEntity = orderRepository.findOneByIdAndUserId(id, userDetail.getId());

		if (orderEntity == null) {
			log.error("Cannot get order with id: [" + id + "]");
			throw new LogicErrorException(Constants.ORDER_PROP_ID, Constants.ORDER_NOT_EXIST_ERROR,
					"order.notexist.error");
		}

		if (orderEntity.getIsProcess()) {
			log.error("Cannot update order with id: [" + id + "] because it had been processed");
			throw new LogicErrorException(Constants.ORDER_PROP_ID, Constants.ORDER_HAD_BEEN_PROCESSED_ERROR,
					"order.processed.error");
		}

		itemEntityList = orderDetailRepository.findAllByOrderId(id);
		for (OrderItemDTO itemDTO : itemDTOList) {
			Integer productId = itemDTO.getProductId();
			validateId(productId);
			
			OrderDetailEntity item = itemEntityList.stream()
					.filter(i -> i.getProduct().getId() == productId)
					.findAny()
					.orElse(null);

			try {
				ProductEntity productEntity = productRepository.findOneById(productId);

				if (productEntity == null) {
					log.error("Cannot get product with id: [" + productId + "]");
					throw new LogicErrorException("productId", Constants.PRODUCT_NOT_EXIST_ERROR,
							"product.notexist.error");
				}

				Integer stockQuantity = productEntity.getQuantity();
				Integer newStockQuantity = stockQuantity + item.getQuantity() - itemDTO.getQuantity();

				if (newStockQuantity < 0) {
					log.error("Product with id: [" + productId + "] quantity out of stock");
					throw new LogicErrorException(Constants.PRODUCT_PROP_QUANTITY,
							Constants.PRODUCT_QUANTITY_NOT_ENOUGH_ERROR, "product.quantity.out.stock.error");
				}

				itemDTO.setAmount(itemDTO.getQuantity() * item.getPrice());

				totalAmount += itemDTO.getAmount();

				item.setQuantity(itemDTO.getQuantity());
				item.setAmount(itemDTO.getAmount());

				items.add(item);
				
				productEntity.setQuantity(newStockQuantity);

				try {
					productRepository.save(productEntity);
				} catch (Exception e) {
					log.error("Error to update product quantity in stock");
					throw new LogicErrorException(e.getMessage(), "product.update.error");
				}

			} catch (Exception e) {
				log.warn("---- Product information had been changed...");
				throw new LogicErrorException(e.getMessage(), "order.create.error");
			}
		}

		orderEntity.setItems(items);
		orderEntity.setAmount(totalAmount);

		try {
			orderRepository.save(orderEntity);
		} catch (Exception e) {
			log.error("Error to update order");
			throw new LogicErrorException(e.getMessage(), "order.update.error");
		}

		for (OrderDetailEntity item : items) {
			try {
				orderDetailRepository.save(item);
			} catch (Exception e) {
				log.error("Error to update item in order");
				throw new LogicErrorException(e.getMessage(), "item.update.error");
			}
		}

		resultBean.setMessage(Constants.UPDATE_SUCCESS);

		log.info("######## End update order ########");

		return resultBean;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ResultBean deleteOrder(Integer id) {
		resultBean = new ResultBean();
		OrderEntity orderEntity = null;

		UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		log.info("######## Begin delete order by id ########");

		validateId(id);

		try {
			orderEntity = orderRepository.findOneByIdAndUserId(id, userDetail.getId());
		} catch (Exception e) {
			log.error("Error to get order");
			throw new LogicErrorException(e.getMessage(), "order.get.error");
		}

		if (orderEntity == null) {
			log.error("Cannot get order with id: [" + id + "]");
			throw new LogicErrorException(Constants.ORDER_PROP_ID, Constants.ORDER_NOT_EXIST_ERROR,
					"order.notexist.error");
		}

		if (orderEntity.getIsProcess()) {
			log.error("Cannot delete order with id: [" + id + "] because it had been processed");
			throw new LogicErrorException(Constants.ORDER_PROP_ID, Constants.ORDER_HAD_BEEN_PROCESSED_ERROR,
					"order.processed.error");
		}

		List<OrderDetailEntity> itemEntityList = orderDetailRepository.findAllByOrderId(orderEntity.getId());

		if (itemEntityList.size() == 0) {
			try {
				orderRepository.deleteById(orderEntity.getId());
			} catch (Exception e) {
				log.error("Error to delete order");
				throw new LogicErrorException(e.getMessage(), "order.delete.error");
			}
		}

		for (OrderDetailEntity item : itemEntityList) {
			try {
				orderDetailRepository.deleteById(item.getId());

				try {
					ProductEntity productEntity = productRepository.findOneById(item.getProduct().getId());

					Integer stockQuantity = productEntity.getQuantity();

					productEntity.setQuantity(stockQuantity + item.getQuantity());

					try {
						productRepository.save(productEntity);
					} catch (Exception e) {
						log.error("Error to update product quantity in stock");
						throw new LogicErrorException(e.getMessage(), "product.update.error");
					}

				} catch (Exception e) {
					log.warn("---- Product information had been changed...");
					throw new LogicErrorException(e.getMessage(), "order.create.error");
				}

			} catch (Exception e) {
				log.error("Error to remove item in order");
				throw new LogicErrorException(e.getMessage(), "order.item.remove.error");
			}
		}

		try {
			orderRepository.deleteById(orderEntity.getId());
		} catch (Exception e) {
			log.error("Error to delete order");
			throw new LogicErrorException(e.getMessage(), "order.delete.error");
		}

		resultBean.setMessage(Constants.DELETE_SUCCESS);

		log.info("######## End delete order by id ########");

		return resultBean;
	}

	@Override
	public ResultBean getAllOrder() {
		resultBean = new ResultBean();
		List<OrderEntity> orderList = null;
		log.info("######## Begin get all order ########");

		try {
			orderList = orderRepository.findAll();
		} catch (Exception e) {
			log.error("Error to get order");
			throw new LogicErrorException(e.getMessage(), "order.get.error");
		}

		List<OrderDTO> orderDTOList = ObjectMapperUtils.mapAll(orderList, OrderDTO.class);

		for (OrderDTO orderDTO : orderDTOList) {
			List<OrderDetailEntity> itemList = orderDetailRepository.findAllByOrderId(orderDTO.getId());

			List<OrderItemDTO> itemDTOList = new ArrayList<>();
			for (OrderDetailEntity itemEntity : itemList) {
				OrderItemDTO tmp = ObjectMapperUtils.map(itemEntity, OrderItemDTO.class);
				tmp.setProductId(itemEntity.getProduct().getId());
				tmp.setProductName(itemEntity.getProduct().getName());
				itemDTOList.add(tmp);
			}

			orderDTO.setOrderItems(itemDTOList);
		}

		resultBean.setData(orderDTOList);
		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get all order ########");

		return resultBean;
	}

	@Override
	public ResultBean getOrderByUserId() {
		resultBean = new ResultBean();
		List<OrderEntity> orderEntityList = null;

		UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		log.info("######## Begin get order by user id ########");

		try {
			orderEntityList = orderRepository.findAllByUserId(userDetail.getId());
		} catch (Exception e) {
			log.error("Error to get order");
			throw new LogicErrorException(e.getMessage(), "order.get.error");
		}

		if (orderEntityList.size() == 0) {
			log.error("Dont have any order");
			throw new LogicErrorException(Constants.ORDER_NOT_EXIST_ERROR, "order.notexist.error");
		}

		List<OrderDTO> orderDTOList = ObjectMapperUtils.mapAll(orderEntityList, OrderDTO.class);

		for (OrderDTO orderDTO : orderDTOList) {
			List<OrderDetailEntity> itemList = orderDetailRepository.findAllByOrderId(orderDTO.getId());

			List<OrderItemDTO> itemDTOList = new ArrayList<>();
			for (OrderDetailEntity itemEntity : itemList) {
				if (itemEntity.getQuantity() > 0) {
					OrderItemDTO tmp = ObjectMapperUtils.map(itemEntity, OrderItemDTO.class);
					tmp.setProductId(itemEntity.getProduct().getId());
					tmp.setProductName(itemEntity.getProduct().getName());
					itemDTOList.add(tmp);
				}
			}

			orderDTO.setOrderItems(itemDTOList);
		}

		resultBean.setData(orderDTOList);
		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get order by user id ########");

		return resultBean;
	}

	@Override
	public ResultBean getOrderById(Integer id) {
		resultBean = new ResultBean();
		OrderEntity orderEntity = null;

		UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		log.info("######## Begin get order by id ########");

		validateId(id);

		try {
			orderEntity = orderRepository.findOneByIdAndUserId(id, userDetail.getId());
		} catch (Exception e) {
			log.error("Error to get order");
			throw new LogicErrorException(e.getMessage(), "order.get.error");
		}

		if (orderEntity == null) {
			log.error("Cannot get order with id: [" + id + "]");
			throw new LogicErrorException(Constants.ORDER_PROP_ID, Constants.ORDER_NOT_EXIST_ERROR,
					"order.notexist.error");
		}

		OrderDTO orderDTO = ObjectMapperUtils.map(orderEntity, OrderDTO.class);
		List<OrderDetailEntity> itemList = orderDetailRepository.findAllByOrderId(orderDTO.getId());

		List<OrderItemDTO> itemDTOList = new ArrayList<>();
		for (OrderDetailEntity itemEntity : itemList) {
			if (itemEntity.getQuantity() > 0) {
				OrderItemDTO tmp = ObjectMapperUtils.map(itemEntity, OrderItemDTO.class);
				tmp.setProductId(itemEntity.getProduct().getId());
				tmp.setProductName(itemEntity.getProduct().getName());
				itemDTOList.add(tmp);
			}
		}

		orderDTO.setOrderItems(itemDTOList);

		resultBean.setData(orderDTO);
		resultBean.setMessage(Constants.GET_SUCCESS);

		log.info("######## End get order by id ########");

		return resultBean;
	}

	private void validateId(Integer id) {

		if (id <= 0 || id > Constants.MAX_VALUE_NUMBER) {
			log.error("######## Error with limit id ########");
			throw new LogicErrorException(Constants.PROP_ID, "Id out of range", "id.out.range.error");
		}
	}
}
