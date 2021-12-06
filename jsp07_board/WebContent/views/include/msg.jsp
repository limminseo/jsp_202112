<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript">
	/* 파라메터의 메세지 */
	if ('${param.msg}' != '')
		alert('${param.msg}');
	
	/* requestScope 메세지 */
	if('${msg}' != '')
		alert('${msg}');
</script>
</body>
</html>