package commands;

public enum CommandEnum {
	ADD_CATEGORY (new AddCategoryCommand()), 
	ADD_COLOR(new AddColorCommand() ), 
	ADD_PRODUCT (new AddProductCommand()), 
	ADD_TO_CART (new AddToCartCommand()), 
	BLOCK_USER (new BlockUserCommand()), 
	UNBLOCK_USER (new UnblockUserCommand()), 
	GET_CART_LIST (new GetCartListCommand()), 
	CHANGE_QUANTITY_IN_CART (new ChangeQuantityInCartCommand()), 
	CHANGE_ORDER_STATUS (new ChangeOrderStatusCommand()), 
	GET_PRODUCT_TO_CHANGE(new GetProductToChangeCommand()),
	CHANGE_PRODUCT_INFO (new ChangeProductInfoCommand()), 
	CREATE_ORDER (new CreateOrderCommand()), 
	DELETE_FROM_CART (new DeleteFromCartCommand()), 
	DELETE_PRODUCT (new DeleteProductCommand()), 
	GET_USERS_LIST (new GetUsersListCommand()), 
	GET_PRODUCTS_AND_PROPERTIES_LIST (new GetProductsAndPropertiesListCommand()), 
	GET_ALL_ORDERS_LIST (new GetAllOrdersListCommand()), 
	GET_USER_ORDERS_LIST (new GetUserOrdersListCommand()), 
	GET_CATEGORIES_AND_COLORS_LIST (new GetCategoriesAndColorsListCommand()), 
	GET_ALL_ORDER_ITEMS (new GetAllOrderItemsCommand()), 
	LOGIN (new LoginCommand()), 
	LOGOUT (new LogoutCommand()), 
	REGISTER (new RegisterCommand()),  
	//SAMPLE_PRODUCT (new SampleProductCommand()), 
	//SORT_PRODUCT  (new SortProductCommand()),
	ERROR_PAGE(new ErrorPageCommand()),
	CHANGE_LANGUAGE(new ChangeLanguageCommand()),
	RESET_FILTERS( new ResetFiltersCommand());
	private ICommand command;
	
	CommandEnum(ICommand command) {
		this.command = command;
	}
	
	public ICommand getCommand(){
		return command;
	}
}
