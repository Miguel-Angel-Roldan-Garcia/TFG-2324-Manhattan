<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>
	
	<div class="lobby-main-container">
		<div class="lobby-title-container">
			<h1 class="title-text">Sala</h1>
		</div>

		<h2>Sala <c:out value="${lobby.getId()}"/>: <c:out value="${lobby.getName()}"/></h2>
		
		<div class="lobby-content">
			<div id="players" class="lobby-player-list-container">
				<div id="player1" class="lobby-player-container">
					<h2><c:out value="${player1.getUsername()}"/></h2>
					<p class="pointer-hover" onclick="toggleReady(1)"><c:out value="${player1.isReady() ? 'Listo' : 'No listo'}"/></p>
					<div class="lobby-player-color-div" style="background-color: ${player1.getColorCode()};" onclick="changeColor(1)"></div>
					<button type="button" <c:if test="${!principal.isLobbyOwner()}">style="display: none;"</c:if> class="form-submit-button" onclick="abandonLobbyByPosition(1)">Expulsar</button>
				</div>
				
				<div id="player2" class="lobby-player-container">
					<h2><c:out value="${player2.getUsername()}"/></h2>
					<p class="pointer-hover" onclick="toggleReady(2)"><c:out value="${player2.isReady() ? 'Listo' : 'No listo'}"/></p>
					<div class="lobby-player-color-div" style="display: block; background-color: ${player2.getColorCode()};" onclick="changeColor(2)"></div>
					<button type="button" <c:if test="${!principal.isLobbyOwner()}">style="display: none;"</c:if> class="form-submit-button" onclick="abandonLobbyByPosition(2)">Expulsar</button>
				</div>
				
				<div id="player3" class="lobby-player-container">
					<h2><c:out value="${player3.getUsername()}"/></h2>
					<p class="pointer-hover" onclick="toggleReady(3)"><c:out value="${player3.isReady() ? 'Listo' : 'No listo'}"/></p>
					<div class="lobby-player-color-div" style="display: block; background-color: ${player3.getColorCode()};" onclick="changeColor(3)"></div>
					<button type="button" <c:if test="${!principal.isLobbyOwner()}">style="display: none;"</c:if> class="form-submit-button" onclick="abandonLobbyByPosition(3)">Expulsar</button>
				</div>
				
				<div id="player4" class="lobby-player-container">
					<h2><c:out value="${player4.getUsername()}"/></h2>
					<p class="pointer-hover" onclick="toggleReady(4)"><c:out value="${player4.isReady() ? 'Listo' : 'No listo'}"/></p>
					<div class="lobby-player-color-div" style="display: block; background-color: ${player4.getColorCode()};" onclick="changeColor(4)"></div>
					<button type="button" <c:if test="${!principal.isLobbyOwner()}">style="display: none;"</c:if> class="form-submit-button" onclick="abandonLobbyByPosition(4)">Expulsar</button>
				</div>
			</div>
			
			<div class="lobby-chat-container">
				<div id="chatMessagesContainer" class="lobby-chat-container-messages"></div>
				
				<div class="lobby-chat-input-container">
					<input id="chatInput" class="chat-text" type="text" placeholder="Escriba para hablar en el chat...">
					<button type="button" class="form-submit-button" onclick="sendChat();">Send</button>
				</div>
			</div>
			
		</div>
		
		<div class="lobby-footer-container">
			<button class="form-submit-button large-text" onclick="abandonLobby();">Abandonar sala</button>
			
			<c:choose>
				<c:when test="${principal.isLobbyOwner()}">
					<button id="start" class="form-submit-button large-text" onclick="startGame();">Empezar partida</button>
				</c:when>
				<c:otherwise>
					<button id="start" style="display: none;" class="form-submit-button large-text" onclick="startGame();">Empezar partida</button>
				</c:otherwise>
			</c:choose>
			
			<div class="lobby-footer-errors-container">
				<h3 id="errors"></h3>
			</div>			
		</div>
	</div>
	
	
	<!-- TODO 
			- Control when players get disconnected or close the window
			- Owner should be able to throw people out
	 -->
	
	<script defer>
		if(document.getElementById("player1").getElementsByTagName("h2")[0].innerHTML == "") {
			document.getElementById("player1").style.display = "none";
		}
		
		if(document.getElementById("player2").getElementsByTagName("h2")[0].innerHTML == "") {
			document.getElementById("player2").style.display = "none";
		}
		
		if(document.getElementById("player3").getElementsByTagName("h2")[0].innerHTML == "") {
			document.getElementById("player3").style.display = "none";
		}
		
		if(document.getElementById("player4").getElementsByTagName("h2")[0].innerHTML == "") {
			document.getElementById("player4").style.display = "none";
		}
	</script>
	
	<script>
		const principalUsername = "${principal.getUsername()}";
	
		function handleUpdateLobby(newLobby) {
			console.log(newLobby);
			let principalInLobby = false;
			let positions = [1,2,3,4];
			
			for(player of newLobby) {
				let playerDiv = document.getElementById("player" + player.position);
				playerDiv.style.display = "";
				playerDiv.getElementsByTagName("h2")[0].innerHTML = player.username;
				playerDiv.getElementsByTagName("p")[0].innerHTML = player.ready ? 'Listo' : 'No listo';
				playerDiv.getElementsByTagName("div")[0].style["background-color"] = player.color;
				playerDiv.getElementsByTagName("div")[0].style["display"] = "block";
				
				let start = document.getElementById("start");
				if(player.lobbyOwner && player.username == principalUsername) {
					start.style["display"] = "";
					for(let i = 1; i <= 4; i++) {
						document.getElementById("player" + i).getElementsByTagName("button")[0].style = "";
					}
				}
				
				delete(positions[player.position-1])
				
				if(!principalInLobby && player.username == principalUsername) {
					principalInLobby = true;
				}
			}
			
			for(position of positions) {
				if(position) {
					document.getElementById("player" + position).style.display = "none";
				}
			}
			
			if(!principalInLobby) {
				window.location.replace("/index");
			}
		}
		
		function addChatMessage(msgText) {
			let chat = document.getElementById("chatMessagesContainer");
			let msg = document.createElement("p");
			msg.classList.add("chat-text");
			msg.innerText = msgText;
			chat.appendChild(msg);
			msg.scrollIntoView();
		}

		function sendChatMessage(msgText) {
			stompClient.send("/lobby-msgs/lobby/${lobby.getId()}/chat", {}, JSON.stringify({'msg': msgText}));
		}
		
		function sendChat() {
			let input = document.getElementById("chatInput");
			const msgText = input.value;
			if(input.value) {
				input.value = null;
				sendChatMessage(msgText);
			}
		}
		
		//STOMP client
		socket = new SockJS("/lobby-ws");
		stompClient = Stomp.over(socket);
		
		(async () => {
			await new Promise((resolve) => {
				stompClient.connect({}, (frame) => {
				    console.log("Connected: " + frame);
				    sendChatMessage(' se ha unido a la sala.');
				    resolve();
				});
			});
			
			socket.onclose = () => {
				console.log("WebSocket connection closed");
			};
	
			socket.onerror = (error) => {
				console.error("WebSocket error", error);
			};
	
			stompClient.onWebSocketError = (error) => {
				console.error("Error with websocket", error);
			};
	
			stompClient.onStompError = (frame) => {
				console.error("Broker reported error: " + frame.headers["message"]);
				console.error("Additional details: " + frame.body);
			};
			
			stompClient.subscribe("/lobby/${lobby.id}/lobbyReload", (newLobby) => {
		        handleUpdateLobby(JSON.parse(newLobby.body));
		    });
			
			stompClient.subscribe("/lobby/${lobby.id}/chat", (ChatMessage) => {
		        addChatMessage(JSON.parse(ChatMessage.body).msg);
		    });
			
			stompClient.subscribe("/lobby/${lobby.id}/start", (responseMsg) => {
				const msg = responseMsg.body;
				
				if(msg.startsWith("start")) {
					gameId = msg.split("-")[1];
					
					let i = 5;
					const countdown = setInterval(() => {
						if(i > 0) {
							addChatMessage("La partida comenzará en " + i-- + " segundos.")
						} else {
							clearInterval(countdown);
							window.location.replace("/game/" + gameId);
						}
					}, 1000);
				} else {
					document.getElementById("errors").innerHTML = msg;
				}
		    });
		})();
		
		document.getElementById("chatInput").addEventListener("keydown", function(event) {
		    if (event.key === "Enter") {
		        sendChat();
		    }
		});
		
		function toggleReady(playerPos) {
			stompClient.send("/lobby-msgs/lobby/${lobby.getId()}/toggle-ready/" + playerPos, {}, null);
		}
		
		function changeColor(playerPos) {
			stompClient.send("/lobby-msgs/lobby/${lobby.getId()}/change-color/" + playerPos, {}, null);
		}
		
		function startGame() {
			for(let i = 1; i <= 4; i++) {
				let playerDiv = document.getElementById("player" + i);
				const ready = playerDiv.getElementsByTagName("p")[0].innerHTML;
				if(ready != "Listo" && document.getElementById("player" + i).style.display !== "none") {
					document.getElementById("errors").innerHTML = "Algún jugador no está listo";
					return;
				}
			}
			
			stompClient.send("/lobby-msgs/lobby/${lobby.getId()}/start", {}, null);
		}
		
		function abandonLobbyByPosition(position) {
			stompClient.send("/lobby-msgs/lobby/${lobby.getId()}/abandon/" + position, {}, null);
		}
		
		function abandonLobby() {
			abandonLobbyByPosition("${principal.getPosition()}");
		}
		
	</script>

</manhattan:manhattanPage>