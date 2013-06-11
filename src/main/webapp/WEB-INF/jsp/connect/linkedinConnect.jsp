<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>
<html>
<jsp:include page="../header.jsp"/>
<body>
<div class="container">
    <jsp:include page="../nav.jsp"/>

<h3>Connect to LinkedIn</h3>

<form action="<c:url value="/connect/linkedin" />" method="POST">
	<div class="formInfo">
		<p>
			You haven't created any connections with LinkedIn yet. Click the button to connect Spring Social Showcase with your LinkedIn account. 
			(You'll be redirected to LinkedIn where you'll be asked to authorize the connection.)
		</p>
	</div>
	<p><button type="submit">Connect with LinkedIn</button></p>
</form>


</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>