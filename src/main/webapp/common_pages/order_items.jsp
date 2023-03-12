<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page isELIgnored="false"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="message" />

<c:if test="${current_user == null}">
	<c:set var="message"
		value="You are not logged in. Please, login first to get access to this page."
		scope="session" />
	<c:redirect url="login.jsp"></c:redirect>
</c:if> 

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

table {
	position: relative;
}

.absolute_td {
	position: absolute;
	top: 50%;
	right: -91%;
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

.custom_form_Linkbtn:active {
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
					name="pageName" value="common_pages/order_items.jsp"> <select
					id="language" name="language" onchange="submit()">
					<option><fmt:message key="label.lang_select" /></option>
					<option value="en"><fmt:message key="label.lang_en" /></option>
					<option value="ua"><fmt:message key="label.lang_ua" /></option>
				</select>
			</form>
			<form action="/MyInternetShop/FrontController" method="get">
				<input type="hidden" id="command" name="command"
					value="GET_CART_LIST">
				<button type="submit" class=" custom_form_Linkbtn">
					<fmt:message key="label.cart" />
				</button>
			</form>

			<c:if test="${current_user == null}">
				<a href="login.jsp"><fmt:message key="label.login" /></a>
				<a href="register.jsp"><fmt:message key="label.register" /></a>
			</c:if>

			<c:if test="${current_user.getUserType().equals('admin')}">
				<a class="active" href="../admin/admin.jsp"><c:out
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
</body>

<div class="container">

	<div class="container-fluid mt-3">
		<jsp:include page="../message.jsp" />
	</div>
	<div class="card">
		<div class="card-body">
			<div class="row">
				<h1 class="text-left pt-3 pb-3">
					<fmt:message key="label.itemdetailsorder" />
					&#8470 ${thisOrderId} :
				</h1>

				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th scope="col"></th>
							<th scope="col"><fmt:message key="label.productname" /></th>
							<th scope="col"><fmt:message key="label.itemquantity" /></th>
							<th scope="col"><fmt:message key="label.producttotalprice" /></th>
					</thead>
					<tbody>
						<c:forEach items="${itemList}" var="item">
							<tr>
								<td scope="row"></td>
								<c:forEach items="${productList}" var="product">
									<c:if test="${item.getProductId() == product.getProductId()}">
										<td scope="row">${product.getProductName()}</td>
									</c:if>
								</c:forEach>
								<td scope="row">${item.getItemQuantity()}</td>
								<td scope="row">${item.getItemPrice()}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<h3 class="text-end">
					<fmt:message key="label.ordertotalprice" />
					: ${totalOrderPrice}&#8372;
				</h3>

				<c:if test="${currentPageAllItems != null}">
					<nav aria-label="Page navigation example">
						<ul class="pagination justify-content-center">
							<li class="page-item"><c:if
									test="${currentPageAllItems != 1}">
									<a class="page-link"
										href="/MyInternetShop/FrontController?command=GET_ALL_ORDER_ITEMS&page=${currentPageAllItems - 1}&orderId=${thisOrderId}"><fmt:message
						key="label.previous" /></a>
								</c:if></li>


							<!--  <li class="page-item"></li>-->
							<c:forEach begin="1" end="${noOfPagesAllItems}" var="i">
								<c:choose>
									<c:when test="${currentPageAllItems eq i}">
										<li class="page-item active"><a class="page-link">${i}</a></li>
									</c:when>
									<c:otherwise>
										<li class="page-item"><a class="page-link"
											href="/MyInternetShop/FrontController?command=GET_ALL_ORDER_ITEMS&page=${i}&orderId=${thisOrderId}">${i}</a>
										</li>
									</c:otherwise>
								</c:choose>
							</c:forEach>

							<li class="page-item"><c:if
									test="${currentPageAllItems lt noOfPagesAllItems}">
									<a class="page-link"
										href="/MyInternetShop/FrontController?command=GET_ALL_ORDER_ITEMS&page=${currentPageAllItems + 1}&orderId=${thisOrderId}"><fmt:message
						key="label.next" /></a>
								</c:if></li>
						</ul>
					</nav>
				</c:if>
				<c:if test="${current_user.getUserType().equals('admin')}">
					<a class="btn btn-secondary" href="../admin/orders.jsp" role="button"><fmt:message
							key="label.goback" /></a>
				</c:if>
				<c:if test="${current_user.getUserType().equals('normal')}">
					<a class="btn btn-secondary" href="../normal/orders_user.jsp" role="button"><fmt:message
							key="label.goback" /></a>
				</c:if>
			</div>
		</div>
	</div>
</div>
</html>


