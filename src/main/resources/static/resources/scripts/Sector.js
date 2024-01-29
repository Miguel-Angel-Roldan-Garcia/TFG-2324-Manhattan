export default class Sector extends Phaser.GameObjects.Image {
	constructor(scene, x, y, width, height, index, city) {
		const textureUnnocupied = "sector";
		super(scene, x, y, textureUnnocupied);
		
		this.displayWidth = width;
		this.displayHeight = height;
		
		this.textureUnnocupied = textureUnnocupied;
		this.textureBlock = "blockUp";
		
		this.index = index;
		this.blocks = [];
		
		this.graphics = scene.add.graphics();
		this.shapeRect = new Phaser.Geom.Rectangle(city.x + this.x,
	    										   city.y + this.y, 
	    										   this.displayWidth, 
	    										   this.displayHeight);
	}
	
	setColor(color) {
		if(typeof color == "string") {
			color = parseInt(color.split("#")[1], 16);
		}
		
		this.setTintFill(color);
	}
	
	getMaxOrder() {
		return this.blocks[this.blocks.length-1].order;
	}
	
	placeBlock(block, highlight) {
		block.depth = block.order;
		this.blocks.push(block);
		this.sortBlocks();
		
		this.setColor(this.blocks[this.blocks.length-1].color);
		
		if(highlight == null || highlight) {
			this.graphics.lineStyle(2, 0xffffff);
			this.graphics.strokeRectShape(this.shapeRect);
			
			setTimeout(() => {
				this.graphics.clear();
			}, 4000);
		}
	}
	
	sortBlocks() {
		this.blocks.sort((a, b) => {
			if(a.order < b.order) {
				return -1;
			} else if(a.order > b.order) {
				return 1;
			} else {
				return 0;
			}
		});
	}
	
	getOwner() {
		if(this.blocks.length > 0) {
			return this.blocks[this.blocks.length-1].player;
		}
	}
	
	getTallestBlock() {
		if(this.blocks.length > 0) {
			return this.blocks[this.blocks.length-1];
		}
	}
	
	getPlayerStories(username) {
		let stories = 0;
		
		for(let block of this.blocks) {
			if(block.player == username) {
				stories += block.size;
			}
		}
		
		return stories;
	}
	
	getTotalHeight() {
		let stories = 0;
		
		for(let block of this.blocks) {
			stories += block.size;
		}
		
		return stories;
	}
}