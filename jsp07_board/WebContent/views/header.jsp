<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./include/includefile.jsp" %>
<%@ include file="./include/msg.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	header {
		display: flex;
		justify-content: space-around;
	} 

</style>
</head>
<body>
	<header>
		<div> <img alt="흰둥이" src="${path}/views/images/ja.png" width="100px"> </div>
		<div> 이젠 컴퓨터</div>
		<div>
			<span><a href="${path}/member/myinfo">${sessionScope.email}</a></span>
			<!-- empty sessionScope.email :null체크 -->
			<c:if test="${empty sessionScope.email}">
				<a href="${path}/views/member/login.jsp">LOGIN</a>
				<a href="${path}/views/member/add.jsp">SIGN UP</a>
			</c:if>
			<c:if test="${not empty sessionScope.email}">
				<a href="${path}/member/logout">LOGOUT</a>
			</c:if>
		</div>
	</header>
	<hr>
	<nav>
		<a href="${path}/views/home.jsp">HOME</a>
		<a href="${path}/board/list">리스트</a>
		<a href="${path}/views/board/add.jsp">등록</a>
	</nav>
	<hr>
</body>
</html>