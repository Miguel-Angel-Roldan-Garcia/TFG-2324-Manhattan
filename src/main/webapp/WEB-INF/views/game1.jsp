<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
	<title>Manhattan</title>
	
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.70.0/dist/phaser.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.60.0/dist/phaser-arcade-physics.min.js"></script>
	
	<style>
		body {
			margin: 0px;
			
			width: 100vw;
			height: 100vh;
			
			overflow-y: None;
		}
	</style>
</head>

<body id="body">
	
	<script src="/resources/scripts/chat.js"></script>
    <script>
    const COLOR_PRIMARY = 0x4e342e;
    const COLOR_LIGHT = 0x7b5e57;
    const COLOR_DARK = 0x260e04;
    
    class scene extends Phaser.Scene
    {
        preload ()
        {	
        	// Scrollable container plugin
        	this.load.scenePlugin({
                key: 'rexuiplugin',
                url: 'https://raw.githubusercontent.com/rexrainbow/phaser3-rex-notes/master/dist/rexuiplugin.min.js',
                sceneKey: 'rexUI'
            }); 
        	
            this.load.image('logo', 'https://labs.phaser.io/assets/sprites/phaser3-logo.png');
            
        }

        create ()
        {
            const logo = this.physics.add.image(100, 40, 'logo');

            logo.setVelocity(0, 100);
            logo.setBounce(1, 1);
            logo.setCollideWorldBounds(true);

            this.chat = new GameChat(this, 600, 400, 300, 300);
            this.chat.addMessage("Hello world!");
            
            //this.chat.addMessage("Hello world!");
            //this.chat.addMessage("Second message");
            
            //this.borderGraphics = this.add.graphics();
            
			//this.chat.updatePanel("Hello world!")	        
        }
        
    }
    
    const vw = Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0)
    const vh = Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0)

    const config = {
    	parent: 'body',
        type: Phaser.AUTO,
        width: vw,
        height: vh,
        scene: scene,
        physics: {
            default: 'arcade',
        },
    	scale: {
    		parent: 'body',
        	
        	mode: Phaser.Scale.FIT,
        	autoCenter: Phaser.Scale.CENTER_BOTH
    	}
    };
    
    const game = new Phaser.Game(config);
    </script>

</body>
</html>