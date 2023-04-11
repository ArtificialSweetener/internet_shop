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
	rel="stylesheet">
	<!-- integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous" -->
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

.c_btn {
	width: 100%;
}

.flex_col {
	display: flex;
	width: 80%;
	margin: 0 auto;
}

.flex_btn {
	margin: 0 auto;
	margin-top: 20px;
	width: 72%;
}

.flex_col_id {
	width: 30%;
	display: flex;
	flex-direction: column;
}

.flex_col_value {
	width: 70%;
	display: flex;
	flex-direction: column;
}

.flex_col_item {
	width: 100%;
	padding: 10px 20px;
	margin-bottom: 15px;
	border: 1px solid #000;
	background: #fff;
}
.header-right {
    display: flex;
    flex-direction: row-reverse;
    align-items: center;
    
	float: right;
}
.custom_form_Linkbtn{
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
.custom_form_Linkbtn:active{
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
					name="pageName" value="normal/user_profile.jsp"> <select id="language"
					name="language" onchange="submit()">
					<option><fmt:message
							key="label.lang_select" /></option>
					<option value="en" ><fmt:message
							key="label.lang_en" /></option>
					<option value="ua"><fmt:message
							key="label.lang_ua" /></option>
				</select>
			</form>

			<form action="/MyInternetShop/FrontController" method="get">
				<input type="hidden" id="command" name="command"
					value="GET_CART_LIST">
				<button type="submit" class=" custom_form_Linkbtn">
					<fmt:message key="label.cart" />
				</button>
			</form>

			<c:if test="${current_user.getUserType().equals('normal')}">
				<a class="active" href="normal.jsp"><c:out
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
			<fmt:message key="label.youruser" />
		</h1>

		<div class="container-fluid mt-3">
			<jsp:include page="../message.jsp" />
		</div>
		<div class='flex_col'>
			<div class='flex_col_id'>
				<div class='flex_col_item'>
					<fmt:message key="label.yourname" />
					:
				</div>
				<div class='flex_col_item'>
					<fmt:message key="label.yoursurname" />
					:
				</div>
				<div class='flex_col_item'>
					<fmt:message key="label.youremail" />
					:
				</div>
			</div>
			<div class='flex_col_value'>
				<div class='flex_col_item'>${current_user.getUserName()}</div>
				<div class='flex_col_item'>${current_user.getUserSurname()}</div>
				<div class='flex_col_item'>${current_user.getUserEmail()}</div>
			</div>
		</div>

	</div>
	<div class='row'>
		<a class="btn btn-secondary flex_btn" href="normal.jsp" role="button"><fmt:message
				key="label.goback" /></a>
	</div>
</body>
</html>