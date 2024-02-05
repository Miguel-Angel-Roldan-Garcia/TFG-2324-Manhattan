<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="shortcut icon" type="image/x-icon" href="/resources/images/favicon.ico">
    
	<title>Manhattan</title>
	
	<%-- <spring:url value="/webjars/jquery/3.7.1/jquery.min.js" var="jQuery"/>
	<script src="${jQuery}"></script>
	
	<spring:url value="/webjars/jquery-ui/1.13.2/jquery-ui.min.css" var="jQueryUiCss"/>
	<link href="${jQueryUiCss}" rel="stylesheet"/>
	
	<spring:url value="/webjars/bootstrap/5.3.2/js/bootstrap.min.js" var="bootstrapJs"/>
	<script src="${bootstrapJs}"></script> --%>
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
	
<!-- 	<link href="/resources/css/game.css" rel="stylesheet"/> -->
	<link href="/resources/css/styles.css" rel="stylesheet"/>
	
	<jsp:doBody></jsp:doBody>
	
</head>