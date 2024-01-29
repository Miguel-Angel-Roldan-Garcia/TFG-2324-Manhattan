export default class Card extends Phaser.GameObjects.Image {
	constructor(scene, card) {
		const texture = "card" + card.sectorIndex;
		super(scene, 0, 0, texture);
		
		this.index = card.index_;
		this.player = card.player;
		this.sector = card.sectorIndex;
		this.used = card.used;
		this.selected = false;
		
		this.setOrigin(0);
		this.setActive(false);	
		
		if(this.player != userUsername) {
			this.setVisible(false);
		}	
		
		this.moving = false;
		this.stopMovingFlag = false;
		this.graphics = scene.add.graphics();
	}
	
	setY(y) {
		this.y = y;
		this.originalY = y;
		this.animatedFinalY = this.y - 20;
	}
	
	async animateSelectionUp() {
		if(this.moving) {
			await this.stopMoving("up");
		}
		this.moving = true;
		
		this.clearAndDrawShade();
							   
		while(!this.stopMovingFlag && this.y > this.animatedFinalY) {
			//console.log("up is moving");
			await new Promise((resolve) => setTimeout(() => {
				this.y -= 2;
				resolve();
				}, 50));
		}
		
		this.stopMovingFlag = false;
		this.moving = false;
	}
	
	async animateSelectionDown() {
		if(this.selected) {
			return;
		}
		if(this.moving) {
			await this.stopMoving("down");
		}
		this.moving = true;
		
		while(!this.stopMovingFlag && this.y <= this.originalY) {
			//console.log("down is moving");
			await new Promise((resolve) => setTimeout(() => {
				this.y += 2;
				resolve();
				}, 25));
		}
		
		if(!this.stopMovingFlag && this.y != this.originalY) {
			this.y = this.originalY;
		}
		
		this.graphics.clear();
		this.stopMovingFlag = false;
		this.moving = false;
	}
	
	async stopMoving(caller) {
		if(this.moving) {
			//console.log(caller + " called stop moving");
			this.stopMovingFlag = true;
			
			return new Promise((resolve) => {
		    	const checkLooping = () => {
					//console.log("checking moving flag");
					if (!this.moving) {
						//console.log("solving moving flag");
		        		clearInterval(intervalId);
		        		resolve();
		        	}
		    	};
		
		    	const intervalId = setInterval(checkLooping, 200); 
		  	});	
		}
	}
	
	clearAndDrawShade() {
		this.graphics.clear();
		this.graphics.fillStyle(0x707070, 0.6);
		this.graphics.fillRect(this.x + this.parentContainer.x, 
							   this.originalY + this.parentContainer.y, 
							   this.displayWidth, 
							   this.displayHeight);
	}
	
	select() {
		this.selected = true;
		this.clearAndDrawShade();
		this.animateSelectionUp()
			.finally(() => {
				this.graphics.lineStyle(4, 0xff4040);
				this.graphics.strokeRect(this.x + this.parentContainer.x, 
									     this.animatedFinalY + this.parentContainer.y, 
									     this.displayWidth, 
									     this.displayHeight);
			});
		return this;
	}
	
	unselect() {
		this.selected = false;
		this.clearAndDrawShade();
		this.animateSelectionDown();
	}
	
}