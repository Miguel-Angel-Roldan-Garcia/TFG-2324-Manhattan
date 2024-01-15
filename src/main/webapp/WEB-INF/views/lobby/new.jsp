<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>

<body>
	<form:form id="form" action="/lobby/new" method="post" modelAttribute="lobby">

		<label for="name" class="formLabel">Name of the lobby</label>
		<form:input type="text" path="name"/>

		<label for="password" class="formLabel">Optional password of the lobby</label>
		<form:input type="password" path="password"/>

		<label for="privacyStatus" class="formLabel">Privacy status</label>
		<form:select path="privacyStatus" items="${attributeStatuses}"/>

		<input type="submit" value="Sign in">
	</form:form>
</body>

</html>