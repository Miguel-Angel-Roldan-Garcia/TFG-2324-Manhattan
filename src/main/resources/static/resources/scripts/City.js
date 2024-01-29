import Sector from "./Sector.js";

export default class City extends Phaser.GameObjects.Container {
	constructor(scene, index, graphics) {
		const cityPositions = {
			"1": {
				"x": 3*vw/9,
				"y": 0.36*vh
			},
			"2": {
				"x": 4.5*vw/9,
				"y": 0.36*vh
			},
			"3": {
				"x": 6*vw/9,
				"y": 0.36*vh
			},
			"4": {
				"x": 3*vw/9,
				"y": 0.64*vh
			},
			"5": {
				"x": 4.5*vw/9,
				"y": 0.64*vh
			},
			"6": {
				"x": 6*vw/9,
				"y": 0.64*vh
			}
		}
		
		const width = vw / 8;
		const height = width;
		
		super(scene, cityPositions[index].x, cityPositions[index].y);
		
		this.width = width;
		this.height = height;
		
		this.globalX = this.x - this.width/2;
		this.globalY = this.y - this.height/2;
		
		this.width = width;
		this.height = height;
		
		this.index = index;
		this.isPointerMoveSet = false;
		this.sectors = {};
		
		graphics.fillStyle(0x303030, 0.75);
		graphics.fillRect(this.globalX, this.globalY, this.width, this.height);
		graphics.lineStyle(1, 0xffffff);
		graphics.strokeRect(this.globalX, this.globalY, this.width, this.height);
		
		const hitArea = new Phaser.Geom.Rectangle(0, 0, this.width, this.height);
		
		this.setInteractive(hitArea, Phaser.Geom.Rectangle.Contains);
				
		this.createSectors();
		
		scene.add.existing(this);
	}
	
	createSectors() {
		this.sectors = {};
		
		const marginX = this.width/12;
		const marginY = this.height/12;
		const width = (this.width - 4*marginX)/3;
		const height = (this.height - 4*marginY)/3;
		
		let gridIndex = 1;
		for(let i = 1; i <= 3; i++) { // Rows
			for(let j = 1; j <= 3; j++) { // Columns
				const x = j*marginX + (j-1)*width - this.width/2; 
				const y = i*marginY + (i-1)*height - this.height/2;
				
				const sector = new Sector(this.scene, x, y, width, height, gridIndex, this);
				sector.setOrigin(0);
				this.sectors[gridIndex] = sector;
				this.add(this.sectors[gridIndex++]);
			}
		}
	}
	
	getRowSectors(row) {
		switch(row){
			case 1:
				return [this.sectors[1], this.sectors[2], this.sectors[3]];
			case 2:
				return [this.sectors[4], this.sectors[5], this.sectors[6]];
			case 3:
				return [this.sectors[7], this.sectors[8], this.sectors[9]];
		}
	}
	
}