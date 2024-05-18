import GameChat from "./chat.js";
import City from "./City.js";
import Player from "./Player.js";
import Block from "./Block.js";
import Card from "./Card.js";
import generateTurnMessage from "./AI.js";
import convertIndexToLocal from "./SectorPositionGlobalToLocalConverter.js"; 

export default class Game extends Phaser.Scene {
	constructor() {
		super('Game');
		
		// Game attributes
		this.roundPlaying;
		this.roundNumber;
		this.turnNumber;
		
		this.chat;
		this.stompClient;
		this.roundNumberText;
		this.turnNumberText;
		
		this.gameStates = {
			"SELECTINGBLOCKS": 1,
			"PLAYINGCARD": 2,
			"PLAYINGBLOCK": 3,
			"WAITING": 4
		};
		this.gameState = this.gameStates.SELECTINGBLOCKS;
		this.receivedTurns = [];
		this.processingTurn = false;
		
		// Card data
		this.cards = {};
		
		// Player data
		this.players = {};
		
		// Board data
		this.cities = {};
		
		// Sector zooming
		this.zoomOn = {city: null, row: null, blocks: []};
		this.blockMask;
		
		// Event bus
		this.gameEventEmitter;
		
		// Misc
		this.tempSelectBlocksObjects;
		this.isSelectBlocksVisible;
		this.infoBlocksGraphics;
		this.cityPointerOverTurnFunction;
		this.cityPointerOutTurnFunction;
		this.soundAttributes = {
			"Volumen": 1,
			"Silenciar": false
		}
	}

	preload() {

	}

	create(data) {
		this.background = this.add.image(0, 0, "background");
		this.background.setOrigin(0);
		const backgroundXScale = vw / this.background.width;
		const backgroundYScale = vh / this.background.height;
		this.background.setScale(Math.max(backgroundXScale, backgroundYScale));
		this.background.setDepth(-2);
		
		// Loading data objects
		this.stompClient = data.stompClient;
		
		data = data.gameData;
		console.log(data);
		
		// Graphics objects
		this.graphics = this.add.graphics();
		this.textureGraphics = this.add.graphics();
		
		// Creation of zoom block mask
		this.graphics.fillStyle(0x303030, 0.75);
		this.blockMask = this.graphics.fillRectShape(new Phaser.Geom.Rectangle(0, vh*0.6, vw*0.2, vh*0.4));
		
		// Creation of game chat
		const chatWidth = vw * 0.2;
		const chatHeight = vh * 0.3;
		const chatX = vw - chatWidth;
		const chatY = vh - chatHeight;
		
		this.chat = new GameChat(this, chatX, chatY, chatWidth, chatHeight, this.stompClient, this.graphics, gameId);
		this.chat.addMessage("Bienvenido al chat!");
		
		// Render game info
		this.roundPlaying = data.game.roundPlaying;
		this.roundNumber = data.game.roundNumber;
		this.turnNumber = data.game.turnNumber;
		
		this.roundNumberText = this.add.bitmapText(0, 0, 'Arial', "Ronda: " + this.roundNumber, 24);
		this.roundNumberText.x = this.chat.x + this.chat.marginX;
		this.roundNumberText.y = this.chat.y - this.roundNumberText.height - this.chat.marginY;
		
		this.turnroundNumberText = this.add.bitmapText(0, 0, 'Arial', "Turno: " + Math.floor((this.turnNumber-1)/4 + 1), 24);
		this.turnroundNumberText.x = this.roundNumberText.x + this.roundNumberText.width + this.chat.marginX;
		this.turnroundNumberText.y = this.chat.y - this.roundNumberText.height - this.chat.marginY;
		
		this.textureGraphics.fillStyle(0x303030, 0.75);
		this.textureGraphics.lineStyle(2, 0xffffff);
		this.textureGraphics.fillRect(chatX, this.roundNumberText.y - this.chat.marginY, chatWidth, vh - this.roundNumberText.height - this.chat.marginY);
		this.textureGraphics.strokeRect(chatX, this.roundNumberText.y - this.chat.marginY, chatWidth, this.roundNumberText.height + 2*this.chat.marginY);
		this.textureGraphics.strokeRect(chatX, chatY, chatWidth, chatHeight);
		
		// Load cards
		for(let card of data.cards) {
			this.cards[card.index_] = new Card(this, card);
		}
		
		// Render player info
		let userIndex;
		let playerPointer;
		let userPosition;
		let renderPosition = 1;
		for(playerPointer in data.players) {
			let player = data.players[playerPointer];
			if(player.username == userUsername) {
				userIndex = playerPointer;
				userPosition = player.position;
				this.players[player.username] = new Player(this, player, this.getPlayerBlocksLoaded(data.blocks, player, userPosition), this.getPlayerCards(player.username), renderPosition++, this.textureGraphics);
				playerPointer = (parseInt(playerPointer) + 1) % 4;
				break;
			}
		}
		
		while(playerPointer != userIndex) {
			let player = data.players[playerPointer];
			this.players[player.username] = new Player(this, player, this.getPlayerBlocksLoaded(data.blocks, player, userPosition), this.getPlayerCards(player.username), renderPosition++, this.textureGraphics);
			playerPointer = (playerPointer + 1) % 4;
		}
		
		// Render board
		for(let i = 1; i <= 6; i++) {
			const city = new City(this, i, this.textureGraphics);
			
			let cityPointerOverZoomFunction = () => {
				// console.log("moved over city: " + city.index);
				if(!city.isPointerMoveSet) {
					city.on("pointermove", (pointer, localX, localY) => {
						const cityHeight = city.height;
						if(localY <= cityHeight / 3) {
							//console.log("moved over city: " + city.index + "; at y: " + localY + "; part 1");
							this.zoomSectors(city.index, 1);
						} else if(localY <= 2 * cityHeight / 3) {
							//console.log("moved over city: " + city.index + "; at y: " + localY + "; part 2");
							this.zoomSectors(city.index, 2);
						} else {
							//console.log("moved over city: " + city.index + "; at y: " + localY + "; part 3");
							this.zoomSectors(city.index, 3);
						}
						city.isPointerMoveSet = true;
					});
					
					this.input.on("wheel", (pointer, gameObjects, deltaX, deltaY, deltaZ) => {
		            	this.scrollZoomedSectors(deltaY);
			        });
				}
			}
			
			city.on("pointerover", cityPointerOverZoomFunction);
			
			let cityPointerOutZoomFunction = () => {
				//console.log("moved out of city: " + city.index);
				city.isPointerMoveSet = false;
				city.off("pointermove");
				this.input.off("wheel");
				this.hideSectorsZoom();
			}
			
			city.on("pointerout", cityPointerOutZoomFunction);
			
			this.cities[i] = city;
		}
		
		// Load blocks from players
		for(let i in this.players) {
			let player = this.players[i];
			for(let j in player.blocks) {
				let block = player.blocks[j];
				if(block.placed) {
					this.cities[block.city].sectors[block.sector].placeBlock(block, false);
				}
			}
		}
		
		
		// Sector zooming
		this.textureGraphics.fillStyle(0x303030, 0.75);
		this.textureGraphics.fillRect(0, vh*0.6, vw*0.2, vh*0.4);
		
		// Converting to texture to save resources
		this.textureGraphics.generateTexture("containerBackgrounds", vw, vh);
		this.containerBackgrounds = this.add.image(0, 0, "containerBackgrounds");
		this.containerBackgrounds.setOrigin(0);
		this.containerBackgrounds.setDepth(-1);
		
		this.textureGraphics.clear().destroy();
		
		// Setup event signals and messaging
		//this.gameEventEmitter = new Phaser.Events.EventEmitter();
		
		this.stompClient.subscribe('/game/'+ gameId + '/select-blocks', (selectBlocksMsg) => {
	        this.selectBlocksEventHandler(JSON.parse(selectBlocksMsg.body));
	    });
	    
	    this.stompClient.subscribe('/game/'+ gameId + '/play-turn', (turnMsg) => {
			this.receivedTurns.push(JSON.parse(turnMsg.body));
	        this.processTurn();
	    });
		
		// Determine state of the game
		if(!this.roundPlaying && !this.players[userUsername].hasSelectedBlocks) {
			this.gameState = this.gameStates.SELECTINGBLOCKS;
			this.selectBlocks();
		} else if(this.roundPlaying) {
			this.startTurn();
			//this.gameEventEmitter.emit("startTurn");
		} else {
			this.gameState = this.gameStates.WAITING;
		}
		
		this.loadAudio();
	}
	
	update(time, delta) {
		super.update(time, delta);
		
		this.sound.volume = this.soundAttributes.Volumen;
		this.sound.mute = this.soundAttributes.Silenciar;
	}
	
	loadAudio() {
		this.gui = new dat.GUI();
		const s = this.gui.addFolder("Musica");
		s.add(this.soundAttributes, 'Volumen', 0, 2).step(0.05).listen();
        s.add(this.soundAttributes, 'Silenciar').listen();
        s.open();
        
		const loopMarker = {
            name: 'loop',
            start: 0,
            config: {
                loop: true
            }
        };
        
        let music = this.sound.add("soundtrack");
        music.addMarker(loopMarker);
        music.play("loop", {
			delay: 0
		});
		
	}
	
	updateGameInfo() {
		this.roundNumberText.text = "Ronda: " + this.roundNumber;
		this.turnroundNumberText.text = "Turno: " + Math.floor((this.turnNumber-1)/4 + 1);
	}

	getPlayerBlocksLoaded(dataBlocks, player, userPosition) {
		let blocks = {};
		
		for(let i in dataBlocks) {
			if(dataBlocks[i].player == player.username) {
				blocks[dataBlocks[i].index_] = new Block(this, dataBlocks[i], player.color, userPosition, this.blockMask);
				this.add.existing(blocks[dataBlocks[i].index_]);
			}
		}
		
		return blocks;
		
	}
	
	getPlayerCards(username) {
		let playerCards = [];
		
		for(let i in this.cards) {
			if(this.cards[i].player == username) {
				playerCards.push(this.cards[i]);
			}
		}
		
		return playerCards;
	}
	
	hideSectorsZoom() {
		if(this.zoomOn.city) {
			const oldSectors = this.cities[this.zoomOn.city].sectors;
			for(let i in oldSectors) {
				if(oldSectors[i].blocks) {
					for(let j in oldSectors[i].blocks) {
						oldSectors[i].blocks[j].setVisible(false);
						oldSectors[i].blocks[j].setActive(false);
					}
				}
			}
			this.zoomOn = {"city": null, "row": null};
		}
	}
	
	zoomSectors(city, row) {
		if(!(this.zoomOn.city == city && this.zoomOn.row == row)) {
			// First we have to clean the previously displayed sectors
			if(this.zoomOn.city) {
				const oldSectors = this.cities[this.zoomOn.city].sectors;
				for(let i in oldSectors) {
					if(oldSectors[i].blocks) {
						for(let j in oldSectors[i].blocks) {
							oldSectors[i].blocks[j].setVisible(false);
							oldSectors[i].blocks[j].setActive(false);
						}
					}
				}
			}
			
			// We replace the zoomed sectors tracker to the new ones
			this.zoomOn = {"city": city, "row": row, blocks: []};
			
			// And start displaying them
			const sectors = this.cities[city].getRowSectors(row);
			
			const width = vw*0.2/4;
			const marginX = (width)/4;
			const marginY = Math.min((1/10) * vh*0.4, 30);
			for(let i in sectors) {
				i = parseInt(i);
				let pointerY = vh - marginY;
				const x = (i+1)*marginX + i*width;
				if(sectors[i].blocks && sectors[i].blocks.length > 0) {
					for(let j in sectors[i].blocks) {
						let block = sectors[i].blocks[j];
						block.setActive(true);
						block.setVisible(true);
						
						block.displayWidth = width;
						block.scaleY = block.scaleX;
						
						block.x = x;
						block.y = pointerY;
						block.setOrigin(0, 1);
						
						pointerY -= block.displayHeight - block.displayHeight / (block.size + 1) + 4;
						this.zoomOn.blocks.push(block);
					}
				}
			}
		}
	}
	
	scrollZoomedSectors(ammount) {
		if(this.zoomOn.blocks.length > 0) {
			if(ammount > 20) {
				ammount = 20;
			} else if(ammount < -20) {
				ammount = -20;
			}
			
			for(let block of this.zoomOn.blocks) {
				block.y += ammount;
			}
		}
	}
	
	selectBlocks() {
		if(this.roundPlaying || this.players[userUsername].hasSelectedBlocks) {
			return;
		}
		
		const width = vw*0.4;
		const height = vh*0.7;
		const x = vw*0.3;
		const y = vh*0.15;
		const marginX = width*0.1;
		const marginY = height*0.1;
		
		const graphics = this.add.graphics();
		
		const player = this.players[userUsername];
		const color = player.color;
		const unselectedBlocks = player.countUnselectedBlocks();
		
		const inputTextConfig = {
			type: 'number',
			placeholder: "(Amount)",
			fontFamily: "Arial",
			fontSize: '30px',
			color: '#FFFFFF'
		}
		
		let tempObjects = [graphics];
		
		graphics.fillStyle(0x808080, 1);
		graphics.fillRect(x, y, width, height);
		graphics.lineStyle(2, 0x909090);
		graphics.strokeRect(x, y, width, height);
		
		let selectBlocksTitle = this.add.bitmapText(0, y + marginY, 'ArialBlack', "Selecciona 6 bloques para esta ronda:", 36);
		selectBlocksTitle.setMaxWidth(width - 2*marginX);
		selectBlocksTitle.x = x + marginX;
		selectBlocksTitle.align = 1;
		tempObjects.push(selectBlocksTitle);
		
		// Select blocks of size 1
		
		let infoBlock1 = new Block(this, {"size": 1}, color);
		infoBlock1.setVisible(true);
		infoBlock1.x = x + width*0.1;
		infoBlock1.y = y + selectBlocksTitle.height + 1.5*marginY;
		infoBlock1.setOrigin(0);
		this.add.existing(infoBlock1);
		tempObjects.push(infoBlock1);
		
		let infoBlock1Text = this.add.bitmapText(0, 0, 'ArialBlack', "Disponibles: " + unselectedBlocks[1] + " -> Seleccionar: ", 28);
		infoBlock1Text.x = infoBlock1.x + infoBlock1.width + 0.5*marginX;
		infoBlock1Text.y = infoBlock1.y + infoBlock1.displayHeight/2;
		infoBlock1Text.setOrigin(0);
		tempObjects.push(infoBlock1Text);
		
		let inputText1 = this.add.rexInputText(
							infoBlock1Text.x + infoBlock1Text.width,
							infoBlock1Text.y,  
						  	width - infoBlock1.width - infoBlock1Text.width - 2*marginX, 
							infoBlock1Text.height, 
							inputTextConfig
						);
		inputText1.setOrigin(0);
		tempObjects.push(inputText1);
			
		// Select blocks of size 2
		let infoBlock2 = new Block(this, {"size": 2}, color);
		infoBlock2.setVisible(true);
		infoBlock2.x = x + width*0.1;
		infoBlock2.y = infoBlock1.y + infoBlock1.height + 0.25*marginY;
		infoBlock2.setOrigin(0);
		this.add.existing(infoBlock2);
		tempObjects.push(infoBlock2);
		
		let infoBlock2Text = this.add.bitmapText(0, 0, 'ArialBlack', "Disponibles: " + unselectedBlocks[2] + " -> Seleccionar: ", 28);
		infoBlock2Text.x = infoBlock2.x + infoBlock2.width + 0.5*marginX;
		infoBlock2Text.y = infoBlock2.y + infoBlock2.displayHeight/2;
		infoBlock2Text.setOrigin(0);
		tempObjects.push(infoBlock2Text);
		
		let inputText2 = this.add.rexInputText(
							infoBlock2Text.x + infoBlock2Text.width,
							infoBlock2Text.y,  
						  	width - infoBlock2.width - infoBlock2Text.width - 2*marginX, 
							infoBlock2Text.height, 
							inputTextConfig
						);
		inputText2.setOrigin(0);
		tempObjects.push(inputText2);	
			
		// Select blocks of size 3
		let infoBlock3 = new Block(this, {"size": 3}, color);
		infoBlock3.setVisible(true);
		infoBlock3.x = x + width*0.1;
		infoBlock3.y = infoBlock2.y + infoBlock2.height + 0.25*marginY;
		infoBlock3.setOrigin(0);
		this.add.existing(infoBlock3);
		tempObjects.push(infoBlock3);
		
		let infoBlock3Text = this.add.bitmapText(0, 0, 'ArialBlack', "Disponibles: " + unselectedBlocks[3] + " -> Seleccionar: ", 28);
		infoBlock3Text.x = infoBlock3.x + infoBlock3.width + 0.5*marginX;
		infoBlock3Text.y = infoBlock3.y + infoBlock3.displayHeight/2;
		infoBlock3Text.setOrigin(0);
		tempObjects.push(infoBlock3Text);
		
		let inputText3 = this.add.rexInputText(
							infoBlock3Text.x + infoBlock3Text.width,
							infoBlock3Text.y,  
						  	width - infoBlock3.width - infoBlock3Text.width - 2*marginX, 
							infoBlock3Text.height, 
							inputTextConfig
						);
		inputText3.setOrigin(0);
		tempObjects.push(inputText3);
			
		// Select blocks of size 4
		let infoBlock4 = new Block(this, {"size": 4}, color);
		infoBlock4.setVisible(true);
		infoBlock4.x = x + width*0.1;
		infoBlock4.y = infoBlock3.y + infoBlock3.height + 0.25*marginY;
		infoBlock4.setOrigin(0);
		this.add.existing(infoBlock4);
		tempObjects.push(infoBlock4);
		
		let infoBlock4Text = this.add.bitmapText(0, 0, 'ArialBlack', "Disponibles: " + unselectedBlocks[4] + " -> Seleccionar: ", 28);
		infoBlock4Text.x = infoBlock4.x + infoBlock4.width + 0.5*marginX;
		infoBlock4Text.y = infoBlock4.y + infoBlock4.displayHeight/2;
		infoBlock4Text.setOrigin(0);
		tempObjects.push(infoBlock4Text);
		
		let inputText4 = this.add.rexInputText(
							infoBlock4Text.x + infoBlock4Text.width,
							infoBlock4Text.y,  
						  	width - infoBlock4.width - infoBlock4Text.width - 2*marginX, 
							infoBlock4Text.height, 
							inputTextConfig
						);
		inputText4.setOrigin(0);
		tempObjects.push(inputText4);
		
		// Send button
		let button = this.add.image(
						x,
						infoBlock4.y + infoBlock4.displayHeight + 0.5*marginY,
						"sector"
					 );
		button.setOrigin(0);
		button.displayWidth = width;
		button.setInteractive();
		
		let buttonText = this.add.bitmapText(0, 0, 'ArialBlack', "Click to select", 24);
		buttonText.x = button.x + button.displayWidth/2;
		buttonText.y = button.y + button.displayHeight/2;
		buttonText.maxWidth = button.displayWidth - 2*marginX;
		buttonText.setOrigin(0.5);
		buttonText.align = 1;
		tempObjects.push(buttonText);
		
		button.on("pointerup", () => {
			const size1 = inputText1.text ? parseInt(inputText1.text) : 0;
			const size2 = inputText2.text ? parseInt(inputText2.text) : 0;
			const size3 = inputText3.text ? parseInt(inputText3.text) : 0;
			const size4 = inputText4.text ? parseInt(inputText4.text) : 0;
			
			if(size1 + size2 + size3 + size4 != 6) {
				buttonText.text = "Se deberán seleccionar 6 bloques.";
			} else if(size1 > unselectedBlocks[1]) {
				buttonText.text = "Se seleccionarán " + unselectedBlocks[1] + " bloques de 1 piso como máximo";
			} else if(size2 > unselectedBlocks[2]) {
				buttonText.text = "Se seleccionarán " + unselectedBlocks[2] + " bloques de 2 piso como máximo";
			} else if(size3 > unselectedBlocks[3]) {
				buttonText.text = "Se seleccionarán " + unselectedBlocks[3] + " bloques de 3 piso como máximo";
			} else if(size4 > unselectedBlocks[4]) {
				buttonText.text = "Se seleccionarán " + unselectedBlocks[4] + " bloques de 4 piso como máximo";
			} else {
				let msg = {
					"username": userUsername,
					"selectedBlockIds": this.players[userUsername].getUnselectedBlocks({1: size1, 2: size2, 3: size3, 4: size4})
				};
				
				this.stompClient.send("/game-msgs/" + gameId + "/select-blocks", {}, JSON.stringify(msg));
				this.tempSelectBlocksObjects = tempObjects;
			}
			
		});
		
		player.toggleSelectBlocks.on("pointerup", () => {
			this.toggleSelectBlocksVisibility(tempObjects)
		});
		
		tempObjects.push(button);
		this.isSelectBlocksVisible = true;
	}
	
	toggleSelectBlocksVisibility(tempObjects) {
		if(tempObjects != null) {
			for(let obj of tempObjects) {
				obj.alpha = this.isSelectBlocksVisible ? 0 : 1;
			}
			this.isSelectBlocksVisible = !this.isSelectBlocksVisible;
		}
	}
	
	selectBlocksEventHandler(selectBlocksMsg) {
		if(this.tempSelectBlocksObjects && selectBlocksMsg.username == userUsername) {
			for(let obj of this.tempSelectBlocksObjects) {
				obj.destroy();
			}
			this.tempSelectBlocksObjects = null;
			this.isSelectBlocksVisible = false;
		}
		
		if(!this.players[selectBlocksMsg.username].hasSelectedBlocks) {
			this.players[selectBlocksMsg.username].selectBlocks(selectBlocksMsg.selectedBlockIds);
		}
		
		for(let p in this.players) {
			if(!this.players[p].hasSelectedBlocks) {
				return;
			}
		}
		
		this.roundPlaying = true;
		this.startTurn();
	}
	
	startTurn() {
		let player = this.players[userUsername];
		if(!player.playing) {
			for(let i in this.players) {
				player = this.players[i];
				if(player.playing && player.isAIControlled && isHost) {
					generateTurnMessage(this.turnNumber, this.roundNumber, player, this.players, this.cards, this.cities)
						.then((msg) => this.stompClient.send("/game-msgs/" + gameId + "/play-turn", {}, JSON.stringify(msg)))
						.catch(error => {
							console.error("An error ocurred while calculating AI turn: ");
							console.error(error);
							});
					return;
				}
			}
			
			this.gameState = this.gameStates.WAITING;
			return;
		}
		
		this.gameState = this.gameStates.PLAYINGCARD;
		let cardSelected = null;
		for(let card of player.cards) {
			card.setInteractive();
			card.on("pointerover", () => {
				//console.log("up");
				if(!player.hasSelectedCard) {
					card.animateSelectionUp();
				}
				
				card.on("pointerup", () => {
					if(cardSelected) {
						cardSelected.unselect();
						
						if(cardSelected.index == card.index) {
							player.hasSelectedCard = false;
							cardSelected = null;
							return;
						}
					}
					
					cardSelected = card.select();
					player.hasSelectedCard = true;
				});
			});
			
			card.on("pointerout", () => {
				//console.log("down");
				card.off("pointerup");
				card.animateSelectionDown();
			})
		}
		
		this.gameState = this.gameStates.PLAYINGCARD;
		this.infoBlocksGraphics = this.add.graphics();
		let blockSizeSelected = null;
		let blocksCount = player.countSelectedBlocks(false);
		for(let b in player.infoBlocks) {
			if(blocksCount[b] > 0) {
				let infoBlock = player.infoBlocks[b];
				infoBlock.setInteractive();
				
				infoBlock.on("pointerover", () => {
					infoBlock.on("pointerup", () => {
						player.selectInfoBlock(this.infoBlocksGraphics, infoBlock);
						blockSizeSelected = infoBlock.size;
					});
				});
				
				infoBlock.on("pointerout", () => {
					//console.log("down");
					infoBlock.off("pointerup");
				});
			}
		}
		
		for(let c in this.cities) {
			let city = this.cities[c];
			
			let cityPointerOverTurnFunction = () => {
				city.on("pointerup", () => {
					if(this.isTurnValid(cardSelected, blockSizeSelected, city)) {
						let msg = {
							"username": userUsername,
							"playedCardIndex": cardSelected.index,
							"placedBlockIndex": player.getSelectedBlockOfSize(blockSizeSelected),
							"cityIndex": city.index,
							"sectorIndex": cardSelected.sector
						};
					
						this.stompClient.send("/game-msgs/" + gameId + "/play-turn", {}, JSON.stringify(msg));
						
					}
				});
			};
			this.cityPointerOverTurnFunction = cityPointerOverTurnFunction;
			
			city.on("pointerover", cityPointerOverTurnFunction);
			
			let cityPointerOutTurnFunction = () => {
				city.off("pointerup");
			};
			this.cityPointerOutTurnFunction = cityPointerOutTurnFunction;
			
			city.on("pointerout", cityPointerOutTurnFunction);
		}
	}
	
	isTurnValid(card, blockSizeSelected, city) {
		if(!blockSizeSelected || !card || !card.selected) {
			return false;
		}
		
		const sectorIndex = card.sector;
		const sector = city.sectors[sectorIndex];
		
		if(sector.getOwner() == userUsername) {
			return true;
		}
		
		const resultingStories = sector.getPlayerStories(userUsername) + blockSizeSelected;
		const ownerStories = sector.getPlayerStories(sector.getOwner());
		if(resultingStories < ownerStories) {
			this.chat.addMessage("Sistema: Sus pisos resultantes serían: " + resultingStories + 
					"; y no es superior o igual a los del dueño actual: " + ownerStories);
			return false;
		} else {
			return true;
		}
	}
	
	cleanTurnHandling() {
		let player = this.players[userUsername];
		
		this.infoBlocksGraphics.clear();
		
		for(let card of player.cards) {
			card.clearAndDrawShade();
			card.animateSelectionDown();
			card.off("pointerover");
			card.off("pointerout");
			card.off("pointerup");
			card.disableInteractive();
		}
		
		for(let b in player.infoBlocks) {
			let infoBlock = player.infoBlocks[b];
			infoBlock.off("pointerover");
			infoBlock.off("pointerout");
			infoBlock.off("pointerup");
			infoBlock.disableInteractive();
		}
		
		for(let c in this.cities) {
			let city = this.cities[c];
			// We have to find the functions because js can't compare them at all
			// when there the phaser objects are involved
			city.off("pointerover", city.listeners("pointerover").filter(f => f.name == "cityPointerOverTurnFunction")[0]);
			city.off("pointerout", city.listeners("pointerout").filter(f => f.name == "cityPointerOutTurnFunction")[0]);
			city.off("pointerup");
		}
	}
	
	async processTurn() {
		if(this.processingTurn) {
			return;
		}
		
		this.processingTurn = true;
		let turnMsg;
		if(this.receivedTurns.length > 0) {
			turnMsg = this.receivedTurns.shift();
		} else {
			this.processingTurn = false;
			return;
		}
		console.log(turnMsg);
		
		if(turnMsg.username == userUsername) {
			this.cleanTurnHandling();
		}
		
		if(turnMsg.isInvalid !== undefined) {
			console.error("Received invalid turn.")
			this.processingTurn = false;
			this.startTurn();
			return;
		}
		
		let player = this.players[turnMsg.username];
		await player.showCard(turnMsg.playedCardIndex);
		
		let localSectorIndex = convertIndexToLocal(turnMsg.sectorIndex, this.players[userUsername].position);
		let block = player.placeBlock(turnMsg.placedBlockIndex, localSectorIndex);
		
		let sector = this.cities[turnMsg.cityIndex].sectors[localSectorIndex];
		if(sector.getOwner() != null) {
			block.order = sector.getMaxOrder() + 1;
		}
		sector.placeBlock(block);
		
		await player.waitIfNotRenderPosition1(turnMsg.playedCardIndex);
		player.useCardByIndex(turnMsg.playedCardIndex);
		player.hasSelectedCard = false;
		
		let drawnCard = this.cards[turnMsg.drawnCardIndex];
		player.addCard(drawnCard);
		player.setPlaying(false);
		
		const nextPosition = (player.position % 4) + 1;
		for(let p in this.players) {
			if(this.players[p].position == nextPosition) {
				this.players[p].setPlaying(true);
				break;
			}
		}
		
		if(this.roundNumber == 4 && this.turnNumber == 24) {
			this.countScores();
			this.processingTurn = false;
			this.finishGame();
		} else if(this.turnNumber == 24) {
			this.turnNumber = 1;
			this.roundNumber++;
			this.roundPlaying = false;
			
			for(let i in this.players) {
				this.players[i].hasSelectedBlocks = false;
			}
			
			this.countScores();
			this.updateGameInfo();
			this.selectBlocks();
			this.processingTurn = false;
		} else {
			this.turnNumber++;
			this.updateGameInfo();
			this.processingTurn = false;
			
			if(this.receivedTurns.length > 0) {
				this.processTurn();
			} else {
				this.startTurn();
			}
		}
		
	}
	
	countScores() {
		let tallestBuildingHeight = -Infinity;
		let tallestBuildingOwner;
		let duplicateTallestBuildingHeight = false;
		
		for(let c in this.cities) {
			let city = this.cities[c];
			let ownedBuildingsByPlayer = {};
			for(let s in city.sectors) {
				let sector = city.sectors[s];
				
				let tallestBlock = sector.getTallestBlock();
				if(tallestBlock) {
					// Each building/sector owned (+1 point) 
					this.players[tallestBlock.player].addScore(1);
					
					if(ownedBuildingsByPlayer[tallestBlock.player]) {
						ownedBuildingsByPlayer[tallestBlock.player] += 1;
					} else {
						ownedBuildingsByPlayer[tallestBlock.player] = 1;
					}
					
					const sectorHeight = sector.getTotalHeight();
					if(sectorHeight > tallestBuildingHeight) {
						tallestBuildingHeight = sectorHeight;
						tallestBuildingOwner = tallestBlock.player;
						duplicateTallestBuildingHeight = false;
					} else if(sectorHeight == tallestBuildingHeight && tallestBlock.player !== tallestBuildingOwner) {
						duplicateTallestBuildingHeight = true;
					}
				}
			}
			
			// Player with most buildings on the city gains +2, if there are no ties
			if(Object.entries(ownedBuildingsByPlayer).length > 0) {
				let maxOwned = -Infinity;
				let playerWithMaxOwnedBuildings;
				let duplicateMaxOwnedBuildings = false;
				
				for(const username in ownedBuildingsByPlayer) {
					if(ownedBuildingsByPlayer[username] > maxOwned) {
						maxOwned = ownedBuildingsByPlayer[username];
						playerWithMaxOwnedBuildings = username;
						duplicateMaxOwnedBuildings = false;
					} else if(ownedBuildingsByPlayer[username] == maxOwned) {
						duplicateMaxOwnedBuildings = true;
					}
				}
				
				if(!duplicateMaxOwnedBuildings) {
					this.players[playerWithMaxOwnedBuildings].addScore(2);
				}
			}
		}
		
		// If there is no tie, player gets +3
		if(!duplicateTallestBuildingHeight) {
			this.players[tallestBuildingOwner].addScore(3);
		}
	}
	
	finishGame() {
		const playersOrderedByPoints = Object.values(this.players).sort((p1, p2) => {
			if(p1.score < p2.score) {
				return -1;
			} else if(p1.score > p2.score) {
				return 1;
			} else {
				return 0;
			}
		});
		const maxPoints = Math.max(playersOrderedByPoints.map((p) => p.score));
		let winners = [];
		for(let player of playersOrderedByPoints) {
			if(player.score == maxPoints) {
				winners.push(player);
			}
		}
		
		this.chat.addMessage("");
		this.chat.addMessage("Sistema: ¡La partida ha terminado!");
		if(winners.length > 1) {
			this.chat.addMessage("Los ganadores son:");
			for(let player of winners) {
				this.chat.addMessage(player.username + " con " + player.score + " puntos.");
			}
		} else {
			this.chat.addMessage("El ganador es: " + winners[0].username + " con " + winners[0].score + " puntos.");
		}
		
		for(let i in this.players) {
			let player = this.players[i];
			if(player.username == userUsername) {
				player.changeHideButtonToExit(this.chat.sendMessage);
				break;
			}
		}
		
		console.log("Game finished!");
	}
	

}