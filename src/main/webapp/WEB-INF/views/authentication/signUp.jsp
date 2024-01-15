<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>

<body>
	<form:form id="form" action="/signup" method="post" modelAttribute="user">

		<label for="username" class="formLabel">Username</label>
		<form:input type="text" path="username" class="inputText" placeholder="Your username.."/>

		<label for="password" class="formLabel">Password</label>
		<form:input type="password" path="password" class="inputText" placeholder="Your password.."/>

		<form:input type="hidden" path="enabled"/>

		<input type="submit" value="Sign in">
	</form:form>
</body>

</html>