import Boot from "./Boot.js";
import Preload from "./Preload.js";
import Game from "./Game.js";

const config = {
	parent: 'main-container',
	type: Phaser.AUTO,
	width: vw,
	height: vh,
	scene: [Boot, Preload, Game],
	scale: {
		parent: 'main-container',

		mode: Phaser.Scale.FIT,
		autoCenter: Phaser.Scale.CENTER_BOTH
	},
	dom: {
		createContainer: true
	},
	fullscreenTarget: 'main-container'
};

let game = new Phaser.Game(config);