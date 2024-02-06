<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	<div class="login-main-container">
		<div class="login-title-container">
			<h1 class="title-text">Login</h1>
		</div>
		
		<form:form id="form" action="/signup" method="post" modelAttribute="signUpForm" acceptCharset="UTF-8">
		
			<form:errors path="" element="div"/>
			
			<div>
				<label for="username" class="formLabel">Username</label>
				<form:input type="text" path="username" class="inputText" placeholder="Su nombre de usuario.."/>
				<form:errors path="username"/>
			</div>
			
			<div>
				<label for="password" class="formLabel">Contraseña</label>
				<form:input type="password" path="password" class="inputText" placeholder="Su contraseña.."/>
				<form:errors path="password"/>
			</div>
			
			<div>
				<label for="accessCode" class="formLabel">Codigo de acceso</label>
				<form:input type="accessCode" path="accessCode" class="inputText" placeholder="El código de acceso proporcionado"/>
				<form:errors path="accessCode"/>
			</div>
	
			<input type="submit" value="Sign in" class="form-submit-button">
		</form:form>
	</div>
</manhattan:manhattanPage>

