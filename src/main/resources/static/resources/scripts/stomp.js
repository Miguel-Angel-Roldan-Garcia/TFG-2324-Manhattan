
class ManhattanStompClient {
	
	constructor(scene) {
		this.socket = new SockJS('/game-ws');
		this.stompClient = Stomp.over(this.socket);
		
		this.socket.onclose = () => {
			console.log('WebSocket connection closed');
		};
		
		this.socket.onerror = (error) => {
			console.error('WebSocket error', error);
		};
		
		this.stompClient.onWebSocketError = (error) => {
		    console.error('Error with websocket', error);
		};
		
		this.stompClient.onStompError = (frame) => {
		    console.error('Broker reported error: ' + frame.headers['message']);
		    console.error('Additional details: ' + frame.body);
		};
		
	}
	
	activate() {
		this.stompClient.activate();
		/*this.stompClient.connect({}, function (frame) {
		   console.log('Connected: ' + frame);
		});*/
		/*this.stompClient.subscribe('/topic/chat', function (ChatMessage) {
	       console.log('Received: ' + JSON.parse(ChatMessage.body).msg);
	   	});*/
	}
	
	sendMsg(msg) {
	    /*stompClient.publish({
	        destination: "/app/hello",
	        body: JSON.stringify({'msg': msg})
	    });*/
	    this.stompClient.send("/app/chat", {}, JSON.stringify({'message': msg}));
	}
	
}
