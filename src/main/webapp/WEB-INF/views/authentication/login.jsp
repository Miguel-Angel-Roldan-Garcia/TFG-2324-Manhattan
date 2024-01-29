<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>

<body>
<%-- 	<form:form id="form" name="newUser" action="/login" method="post"> --%>
	<form:form action="/login" method="post" acceptCharset="UTF-8">

		<form:errors path="*" element="div"/>

		<div>
			<label for="username" class="formLabel">Username</label>
			<input type="text" id="username" class="inputText" name="username"
				placeholder="Your username.." required>
			<form:errors path="username"/>
		</div>
		
		<div>
			<label for="password" class="formLabel">Password</label>
			<input type="password" id="password" class="inputText" name="password"
				placeholder="Your password.." required>
			<form:errors path="password"/>
		</div>
		
		<div>
	        <input type="checkbox" name="remember-me" id="remember-me">
	        <label for="remember-me">Remember me</label>
	        <form:errors path="remember-me"/>
		</div>

		<input type="submit" value="Sign in">
	</form:form>
	
</body>

</html>