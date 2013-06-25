<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<html>
<jsp:include page="header.jsp"/>
<body>
<div class="container">
    <jsp:include page="nav.jsp"/>

<h3>Sign Up</h3>

<c:if test="${not empty message}">
<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<c:url value="/signup" var="signupUrl" />
<form:form id="signup" action="${signupUrl}" method="post" modelAttribute="signupForm" cssClass="form-horizontal">
	<div class="formInfo">
		<s:bind path="*">
			<c:choose>
				<c:when test="${status.error}">
					<div class="error">Unable to sign up. Please fix the errors below and resubmit.</div>
				</c:when>
			</c:choose>                     
		</s:bind>
	</div>
	
	<fieldset>
        <div class="control-group">
            <form:label path="firstName">First Name</form:label>
            <div class="controls">
                <form:input path="firstName" placeholder="First name" />
            </div>

            <form:errors path="firstName" element="span" cssClass="help-block error" />
        </div>

		<form:label path="firstName">First Name <form:errors path="firstName" cssClass="error" /></form:label>

		<form:label path="lastName">Last Name <form:errors path="lastName" cssClass="error" /></form:label>
		<form:input path="lastName" />
		<form:label path="email">Email<form:errors path="email" cssClass="error" /></form:label>
		<form:input path="email" />
	</fieldset>
	<p><button type="submit">Sign Up</button></p>
</form:form>


</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>