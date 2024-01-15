
class GameChat extends Phaser.GameObjects.Container {
	
	constructor (scene, x, y, width, height) {
		super(scene, x, y)
		this.scene = scene;
		
		this.width = width;
		this.height = height;
		
		this.marginX = (1/10) * width;
		this.marginY = (1/10) * height; 
		
		this.textWidth = width - 2 * this.marginX;
		this.usableHeight = height - 2 * this.marginY;
		
		scene.add.existing(this);
		this.msgY = this.marginY;
		
		this.textStyle = {
			font: '20px Arial',
			fill: '#FFFFFF',
			width: this.textWidth,
			useAdvancedWrap: true
		}
		
		this.COLOR_PRIMARY = 0x4e342e;
		this.COLOR_LIGHT = 0x7b5e57;
		this.COLOR_DARK = 0x260e04;
        
        this.scrollablePanel = this.scene.rexUI.add.scrollablePanel({
            x: 400,
            y: 300,
            width: 300,
            height: 300,

            scrollMode: 0,

            background: this.scene.rexUI.add.roundRectangle(0, 0, 2, 2, 10, COLOR_PRIMARY),

            panel: {
                child: this.scene.rexUI.add.fixWidthSizer({
                    space: {
                        left: 3,
                        right: 3,
                        top: 3,
                        bottom: 3,
                        item: 8,
                        line: 8,
                    }
                }),

                mask: {
                    padding: 1
                },
            },

            slider: {
                track: this.scene.rexUI.add.roundRectangle(0, 0, 20, 10, 10, COLOR_DARK),
                thumb: this.scene.rexUI.add.roundRectangle(0, 0, 0, 0, 13, COLOR_LIGHT),
            },

            space: {
                left: 10,
                right: 10,
                top: 10,
                bottom: 10,

                panel: 10,
            }
        }).layout()

		this.add(this.scrollablePanel)
		
	}
	
	addMessage (message) {
		this.panel
            .getElement('panel')
            .add(
                this.createPaper(
                    message,
                    this.scene.rexUI.add.roundRectangle(0, 0, 200, 400, 20, this.COLOR_PRIMARY)
                )
            )
            
        this.panel.layout() 
        
        /*
		this.msgY += this.lasMessage.height;
		
		if (this.msgY > this.usableHeight) {
			
		}
		
		const msgTextObject = this.scene.add.text(this.marginX, this.msgY, message, this.textStyle);
		this.add(msgTextObject);
		
		this.lastMessage = msgTextObject;
		*/
	}
	
	updatePanel (content) {
		var panel = this.scrollablePanel;
        var sizer = panel.getElement('panel');
        var scene = panel.scene;

        sizer.clear(true);
        var lines = content.split('\n');
        for (var li = 0, lcnt = lines.length; li < lcnt; li++) {
            var words = lines[li].split(' ');
            for (var wi = 0, wcnt = words.length; wi < wcnt; wi++) {
                sizer.add(
                    scene.add.text(0, 0, words[wi], {
                        fontSize: 18
                    })
                        .setInteractive()
                        .on('pointerdown', function () {
                            this.scene.print.text = this.text;
                            this.setTint(Phaser.Math.Between(0, 0xffffff))
                        })
                );
            }
            if (li < (lcnt - 1)) {
                sizer.addNewLine();
            }
        }


        panel.layout();
        return panel;
    }
	
}