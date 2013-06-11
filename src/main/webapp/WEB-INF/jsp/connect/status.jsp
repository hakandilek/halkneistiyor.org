<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ page session="false" %>
<html>
<jsp:include page="../header.jsp"/>
<body>
<div class="container">
    <jsp:include page="../nav.jsp"/>

<h3>Your Connections</h3>

<c:forEach var="providerId" items="${providerIds}">
	<c:set var="connections" value="${connectionMap[providerId]}" />
	<s:message code="${providerId}.displayName" var="providerDisplayName" />
	<div class="accountConnection">
		<s:message code="${providerId}.icon" var="iconUrl"/>
		<h4><img src="<c:url value="${iconUrl}" />" width="36" height="36" />${providerDisplayName}</h4>
		
		<p>
		<c:if test="${not empty connections}">
			You are connected to ${providerDisplayName} as ${connections[0].displayName}.
		</c:if>
		<c:if test="${empty connections}">
			You are not yet connected to ${providerDisplayName}.
		</c:if>
		Click <a href="<c:url value="/connect/${providerId}" />">here</a> to manage your ${providerDisplayName} connection.
		</p>
	</div>
</c:forEach>


</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>