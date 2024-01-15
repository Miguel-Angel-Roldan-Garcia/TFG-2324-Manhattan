<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>

<body>
	<form:form id="form" name="newUser" action="/signUp" method="post">

		<c:if test="${param.error != null}">
			<div style="margin-bottom: 15px;">
				Failed to login.
				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
							Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
				</c:if>
			</div>
		</c:if>

		<label for="username" class="formLabel">Username</label>
		<input type="text" id="username" class="inputText" name="username"
			placeholder="Your username.." required>

		<label for="password" class="formLabel">Password</label>
		<input type="password" id="password" class="inputText" name="password"
			placeholder="Your password.." required>
		<i class="far fa-eye" id="togglePassword"></i>

		<input type="submit" value="Sign in">
	</form:form>
</body>

</html>