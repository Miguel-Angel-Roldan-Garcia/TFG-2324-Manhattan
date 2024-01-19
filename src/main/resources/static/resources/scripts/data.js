class Block {
	
	constructor(index, size, selected, placed, order, cityIndex, sectorIndex) {
		this.index = index;
		this.size = size;
		this.selected = selected;
		this.placed = placed;
		this.order = order;
		this.cityIndex = cityIndex; 
		this.sectorIndex = sectorIndex;
	}
	
}

class Card {
	
	constructor(index, sectorIndex, used, player) {
		this.index = index;
		this.sectorIndex = sectorIndex;
		this.used = used;
		this.player = player;
	}
	
}

class Player {
	
	constructor(username, score, color, position, hasSelectedBlocks, playing, blocks) {
		this.username = username;
		this.score = score;
		this.color = color;
		this.position = position;
		this.hasSelectedBlocks = hasSelectedBlocks;
		this.playing = playing;
		this.blocks = blocks;
	}
	
}



