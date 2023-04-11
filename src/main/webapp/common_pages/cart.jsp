<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page isELIgnored="false"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="message" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="${language}">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<style>
@import
	url('https://fonts.googleapis.com/css2?family=Poppins&display=swap');

* {
	box-sizing: border-box;
}

.header {
	overflow: hidden;
	background-color: #f1f1f1;
	padding: 20px 10px;
}

.header a {
	float: left;
	color: black;
	text-align: center;
	padding: 12px;
	text-decoration: none;
	font-size: 18px;
	line-height: 25px;
	border-radius: 4px;
}

.btn-link {
	float: left;
	color: black;
	text-align: center;
	padding: 12px;
	text-decoration: none;
	font-size: 25px;
	font-weight: bold;
	line-height: 25px;
	border-radius: 4px;
}

.header a.logo {
	font-size: 25px;
	font-weight: bold;
}

.header a:hover {
	background-color: #ddd;
	color: black;
}

.header a.active {
	background-color: dodgerblue;
	color: white;
}

.header-right {
	float: right;
}

@media screen and (max-width: 500px) {
	.header a {
		float: none;
		display: block;
		text-align: left;
	}
	.header-right {
		float: none;
	}
}

body {
	font-family: 'Poppins', sans-serif;
	background-color: #00ac96;
}

.content {
	margin: 8%;
	background-color: #fff;
	padding: 4rem 1rem 4rem 1rem;
	box-shadow: 0 0 5px 5px rgba(0, 0, 0, .05);
}

.signin-text {
	font-style: normal;
	font-weight: 600 !important;
}

.form-control {
	display: block;
	width: 100%;
	font-size: 1rem;
	font-weight: 400;
	line-height: 1.5;
	border-color: #00ac96 !important;
	border-style: solid !important;
	border-width: 0 0 1px 0 !important;
	padding: 0px !important;
	color: #495057;
	height: auto;
	border-radius: 0;
	background-color: #fff;
	background-clip: padding-box;
}

.form-control:focus {
	color: #495057;
	background-color: #fff;
	border-color: #fff;
	outline: 0;
	box-shadow: none;
}

.birthday-section {
	padding: 15px;
}

.btn-class {
	border-color: #00ac96;
	color: #00ac96;
}

.btn-class:hover {
	background-color: #00ac96;
	color: #fff;
}

.header a.active {
	background-color: dodgerblue;
	color: white;
}

.header-right {
	display: flex;
	flex-direction: row-reverse;
	align-items: center;
	float: right;
}

.custom_form_Linkbtn {
	float: left;
	color: black;
	text-align: center;
	padding: 12px;
	text-decoration: none;
	font-size: 18px;
	line-height: 25px;
	border-radius: 4px;
	border: none;
}

.custom_form_Linkbtn:last-child {
	margin-right: 20px;
}

.custom_form_Linkbtn.active {
	background-color: dodgerblue;
	color: white;
}
</style>
</head>
<body>

	<div class="header">

		<form action="/MyInternetShop/FrontController" method="get">
			<input type="hidden" id="command" name="command"
				value="GET_PRODUCTS_AND_PROPERTIES_LIST">
			<button type="submit" class=" btn btn-link">
				<fmt:message key="label.products" />
			</button>
		</form>
		<div class="header-right">
			<a href="../index.jsp"><fmt:message key="label.about" /></a>

			<form action="/MyInternetShop/FrontController" method="post">
				<input type="hidden" id="command" name="command"
					value="CHANGE_LANGUAGE"> <input type="hidden" id="command"
					name="pageName" value="common_pages/cart.jsp"> <select
					id="language" name="language" onchange="submit()">
					<option><fmt:message key="label.lang_select" /></option>
					<option value="en"><fmt:message key="label.lang_en" /></option>
					<option value="ua"><fmt:message key="label.lang_ua" /></option>
				</select>
			</form>


			<form action="/MyInternetShop/FrontController" method="get">
				<input type="hidden" id="command" name="command"
					value="GET_CART_LIST" class="active">
				<button type="submit" class=" custom_form_Linkbtn active">
					<fmt:message key="label.cart" />
				</button>
			</form>

			<c:if test="${current_user == null}">
				<a href="login.jsp"><fmt:message key="label.login" /></a>
				<a href="register.jsp"><fmt:message key="label.register" /></a>
			</c:if>

			<c:if test="${current_user.getUserType().equals('admin')}">
				<a href="../admin/admin.jsp"><c:out
						value="${current_user.getUserName()}" /></a>

				<form action="/MyInternetShop/FrontController" method="get">
					<input type="hidden" id="command" name="command" value="LOGOUT">
					<button type="submit" class=" btn btn-link">
						<fmt:message key="label.logout" />
					</button>
				</form>

			</c:if>

			<c:if test="${current_user.getUserType().equals('normal')}">
				<a href="../normal/normal.jsp"><c:out
						value="${current_user.getUserName()}" /></a>
				<form action="/MyInternetShop/FrontController" method="get">
					<input type="hidden" id="command" name="command" value="LOGOUT">
					<button type="submit" class=" btn btn-link">
						<fmt:message key="label.logout" />
					</button>
				</form>
			</c:if>
		</div>
	</div>


	<div class="container-fluid mt-3">
		<jsp:include page="../message.jsp" />
	</div>

	<div class="container">
		<div class="card">
			<div class="card-body">
				<div class="row">
					<h1 class="text-left pt-3 pb-3">
						<fmt:message key="label.totalpriceincart" />
						: ${cart.getTotalPrice()} &#8372;
					</h1>
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th scope="col"></th>
								<th scope="col"><fmt:message key="label.name" /></th>
								<th scope="col"><fmt:message key="label.category" /></th>
								<th scope="col"><fmt:message key="label.priceforone" /></th>
								<th scope="col"><fmt:message key="label.quantity" /></th>
								<th scope="col"><fmt:message key="label.update" /> <fmt:message
										key="label.quantity" /></th>
								<th scope="col"><fmt:message key="label.totalprice" /></th>
								<th scope="col"><fmt:message key="label.cancel" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${cart.getProducts()}" var="entry">
								<c:set var="chosenProduct" value="${entry.key}" />
								<tr>
									<th scope="row"></th>
									<td>${chosenProduct.getProductName()}</td>

									<td><c:forEach items="${categoryList}" var="category">
											<c:if
												test="${chosenProduct.getCategoryId() == category.getCategoryId()}">
												<c:out value="${category.getCategoryTitle()}" />
											</c:if>
										</c:forEach></td>

									<td>${chosenProduct.getProductPrice()}</td>

									<td>${cart.getProductQuantity(chosenProduct)}</td>


									<td><form style="margin-top: 1em;"
											action="/MyInternetShop/FrontController" method="get">
											<input type="hidden" id="command" name="command"
												value="CHANGE_QUANTITY_IN_CART"> <input
												type="number" class="textAreaStyle cust_input mt-3"
												placeholder="1" name="quantity" required></input> <input
												type="hidden" id="productId" name="chosenProductId"
												value="${chosenProduct.getProductId()}">
											<button type="submit" id="delete" class="btn btn-danger">
												<fmt:message key="label.update" />
											</button>
										</form></td>

									<td>${cart.getProductTotalPrice(chosenProduct)}</td>

									<td>
										<form style="margin-top: 1em;"
											action="/MyInternetShop/FrontController" method="get">
											<input type="hidden" id="command" name="command"
												value="DELETE_FROM_CART"> <input type="hidden"
												id="productId" name="chosenProductId"
												value="${chosenProduct.getProductId()}">
											<button type="submit" id="delete" class="btn btn-danger">
												<fmt:message key="label.remove" />
											</button>
										</form>
									</td>

								</tr>

							</c:forEach>
						</tbody>
					</table>


					<c:if
						test="${cart == null || cart.getProducts().size() == 0 || current_user == null}">
						<button type="submit" id="order" class="btn btn-primary" disabled>
							<fmt:message key="label.placeanorder" />
						</button>
					</c:if>
					<c:if test="${cart.getProducts().size() > 0 && current_user != null}">
						<a class="btn btn-primary" type="submit" id="order"
							href="../normal/checkout.jsp" role="button"><fmt:message
								key="label.placeanorder" /></a>
					</c:if>

				</div>
			</div>
		</div>
	</div>
</body>
</html>


