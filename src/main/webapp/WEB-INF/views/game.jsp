<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html>

<manhattan:head>
	<link href="/resources/css/game.css" rel="stylesheet"/>
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.60.0/dist/phaser.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/phaser@3.60.0/dist/phaser-arcade-physics.min.js"></script>
	<script src="https://labs.phaser.io/js/datgui.js"></script>
</manhattan:head>

<body>

	<noscript><h2 style="color: #ff0000">Parece que su navegador no soporta o no tiene activado Javascript! Manhattan necesita Javascript para funcionar. Por favor, active o permita el uso de Javascript y recargue la página.</h2></noscript>

	<div id="main-container"></div>

	<script>
	    const gameId = "${gameId}";
	    const vw = Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0);
	    //const vh =  950;// Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0);
	    const vh = Math.floor(vw * 0.49635);
	    const userUsername = "${username}";
	    let isHost = "${isHost}" == "true";
	</script>
	
	<script src="/resources/scripts/main.js" type="module"></script>

</body>

</html>