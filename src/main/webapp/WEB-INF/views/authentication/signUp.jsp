<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>

<body>
	<form:form id="form" action="/signup" method="post" modelAttribute="user">
	
		<form:errors path="*" element="div"/>
		
		<br/>
		
		<div>
			<label for="username" class="formLabel">Username</label>
			<form:input type="text" path="username" class="inputText" placeholder="Your username.."/>
			<form:errors path="username"/>
		</div>
		
		<div>
			<label for="password" class="formLabel">Password</label>
			<form:input type="password" path="password" class="inputText" placeholder="Your password.."/>
			<form:errors path="password"/>
		</div>
		
		<form:input type="hidden" path="enabled"/>

		<input type="submit" value="Sign in">
	</form:form>
</body>

</html>