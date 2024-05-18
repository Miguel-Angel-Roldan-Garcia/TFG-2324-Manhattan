<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	<div class="index-main-container">
		<div class="index-title-container">
			<h1 class="title-text">¡Bienvenido a Manhattan!</h1>
		</div>
	
		<div class="index-links-list">
			<sec:authorize access="isAuthenticated()">
				<a href="/about" class="index-links">Sobre este proyecto</a>
				<a href="/how-to-play" class="index-links">Como jugar a Manhattan</a>
				<a href="/lobby/list" class="index-links">Lista de salas</a>
				<a href="/lobby/new" class="index-links">Crear una sala</a>
				<a href="/game/history" class="index-links">Historial de partidas</a>
			</sec:authorize>
			<sec:authorize access="!isAuthenticated()">
				<a href="/about" class="index-links">Sobre este proyecto</a>
			</sec:authorize>
		</div>
	</div>

</manhattan:manhattanPage>
