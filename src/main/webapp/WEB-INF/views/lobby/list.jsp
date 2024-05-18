<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	<div class="lobby-list-main-container">
		<div class="lobby-list-title-container">
			<h1 class="title-text">Lista de salas</h1>
		</div>
		
		<div>
			<h2>Mis salas</h2>
			<ul>
				<c:forEach var="ownedLobby" items="${ownedLobbies}">
					<li class="pointer-hover" onclick="setFormName('${ownedLobby.getName()}')"><c:out value="${ownedLobby.getName()}"/></li>
				</c:forEach>
			</ul>
		</div>
		
		<div>
			<h2>Salas de amigos</h2>
			<ul>
				<c:forEach var="friendLobby" items="${friendsLobbies}">
					<li class="pointer-hover" onclick="setFormName('${friendLobby.getName()}')"><c:out value="${friendLobby.getName()}"/></li>
				</c:forEach>
			</ul>
		</div>
		
		<div>
			<h2>Salas públicas</h2>
			<ul>
				<c:forEach var="publicLobby" items="${publicLobbies}">
					<li class="pointer-hover" onclick="setFormName('${publicLobby.getName()}')"><c:out value="${publicLobby.getName()}"/></li>
				</c:forEach>
			</ul>
		</div>
		
		<div>
			<form:form id="form" action="/lobby/join" method="post" modelAttribute="lobby" acceptCharset="UTF-8">
	
			<form:errors path="*" element="div"/>
			<br/>
	
			<div>
				<label for="name">Nombre de la sala</label>
				<form:input id="formNameInput" type="text" path="name"/>
				<form:errors path="name"/>
			</div>
			
			<div>
				<label for="password">Contraseña opcional de la sala</label>
				<form:input type="password" path="password"/>
				<form:errors path="password"/>
			</div>
			<br/>
	
			<input type="submit" value="Unirse a la sala" class="form-submit-button">
		</form:form>
		</div>
	</div>

<script>
function setFormName(name) {
	document.getElementById('formNameInput').value = name;
}
</script>

</manhattan:manhattanPage>