<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page isELIgnored="false"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="message" />

<!--<c:if test="${current_user == null}">
	<c:set var="message"
		value="You are not logged in. Please, login first to get access to this page."
		scope="session" />
	<c:redirect url="/common_pages/login.jsp"></c:redirect>
</c:if>
<c:if test="${current_user.getUserType().equals('normal')}">
	<c:set var="message"
		value="You are not an admin. Your access to this page is denied. Please login as an admin to get access to this page."
		scope="session" />
	<c:redirect url="/common_pages/login.jsp"></c:redirect>
</c:if> -->


<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="${language}">
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
	crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/vanillajs-datepicker@1.2.0/dist/css/datepicker-bs5.min.css">
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

.card:hover {
	background: #ddd;
	color: black;
	cursor: pointer;
}

.modal-header {
	font-family: 'Poppins', sans-serif;
	background-color: #00ac96;
}

.c_btn {
	border: none;
}

.card-body {
	min-height: 190px;
	height: 100%;
	font-size: 30px;
}

.card-body .text-uppercase {
	font-size: 30px;
}

.container {
	padding-bottom: 100px;
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

.card form {
	height: 100%;
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
					name="pageName" value="admin/admin.jsp"> <select id="language"
					name="language" onchange="submit()">
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

			<c:if test="${current_user.getUserType().equals('admin')}">
				<a class="active" href="admin.jsp"><c:out
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

	<div class="container">
		<h1 class="text-center pt-3 pb-3">
			<fmt:message key="label.welcomeadmin" />
		</h1>

		<div class="container-fluid mt-3">
			<jsp:include page="../message.jsp" />
		</div>


		<div class="row row-cols-3 g-3">

			<div class="col">
				<div class="card h-100">

					<form action="/MyInternetShop/FrontController" method="get">
						<input type="hidden" id="command" name="command"
							value="GET_USERS_LIST">
						<button type="submit" class="card-body text-center c_btn">
							<div class="card-body text-center">

								<img style="max-width: 150px;" class="img-fluid "
									src="../img/users.png" alt="category<-icon">

								<h1>
									<fmt:message key="label.allusers" />
								</h1>
								<h1 class="text-uppercase text-muted">
									<fmt:message key="label.users" />
								</h1>
							</div>
						</button>
					</form>

				</div>
			</div>

			<div class="col">
				<div class="card h-100">
					<form action="/MyInternetShop/FrontController" method="get">
						<input type="hidden" id="command" name="command"
							value="GET_ALL_ORDERS_LIST">
						<button type="submit" class="card-body text-center c_btn">
							<div class="card-body text-center">

								<img style="max-width: 150px;" class="img-fluid "
									src="../img/category.png" alt="category<-icon">

								<h1>
									<fmt:message key="label.allorders" />
								</h1>
								<h1 class="text-uppercase text-muted">
									<fmt:message key="label.orders" />
								</h1>
							</div>
						</button>
					</form>
				</div>
			</div>

			<div class="col">
				<div class="card h-100">
					<form action="/MyInternetShop/FrontController" method="get">
						<input type="hidden" id="command" name="command"
							value="GET_PRODUCTS_AND_PROPERTIES_LIST">
						<button type="submit" class="card-body text-center c_btn">
							<div class="card-body text-center">

								<img style="max-width: 150px;" class="img-fluid"
									src="../img/products.png" alt="product<-icon">

								<h1>
									<fmt:message key="label.allproducts" />
								</h1>
								<h1 class="text-uppercase text-muted">
									<fmt:message key="label.products" />
								</h1>
							</div>
						</button>
					</form>
				</div>
			</div>

			<div class="col">
				<div class="card h-100">
					<form action="add_category.jsp">
						<button type="submit" class="card-body text-center c_btn">
							<div class="card-body text-center">

								<img style="max-width: 200px;" class="img-fluid"
									src="../img/add_category.png" alt="add_category<-icon">

								<h1>
									<fmt:message key="label.addcategoryclick" />
								</h1>
								<h1 class="text-uppercase text-muted">
									<fmt:message key="label.addcategory" />
								</h1>
							</div>
						</button>
					</form>
				</div>
			</div>

			<div class="col">
				<div class="card h-100">
					<form action="add_color.jsp">
						<button type="submit" class="card-body text-center c_btn">
							<div class="card-body text-center">

								<img style="max-width: 200px;" class="img-fluid"
									src="../img/add_category.png" alt="add_category<-icon">

								<h1>
									<fmt:message key="label.addcolorclick" />
								</h1>
								<h1 class="text-uppercase text-muted">
									<fmt:message key="label.addcolor" />
								</h1>
							</div>
						</button>
					</form>
				</div>
			</div>

			<div class="col">
				<div class="card h-100">
					<form id="myform" method="get" action="/MyInternetShop/FrontController">
						<input type="hidden" id="command" name="command"
							value="GET_CATEGORIES_AND_COLORS_LIST">
						<button type="submit" class="card-body text-center c_btn">
							<div class="card-body text-center">

								<img style="max-width: 200px;" class="img-fluid"
									src="../img/add_product.png" alt="add_product<-icon">

								<h1>
									<fmt:message key="label.addproductclick" />
								</h1>
								<h1 class="text-uppercase text-muted">
									<fmt:message key="label.addproduct" />
								</h1>
							</div>
						</button>
					</form>
				</div>
			</div>

		</div>

	</div>

</body>

</html>