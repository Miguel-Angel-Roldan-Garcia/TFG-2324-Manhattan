<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="shortcut icon" type="image/x-icon" href="/resources/images/favicon.ico">
    
	<title>Manhattan</title>
	
	<spring:url value="/webjars/jquery/3.7.1/jquery.min.js" var="jQuery"/>
	<script src="${jQuery}"></script>
	
	<spring:url value="/webjars/jquery-ui/1.13.2/jquery-ui.min.css" var="jQueryUiCss"/>
	<link href="${jQueryUiCss}" rel="stylesheet"/>
	
	<spring:url value="/webjars/bootstrap/5.3.2/js/bootstrap.min.js" var="bootstrapJs"/>
	<script src="${bootstrapJs}"></script>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

	<style>
	.colorDiv {
		border-style: solid;
		border-color: #000000;
		border-width: 5px;
		margin: auto;
		height:3vh;
		width: 10vh;
	}
	</style>
</head>

<body>
	<h2>Lobby of id <c:out value="${lobby.getId()}"/></h2>
	
	<div id="players">
		<div id="player1">
			<h2><c:out value="${lobby.getOwner().getUsername()}"/></h2>
			<p><c:out value="${lobby.getOwner().isReady()}"/></p>
			<c:out value="${lobby.getOwner().getColorCode()}"/>
			<c:out value="${lobby.getPlayer2().getColorCode()}"/>
			<div class="colorDiv" style="background-color: ${lobby.getOwner().getColorCode()};"></div>
		</div>
		
		<div id="player2">
			<h2><c:out value="${lobby.getPlayer2().getUsername()}"/></h2>
			<p><c:out value="${lobby.getPlayer2().isReady()}"/></p>
			<div class="colorDiv" style="display: block; background-color: ${lobby.getPlayer2().getColorCode()};"></div>
		</div>
		
		<div id="player3">
			<h2><c:out value="${lobby.getPlayer3().getUsername()}"/></h2>
			<p><c:out value="${lobby.getPlayer3().isReady()}"/></p>
			<div class="colorDiv" style="display: block; background-color: ${lobby.getPlayer3().getColorCode()};"></div>
		</div>
		
		<div id="player4">
			<h2><c:out value="${lobby.getPlayer4().getUsername()}"/></h2>
			<p><c:out value="${lobby.getPlayer4().isReady()}"/></p>
			<div class="colorDiv" style="display: block; background-color: ${lobby.getPlayer4().getColor().getColorCode()};"></div>
		</div>
	</div>
	
	<div>
		<a href="/lobby/${lobby.getId()}/start">Start game</a>
	</div>
	
	
	<!-- TODO 
			- Mostrar datos del lobby. Kinda done
			- Controlar si los jugadores estan listos.
			- Controlar abandonos de jugadores que no son el owner.
			- Mecanismo de cambio de color del jugador.
			- Añadir el chat.
			- Que se borre si el owner se va, con sesiones y el websocket.
			- Que se eche a los demas si el owner se va.
			- Empezar partida.
			- Que el host pueda echar a otros usuarios del lobby.
	 -->
	
</body>

<script defer>
	if(document.getElementById("player2").getElementsByTagName("h2")[0].innerHTML == "") {
		document.getElementById("player2").getElementsByTagName("div")[0].style.display = "none";
	}
	if(document.getElementById("player3").getElementsByTagName("h2")[0].innerHTML == "") {
		document.getElementById("player3").getElementsByTagName("div")[0].style.display = "none";
	}
	if(document.getElementById("player4").getElementsByTagName("h2")[0].innerHTML == "") {
		document.getElementById("player4").getElementsByTagName("div")[0].style.display = "none";
	}
</script>

<script>
	//STOMP client
	socket = new SockJS('/lobby-ws');
	stompClient = Stomp.over(socket);
	
	stompClient.connect({}, (frame) => {
	    console.log('Connected: ' + frame);
	    stompClient.subscribe('/lobby/${lobby.id}/lobbyReload', (newLobby) => {
	        handleUpdateLobby(JSON.parse(newLobby.body));
	    });
	});
	
	socket.onclose = () => {
		console.log('WebSocket connection closed');
	};
	
	socket.onerror = (error) => {
		console.error('WebSocket error', error);
	};
	
	stompClient.onWebSocketError = (error) => {
	    console.error('Error with websocket', error);
	};
	
	stompClient.onStompError = (frame) => {
	    console.error('Broker reported error: ' + frame.headers['message']);
	    console.error('Additional details: ' + frame.body);
	};
	
	function handleUpdateLobby(newLobby) {
		console.log(newLobby);
		if(newLobby.player2) {
			let player2Div = document.getElementById("player2");
			player2Div.getElementsByTagName("h2")[0].innerHTML = newLobby.player2.username;
			player2Div.getElementsByTagName("p")[0].innerHTML = "false";
			player2Div.getElementsByTagName("div")[0].style["brackground-color"] = newLobby.player2.color;
			player2Div.getElementsByTagName("div")[0].style["display"] = "block";
		}
			
		if(newLobby.player3) {
			let player2Div = document.getElementById("player3");
			player2Div.getElementsByTagName("h2")[0].innerHTML = newLobby.player3.username;
			player2Div.getElementsByTagName("p")[0].innerHTML = "false";
			player2Div.getElementsByTagName("div")[0].style["brackground-color"] = newLobby.player3.color;
			player2Div.getElementsByTagName("div")[0].style["display"] = "block";
		}
			
		if(newLobby.player4) {
			let player2Div = document.getElementById("player4");
			player2Div.getElementsByTagName("h2")[0].innerHTML = newLobby.player4.username;
			player2Div.getElementsByTagName("p")[0].innerHTML = "false";
			player2Div.getElementsByTagName("div")[0].style["brackground-color"] = newLobby.player4.color;
			player2Div.getElementsByTagName("div")[0].style["display"] = "block";
		}
	}
</script>

</html>