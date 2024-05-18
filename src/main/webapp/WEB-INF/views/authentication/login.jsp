<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	<div class="login-main-container">
		<div class="login-title-container">
			<h1 class="title-text">Inicio de sesión</h1>
		</div>
		
		<form:form action="/login" method="post" acceptCharset="UTF-8">
	
			<form:errors path="*" element="div"/>
	
			<div>
				<label for="username" class="formLabel">Nombre de usuario</label>
				<input type="text" id="username" class="inputText" name="username"
					placeholder="Su nombre de usuario.." required>
				<form:errors path="username"/>
			</div>
			
			<div>
				<label for="password" class="formLabel">Contraseña</label>
				<input type="password" id="password" class="inputText" name="password"
					placeholder="Su contraseña.." required>
				<form:errors path="password"/>
			</div>
			
			<div>
		        <input type="checkbox" name="remember-me" id="remember-me">
		        <label for="remember-me">Recuérdame</label>
		        <form:errors path="remember-me"/>
			</div>
	
			<input type="submit" value="Iniciar sesión" class="form-submit-button">
		</form:form>
	</div>
	
</manhattan:manhattanPage>
