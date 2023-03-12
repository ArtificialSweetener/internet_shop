<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test = "${message != null}">
	<div class="alert alert-warning" role="alert">
		${message}
	</div>
	<!--<c:remove var ="message"/> -->
</c:if>

