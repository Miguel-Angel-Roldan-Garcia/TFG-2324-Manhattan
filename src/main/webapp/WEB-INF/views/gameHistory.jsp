<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>
<%@ page import="java.time.format.DateTimeFormatter" %>

<manhattan:manhattanPage>

	<div class="lobby-main-container">
		<div class="lobby-title-container">
			<h1 class="title-text">Historial de partidas</h1>
		</div>

		<h2>Partidas del usuario: <c:out value="${username}"/></h2>
		
		<div style="display: flex; flex-direction: row; max-width: 90vw; flex-wrap: wrap;">
			<c:forEach items="${games.entrySet()}" var="en">
				<spring:eval expression="en.getKey()" var="game"/>
				<div style="margin-left: 40px">
					<h3>Partida <c:out value="${game.id}"/></h3>
					<h4>Fecha de comienzo: <c:out value="${game.startDate.format(DateTimeFormatter.ofPattern(\"hh:mm d/M/uuuu\"))}"/></h4>
					<c:if test="${game.isFinished()}">
						<h4>Fecha de finalización: <c:out value="${game.startDate.format(DateTimeFormatter.ofPattern(\"hh:mm d/M/uuuu\"))}"/></h4>
					</c:if>
					<h4>Jugadores:</h4>
					<div style="display: flex; flex-direction: row">
						<c:forEach items="${en.getValue()}" var="player">
							<div style="margin-left: 30px">
								<h5><c:out value="${player.username}"/></h5>
								<h5><c:out value="${player.score}"/></h5>
							</div>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
		</div>
		
	</div>
	
</manhattan:manhattanPage>