
function deepCopy(obj) {
	return JSON.parse(JSON.stringify(obj));
}

function getRandomInt(min, max) {
		const minCeiled = Math.ceil(min);
		const maxFloored = Math.floor(max);
		return Math.floor(Math.random() * (maxFloored - minCeiled) + minCeiled);
	}
	
function selectRandomNotZero(items) {
	let nonZeroItems = [];
	
	for(let i in items) {
		if(items[i] > 0)
			nonZeroItems.push(i);
	}
	
	const randomElement = nonZeroItems[getRandomInt(0, nonZeroItems.length)];
	return randomElement;
}

export default async function generateTurnMessage(turnNumber, roundNumber, player, players, cards, cities) {
	let cards2 = player.cards;
	const cardSelected = cards2[getRandomInt(0, cards2.length)];
	
	const blockSize = selectRandomNotZero(player.countSelectedBlocks(false));
	
	let msg = {
		"username": player.username,
		"playedCardIndex": cardSelected.index,
		"placedBlockIndex": player.getSelectedBlockOfSize(blockSize),
		"cityIndex": getRandomInt(1, 7),
		"sectorIndex": cardSelected.sector
	};
	
	// Temporal call for uct search
	let state = new State(false, roundNumber, turnNumber, player, players, cards, cities);
	
	let start = performance.now();
	let result = await getUCTAction(state, roundNumber * 300);
	result = result.action;
	console.log(result);
	console.log("Time taken on user: " + player.username + "; " + (performance.now() - start)/1000 + " seconds.");

	msg = {
		"username": player.username,
		"playedCardIndex": result.playedCardIndex,
		"placedBlockIndex": player.getSelectedBlockOfSize(result.placedBlockHeight),
		"cityIndex": result.cityIndex,
		"sectorIndex": result.sectorIndex
	};

	return msg;
}

async function getUCTAction(rootState, iterations) {
	console.log("iterations: " + iterations)
	let rootNode = new Node(undefined, rootState, undefined);
	for(let i = 0; i < iterations; i++) {
        let node = rootNode;
        let state = rootState.copy();
        
        // Selection
        while(node.untriedActions.length == 0 && node.children.length > 0) {
            node = node.selectChild();
            state.apply(node.action);
        }
        
        // Expansion
        if(node.untriedActions.length != 0) {
            let action = node.untriedActions[getRandomInt(0, node.untriedActions.length)];
            state.apply(action);
            node = node.addChild(action, state);
        }
        
        // Simulation
        while(true) {
			const availableActions = state.getAvailableActions();
			if(state.isTerminal()) {
				break;
			}
			
			const selectedAction = availableActions[getRandomInt(0, availableActions.length)];
			state.apply(selectedAction);
		}
        
        // Backpropagation
        const result = state.getResult();
        while(node) {
            node.update(result);
            node = node.parent;
        }
        
        await new Promise((resolve, reject) => {
			setTimeout(() => {
				resolve();
			}, 10);
		});
    }
    
    return rootNode.selectChild(0);
}

class Action {
	constructor(playedCardIndex, placedBlockHeight, cityIndex, sectorIndex) {
		this.playedCardIndex = playedCardIndex;
		this.placedBlockHeight = placedBlockHeight;
		this.cityIndex = cityIndex;
		this.sectorIndex = sectorIndex;
	}
}

class SelectAction {
	constructor(selected1, selected2, selected3, selected4) {
		this.selected1 = selected1;
		this.selected2 = selected2;
		this.selected3 = selected3;
		this.selected4 = selected4;
	}
}

class State {
	constructor(blankConstructor, roundNumber, turnNumber, currentPlayer, players, cards, cities) {
		if(blankConstructor) {
			return;
		}
		
		this.roundNumber = roundNumber;
		this.turnNumber = turnNumber;
		
		this.initialPlayer = currentPlayer.username;
		this.currentPlayer = currentPlayer.username;
		
		this.players = {}
		for(let username in players) {
			const player = players[username];
			
			let blocks = [];
			for(let i in player.blocks) {
				const block = player.blocks[i];
				blocks.push({
					"city": block.city,
					"order": block.order,
					"placed": block.placed,
					"sector": block.sector,
					"size": block.size,
					"selected": block.selected,
					"index": block.index,
					"player": username
				});
			}
			
			this.players[username] = {
				"blocks": blocks,
				"position": player.position,
				"score": player.score,
				"unselected": player.countUnselectedBlocks(),
				"selected": player.countSelectedBlocks(false)
			}
		}
		
		let filteredCards = [];
		for(let i in cards) {
			const card = cards[i];
			filteredCards.push({
				"index": card.index,
				"sector": card.sector,
				"selected": card.selected,
				"used": card.used,
				"player": card.player
			})
		}
		this.cards = filteredCards;
		
	}
	
	copy() {
		let stateCopy = new State(true, null, null, null, null, null, null);
		stateCopy.roundNumber = this.roundNumber;
		stateCopy.turnNumber = this.turnNumber;
		stateCopy.initialPlayer = this.initialPlayer;
		stateCopy.currentPlayer = this.currentPlayer;
		stateCopy.players = deepCopy(this.players);
		stateCopy.cards = deepCopy(this.cards);
		
		return stateCopy;
	}
	
	isTerminal() {
		// HINT: 25 instead of 24 because here we look at it before instead of after the turn handling
		return this.roundNumber == 4 && this.turnNumber == 25;
	}
	
	isSelectTurn() {
		return this.turnNumber == 25;
	}
	
	haveAllSelected() {
		let res = true;
		
		for(let i in this.players) {
			if(Object.values(this.players[i].selected).every(value => value == 0)) {
				res = false;
				break;
			}
		}
		
		return res;
	}
	
	getAvailableActions() {
		let actions = [];
		if(this.isSelectTurn()) {	
			let blocksLeft = 6;
			let unselected = deepCopy(this.players[this.currentPlayer].unselected);
			let current = {
				1: unselected[1] > 0 ? 1 : 0,
				2: unselected[2] > 0 ? 1 : 0,
				3: unselected[3] > 0 ? 1 : 0,
				4: unselected[4] > 0 ? 1 : 0
			};
			let combinations = [];
			
			// To take into account the hardcoded minimum of 1 selected
			for(let i in current) {
				blocksLeft -= current[i];
				unselected[i] -= current[i];
			}
			
			this.getSelectCardCombinations(blocksLeft, current, unselected, 1, combinations);
			
			actions = combinations;
			
			return actions;
		} else {
			const selectedBlocks = this.players[this.currentPlayer].selected;
			let nonZeroSelectedBlockHeights = [];
			
			for(let i in selectedBlocks) {
				if(selectedBlocks[i] > 0) {
					nonZeroSelectedBlockHeights.push(i);
				}
			}
			
			// TODO: Ignore cards with duplicate sectors for efficiency
			for(let card of this.getPlayerCards(this.currentPlayer)) {
				for(let cityIndex = 1; cityIndex < 7; cityIndex++) {
					for(let blockHeight of nonZeroSelectedBlockHeights) {
						actions.push(new Action(card.index, parseInt(blockHeight), cityIndex, card.sector));
					}
				}
			}
			
			return actions;
		}
	}
	
	apply(action) {
		if(this.isSelectTurn()) {
			this.players[this.currentPlayer].selected = {
				1: action.selected1,
				2: action.selected2,
				3: action.selected3,
				4: action.selected4
			}
			
			for(let i in this.players[this.currentPlayer].selected) {
				this.players[this.currentPlayer].unselected[i] -= this.players[this.currentPlayer].selected[i];
				
				for(let j = 0; j < this.players[this.currentPlayer].selected[i]; j++) {
					this.getUnselectedPlayerBlockOfHeight(i).selected = true;
				}
			}
			
			this.advancePlayer();
			
			if(this.haveAllSelected()) {
				this.turnNumber = 1;
				this.roundNumber++;
			} 
		} else {
			let player = this.players[this.currentPlayer];
		
			let card = this.cards.filter(c => c.index == action.playedCardIndex)[0];
			card.used = true;
			card.player = null;
			
			let block = this.getSelectedPlayerBlockOfHeight(action.placedBlockHeight);
			block.sector = action.sectorIndex;
			block.city = action.cityIndex;
			block.order = this.getSectorNextOrderByIndex(block.city, block.sector);
			block.placed = true;
			block.selected = false;
			player.selected[block.size]--;
			
			this.drawCard();
			
			this.advancePlayer();
			
			this.turnNumber++;
			
			if(this.isSelectTurn()) {
				this.calculateScore();
			}
		}
	}
	
	advancePlayer() {
		const nextPosition = (this.players[this.currentPlayer].position % 4) + 1;
		for(let i in this.players) {
			if(this.players[i].position == nextPosition) {
				this.currentPlayer = i;
				break;
			}
		}
	}
	
	getResult() {
		let initialPlayerScore = this.players[this.initialPlayer].score;
		let winner = true;
		for(let i in this.players) {
			if(i != this.initialPlayer && this.players[i].score > initialPlayerScore) {
				winner = false;
				break;
			}
		}
				
		return winner;
	}
	
	getPlayerCards(player) {
		return this.cards.filter(c => c.player == player);
	}
	
	getUnselectedPlayerBlockOfHeight(height) {
		const blocks = this.players[this.currentPlayer].blocks.filter(b => !b.selected && !b.placed && b.size == height);
		
		return blocks.length > 0 ? blocks[0] : null;
	}
	
	getSelectedPlayerBlockOfHeight(height) {
		const blocks = this.players[this.currentPlayer].blocks.filter(b => b.selected && !b.placed && b.size == height);
		
		return blocks.length > 0 ? blocks[0] : null;
	}
	
	getSectorNextOrderByIndex(cityIndex, sectorIndex) {
		let sectorBlocks = [];
		for(let i in this.players) {
			const player = this.players[i];
			for(let j in player.blocks) {
				if(player.blocks[j].city == cityIndex && player.blocks[j].sector == sectorIndex) {
					sectorBlocks.push(player.blocks[j]);
				}
			}
		}
		
		sectorBlocks.sort((b1, b2) => b1.order == b2.order ? 0 : b1.order < b2.order ? 1 : -1)
		
		return sectorBlocks[0].order + 1;
	}
	
	drawCard() {		
		let cards = this.cards.filter(c => c.player == null && !c.used)
		if(cards.length == 0) {
			this.reshufleCards()
			cards = this.cards.filter(c => c.player == null && !c.used)
		}
		
		const i = cards.length == 1 ? 0 : getRandomInt(0, cards.length);
		
		cards[i].player = this.currentPlayer;
	}
	
	reshufleCards() {
		this.cards.forEach(c => {
			if(c.used) {
				c.used = false;
			}
		})
	}
	
	getSelectCardCombinations(blocksLeft, current, unselected, unselectedIndex, combinations) {
		if(blocksLeft == 0) {
			combinations.push(new SelectAction(current[1], current[2], current[3], current[4]));
			return;
		}
		
		for(let i in unselected) {
			if(i >= unselectedIndex && unselected[i] > 0) {
				unselected[i]--;
				current[i]++;
				blocksLeft--;
				
				this.getSelectCardCombinations(blocksLeft, current, unselected, i, combinations);
				
				unselected[i]++;
				current[i]--;
				blocksLeft++;				
			}
		}
	}
	
	calculateScore() {
		// Calculate points of round
		
		let tallestBuildingHeight = -Infinity;
		let tallestBuildingOwner;
		let duplicateTallestBuildingHeight = false;
		
		for(let city = 0; city < 7; city++) {
			let ownedBuildingsByPlayer = {};
			for(let sector = 0; sector < 10; sector++) {
				
				let tallestBlock = this.getSectorTallestBlock(city, sector);
				if(tallestBlock) {
					// Each building/sector owned (+1 point) 
					this.players[tallestBlock.player].score++;
					
					if(ownedBuildingsByPlayer[tallestBlock.player]) {
						ownedBuildingsByPlayer[tallestBlock.player] += 1;
					} else {
						ownedBuildingsByPlayer[tallestBlock.player] = 1;
					}
					
					const sectorHeight = this.getSectorTotalHeight(city, sector);
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
					this.players[playerWithMaxOwnedBuildings].score += 2;
				}
			}
		}
		
		// If there is no tie, player gets +3
		if(!duplicateTallestBuildingHeight) {
			this.players[tallestBuildingOwner].score += 3;
		}
	}
	
	getSectorTallestBlock(cityIndex, sectorIndex) {
		let sectorBlocks = [];
		for(let i in this.players) {
			const player = this.players[i];
			for(let j in player.blocks) {
				if(player.blocks[j].city == cityIndex && player.blocks[j].sector == sectorIndex) {
					sectorBlocks.push(player.blocks[j]);
				}
			}
		}
		
		sectorBlocks.sort((b1, b2) => b1.order == b2.order ? 0 : b1.order < b2.order ? 1 : -1)
		
		return sectorBlocks[0];
	}
	
	getSectorTotalHeight(cityIndex, sectorIndex) {
		let sectorHeight = 0;
		for(let i in this.players) {
			const player = this.players[i];
			for(let j in player.blocks) {
				if(player.blocks[j].city == cityIndex && player.blocks[j].sector == sectorIndex) {
					sectorHeight += player.blocks[j].size;
				}
			}
		}
		
		return sectorHeight;
	}
}

class Node {
    constructor(action, state, parent) {
		this.action = action;
        this.parent = parent;
        this.children = [];
        this.wins = 0;
        this.visits = 0;
        this.untriedActions = state.getAvailableActions();
	}

    selectChild(param = 2) {
		let choices_weights = [];
		
        for(let child of this.children) {
			choices_weights.push((child.wins / child.visits) + param * Math.sqrt((2 * Math.log(this.visits) / child.visits)));
		}
		
        return this.children[choices_weights.indexOf(Math.max(...choices_weights))];
	}
        
    addChild(action, state) {
		const node = new Node(action, state, this);
		this.children.push(node);
		
		this.untriedActions = this.untriedActions.filter(a => a != action);
		
		return node;
	}
	
	update(result) {
		this.visits++;
		if(result) {
			this.wins++;
		}
	}
	    
}
