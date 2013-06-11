<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ page session="false" %>
<html>
<jsp:include page="../header.jsp"/>
<body>
<div class="container">
    <jsp:include page="../nav.jsp"/>

<h3>Connected to Facebook</h3>

<form id="disconnect" method="post">
	<div class="formInfo">
		<p>
			Spring Social Showcase is connected to your Facebook account.
			Click the button if you wish to disconnect.
		</p>		
	</div>
	<button type="submit">Disconnect</button>	
	<input type="hidden" name="_method" value="delete" />
</form>

<a href="<c:url value="/facebook"/>">View your Facebook profile</a>


</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>