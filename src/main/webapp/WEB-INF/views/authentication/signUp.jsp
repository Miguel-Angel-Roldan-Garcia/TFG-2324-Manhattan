<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	<div class="login-main-container">
		<div class="login-title-container">
			<h1 class="title-text">Registro</h1>
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
			
			<input type="submit" value="Registrarse" class="form-submit-button">
		</form:form>
	</div>
</manhattan:manhattanPage>

