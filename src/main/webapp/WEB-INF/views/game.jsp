<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="shortcut icon" type="image/x-icon" href="/resources/images/favicon.ico">
    
	<title>Manhattan</title>
	
	<spring:url value="/webjars/jquery/3.7.1/jquery.min.js" var="jQuery"/>
	<script src="${jQuery}"></script>
	
	<spring:url value="/webjars/jquery-ui/1.13.2/jquery-ui.min.css" var="jQueryUiCss"/>
	<link href="${jQueryUiCss}" rel="stylesheet"/>
	
	<spring:url value="/webjars/bootstrap/5.3.2/js/bootstrap.min.js" var="bootstrapJs"/>
	<script src="${bootstrapJs}"></script>
	
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.70.0/dist/phaser.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.60.0/dist/phaser-arcade-physics.min.js"></script>
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
	
	<style>
		html, body {
			margin: 0px;
			
			width: 100%;
			height: 100%;
			
			overflow-y: None;
		}
	</style>
</head>

<body>

	<div id="main-container"></div>

	<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being enabled. Please enable Javascript and reload this page!</h2></noscript>
	
	<script src="/resources/scripts/chat.js"></script>
	<!-- <script src="/resources/scripts/stomp.js"></script> -->
    <script>
    class scene extends Phaser.Scene
    {
    	constructor()
    	{
    		super({ key: 'MainScene' });
    		this.chat = null;
    	}
    	
        preload ()
        {	
        	// Scrollable container plugin
        	this.load.plugin(
        			'rexinputtextplugin', 
        			'https://raw.githubusercontent.com/rexrainbow/phaser3-rex-notes/master/dist/rexinputtextplugin.min.js', 
        			true
        	); 
        	
            this.load.image('logo', 'https://labs.phaser.io/assets/sprites/phaser3-logo.png');
            
        }

        create ()
        {
            const logo = this.physics.add.image(100, 40, 'logo');

            logo.setVelocity(0, 100);
            logo.setBounce(1, 1);
            logo.setCollideWorldBounds(true);
            
            this.chat = new GameChat(this, 600, 400, 300, 300);
            //this.chat.addMessage("Hello world!");
            //this.chat.addMessage("Phaser is a fast, free, and fun open source HTML5 game framework that offers WebGL and Canvas rendering across desktop and mobile web browsers. Games can be compiled to iOS, Android and native apps by using 3rd party tools. You can use JavaScript or TypeScript for development. Along with the fantastic open source community, Phaser is actively developed and maintained by Photon Storm. As a result of rapid support, and a developer friendly API, Phaser is currently one of the most starred game frameworks on GitHub. Thousands of developers from indie and multi-national digital agencies, and universities worldwide use Phaser. You can take a look at their incredible games.");
            this.chat.addMessage("Alo1");
            this.chat.addMessage("Alo2");
            this.chat.addMessage("Alo3");
            this.chat.addMessage("Alo5");
            this.chat.addMessage("Alo6");
            this.chat.addMessage("Alo7");
            this.chat.addMessage("Alo8");
            this.chat.addMessage("Alo9");
            
            this.borderGraphics = this.add.graphics();
            
            this.input.keyboard.on('keydown-UP', (event) => { this.chat.scrollTextUp() });
            this.input.keyboard.on('keydown-DOWN', (event) => { this.chat.scrollTextDown() });
        }
        
        update () 
        {
        	this.updateContainerBorders();
        }
        
        updateContainerBorders() 
        {
            let container = this.chat;

            // Clear previous graphics and draw new borders based on the container size
            this.borderGraphics.clear();
            this.borderGraphics.lineStyle(2, 0xffffff); // Border color and thickness
            this.borderGraphics.strokeRect(container.x, container.y, container.width, container.height);
        }

        printChatMsg(msg) 
        {
        	this.chat.addMessage(msg);
        }
    }
    
    const vw = Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0)
    const vh = Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0)

    const config = {
    	parent: 'main-container',
        type: Phaser.AUTO,
        width: vw,
        height: vh,
        scene: scene,
        physics: {
            default: 'arcade',
        },
    	scale: {
    		parent: 'main-container',
        	
        	mode: Phaser.Scale.FIT,
        	autoCenter: Phaser.Scale.CENTER_BOTH
    	},
    	dom: {
    		createContainer: true
    	},
    	fullscreenTarget: 'main-container'
    };
    
    const game = new Phaser.Game(config);
    </script>

</body>
</html>