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
<meta charset="ISO-8859-1">
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

a.button {
	background-color: #00ac96;
	color: #fff;
}

.textAreaStyle {
	border: 1px solid #00ac96;
	border-radius: 5px;
	padding: 20px 30px;
	font-size: 1rem;
	font-weight: 400;
	line-height: 1.5;
	border-color: #00ac96 !important;
	width: 100%;
	color: #495057;
}

.cust_input {
	height: 45px;
	padding: 10px 15px;
}

.c_card {
	padding-bottom: 50px;
}

.c_btn {
	border: none;
}

.card-body {
	min-height: 190px;
	height: fit-content;
	font-size: 30px;
}

.card-body .text-uppercase {
	font-size: 30px;
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
					name="pageName" value="normal/checkout.jsp"> <select id="language"
					name="language" onchange="submit()">
					<option><fmt:message key="label.lang_select" /></option>
					<option value="en"><fmt:message key="label.lang_en" /></option>
					<option value="ua"><fmt:message key="label.lang_ua" /></option>
				</select>
			</form>


			<form action="/MyInternetShop/FrontController" method="get">
				<input type="hidden" id="command" name="command"
					value="GET_CART_LIST" class="active">
				<button type="submit" class=" custom_form_Linkbtn">
					<fmt:message key="label.cart" />
				</button>
			</form>

			<c:if test="${current_user == null}">
				<a href="../common_pages/login.jsp"><fmt:message key="label.login" /></a>
				<a href="../common_pages/register.jsp"><fmt:message key="label.register" /></a>
			</c:if>

			<c:if test="${current_user.getUserType().equals('admin')}">
				<a href="../admin/admin.jsp"><c:out value="${current_user.getUserName()}" /></a>
				<form action="/MyInternetShop/FrontController" method="get">
					<input type="hidden" id="command" name="command" value="LOGOUT">
					<button type="submit" class=" btn btn-link">
						<fmt:message key="label.logout" />
					</button>
				</form>
			</c:if>

			<c:if test="${current_user.getUserType().equals('normal')}">
				<a href="normal.jsp"><c:out
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
		<div class="row mt-5">
			<div class="col-md-6">
				<div class="card">
					<h1 class="cart title text-center mt-3">
						<fmt:message key="label.yourselecteditems" />
					</h1>
					<div class="card-body">

						<div class="row">


							<table class="table table-striped table-hover">
								<thead>
									<tr>
										<th scope="col"></th>
										<th scope="col"><fmt:message key="label.name" /></th>
										<th scope="col"><fmt:message key="label.priceforone" /></th>
										<th scope="col"><fmt:message key="label.quantity" /></th>
										<th scope="col"><fmt:message key="label.totalprice" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${cartList}" var="chosenProduct">
										<tr>
											<th scope="row"></th>
											<td>${chosenProduct.getProductName()}</td>

											<td>${chosenProduct.getProductPrice()}</td>

											<td>${chosenProduct.getQuantityInCart()}</td>

											<td>${chosenProduct.getTotalPrice()}</td>

										</tr>

									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<h3 class="text-center pt-3 pb-3">
						<fmt:message key="label.totalpriceincart" />
						: ${totalPrice} &#8372;
					</h3>

					<form action="/MyInternetShop/FrontController" method="get">
						<input type="hidden" id="command" name="command"
							value="GET_CART_LIST">
						<div class="text-center mb-3">
							<button type="submit" id="order" class=" btn btn-secondary">
								<fmt:message key="label.goback" />
							</button>
						</div>
					</form>
				</div>
			</div>
			<div class="card col-md-6">
				<h1 class="cart title text-center mt-3">
					<fmt:message key="label.detailsorder" />
				</h1>
				<div class="card-body text-left pt-4 pb-6 c_card">

					<form action="/MyInternetShop/FrontController" method="post">
						<input type="hidden" id="command" name="command"
							value="CREATE_ORDER">
						<div class="row g-2">
							<label for="staticEmail" class="col-sm-2 col-form-label"><fmt:message
									key="label.shortemail" />:</label>
							<div class="col-sm-10">
								<input type="text" readonly class="form-control-plaintext"
									id="staticEmail" value="${current_user.getUserEmail()}">
							</div>

							<label for="exampleFormControlInput1"><fmt:message
									key="label.enteryourmobile" />:</label>

							<div class="form-group">
								<div class="row">
									<div class="col-md-1">+</div>
									<div class="col-md-3">
										<input type="text" class="textAreaStyle"
											placeholder="<fmt:message
								key="label.entercode" />..."
											name="orderCountryCode" required>
									</div>
									<div class="col">
										<input type="text" class="textAreaStyle"
											placeholder="<fmt:message
								key="label.enterphone" />..."
											name="orderPhone" required>
									</div>
								</div>
							</div>

							<!-- 	<div class="form-group">
								<input type="text" class="textAreaStyle"
									placeholder="<fmt:message
								key="label.enterphone" />..."
									name="orderPhone" required />
							</div> -->

							<label for="exampleFormControlInput1"><fmt:message
									key="label.enteryourshippingaddress" />:</label>
							<div class="form-group">
								<textarea style="height: 300px;" class="textAreaStyle"
									placeholder="<fmt:message
								key="label.entershippingaddress" />..."
									name="orderAddress" required></textarea>
							</div>
						</div>
						<div class="text-center mt-3">
							<button type="submit" class="btn btn-outline-success">
								<fmt:message key="label.ordernow" />
							</button>
						</div>
					</form>

					<form action="/MyInternetShop/FrontController" method="get">
						<input type="hidden" id="command" name="command"
							value="GET_PRODUCTS_AND_PROPERTIES_LIST">
						<div class="text-center mb-3">
							<button type="submit" class="btn btn-secondary">
								<fmt:message key="label.continueshopping" />
							</button>
						</div>
					</form>

				</div>
			</div>
		</div>
	</div>
</body>
</html>