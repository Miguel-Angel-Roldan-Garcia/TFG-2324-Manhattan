
class ChatStompClient {
	
	constructor(scene) {
		this.socket = new SockJS('/game-ws');
		this.stompClient = Stomp.over(this.socket);
		//this.stompClient = new Stomp.client('ws://localhost:8080/game-ws');
		
		this.stompClient.onConnect = (frame) => {
		    console.log('Connected: ' + frame);
		    stompClient.subscribe('/topic/chat', (ChatMessage) => {
		        scene.chat.addMessage(JSON.parse(ChatMessage.body).msg);
		    });
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
