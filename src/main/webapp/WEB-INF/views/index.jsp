<%@ page language="java" contentType="text/html"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
	<title>${pageName}</title>
	
</head>

<body>
	<div>
		Hello Manhattan!
	</div>
	<div>
		<sec:authorize access="isAuthenticated()">
			<a href="/game">Link to game</a><br/>
			<a href="/lobby/list">List of lobbies</a><br/>
			<a href="/lobby/new">Create a lobby</a><br/>
			<a href="/logout">Logout</a><br/>
		</sec:authorize>
		<sec:authorize access="!isAuthenticated()">
			<a href="/signup">Sign up</a><br/>
			<a href="/login">Login</a>
		</sec:authorize>
	</div>

</body>

<spring:url value="/webjars/jquery/3.7.1/jquery.min.js" var="jQuery"/>
<script src="${jQuery}"></script>

<spring:url value="/webjars/jquery-ui/1.13.2/jquery-ui.min.css" var="jQueryUiCss"/>
<link href="${jQueryUiCss}" rel="stylesheet"/>

<spring:url value="/webjars/bootstrap/5.3.2/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>

</html>