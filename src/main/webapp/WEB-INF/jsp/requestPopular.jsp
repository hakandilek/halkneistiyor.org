<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<jsp:include page="header.jsp"/>
<body>

<div class="container">
    <jsp:include page="nav.jsp"/>

    <table class="table table-striped">
        <thead>
        <tr>
            <th></th>
            <th>Talep</th>
            <th>Onaylayanlar</th>
            <th>Reddedenler</th>
            <th>Oran</th>
        </tr>
        </thead>

        <tbody>
        <tr>
            <td><span class="badge badge-success">1</span></td>
            <td>Topçu kışlası projesi iptal edilmeli.</td>
            <td>
                <a href="/topcu-kislasi-iptal-edilmeli?onay=evet">10101</a> %95 <span class="label label-success">Evet</span>
				<c:if test="${authenticated}">
	                <div style="padding-top: 10px;">
	                    <a class="btn btn-small btn-success" href="#"><i class="icon-thumbs-up icon-white"></i> Evet</a>
	                </div>
                </c:if>
            </td>
            <td>
                <a href="/topcu-kislasi-iptal-edilmeli?onay=hayir">120</a> %5 <span class="label label-important">Hayır</span>

				<c:if test="${authenticated}">
	                <div style="padding-top: 10px;">
	                    <a class="btn btn-small btn-danger" href="#"><i class="icon-thumbs-down icon-white"></i> Hayır</a>
	                </div>
                </c:if>
            </td>
            <td>
                <div class="progress">
                    <div class="bar bar-success" style="width: 95%;"></div>
                    <div class="bar bar-danger" style="width: 5%;"></div>
                </div>
            </td>
        </tr>
        <tr>
            <td><span class="badge badge-warning">2</span></td>
            <td>Başbakan halktan özür dilemeli.</td>
            <td>10101 %90 <span class="label label-success">Evet</span></td>
            <td>120 %10 <span class="label label-important">Hayır</span></td>
            <td>
                <div class="progress">
                    <div class="bar bar-success" style="width: 90%;"></div>
                    <div class="bar bar-danger" style="width: 10%;"></div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>

    <div class="pagination">
        <ul>
            <li><a href="#">Önceki</a></li>
            <li><a href="#">1</a></li>
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>
            <li><a href="#">Sonraki</a></li>
        </ul>
    </div>
</div>
<!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>