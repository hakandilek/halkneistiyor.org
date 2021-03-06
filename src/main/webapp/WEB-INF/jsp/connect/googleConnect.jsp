<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>
<html>
<jsp:include page="../header.jsp"/>
<body>
<div class="container">
    <jsp:include page="../nav.jsp"/>

<h3>Connect to Google</h3>

<form action="<c:url value="/connect/google" />" method="POST">
	<div class="formInfo">
		<p>
			You haven't created any connections with Google yet. Click the button to connect Spring Social Showcase with your Google account. 
			(You'll be redirected to Google where you'll be asked to authorize the connection.)
		</p>
	</div>
	<p><button type="submit"><img src="<c:url value="/resources/social/google/connect-with-google.png" />"/></button></p>
	<label for="postTweet"><input id="postTweet" type="checkbox" name="postTweet" /> Post a tweet about connecting with Spring Social Showcase</label>
</form>



</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>