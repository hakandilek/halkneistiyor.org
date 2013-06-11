<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<html>
<jsp:include page="header.jsp"/>
<body>
<div class="container">
    <jsp:include page="nav.jsp"/>

    <!-- TWITTER SIGNIN -->
    <form id="tw_signin" action="<c:url value="/signin/twitter"/>" method="POST">
        <button type="submit"><img src="<c:url value="/img/sign-in-with-twitter-d.png"/>" /></button>
    </form>

    <!-- FACEBOOK SIGNIN -->
    <form name="fb_signin" id="fb_signin" action="<c:url value="/signin/facebook"/>" method="POST">
        <input type="hidden" name="scope" value="publish_stream,user_photos,offline_access" />
        <button type="submit"><img src="<c:url value="/img/sign-in-with-facebook.png"/>" /></button>
    </form>
</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>