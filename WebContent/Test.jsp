<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<button onclick="doit()">测试</button>
<script src="js/jquery-1.3.2.js"></script>
<script src="js/json2.js"></script>
<script>
	function doit(){
		var info = {"groupid" : "1", "userid" : "123456"};
		$.ajax({
			type:"POST",
			contentType:"application/json",
			data:JSON.stringify(info),
			url:"kickGroupMember.spring",
		});
	}
</script>
</body>
</html>