import fetchData from "./fetchData.js";
import ManhattanStompClient from "./stomp.js";

export default class Preload extends Phaser.Scene {
	constructor() {
		super("Preload");
	}

	preload() {
		// Scrollable container plugin
		this.load.plugin(
			'rexinputtextplugin',
			'https://raw.githubusercontent.com/rexrainbow/phaser3-rex-notes/master/dist/rexinputtextplugin.min.js',
			true
		);
		
		this.load.setPath("/resources/images/");
		this.load.image("avatarPlaceholder", "avatar-placeholder.png");
		this.load.image("blockUp", "BlockUp.png");
		this.load.image("sector", "Sector.png");
		this.load.image("cardBack", "CardBack.png");
		this.load.image("background", "background.jpg");
		this.load.audio('soundtrack', "../audio/Manhattan.ogg");
		
		for(let i = 1; i <= 4; i++) {
			this.load.image("story" + i + "block", "Story" + i +".png");
		}
		
		for(let i = 1; i <= 9; i++) {
			this.load.image("card" + i, "Card" + i +".png");
		}
	}

	create() {
		let text = this.add.bitmapText(0, 0, "Arial", "Espere mientras el juego carga", 72).setOrigin(0.5);
		text.x = vw/2;
		text.y = vh/2;
		
		if(this.sys.game.config.renderType !== Phaser.WEBGL) {
			text.text = "Su navegador no soporta WebGL.";
			
		} else {
			fetchData("/game/" + gameId + "/get-data", 3)
			.then(data => {
				let stompClient = new ManhattanStompClient();
				stompClient.connect({}, (frame) => {
				    console.log('Connected: ' + frame);
				    this.scene.start("Game", { "gameData": data, "stompClient": stompClient });
				});
			})
			.catch(error => {
				text.text = "Algo ha fallado obteniendo los datos del juego.";
				console.error(error);
			});
		}

	}

}