<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<jsp:include page="../header.jsp"/>
<body>
<div class="container">
    <jsp:include page="../nav.jsp"/>

<h3>Connected to Twitter</h3>

<form id="disconnect" method="post">
	<div class="formInfo">
		<p>The Spring Social Showcase sample application is already connected to your Twitter account.
			Click the button if you wish to disconnect.			
	</div>

	<button type="submit">Disconnect</button>	
	<input type="hidden" name="_method" value="delete" />
</form>

<p><a href="<c:url value="/twitter" />">View your Twitter profile</a></p>



</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>