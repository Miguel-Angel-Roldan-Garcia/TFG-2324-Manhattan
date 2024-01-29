<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>
<head>
	<meta charset="UTF-8">
	<style>
		li.pointer-hover:hover {
			cursor: pointer;
		}
	</style>
</head>

<body>
	<h2>Lobbies of your friends</h2>
	<div>
		<ul>
			<c:forEach var="friendLobby" items="${friendsLobbies}">
				<li class="pointer-hover" onclick="setFormName('${friendLobby.getName()}')"><c:out value="${friendLobby.getName()}"/></li>
			</c:forEach>
		</ul>
	</div>
	<h2>Public lobbies</h2>
	<div>
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
			<label for="name" class="formLabel">Name of the lobby</label>
			<form:input id="formNameInput" type="text" path="name"/>
			<form:errors path="name"/>
		</div>
		
		<div>
			<label for="password" class="formLabel">Optional password of the lobby</label>
			<form:input type="password" path="password"/>
			<form:errors path="password"/>
		</div>
		<br/>

		<input type="submit" value="Join lobby">
	</form:form>
	</div>
</body>

<script>
function setFormName(name) {
	document.getElementById('formNameInput').value = name;
}
</script>

</html>