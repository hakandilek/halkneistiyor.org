<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="masthead">
    <h3 class="muted">Halk ne istiyor?</h3>
    <div class="navbar">
        <div class="navbar-inner">
            <div class="container">
                <ul class="nav">
                    <li><a href="/">Ana sayfa</a></li>
                    <li><a href="/talepler/populerr">En popüler talepler</a></li>
                    <li><a href="/talepler/yeni">Yeni talep gir</a></li>
                    <li><a href="#">En yeni talepler</a></li>
                    <li><a href="#">Hakkımızda</a></li>
                    <li><a href="#">İletişim</a></li>
                    <li><a href="/connect">Welcome, <c:out value="${account.firstName}"/>!</a></li>
					<li><a href="/signout">Sign Out</a></li>
                </ul>
            </div>
        </div>
    </div><!-- /.navbar -->
</div>