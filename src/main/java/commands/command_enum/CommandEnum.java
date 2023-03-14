package commands.command_enum;

import commands.admin_commands.AddCategoryCommand;
import commands.admin_commands.AddColorCommand;
import commands.admin_commands.AddProductCommand;
import commands.admin_commands.BlockUserCommand;
import commands.admin_commands.ChangeOrderStatusCommand;
import commands.admin_commands.ChangeProductInfoCommand;
import commands.admin_commands.DeleteProductCommand;
import commands.admin_commands.GetAllOrdersListCommand;
import commands.admin_commands.GetCategoriesAndColorsListCommand;
import commands.admin_commands.GetProductToChangeCommand;
import commands.admin_commands.GetUsersListCommand;
import commands.admin_commands.UnblockUserCommand;
import commands.common_commands.AddToCartCommand;
import commands.common_commands.ChangeLanguageCommand;
import commands.common_commands.ChangeQuantityInCartCommand;
import commands.common_commands.DeleteFromCartCommand;
import commands.common_commands.ErrorPageCommand;
import commands.common_commands.GetAllOrderItemsCommand;
import commands.common_commands.GetCartListCommand;
import commands.common_commands.GetProductsAndPropertiesListCommand;
import commands.common_commands.LoginCommand;
import commands.common_commands.RegisterCommand;
import commands.common_commands.ResetFiltersCommand;
import commands.common_commands.WelcomePageCommand;
import commands.icommand.ICommand;
import commands.logged_in_user_commands.CreateOrderCommand;
import commands.logged_in_user_commands.GetUserOrdersListCommand;
import commands.logged_in_user_commands.LogoutCommand;

/**
 * This package contains an enum class named CommandEnum that lists all the
 * available commands for the system. Each command is represented by an instance
 * of ICommand interface.
 * 
 * The available commands are: 
 * WELCOME_PAGE: displays the welcome page of the system. 
 * ADD_CATEGORY: allows admin users to add a new category to the system.
 * ADD_COLOR: allows admin users to add a new color to the system. 
 * ADD_PRODUCT:allows admin users to add a new product to the system. 
 * ADD_TO_CART: allows common users to add a product to their shopping cart. 
 * BLOCK_USER: allows admin users to block a user account. 
 * UNBLOCK_USER: allows admin users to unblock a user account. GET_CART_LIST: retrieves the list of products in the user's shopping cart. 
 * CHANGE_QUANTITY_IN_CART: allows common users to change the quantity of a product in their shopping cart. 
 * CHANGE_ORDER_STATUS: allows admin users to change the status of an order. 
 * GET_PRODUCT_TO_CHANGE: allows admin users to retrieve information about a product to be modified.
 * CHANGE_PRODUCT_INFO: allows admin users to modify the information of a product. 
 * CREATE_ORDER: allows common users to create an order.
 * DELETE_FROM_CART: allows common users to delete a product from their shopping cart. 
 * DELETE_PRODUCT: allows admin users to delete a product from the system.
 * GET_USERS_LIST: allows admin users to retrieve the list of users.
 * GET_PRODUCTS_AND_PROPERTIES_LIST: retrieves the list of products and their properties. 
 * GET_ALL_ORDERS_LIST: allows admin users to retrieve the list of all orders. 
 * GET_USER_ORDERS_LIST: allows common users to retrieve their order history. 
 * GET_CATEGORIES_AND_COLORS_LIST: retrieves the list of categories and colors available in the system. 
 * GET_ALL_ORDER_ITEMS: allows admin users to retrieve the list of items in an order.
 * LOGIN: allows users to log in to the system. 
 * LOGOUT: allows users to log out of the system. 
 * REGISTER: allows users to register a new account. 
 * ERROR_PAGE: displays an error page when an error occurs. 
 * CHANGE_LANGUAGE: allows users to change the language of the system.
 * RESET_FILTERS: allows users to reset the applied filters.
 * 
 * Each command is associated with an ICommand object that implements the
 * execute method, which is called when the command is executed. The getCommand
 * method can be used to retrieve the ICommand object associated with a
 * CommandEnum value.
 * 
 * This class also overrides the toString method to return the name of the enum
 * constant.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public enum CommandEnum {
	WELCOME_PAGE(new WelcomePageCommand()), ADD_CATEGORY(new AddCategoryCommand()), ADD_COLOR(new AddColorCommand()),
	ADD_PRODUCT(new AddProductCommand()), ADD_TO_CART(new AddToCartCommand()), BLOCK_USER(new BlockUserCommand()),
	UNBLOCK_USER(new UnblockUserCommand()), GET_CART_LIST(new GetCartListCommand()),
	CHANGE_QUANTITY_IN_CART(new ChangeQuantityInCartCommand()), CHANGE_ORDER_STATUS(new ChangeOrderStatusCommand()),
	GET_PRODUCT_TO_CHANGE(new GetProductToChangeCommand()), CHANGE_PRODUCT_INFO(new ChangeProductInfoCommand()),
	CREATE_ORDER(new CreateOrderCommand()), DELETE_FROM_CART(new DeleteFromCartCommand()),
	DELETE_PRODUCT(new DeleteProductCommand()), GET_USERS_LIST(new GetUsersListCommand()),
	GET_PRODUCTS_AND_PROPERTIES_LIST(new GetProductsAndPropertiesListCommand()),
	GET_ALL_ORDERS_LIST(new GetAllOrdersListCommand()), GET_USER_ORDERS_LIST(new GetUserOrdersListCommand()),
	GET_CATEGORIES_AND_COLORS_LIST(new GetCategoriesAndColorsListCommand()),
	GET_ALL_ORDER_ITEMS(new GetAllOrderItemsCommand()), LOGIN(new LoginCommand()), LOGOUT(new LogoutCommand()),
	REGISTER(new RegisterCommand()), ERROR_PAGE(new ErrorPageCommand()), CHANGE_LANGUAGE(new ChangeLanguageCommand()),
	RESET_FILTERS(new ResetFiltersCommand());

	private ICommand command;

	CommandEnum(ICommand command) {
		this.command = command;

	}

	public ICommand getCommand() {
		return command;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
