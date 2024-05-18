<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="navbar-background">

	<div class="index-links-list">
		<a href="/index" class="index-links" style="margin: 12px;">Página principal</a>
	</div>
	
	<div class="index-links-list" style="flex-direction: row;">
		<sec:authorize access="isAuthenticated()">
			<a href="/logout" class="index-links">Cerrar sesión</a>
		</sec:authorize>
		<sec:authorize access="!isAuthenticated()">
			<a href="/signup" class="index-links">Registrarse</a>
			<a href="/login" class="index-links">Iniciar sesión</a>
		</sec:authorize>
	</div>

</div>