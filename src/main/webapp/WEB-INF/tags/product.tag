<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="product" required="true" type="models.Product"%>
<%@ attribute name="categoryList" required="true" type="java.util.List"%>
<%@ attribute name="colorList" required="true" type="java.util.List"%>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="message" />

<div class="card h-100">
	<img src="data:image/jpg;base64,${product.getBase64Image()}"
		class="card-img-top" alt="${product.getProductPhotoName()}" />
	<div class="card-body d-flex flex-column">
		<h5 class="card-title">${product.getProductName()}</h5>
		<c:if test="${product.getProductQuantity() == 0}">
		
			<h6 class="card-subtitle mb-2 text-muted"><fmt:message key="label.out_of_stock" /></h6>
		</c:if>
		<c:if test="${product.getProductQuantity() > 0}">
		
			<h6 class="card-subtitle mb-2 text-muted"><fmt:message key="label.in_stock" /></h6>
		</c:if>
		<p class="card-text">${product.getProductDescription()}</p>
		<c:forEach items="${categoryList}" var="category">
			<c:if test="${category.getCategoryId() == product.getCategoryId()}">
				<p class="card-text">
					<fmt:message key="label.category" />
					: ${category.getCategoryTitle()}
				</p>
			</c:if>
		</c:forEach>
		<c:forEach items="${colorList}" var="color">
			<c:if test="${color.getColorId() == product.getColorId()}">
				<p class="card-text">
					<fmt:message key="label.color" />
					: ${color.getColorName()}
				</p>
			</c:if>
		</c:forEach>
		<p id="price" class="card-text">${product.getProductPrice()}
			&#8372;</p>
	</div>

	<div class="card-footer bg-transparent border-success border-0">
		<c:if test="${!current_user.getUserType().equals('admin')}">

			<form action="/MyInternetShop/FrontController" method="get">
				<input type="hidden" id="command" name="command" value="ADD_TO_CART">
				<input type="hidden" id="productId" name="productId"
					value="${product.getProductId()}">

				<c:if test="${product.getProductQuantity() == 0}">
					<button type="submit" id="add_to_cart"
						class="btn mt-auto btn btn-primary" disabled>
						<fmt:message key="label.addtocart" />
					</button>
				</c:if>
				<c:if test="${product.getProductQuantity() > 0}">
					<button type="submit" id="add_to_cart"
						class="btn mt-auto btn btn-primary">
						<fmt:message key="label.addtocart" />
					</button>
				</c:if>
			</form>

		</c:if>

		<c:if test="${current_user.getUserType().equals('admin')}">
			<form style="margin-top: 1em;"
				action="/MyInternetShop/FrontController" method="get">
				<input type="hidden" id="command" name="command"
					value="GET_PRODUCT_TO_CHANGE"> <input type="hidden"
					id="productid" name="productId" value="${product.getProductId()}">
				<button type="submit" id="change info" class="btn btn-primary">
					<fmt:message key="label.changeproductinfo" />
				</button>
			</form>

			<form style="margin-top: 1em;"
				action="/MyInternetShop/FrontController" method="post">
				<input type="hidden" id="command" name="command"
					value="DELETE_PRODUCT"> <input type="hidden" id="productId"
					name="productId" value="${product.getProductId()}">
				<button type="submit" id="delete" class="btn btn-danger">
					<fmt:message key="label.deleteproduct" />
				</button>
			</form>
		</c:if>
	</div>
</div>