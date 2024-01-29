<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
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
	
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.60.0/dist/phaser.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.60.0/dist/phaser-arcade-physics.min.js"></script>
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
	
	<link href="/resources/css/game.css" rel="stylesheet"/>
	
</head>

<body>
	<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Manhattan relies on Javascript being enabled. Please enable Javascript and reload this page!</h2></noscript>

	<!-- Loading page overlay 
	<div id="loading-overlay">
		<h1 id="loading-msg">Please wait while the game loads.</h1>
	</div>
	-->

	<div id="main-container"></div>

	<script>
	    const gameId = "${gameId}";
	    const vw = 1920; // Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0);
	    const vh =  967;// Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0);
	    const userUsername = "${username}";
	    //let clicked = false;
	</script>
	
	<script src="/resources/scripts/main.js" type="module"></script>

</body>
</html>