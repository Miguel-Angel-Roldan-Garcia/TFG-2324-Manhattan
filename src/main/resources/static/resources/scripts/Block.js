import convertIndexToLocal from "./SectorPositionGlobalToLocalConverter.js"; 

export default class Block extends Phaser.GameObjects.Image {
	constructor(scene, block, color, playerPosition, blockMask) {
		super(scene, 0, 0, "story" + block.size + "block");
		
		this.setVisible(false);
		this.setActive(false);
		
		this.index = block.index_;
		this.order = block.order_;
		this.placed = block.placed;
		this.player = block.player;
		if(block.placed) {
			this.sector = convertIndexToLocal(block.sector.index_, playerPosition);
			this.city = block.sector.city.index_;
		} else {
			this.sector = null;
			this.city = null;
		}
		
		this.selected = block.selected;
		this.size = block.size;
		
		if(blockMask) {
	        this.setMask(blockMask.createGeometryMask());
        }
		
		if(typeof color == "string") {
			color = parseInt(color.split("#")[1], 16);
		}
		
		this.color = color;
		this.setTint(color);
	}
	
}