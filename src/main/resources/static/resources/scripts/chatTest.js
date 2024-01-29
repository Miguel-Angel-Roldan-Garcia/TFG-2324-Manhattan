
export default class GameChat2 extends Phaser.GameObjects.Container {
	
	constructor (scene, x, y, width, height, stompClient, gameId, graphics) {
		super(scene, x, y)
		this.scene = scene;
		this.gameId = gameId;
		this.graphics = graphics;
		
		//General params of the chat container and text
		
		this.width = width;
		this.height = height;
		
		this.marginX = Math.min((1/10) * width, 30);
		this.marginY = Math.min((1/10) * height, 30); 
		
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
        this.textMask = this.graphics.fillRectShape(maskShape);
        
		// TODO Change the text objects to bitmap objects
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
		
		// Stomp client
		
		this.stompClient = stompClient;
		
		this.stompClient.connect({}, (frame) => {
		    console.log('Connected: ' + frame);
		    this.stompClient.subscribe('/game/'+ this.gameId + '/chat', (ChatMessage) => {
		        this.addMessage(JSON.parse(ChatMessage.body).msg);
		    });
		});
		
		// Interactive scroll of the chat
		this.setInteractive();
		
		this.scene.input.keyboard.on('keydown-UP', (event) => { this.scrollTextUp() });
        this.scene.input.keyboard.on('keydown-DOWN', (event) => { this.scrollTextDown() });
        
        this.on('pointerover', () => {
        	// console.log('Mouse is over the container');
        	
            this.scene.input.on('wheel', (pointer, gameObjects, deltaX, deltaY, deltaZ) => {
            	// console.log('deltaY: ' + deltaY);
                if(deltaY < 0) {
                	// console.log('Scrolling up or swiping down with touchpad');
                	this.scrollTextDown(deltaY);
                } else if(deltaY > 0) {
                	// console.log('Scrolling down or swiping up with touchpad');
                	this.scrollTextUp(deltaY);
                }
            });
        });

        this.on('pointerout', () => {
        	// console.log('Mouse is no longer over the container');
            this.scene.input.off('wheel');
        });
		
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
		
		msgTextObject.setMask(this.textMask.createGeometryMask(this.graphics));
		this.add(msgTextObject);
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
		this.stompClient.send("/game-msgs/" + this.gameId + "/chat", {}, JSON.stringify({'msg': msg}));
	}
	
}