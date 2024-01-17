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

		<label for="privacyStatus" class="formLabel">Privacy status (Whether or not it is shown on the list, it doesn't control who can enter)</label>
		<form:select path="privacyStatus" items="${attributeStatuses}"/>

		<input type="submit" value="Create lobby">
	</form:form>
</body>

</html>