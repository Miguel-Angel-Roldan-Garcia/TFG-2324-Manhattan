import Block from "./Block.js";

export default class Player extends Phaser.GameObjects.Container {
	constructor(scene, player, blocks, cards, renderPosition, graphics) {
		// Calculate coordinates based on render position
		let x, y, width, height;
		switch(renderPosition) {
			case 1:
				width = vw * 0.6;
				height = 200;
				x = vw * 0.2;
				y = vh - height;
				break;
			case 2:
				width = vw * 0.2;
				height = vh * 0.4;
				x = 0;
				y = vh * 0.1;
				break;
			case 3:
				width = vw * 0.4;
				height = 200;
				x = vw * 0.3;
				y = 0;
				break;
			case 4:
				width = vw * 0.2;
				height = vh * 0.4;
				x = vw - width;
				y = vh * 0.1;
				break;
		}
		
		super(scene, x, y);
		this.width = width;
		this.height = height;
		
		this.x -= width/2;
		this.y -= height/2;
		
		this.marginX = Math.min(width * 0.1, 20);
		this.marginY = Math.min(height * 0.1, 20);
				
		this.username = player.username;
		this.hasSelectedCard = false;
		this.hasSelectedBlocks = player.hasSelectedBlocks;
		this.playing = player.playing;
		this.isAIControlled = player.aicontrolled;
		this.position = player.position;
		this.renderPosition = renderPosition;
		this.score = player.score;
		this.removedCardPosition = {x: 0, y:0};
		this.cards = [];
		
		if(typeof player.color == "string") {
			this.color = parseInt(player.color.split("#")[1], 16);
		} else {
			this.color = player.color;
		}
		
		graphics.fillStyle(0x303030, 0.75);
		graphics.fillRect(x, y, width, height);
		graphics.lineStyle(2, 0xffffff);
		graphics.strokeRect(x, y, width, height);
		
		/*
		this.playerArea = this.scene.add.rectangle(x, y, width, height);
		this.playerArea.setOrigin(0);
		this.playerArea.setStrokeStyle(2, 0xffffff);
		*/
		
		const avatarSize = Math.min(height - 2 * this.marginY, 100);
		this.avatar = new Phaser.GameObjects.Image(scene, this.marginX, this.marginY, "avatarPlaceholder");
		this.avatar.setOrigin(0);
		this.avatar.width = avatarSize;
		this.avatar.height = avatarSize;
		this.add(this.avatar);
		
		this.usernameText = new Phaser.GameObjects.BitmapText(scene, 0, 0, 'Arial', this.username, 24);
		this.usernameText.x = avatarSize + 2 * this.marginX;
		this.usernameText.y = this.marginY;
		this.usernameText.setTint(this.color);
		this.add(this.usernameText);
		
		this.scoreText = new Phaser.GameObjects.BitmapText(scene, 0, 0, 'Arial', "Score: " + this.score, 24);
		this.scoreText.x = avatarSize + 2 * this.marginX;
		this.scoreText.y = this.usernameText.height + 2 * this.marginY;
		this.add(this.scoreText);
		
		this.blocks = blocks;
		this.infoBlocks = {};
		let endOfBlockInfo;
		
		let infoBlock4;
		let infoBlockScale;
		if(renderPosition == 1 || renderPosition == 3) { 
			infoBlock4 = new Block(scene, {"size": 4}, this.color);
			infoBlock4.setVisible(true);
			infoBlockScale = (this.height - 3 * this.marginY) / (3 * infoBlock4.height);
			infoBlock4.scale = infoBlockScale;
			infoBlock4.x = this.usernameText.x + Math.max(this.usernameText.width, 75) + 2*this.marginX;
			infoBlock4.y = this.marginY;
			infoBlock4.setOrigin(0);
			this.infoBlocks[4] = infoBlock4;
			this.add(infoBlock4);
			
			this.infoBlock4Text = this.generateInfoBlockText(infoBlock4);
		} else {
			infoBlock4 = new Block(scene, {"size": 4}, this.color);
			infoBlock4.setVisible(true);
			infoBlockScale = (this.height - 3.5 * this.marginY - this.avatar.displayHeight)  / (3 * infoBlock4.height);
			infoBlock4.scale = infoBlockScale;
			infoBlock4.x = this.marginX;
			infoBlock4.y = this.avatar.y + this.avatar.displayHeight + 2*this.marginY;
			infoBlock4.setOrigin(0);
			this.infoBlocks[4] = infoBlock4;
			this.add(infoBlock4);
			
			this.infoBlock4Text = this.generateInfoBlockText(infoBlock4);
		}
		
		let infoBlock1 = new Block(scene, {"size": 1}, this.color);
		infoBlock1.setVisible(true);
		infoBlock1.scale = infoBlockScale;
		infoBlock1.x = infoBlock4.x;
		infoBlock1.y = this.infoBlock4Text.y + this.infoBlock4Text.height + this.marginY/2 + infoBlock1.displayHeight / (infoBlock1.size + 1);
		infoBlock1.setOrigin(0);
		this.infoBlocks[1] = infoBlock1;
		this.add(infoBlock1);
		
		this.infoBlock1Text = this.generateInfoBlockText(infoBlock1);
		
		let infoBlock3 = new Block(scene, {"size": 3}, this.color);
		infoBlock3.setVisible(true);
		infoBlock3.scale = infoBlockScale;
		infoBlock3.x = infoBlock4.x + infoBlock4.displayWidth + this.marginX;
		infoBlock3.y = infoBlock4.y + infoBlock3.displayHeight / (infoBlock3.size + 1);
		infoBlock3.setOrigin(0);
		this.infoBlocks[3] = infoBlock3;
		this.add(infoBlock3);
		
		this.infoBlock3Text = this.generateInfoBlockText(infoBlock3);
		
		let infoBlock2 = new Block(scene, {"size": 2}, this.color);
		infoBlock2.setVisible(true);
		infoBlock2.scale = infoBlockScale;
		infoBlock2.x = infoBlock3.x;
		infoBlock2.y = infoBlock1.y - infoBlock1.displayHeight / (infoBlock1.size + 1);
		infoBlock2.setOrigin(0);
		this.infoBlocks[2] = infoBlock2;
		this.add(infoBlock2);
		
		this.infoBlock2Text = this.generateInfoBlockText(infoBlock2);
		
		this.endOfBlockInfo = infoBlock3.x + infoBlock3.displayWidth + this.marginX;
		this.countSelectedBlocks();
		
//		this.cards = cards;
		// Using this as temporary pointer to place initial cards
		this.removedCardPosition = this.endOfBlockInfo;
		
		for(let card of cards) {
			this.addCard(card);
			this.removedCardPosition += card.displayWidth + this.marginX/2;
		}
		
		if(this.username == userUsername) {
			const toggleSelectBlocksX = this.removedCardPosition + this.marginX/2;
			const toggleSelectBlocksWidth = width - toggleSelectBlocksX - this.marginX/2;
			const toggleSelectBlocksHeight = height - 2*this.marginY;
			const toggleSelectBlocksY = this.marginY;
			
			let toggleSelectBlocks = scene.add.image(
							toggleSelectBlocksX,
							toggleSelectBlocksY,
							"sector"
						 );
			toggleSelectBlocks.setOrigin(0);
			toggleSelectBlocks.displayWidth = toggleSelectBlocksWidth;
			toggleSelectBlocks.displayHeight = toggleSelectBlocksHeight;
			toggleSelectBlocks.setInteractive();
		
			let toggleSelectBlocksText = scene.add.bitmapText(0, 0, 'ArialBlack', "Toggle select panel", 16);
			toggleSelectBlocksText.x = toggleSelectBlocks.x + toggleSelectBlocks.displayWidth/2;
			toggleSelectBlocksText.y = toggleSelectBlocks.y + toggleSelectBlocks.displayHeight/2;
			toggleSelectBlocksText.maxWidth = toggleSelectBlocks.displayWidth - 2*this.marginX;
			toggleSelectBlocksText.setOrigin(0.5);
			toggleSelectBlocksText.align = 1;
			
			this.toggleSelectBlocks = toggleSelectBlocks;
			this.toggleSelectBlocksText = toggleSelectBlocksText;
			
			this.add(this.toggleSelectBlocks);
			this.add(this.toggleSelectBlocksText);
		}
		
		this.x = x;
		this.y = y;
		
		scene.add.existing(this);
	}
	
	generateInfoBlockText(infoBlock) {
		let infoBlockText = new Phaser.GameObjects.BitmapText(this.scene, 0, 0, 'Arial', "1", 24);
		infoBlockText.x = infoBlock.x + infoBlock.displayWidth/2;
		infoBlockText.y = infoBlock.y + infoBlock.displayHeight + this.marginY/2;
		infoBlockText.setOrigin(0.5, 0);
		this.add(infoBlockText);
		return infoBlockText;
	}
	
	countSelectedBlocks(updateText) {
		let count = {1:0, 2:0, 3:0, 4:0}
		for(let i in this.blocks) {
			if(this.blocks[i].selected && !this.blocks[i].placed) {
				count[this.blocks[i].size]++;
			}
		}
		
		if(updateText === undefined || updateText) {
			this.infoBlock1Text.text = count[1];
			this.infoBlock2Text.text = count[2];
			this.infoBlock3Text.text = count[3];
			this.infoBlock4Text.text = count[4];
		}
		
		return count;
	}
	
	countUnselectedBlocks() {
		let count = {1:0, 2:0, 3:0, 4:0}
		for(let i in this.blocks) {
			if(!(this.blocks[i].selected || this.blocks[i].placed)) {
				count[this.blocks[i].size]++;
			}
		}
		return count;
	}
	
	getUnselectedBlocks(toSelect) {
		let selectedBlocksIndexes = [];
		for(let i in this.blocks) {
			const block = this.blocks[i];
			if(!(block.selected || block.placed) && toSelect[block.size] > 0) {
				toSelect[block.size]--;
				selectedBlocksIndexes.push(block.index);
			}
		}
		return selectedBlocksIndexes;
	}
	
	selectBlocks(blockIndexes) {
		for(let i in this.blocks) {
			const block = this.blocks[i];
			if(blockIndexes.includes(block.index)) {
				block.selected = true;
			}
		}
		this.hasSelectedBlocks = true;
		this.countSelectedBlocks();
	}
	
	placeBlock(blockIndex, sectorIndex) {
		for(let i in this.blocks) {
			const block = this.blocks[i];
			if(block.index == blockIndex) {
				block.placed = true;
				block.selected = false;
				block.sector = sectorIndex;
				this.countSelectedBlocks();
				return block;
			}
		}
	}
	
	removeCard(card) {
		card.player = null;
		card.setVisible(false);
		card.setActive(false);
		card.angle = 0;
		this.removedCardPosition = card.x;
		this.cards = this.cards.filter(c => c !== card);
		this.remove(card);
	}
	
	addCard(card) {
		card.player = this.username;
		
		if(this.username == userUsername) {
			card.x = this.removedCardPosition;
			card.setY(this.marginY);
			card.scale = (this.height - 2 * this.marginY) / card.height;
			card.setOrigin(0);
			card.setVisible(true);
			card.setActive(true);
		}
		
		this.cards.push(card);
		this.add(card);
	}
	
	useCardByIndex(cardIndex) {
		for(let c of this.cards) {
			if(c.index == cardIndex) {
				this.removeCard(c);
				break;
			}
		}
	}
	
	selectInfoBlock(graphics, infoBlock) {
		graphics.clear();
		graphics.lineStyle(2, 0xff4040);
		graphics.strokeRect(this.x + infoBlock.x,
							this.y + infoBlock.y,
							infoBlock.displayWidth,
							infoBlock.displayHeight);
	}
	
	getSelectedBlockOfSize(size) {
		for(let i in this.blocks) {
			if(this.blocks[i].selected && this.blocks[i].size == size) {
				return this.blocks[i].index;
			}
		}
	}
	
	addScore(score) {
		this.score += score;
		this.scoreText.text = "Score: " + this.score; 
	}
	
	async showCard(cardIndex) {
		if(this.renderPosition == 1) {
			return;
		}
		
		let card;
		for(let c of this.cards) {
			if(c.index == cardIndex) {
				card = c;
				break;
			}
		}
		
		if(this.renderPosition == 2) {
			card.scale = (this.width/2 - 2 * this.marginX) / card.height;
			card.x = this.width - card.displayHeight/2 - this.marginX;
			card.setY(this.height - card.displayWidth - this.marginY);
			card.angle = 90;
		} else if(this.renderPosition == 3) {
			card.scale = (this.height - 2 * this.marginY) / card.height;
			card.x = this.endOfBlockInfo + card.displayWidth/2 + this.marginX;
			card.setY(this.height/2);
			card.angle = 180;
		} else if(this.renderPosition == 4) {
			card.scale = (this.width/2 - 2 * this.marginX) / card.height;
			card.x = this.width - card.displayHeight/2 - this.marginX;
			card.setY(this.height - card.displayWidth - this.marginY);
			card.angle = -90;
		}
		
		card.setOrigin(0.5);
		card.setVisible(true);
		card.setActive(true);
		//this.add(card);
		
		return new Promise((resolve) => {
			setTimeout(resolve, 5000);
		});
	}
	
	async waitIfNotRenderPosition1() {
		if(this.renderPosition == 1) {
			return;
		}
		
		return new Promise((resolve) => {
			setTimeout(resolve, 5000);
		});
	}
	
}