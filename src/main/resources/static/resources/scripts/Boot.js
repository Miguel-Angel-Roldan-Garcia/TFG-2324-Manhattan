export default class Boot extends Phaser.Scene {
	constructor() {
		super('Boot');
	}

	preload() {
		this.load.setPath("/resources/fonts/");
		this.load.bitmapFont("Arial", "Arial.png", "Arial.xml");
		this.load.bitmapFont("ArialBlack", "ArialBlack.png", "ArialBlack.xml");
	}

	create() {
		this.scene.start('Preload');
	}

}