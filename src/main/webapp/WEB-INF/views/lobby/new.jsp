<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	<div class="lobby-list-main-container">
		<div class="lobby-list-title-container">
			<h1 class="title-text">Crea una sala</h1>
		</div>
		<form:form id="form" action="/lobby/new" method="post" modelAttribute="lobby" acceptCharset="UTF-8">
			
			<div>
				<label for="name" class="formLabel">Nombre de la sala</label>
				<form:input type="text" path="name" maxlength="75"/>
			</div>
	
			<div>
				<label for="password" class="formLabel">Contraseña opcional de la sala</label>
				<form:input type="password" path="password" maxlength="75"/>
			</div>
	
			<div>
				<label for="privacyStatus" class="formLabel">Visibilidad (Solo dicta si aparece o no en la lista, no el acceso)</label>
				<form:select path="privacyStatus" items="${attributeStatuses}"/>
			</div>
			
			<form:input type="hidden" path="available" value="true"/>
	
			<input class="form-submit-button" type="submit" value="Create lobby">
		</form:form>
	</div>
</manhattan:manhattanPage>