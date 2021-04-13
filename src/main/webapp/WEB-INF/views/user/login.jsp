<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1 class="text-primary">로그인 창</h1>
	<form action="/user/loginPost" method="post">
		<div class="form-group has feedback">
			<input type="text" name="uid" class="form-control" placeholder="ID" />
			<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
		</div>
		<div class="form-group has-feedback">
			<input type="password" name="upw" class="form-control" placeholder="PW" />
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		</div>
		<div class="row">
			<div class="col-xs-8">
				<div class="checkbox icheck">
					<label>
						<input type="checkbox" name="useCookie">Remember Me
					</label>
				</div>
			</div>
		</div><!-- /.col -->
		<div class="col-xs-4">
			<button type="submit" class="btn btn-primary btn-block btn-flat">Sign in</button>
		</div><!-- /.col -->
	</form>

</body>
</html>