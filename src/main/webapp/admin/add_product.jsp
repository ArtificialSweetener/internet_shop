<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					name="pageName" value="admin/add_product.jsp"> <select
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
			<fmt:message key="label.welcomeaddproduct" />
		</h1>
		<div class="container-fluid mt-3">
			<jsp:include page="../message.jsp" />
		</div>
	</div>

	<div class=" row d-flex justify-content-center">
		<div class="card col-md-6 center-block">
			<div class="card-body text-center pt-4 pb-6 c_card">


				<form action="/MyInternetShop/FrontController" method="post"
					enctype="multipart/form-data">
					<input type="hidden" id="command" name="command"
						value="ADD_PRODUCT">
					<div class="row g-2">
						<div class="form-group">
							<input type="text" class="textAreaStyle"
								placeholder="<fmt:message key="label.enterproductname" />"
								name="pName" required />
						</div>

						<div class="form-group">
							<textarea style="height: 300px;" class="textAreaStyle"
								placeholder="<fmt:message key="label.enterproductdesc" />"
								name="pDescription" required></textarea>
						</div>

						<div class="form-group">
							<label for=""><fmt:message key="label.chooseproductcolor" /></label>
							<select name="color_id" class="textAreaStyle cust_input"
								id="color_id" required>
								<c:forEach items="${colorList}" var="color">
									<option value="${color.getColorId()}">
										${color.getColorName()}</option>
								</c:forEach>

							</select>
						</div>

						<div class="form-group">
							<label for=""><fmt:message
									key="label.chooseproductcategory" /></label> <select name="catId"
								class="textAreaStyle cust_input" id="catId" required>
								<c:forEach items="${categoryList}" var="category">
									<option value="${category.getCategoryId()}">
										${category.getCategoryTitle()}</option>
								</c:forEach>

							</select>
						</div>
					</div>

					<div class="form-group">
						<input type="number" step="0.01"
							class="textAreaStyle cust_input mt-3"
							placeholder="<fmt:message key="label.enterproductprice" />"
							name="pPrice" required></input>
					</div>

					<div class="form-group">
						<input type="number" class="textAreaStyle cust_input mt-3"
							placeholder="<fmt:message key="label.enterproductquantity" />"
							name="pQuantity" required></input>
					</div>

					<!-- product file -->
					<div class="form-group">
						<label for=""><fmt:message
								key="label.chooseproductpicture" /></label> <br> <input
							type="file" class="textAreaStyle cust_input"
							placeholder="Product photo" name="pPhoto" required></input>
					</div>

					<div class="container text-center mt-3">
						<a class="btn btn-secondary" href="admin.jsp" role="button"><fmt:message
								key="label.goback" /></a>
						<button type="submit" class="btn btn-outline-success">
							<fmt:message key="label.addproduct" />
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>
