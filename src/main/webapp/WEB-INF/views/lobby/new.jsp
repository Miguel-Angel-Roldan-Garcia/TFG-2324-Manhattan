<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	<div class="lobby-list-main-container">
		<div class="lobby-list-title-container">
			<h1 class="title-text">¡Bienvenido a Manhattan!</h1>
		</div>
		<form:form id="form" action="/lobby/new" method="post" modelAttribute="lobby" acceptCharset="UTF-8">
			
			<div>
				<label for="name" class="formLabel">Name of the lobby</label>
				<form:input type="text" path="name"/>
			</div>
	
			<div>
				<label for="password" class="formLabel">Optional password of the lobby</label>
				<form:input type="password" path="password"/>
			</div>
	
			<div>
				<label for="privacyStatus" class="formLabel">Privacy status (Whether or not it is shown on the list, it doesn't control who can enter)</label>
				<form:select path="privacyStatus" items="${attributeStatuses}"/>
			</div>
			
			<form:input type="hidden" path="available" value="true"/>
	
			<input type="submit" value="Create lobby">
		</form:form>
	</div>
</manhattan:manhattanPage>