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
