<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1 class="text-primary">회원가입창</h1>
	
	<form method="post" action="/user/joinmember">
		아이디<input type="text" id="uidInput" name="uid" placeholer="ID" /><span id="checkId">중복체크</span><br>
		<span id="resultComment"></span>
		비밀번호<input type="password" name="upw" placeholer="PW" /><br>
		닉네임<input type="text" name="uname" placeholer="NAME" /><br>
		<!-- <input type="submit" value="가입하기"> -->
		<div id="inputSubmit"></div>
		<input type="reset" value="재입력">
	</form>
	
	<script type="text/javascript">
		$("#checkId").on("click", function(e){
			e.preventDefault();
			
			var uidValue = $("#uidInput").val();
			console.log(uidValue);
			
			var str = "";
			
			$.ajax({
				type : 'post',
				url : '/user/check/' + uidValue,
				headers : {
					"Content-Type" : "application/json",
					"X-HTTP-Method-Override" : "POST"
				},
				dataType : 'text',
			    success : function(result) {
			    	if(result.uid === uidValue){
			    		str += "아이디 체크가 완료되었습니다.";
			    		$("#resultComment").html(str);
			    	}
			    		console.log(result);
			    },
			    error : function(result){
			    	console.log("에러발생");
			    }
		    });
		});
	</script>
</body>
</html>