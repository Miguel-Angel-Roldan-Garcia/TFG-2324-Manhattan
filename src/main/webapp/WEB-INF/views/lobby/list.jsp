<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>

<body>
	<h2>Lobbies of your friends</h2>
	<ul>
		<c:forEach var="friendLobby" items="${friendsLobbies}">
			<li><c:out value="${friendsLobbies.getName()}"/></li>
		</c:forEach>
	</ul>
	<h2>Public lobbies</h2>
	<ul>
		<c:forEach var="publicLobby" items="${publicLobbies}">
			<li><c:out value="${publicLobby.getName()}"/></li>
		</c:forEach>
	</ul>
</body>

</html>