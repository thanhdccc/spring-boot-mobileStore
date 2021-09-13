package com.fabbi.springbootmobileStore.constant;

public final class Constants {
	public static final String API_PATH = "/api";
	public static final String ID_PATH = "/{id}";
	public static final int MAX_VALUE_NUMBER = 999999;
	public static final int MAX_VALUE_QUANTITY = 9999;
	public static final float MAX_VALUE_PRICE = 999999999;
	public static final String PROP_ID = "id";
	public static final String ID_ERROR = "Id is out of range";
	
	/**
	 * ******* Common *******
	 */
	public static final String GET_SUCCESS = "Get Success";
	public static final String CREATE_SUCCESS = "Create Success";
	public static final String UPDATE_SUCCESS = "Update Success";
	public static final String DELETE_SUCCESS = "Delete Success";
	public static final String GET_FAIL = "Get Fail";
	public static final String CREATE_FAIL = "Create Fail";
	public static final String UPDATE_FAIL = "Update Fail";
	public static final String DELETE_FAIL = "Delete Fail";

	/**
	 * ******* AUTH *******
	 */
	public static final String AUTH_PATH = "/auth";
	public static final String AUTH_API_SIGNIN = "/signin";
	public static final String AUTH_API_SIGNUP = "/signup";
	public static final String USER_PROP_USERNAME_PASSWORD = "username or password";
	public static final String USER_PROP_USERNAME = "username";
	public static final String USER_PROP_PASSWORD = "password";
	public static final String USER_PROP_ROLE = "role";
	public static final String LOGIN_SUCCESS = "Login Success";
	public static final String LOGIN_FAIL = "Login Fail";
	public static final String REGISTER_SUCCESS = "Register Success";
	public static final String REGISTER_FAIL = "Register Fail";
	public static final String USERNAME_EXIST_ERROR = "Username is already taken";
	public static final String ROLE_NOT_EXIST_ERROR = "Role is not found";
	
	/**
	 * ******* Category *******
	 */
	public static final String CATEGORY_PATH = "/categories";
	public static final String CATEGORY_PROP_ID = "id";
	public static final String CATEGORY_PROP_NAME = "name";
	public static final String CATEGORY_EXIST_ERROR = "Category name is already taken";
	public static final String CATEGORY_NOT_EXIST_ERROR = "Category not found";
	public static final String CATEGORY_PRODUCT_BELONG_ERROR = "There are products belong to";
	public static final String CATEGORY_ID_ERROR = "Category id is out of range";
	public static final String CATEGORY_ID_NULL_ERROR = "Category id is null";
	
	/**
	 * ******* Product *******
	 */
	public static final String PRODUCT_PATH = "/products";
	public static final String PRODUCT_PROP_ID = "id";
	public static final String PRODUCT_PROP_NAME = "name";
	public static final String PRODUCT_PROP_QUANTITY = "quantity";
	public static final String PRODUCT_PROP_PRICE = "price";
	public static final String PRODUCT_PROP_CATEGORY = "categoryId";
	public static final String PRODUCT_EXIST_ERROR = "Product name is already taken";
	public static final String PRODUCT_NOT_EXIST_ERROR = "Product not found";
	public static final String PRODUCT_QUANTITY_NOT_ENOUGH_ERROR = "Not enough product quantity";
	public static final String PRODUCT_ID_ERROR = "Product id is out of range";
	public static final String PRODUCT_ID_NULL_ERROR = "Product id is null";
	
	/**
	 * ******* Order *******
	 */
	public static final String ORDER_PATH = "/orders";
	public static final String ORDER_USER_PATH = "/user-orders";
	public static final String ORDER_PROP_ID = "id";
	public static final String ORDER_PROP_ID_DETAIL = "orderId";
	public static final String ORDER_PROP_ORDER_AND_PRODUCT_ID = "orderId and productId";
	public static final String ORDER_PROP_ORDER_AND_PRODUCT_ID_ERROR = "Already exist product in order";
	public static final String ORDER_ID_ERROR = "Order id is out of range";
	public static final String ORDER_DETAIL_ID_NULL_ERROR = "Order detail id is null";
	public static final String ORDER_NOT_EXIST_ERROR = "Order not found";
	public static final String ORDER_HAD_BEEN_PROCESSED_ERROR = "Order had been processed";
	public static final String ORDER_DETAIL_NOT_EXIST_ERROR = "Detail product not found";
}
