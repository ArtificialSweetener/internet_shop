package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ItemDao;
import dao.ProductDao;
import dao.impl.ItemDaoImpl;
import dao.impl.ProductDaoImpl;
import models.Item;
import models.Product;
import service.ItemService;
import service.ProductService;
import service.impl.ItemServiceImpl;
import service.impl.ProductServiceImpl;

public class GetAllOrderItemsCommand implements ICommand {
	private ProductService productService;
	private ItemService itemService;

	public GetAllOrderItemsCommand() {
		ProductDao productDao = new ProductDaoImpl();
		this.productService = new ProductServiceImpl(productDao);
		ItemDao itemdao = new ItemDaoImpl();
		this.itemService = new ItemServiceImpl(itemdao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String forward_if_success = "order_items.jsp";
		Optional<String> currentOrderId = Optional.ofNullable(req.getParameter("orderId"));
			int page = 1;
			int recordsPerPage = 3;
			if (req.getParameter("page") != null) {
				page = Integer.parseInt(req.getParameter("page"));}
			
			Long id = Long.parseLong( currentOrderId.get());
			long totalOrderPrice = 0;
			Optional<List<Item>> itemListOptional = itemService.getAllByOrderId(id);
			if (itemListOptional.isPresent()) {
				for (Item item : itemListOptional.get()) {
					totalOrderPrice += item.getItemPrice();
				}
				//pagination done
				List<Item> itemList = itemService.getAllByOrderId(id, (page - 1) * recordsPerPage, recordsPerPage);
				int noOfRecords = itemService.getNoOfRecords();
				int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
				List<Product> productList = new ArrayList<>();
				
				for (Item item: itemList) {
					Product product = productService.get(item.getProductId());
					productList.add(product);
				}
				req.getSession().setAttribute("thisOrderId", id);
				req.getSession().setAttribute("itemList", itemList);
				req.getSession().setAttribute("productList", productList);
				req.getSession().setAttribute("noOfPagesAllItems", noOfPages);
				req.getSession().setAttribute("currentPageAllItems", page);
				req.getSession().setAttribute("totalOrderPrice", totalOrderPrice);
				System.out.println("Number of pages is: " + noOfPages);
				System.out.println("This page is:" + page);
				return forward_if_success;
			} 
			return forward_if_success;
			}
}
