<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
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

.btn-link:hover {
	background-color: #ddd;
	color: black;
}

.btn-link:active {
	background-color: dodgerblue;
	color: dodgerblue;
	box-shadow: none;
}

.btn-link:focus {
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

.column {
	float: left;
	width: 25%;
	padding: 0 10px;
}

/* Remove extra left and right margins, due to padding */
.row {
	margin: 0 -5px;
}

/* Clear floats after the columns */
.row:after {
	content: "";
	display: table;
	clear: both;
}

/* Responsive columns */
@media screen and (max-width: 600px) {
	.column {
		width: 100%;
		display: block;
		margin-bottom: 20px;
	}
}

/* Style the counter cards */
.card {
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
	padding: 16px;
	text-align: center;
	background-color: #f1f1f1;
	margin-top: 1rem;
}

.card-title {
	font-weight: bold;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.card-text {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
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

.btn-primary {
	color: #fff;
	background-color: #00ac96;
	border-color: #00ac96;
}

.price-input {
	width: 100%;
	display: flex;
	margin: 5px 0 10px;
}

.price-input .field {
	display: flex;
	width: 100%;
	height: 45px;
	align-items: center;
}

.field input {
	width: 100%;
	height: 100%;
	outline: none;
	font-size: 19px;
	margin-left: 12px;
	border-radius: 5px;
	text-align: center;
	border: 1px solid #999;
	-moz-appearance: textfield;
}

input[type="number"]::-webkit-outer-spin-button, input[type="number"]::-webkit-inner-spin-button
	{
	-webkit-appearance: none;
}

.price-input .separator {
	width: 130px;
	display: flex;
	font-size: 19px;
	align-items: center;
	justify-content: center;
}

#price {
	color: red;
	font-weight: bold;
	font-size: 50px;
}

img {
	max-width: 100%;
	max-height: 350px;
	height: auto;
	width: auto;
}

.text_end {
	float: right;
}

.centered__sample {
	position: absolute;
	bottom: 15px;
	left: 50%;
}

.centered__reset {
	position: absolute;
	bottom: 15px;
	left: 50%;
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

.custom_form_Linkbtn:active {
	background-color: dodgerblue;
	color: white;
}

.custom_buttons {
	display: flex;
	justify-content: center;
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
					name="pageName" value="common_pages/products.jsp"> <select id="language"
					name="language" onchange="submit()">
					<option><fmt:message key="label.lang_select" /></option>
					<option value="en"><fmt:message key="label.lang_en" /></option>
					<option value="ua"><fmt:message key="label.lang_ua" /></option>
				</select>
			</form>

			<form action="/MyInternetShop/FrontController" method="post">
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
				<a href="../admin/admin.jsp"><c:out value="${current_user.getUserName()}" /></a>
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

	<div class="card">
		<div class="card-body">

			<form action="/MyInternetShop/FrontController" method="get">
				<input type="hidden" id="command" name="command"
					value="GET_PRODUCTS_AND_PROPERTIES_LIST">
				<div class="row row-cols-3 g-3" style="text-align: left">

					<div class="col">
						<h3>
							<fmt:message key="label.chooseproductcat" />
						</h3>
						<div class="input-group mb-3">
							<div class="input-group-prepend">
								<label class="input-group-text" for="selectCategory"><fmt:message
										key="label.categories" /></label>
							</div>
							<select class="custom-select" id="selectCategory" name="category">
								<option selected disabled><fmt:message
										key="label.choose" />...
								</option>
								<c:forEach items="${categoryList}" var="category">
									<option value="${category.getCategoryId()}">${category.getCategoryTitle()}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="col">
						<h3>
							<fmt:message key="label.chooseproductcol" />
						</h3>
						<div class="input-group mb-3">
							<div class="input-group-prepend">
								<label class="input-group-text" for="selectColor"><fmt:message
										key="label.colors" /></label>
							</div>
							<select class="custom-select" id="selectColor" name="color">
								<option selected disabled><fmt:message
										key="label.choose" />...
								</option>
								<c:forEach items="${colorList}" var="color">
									<option value="${color.getColorId()}">${color.getColorName()}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="col">
						<h3>
							<fmt:message key="label.chooseproductpr" />
						</h3>
						<div class="price-input">
							<div class="field">
								<span><fmt:message key="label.min" /></span> <input
									type="number" class="input-min" name="min"
									placeholder="${productMinPrice}">
							</div>
							<div class="separator">-</div>
							<div class="field">
								<span><fmt:message key="label.max" /></span> <input
									type="number" class="input-max" name="max"
									placeholder="${productMaxPrice}">
							</div>
						</div>
					</div>
				</div>
				<button type="submit" class="btn btn-success">
					<fmt:message key="label.sample" />
				</button>
			</form>

			<div class="custom_buttons mt-3">
				<form action="/MyInternetShop/FrontController" method="get">
					<input type="hidden" id="command" name="command"
						value="RESET_FILTERS">
					<button type="submit" class="btn btn-secondary">
						<fmt:message key="label.reset" />
					</button>
				</form>
			</div>
		</div>
	</div>

	<form action="/MyInternetShop/FrontController" method="get">
		<input type="hidden" id="command" name="command"
			value="GET_PRODUCTS_AND_PROPERTIES_LIST">
		<div class="input-group mt-3">
			<select class="custom-select" id="sort" name="sort"
				onchange="this.form.submit()">
				<option selected><fmt:message key="label.choose" />...
				</option>
				<option value="newest_first"><fmt:message
						key="label.newest" /></option>
				<option value="oldest_first"><fmt:message
						key="label.oldest" /></option>
				<option value="cheapest_first"><fmt:message
						key="label.cheapest" /></option>
				<option value="expensive_first"><fmt:message
						key="label.expensive" /></option>
				<option value="a-z"><fmt:message key="label.bynamea" /></option>
				<option value="z-a"><fmt:message key="label.bynamez" /></option>
			</select>
			<div class="input-group-append">
				<label class="input-group-text" for="inputGroupSelect02"><fmt:message
						key="label.sortproducts" /></label>
			</div>
		</div>
	</form>

	<c:if test="${currentPageProducts != null}">
		<nav aria-label="Page navigation example">
			<ul class="pagination justify-content-center">
				<li class="page-item"><c:if test="${currentPageProducts != 1}">
						<a class="page-link"
							href="/MyInternetShop/FrontController?command=GET_PRODUCTS_AND_PROPERTIES_LIST&page=${currentPageProducts - 1}"><fmt:message
						key="label.previous" /></a>
					</c:if></li>

				<!--  <li class="page-item"></li>-->
				<c:forEach begin="1" end="${noOfPagesProducts}" var="i">
					<c:choose>
						<c:when test="${currentPageProducts eq i}">
							<li class="page-item active"><a class="page-link">${i}</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link"
								href="/MyInternetShop/FrontController?command=GET_PRODUCTS_AND_PROPERTIES_LIST&page=${i}">${i}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<li class="page-item"><c:if
						test="${currentPageProducts lt noOfPagesProducts}">
						<a class="page-link"
							href="/MyInternetShop/FrontController?command=GET_PRODUCTS_AND_PROPERTIES_LIST&page=${currentPageProducts + 1}"><fmt:message
						key="label.next" /></a>
					</c:if></li>
			</ul>
		</nav>
	</c:if>

	<div class="row row-cols-3 g-3">
		<c:forEach items="${productList}" var="product">
			<custom:product product="${product}" categoryList="${categoryList}"
				colorList="${colorList}" />
		</c:forEach>
	</div>

	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
		integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
		integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
		crossorigin="anonymous"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
		integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
		crossorigin="anonymous"></script>
</body>
</html>

