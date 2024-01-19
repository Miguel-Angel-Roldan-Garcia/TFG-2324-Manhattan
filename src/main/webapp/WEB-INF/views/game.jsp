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
	
	<%-- <spring:url value="/webjars/bootstrap/5.3.2/js/bootstrap.min.js" var="bootstrapJs"/>
	<script src="${bootstrapJs}"></script> --%>
	
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.70.0/dist/phaser.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.60.0/dist/phaser-arcade-physics.min.js"></script>
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
	
	<link href="/resources/css/game.css" rel="stylesheet"/>
</head>

<body>

	<!-- Loading page overlay -->
	<div id="loading-overlay">
		<h1 id="loading-msg">Please wait while the game loads.</h1>
	</div>

	<div id="main-container"></div>

	<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being enabled. Please enable Javascript and reload this page!</h2></noscript>
	
	<script src="/resources/scripts/stomp.js"></script>
	<script src="/resources/scripts/chat.js"></script>
	<script src="/resources/scripts/fetchData.js"></script>
    <script>
    const vw = Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0)
    const vh = Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0)
    const gameId = "${gameId}";
    const phaserGame = null;
    const gameData = null;
    
    class scene extends Phaser.Scene
    {
    	constructor()
    	{
    		super({ key: 'MainScene' });
    		this.chat = null;
    		this.stompClient = null;
    	}
    	
        preload ()
        {	
        	// Scrollable container plugin
        	this.load.plugin(
        			'rexinputtextplugin', 
        			'https://raw.githubusercontent.com/rexrainbow/phaser3-rex-notes/master/dist/rexinputtextplugin.min.js', 
        			true
        	); 
        	
        }

        create ()
        {
        	// Creation of the stomp client
            this.stompClient = new ManhattanStompClient();
            
        	// Creation of game chat
            const chatWidth = vw * 0.2;
            const chatHeight = vh * 0.3;
            const chatX = 0;
            const chatY = vh - chatHeight;
            
            this.chat = new GameChat(this, chatX, chatY, chatWidth, chatHeight, this.stompClient, gameId);
            this.chat.addMessage("Welcome to the chat!");
            
            this.borderGraphics = this.add.graphics();
            
            // 
            
            
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
            this.borderGraphics.lineStyle(1, 0xffffff); // Border color and thickness
            this.borderGraphics.strokeRect(container.x, container.y, container.width, container.height);
        }

    }
    
    const config = {
    	parent: 'main-container',
        type: Phaser.AUTO,
        width: vw,
        height: vh,
        scene: scene,
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
    
    fetchData("/game/" + gameId + "/get-data")
    	.then(data => {
    		gameData = data;
    		phaserGame = new Phaser.Game(config);
    		document.getElementById("loading-overlay").style.display = "none";
    	})
    	.catch(error => {
    		document.getElementById("loading-msg").innerHtml = "Something went wrong fetching game data!";
    	});
    
    </script>

</body>
</html>