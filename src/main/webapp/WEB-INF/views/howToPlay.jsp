<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>

	<div class="info-main-container">
		<div class="info-title-container">
			<h1 class="title-text">Como jugar a Manhattan</h1>
		</div>
		
		<div class="info-text-container">
			<h2>Descripci�n</h2>
			<p>Manhattan es un juego de construcci�n de rascacielos creado por Andreas Seyfarth.</p>
			
			<h2>El tablero del juego</h2>
			<p>El juego ocurre en un tablero tridimensional donde existen 6 ciudades, cada una con 
			9 sectores donde se podr�n colocar, superpuestos, los bloques de construcci�n. Cada 
			jugador cuenta con 24 bloques para colocar a lo largo de 4 rondas, con 6 turnos cada una. 
			Adem�s, el jugador cuenta con 4 cartas que usar� para indicar en qu� sector va a colocar
			cada bloque.</p>
			
			<h2>Estado inicial</h2>
			<p>La partida empieza en la ronda 1 y turno 1. El primer jugador en colocar un bloque 
			ser� el que tenga la cuenta con m�s antig�edad. A partir de ah�, los jugadores ir�n 
			colocando seg�n el sentido de las agujas del reloj.</p>
			
			<h2>Los estados del juego</h2>
			<p>La partida consta de 4 rondas y cada ronda de 6 turnos. Los jugadores realizar�n las 
			siguientes acciones en orden: </p>
			<ol>
				<li>Al principio de cada ronda, cada jugador seleccionar� 6 bloques para usar durante 
				la ronda.</li>
				<li>En turnos y en direcci�n de las agujas del reloj, cada jugador selecciona una carta 
				y el tipo de bloque que desea poner. Acto seguido, seleccionar� una ciudad donde poner 
				el bloque. El tipo de carta decidir� en qu� sector se colocar� el bloque.</li>
				<li>Al final de cada ronda, cuando se hayan jugado todos los bloques seleccionados, 
				se hace un recuento de puntos seg�n el estado del tablero y se suman los puntos a 
				cada jugador.</li>
			</ol>
			
			<h2>Interfaz del juego</h2>
			<p>Cada jugador ver� las mismas 6 ciudades en la misma posici�n, sin embargo, para simular 
			un tablero real, donde cada jugador est� en un lado del tablero, los 9 sectores interiores 
			ser�n mostrados desde el punto de vista que corresponder�a a cada persona. Las posiciones 
			de la informaci�n de los jugadores tambi�n ser� mostrada acorde a una situaci�n realista.</p>
			<p>En el panel de informaci�n de cada jugador, podemos ver su foto de perfil, su nombre de 
			usuario y los bloques que ha seleccionado para la ronda. En nuestro panel tambi�n aparecer�n 
			las 4 cartas que tengamos en ese momento.</p>
			<p>Si pasamos el rat�n o puntero por encima de cada una de las ciudades, en la esquina 
			inferior izquierda podremos notar una vista lateral de cada uno de los edificios de los 3 
			sectores correspondientes a la parte de la ciudad donde tengamos el cursor. Cada ciudad 
			tendr� 3 bandas horizontales con 3 sectores cada una. Con la rueda del rat�n, o gestos del 
			touchpad, podemos mover verticalmente la proyecci�n lateral de los edificios, en caso de 
			que los rascacielos sean m�s altos que el espacio disponible. </p>
			<p>Finalmente, en la esquina inferior derecha tendremos un chat para la partida donde 
			podremos hablar con los otros jugadores en tiempo real.</p>
			
			
			<h2>Puntuaciones</h2>
			<ol>
				<li>Edificio m�s alto. El jugador con el edificio m�s alto del tablero ganar� 
				3 puntos. Si hubiera alg�n empate, ninguno de los jugadores sumar�a ning�n punto.</li>
				<li>Mayor n�mero de edificios de los que se es due�o en una ciudad. Un jugador se 
				considerar� due�o de un edificio cuando lo sea del bloque m�s alto (El �ltimo 
				colocado). El jugador con el mayor n�mero de edificios en una ciudad sumar� 2 
				puntos. Si hubiera alg�n empate dentro de una ciudad, ning�n jugador sumar� 
				puntos en esa ciudad.</li>
				<li>Edificios de los que se es due�o. Cada jugador sumar� 1 punto por cada edificio 
				del que sea due�o, independientemente de que un edificio ya le diera puntos mediante 
				alguno de los otros m�todos.</li>
			</ol>
			
			<h2></h2>
			<p></p>
		</div>
		
	</div>
	
</manhattan:manhattanPage>