<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="header.jsp"/>
<body>
<div class="container">
    <jsp:include page="nav.jsp"/>

    <form class="form-horizontal">
        <div class="control-group">
            <label class="control-label" for="inputRequest">Talep</label>
            <div class="controls">
                <input type="text" id="inputRequest" placeholder="Talebiniz">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">Talebimi ekle</button>
            </div>
        </div>
    </form>
</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>