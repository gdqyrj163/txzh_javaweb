<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
用户id：<input type="text" id="userid" />
经度：<input type="text" id="longtude" />
纬度：<input type="text" id="latitude" />

<button onclick="doit()">测试GPS</button>
<script src="js/jquery-1.3.2.js"></script>
<script src="js/json2.js"></script>
<script>
	function doit(){
		var userid = $("#userid").val();
		var longtude = $("#longtude").val();
		var latitude = $("#latitude").val();
		var checkCode = $("#checkCode").val();
		var info = {"userid" : userid,"longtude" : longtude, "latitude" : latitude};
		$.ajax({
			type:"POST",
			contentType:"application/json",
			data:JSON.stringify(info),
			url:"catchGPS.spring",
			success:function(resultString){
				var result = JSON.parse(resultString);
				if(result.resultCode == 1){
					alert("success");
				}else{
					alert("fail");
				}
			}
		});
	}
</script>
</body>
</html>