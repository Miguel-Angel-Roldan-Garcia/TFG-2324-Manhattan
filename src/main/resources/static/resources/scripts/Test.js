import ManhattanStompClient from "./stomp.js";
import GameChat2 from "./chatTest.js";

export default class testRect extends Phaser.Scene
{
	constructor() {
		super("testRect");
		this.chat;
		this.graphics;
		this.stompClient;
	}
	
    preload ()
    {
    }

    create ()
    {
		this.stompClient = new ManhattanStompClient();

		// Creation of game chat
		const chatWidth = vw * 0.2;
		const chatHeight = vh * 0.3;
		const chatX = 0;
		const chatY = vh - chatHeight;
		
		//const border = this.add.rectangle(100, 100, 100, 100, "#ff0000");
		//border.setStrokeStyle(2, "#ff000")
		
		this.graphics = this.add.graphics();

		this.graphics.fillStyle(0x000000, 1);
		this.graphics.lineStyle(1, 0xffffff);
		this.chat = new GameChat2(this, chatX, chatY, chatWidth, chatHeight, this.stompClient, gameId, this.graphics);
		this.chat.addMessage("Welcome to the chat!");
		//this.updateContainerBorders();
		
		//this.graphics = this.add.graphics();
		//this.graphics2 = this.add.graphics();
		
		this.graphics.fillStyle(0xff0000, 1);
		this.graphics.lineStyle(1, 0xffffff);
		let maskShape = new Phaser.Geom.Rectangle(400, 200, 148, 148);
		//this.graphics.strokeRect(400, 200, 148, 148);
        let textMask = this.graphics.fillRect(400, 200, 148, 148);
        //this.add.existing(textMask);
		
		this.graphics.fillStyle("#000000", 1);
		this.graphics.lineStyle(1, 0xffffff);
		this.graphics.strokeRect(200, 200, 148, 148);
		
        const r1 = this.add.rectangle(200, 200, 148, 148, 0x6666ff);

        const r2 = this.add.rectangle(400, 200, 148, 148, 0x9966ff);

        r2.setStrokeStyle(4, 0xefc53f);

        const r3 = this.add.rectangle(600, 200, 148, 148);

        r3.setStrokeStyle(2, 0x1a65ac);

        const r4 = this.add.rectangle(200, 400, 148, 148, 0xff6699);

        const r5 = this.add.rectangle(400, 400, 148, 148, 0xff33cc);

        const r6 = this.add.rectangle(600, 400, 148, 148, 0xffff00);

    }
    
    update() {
		//this.updateContainerBorders()
	}
	
	updateContainerBorders() {
		let container = this.chat;

		//this.graphics.clear();
		this.graphics.fillStyle("#000000", 1);
		this.graphics.lineStyle(1, 0xffffff);
		this.graphics.strokeRect(container.x, container.y, container.width, container.height);
	}
}

