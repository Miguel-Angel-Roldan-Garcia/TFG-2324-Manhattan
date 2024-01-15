
class GameChat extends Phaser.GameObjects.Container {
	
	constructor (scene, x, y, width, height, stompClient) {
		super(scene, x, y)
		this.scene = scene;
		
		//General params of the chat container and text
		
		this.width = width;
		this.height = height;
		
		this.marginX = (1/10) * width;
		this.marginY = (1/10) * height; 
		
		this.inputTextHeight = 20
		
		this.textWidth = width - 2 * this.marginX;
		this.usableHeight = height - 2 * this.marginY - this.inputTextHeight;
		
		scene.add.existing(this);
		this.msgY = this.marginY;
		
		this.messages = []
		
		this.textStyle = {
			font: '20px Arial',
			fill: '#FFFFFF',
			wordWrap: {
				width: this.textWidth,
				useAdvancedWrap: true
			}
		}
		
		// Mask to not overflow text when it is too long
	    let maskShape = new Phaser.Geom.Rectangle(this.x + this.marginX,
	    										  this.y + this.marginY, 
	    										  this.textWidth, 
	    										  this.usableHeight);
        this.textMask = this.scene.add.graphics().fillRectShape(maskShape);
        //text.setMask(this.mask.createGeometryMask());
		
		// Input text object for sending messages
		const inputTextConfig = {
			type: 'text',
			placeholder: "Escriba para hablar en el chat...",
			tooltip: "Presione enter para enviar",
			fontFamily: "Arial",
			fontSize: '20px',
			color: '#FFFFFF'
		}
		
		this.inputText = this.scene.add.rexInputText(
										this.marginX, //this.x + this.marginX, //this.marginX, 
										this.height - this.marginY - this.inputTextHeight, //this.y + this.height - this.marginY - this.inputTextHeight, //this.height - this.marginY - this.inputTextHeight, 
										this.textWidth, 
										this.inputTextHeight, 
										inputTextConfig
						 )
			.resize(this.textWidth, this.inputTextHeight)
			.setOrigin(0);
			
		this.inputText.on('pointerdown', function (inputText, e) {
            inputText.setBlur();
        });
						 
		this.inputText.on('keydown', (inputText, e) => {
			if(e.key === 'Enter' && inputText.text != '') {
				inputText.setBlur();
				this.sendMessage(inputText.text)
				inputText.text = '';
			}
		});
		
		this.add(this.inputText);
		
		// STOMP client
		this.socket = new SockJS('/game-ws');
		this.stompClient = Stomp.over(this.socket);
		
		this.stompClient.connect({}, (frame) => {
		    console.log('Connected: ' + frame);
		    this.stompClient.subscribe('/topic/chat', (ChatMessage) => {
		        this.addMessage(JSON.parse(ChatMessage.body).msg);
		    });
		});
		
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
	
	getLastMessage() {
		return this.messages[this.messages.length - 1]
	}
	
	/*addMessage (message) {
		let msgTextObject;
		if(this.messages.length != 0) {
			this.msgY += this.getLastMessage().height;
		
			msgTextObject = this.scene.add.text(this.marginX, this.msgY, message, this.textStyle);
		
			if(this.msgY + msgTextObject.height > this.usableHeight) {
				while(this.msgY + msgTextObject.height > this.usableHeight && !(this.messages.length == 0)) {
					const removedMsg = this.messages.shift();
					const removedHeight = removedMsg.height;
					this.msgY -= removedHeight;
					
					this.remove(removedMsg, true);
					
					for(var i = 0; i < this.messages.length; i++) {
						this.messages[i].y -= removedHeight;
					}
				}
				
				if(this.messages.length == 0) {
					msgTextObject.y = this.usableHeight - msgTextObject.height;
				} else {
					msgTextObject.y = this.msgY;
				}
			}
		} else {
			msgTextObject = this.scene.add.text(this.marginX, this.msgY, message, this.textStyle);
		}
		
		this.add(msgTextObject);
		msgTextObject.setMask(this.mask.createGeometryMask());
		this.messages.push(msgTextObject);
		
	}*/
	
	addMessage (message) {
		let msgTextObject;
		if(this.messages.length != 0) {
			this.msgY += this.getLastMessage().height;
		
			msgTextObject = this.scene.add.text(this.marginX, this.msgY, message, this.textStyle);
		
			if(this.msgY + msgTextObject.height > this.usableHeight) {
				while(this.msgY + msgTextObject.height > this.usableHeight && !(this.messages.length == 0)) {
					this.scrollTextUp();
				}
				
				if(this.messages.length == 0) {
					msgTextObject.y = this.usableHeight - msgTextObject.height;
				} else {
					msgTextObject.y = this.msgY;
				}
			}
		} else {
			msgTextObject = this.scene.add.text(this.marginX, this.msgY, message, this.textStyle);
		}
		
		this.add(msgTextObject);
		msgTextObject.setMask(this.textMask.createGeometryMask());
		this.messages.push(msgTextObject);
		
	}
	
	 // Function to scroll the text up
    scrollTextUp() {
		if(this.getLastMessage().y > this.marginY) {
			const movedHeight = 5;
			this.msgY -= movedHeight;
			
			for(var i = 0; i < this.messages.length; i++) {
				this.messages[i].y -= movedHeight;
			}
		}
    };

    // Function to scroll the text down
    scrollTextDown() {
        if (this.messages[0].y < 0) {
            const movedHeight = 5;
			this.msgY += movedHeight;
			
			for(var i = 0; i < this.messages.length; i++) {
				this.messages[i].y += movedHeight;
			}
        }
    };
	
	sendMessage(msg) {
		this.stompClient.send("/app/chat", {}, JSON.stringify({'msg': msg}));
	}
	
}