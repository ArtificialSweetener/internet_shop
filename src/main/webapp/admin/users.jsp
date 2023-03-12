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
					name="pageName" value="admin/users.jsp"> <select id="language"
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

			<c:if test="${current_user == null}">
				<a href="../common_pages/login.jsp"><fmt:message key="label.login" /></a>
				<a href="../common_pages/register.jsp"><fmt:message key="label.register" /></a>
			</c:if>

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
					<fmt:message key="label.welcometousers" />
				</h1>

				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th scope="col"></th>
							<th scope="col">id</th>
							<th scope="col"><fmt:message key="label.name" /></th>
							<th scope="col"><fmt:message key="label.surname" /></th>
							<th scope="col"><fmt:message key="label.email" /></th>
							<th scope="col"><fmt:message key="label.usertype" /></th>
							<th scope="col"><fmt:message key="label.isblocked" /></th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${userList}" var="user">
							<tr>
								<th scope="row"></th>
								<td>${user.getUserId()}</td>
								<td>${user.getUserName()}</td>
								<td>${user.getUserSurname()}</td>
								<td>${user.getUserEmail()}</td>
								<td>${user.getUserType()}</td>
								<td>${user.isIs_bloked()}</td>

								<td><c:if test="${user.isIs_bloked() == false}">

										<form style="margin-top: 1em;" action="/MyInternetShop/FrontController"
											method="post">
											<input type="hidden" id="command" name="command"
												value="BLOCK_USER"> <input type="hidden"
												id="operation" name="userId" value="${user.getUserId()}">
											<button type="submit" id="delete" class="btn btn-danger">
												<fmt:message key="label.block" />
											</button>
										</form>

									</c:if> <c:if test="${user.isIs_bloked() == true}">
										<form style="margin-top: 1em;" action="/MyInternetShop/FrontController"
											method="post">
											<input type="hidden" id="command" name="command"
												value="UNBLOCK_USER"> <input type="hidden"
												id="operation" name="userId" value="${user.getUserId()}">
											<button type="submit" id="delete" class="btn btn-danger">
												<fmt:message key="label.unblock" />
											</button>
										</form>
									</c:if></td>
							</tr>

						</c:forEach>
					</tbody>
				</table>

				<c:if test="${currentPageAllUsers != null}">
					<nav aria-label="Page navigation example">
						<ul class="pagination justify-content-center">
							<li class="page-item"><c:if
									test="${currentPageAllUsers != 1}">
									<a class="page-link"
										href="/MyInternetShop/FrontController?command=GET_USERS_LIST&page=${currentPageAllUsers - 1}"><fmt:message
						key="label.previous" /></a>
								</c:if></li>


							<!--  <li class="page-item"></li>-->
							<c:forEach begin="1" end="${noOfPagesAllUsers}" var="i">
								<c:choose>
									<c:when test="${currentPageAllUsers eq i}">
										<li class="page-item active"><a class="page-link">${i}</a></li>
									</c:when>
									<c:otherwise>
										<li class="page-item"><a class="page-link"
											href="/MyInternetShop/FrontController?command=GET_USERS_LIST&page=${i}">${i}</a>
										</li>
									</c:otherwise>
								</c:choose>
							</c:forEach>

							<li class="page-item"><c:if
									test="${currentPageAllUsers lt noOfPagesAllUsers}">
									<a class="page-link"
										href="/MyInternetShop/FrontController?command=GET_USERS_LIST&page=${currentPageAllUsers + 1}"><fmt:message
						key="label.next" /></a>
								</c:if></li>
						</ul>
					</nav>
				</c:if>
				<a class="btn btn-secondary" href="admin.jsp" role="button"><fmt:message
						key="label.goback" /></a>

			</div>
		</div>
	</div>

</div>
</html>


