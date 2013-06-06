<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<jsp:include page="header.jsp" />
<body>

<div class="container">

  <jsp:include page="nav.jsp"/>
    
  <!-- Jumbotron -->
  <div class="jumbotron">
    <h1>Sesinizi duyurun!</h1>
    
    <p class="lead"></p>
    <a class="btn btn-large btn-success" href="#">Facebook</a>
    <a class="btn btn-large btn-success" href="#">Twitter</a>
    <a class="btn btn-large btn-success" href="#">Google</a>
  </div>

  <hr>

  <!-- Example row of columns -->
  <div class="row-fluid">
    <div class="span4">
      <h2>Bağımsız</h2>
      <p>halkneistiyor.org hiç bir siyasi partiye veya örgüte veya marjinal! gruplara bağlı değildir. Bu platform yalnızca halkımızın sesini özgürce duyurabilmesi ve taleplerini ortak bir paydada buluşturabilmesi için kurulmuştur.</p>
      <p><a class="btn" href="#">View details &raquo;</a></p>
    </div>
    <div class="span4">
      <h2>Şeffaf</h2>
      <p>Bu web sitesinde paylaşılan, kişilerin onayladığını ya da reddettiğini belirttiği her türlü talep kişilerin açık kimlikleriyle yapılmıştır.</p>
      <p><a class="btn" href="#">View details &raquo;</a></p>
    </div>
    <div class="span4">
      <h2>Açık kaynaklı</h2>
      <p>Bu web platformunun oluşturulmasında kullanılan yazılım tamamen açık kaynaklıdır.</p>
      <p><a class="btn" href="#">View details &raquo;</a></p>
    </div>
  </div>

  <hr>

  <div class="footer">
    <p></p>
  </div>

</div> <!-- /container -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>