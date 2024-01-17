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
			<h2><c:out value="${player1.getUsername()}"/></h2>
			<p><c:out value="${player1.isReady()}"/></p>
			<div class="colorDiv" style="background-color: ${player1.getColorCode()};"></div>
		</div>
		
		<div id="player2">
			<h2><c:out value="${player2.getUsername()}"/></h2>
			<p><c:out value="${player2.isReady()}"/></p>
			<div class="colorDiv" style="display: block; background-color: ${player2.getColorCode()};"></div>
		</div>
		
		<div id="player3">
			<h2><c:out value="${player3.getUsername()}"/></h2>
			<p><c:out value="${player3.isReady()}"/></p>
			<div class="colorDiv" style="display: block; background-color: ${player3.getColorCode()};"></div>
		</div>
		
		<div id="player4">
			<h2><c:out value="${player4.getUsername()}"/></h2>
			<p><c:out value="${player4.isReady()}"/></p>
			<div class="colorDiv" style="display: block; background-color: ${player4.getColor().getColorCode()};"></div>
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
			- A�adir el chat.
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
		
		for(player in newLobby) {
			let playerDiv = document.getElementById("player" + player.position);
			playerDiv.getElementsByTagName("h2")[0].innerHTML = player.username;
			playerDiv.getElementsByTagName("p")[0].innerHTML = player.ready;
			playerDiv.getElementsByTagName("div")[0].style["brackground-color"] = player.color;
			playerDiv.getElementsByTagName("div")[0].style["display"] = "block";
		}
		
	}
</script>

</html>