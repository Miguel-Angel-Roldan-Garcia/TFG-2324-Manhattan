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
			<h2>Descripción</h2>
			<p>Manhattan es un juego de construcción de rascacielos creado por Andreas Seyfarth.</p>
			
			<h2>El tablero del juego</h2>
			<p>El juego ocurre en un tablero tridimensional donde existen 6 ciudades, cada una con 
			9 sectores donde se podrán colocar, superpuestos, los bloques de construcción. Cada 
			jugador cuenta con 24 bloques para colocar a lo largo de 4 rondas, con 6 turnos cada una. 
			Además, el jugador cuenta con 4 cartas que usará para indicar en qué sector va a colocar
			cada bloque.</p>
			
			<h2>Estado inicial</h2>
			<p>La partida empieza en la ronda 1 y turno 1. El primer jugador en colocar un bloque 
			será el que tenga la cuenta con más antigüedad. A partir de ahí, los jugadores irán 
			colocando según el sentido de las agujas del reloj.</p>
			
			<h2>Los estados del juego</h2>
			<p>La partida consta de 4 rondas y cada ronda de 6 turnos. Los jugadores realizarán las 
			siguientes acciones en orden: </p>
			<ol>
				<li>Al principio de cada ronda, cada jugador seleccionará 6 bloques para usar durante 
				la ronda.</li>
				<li>En turnos y en dirección de las agujas del reloj, cada jugador selecciona una carta 
				y el tipo de bloque que desea poner. Acto seguido, seleccionará una ciudad donde poner 
				el bloque. El tipo de carta decidirá en qué sector se colocará el bloque.</li>
				<li>Al final de cada ronda, cuando se hayan jugado todos los bloques seleccionados, 
				se hace un recuento de puntos según el estado del tablero y se suman los puntos a 
				cada jugador.</li>
			</ol>
			
			<h2>Interfaz del juego</h2>
			<p>Cada jugador verá las mismas 6 ciudades en la misma posición, sin embargo, para simular 
			un tablero real, donde cada jugador está en un lado del tablero, los 9 sectores interiores 
			serán mostrados desde el punto de vista que correspondería a cada persona. Las posiciones 
			de la información de los jugadores también será mostrada acorde a una situación realista.</p>
			<p>En el panel de información de cada jugador, podemos ver su foto de perfil, su nombre de 
			usuario y los bloques que ha seleccionado para la ronda. En nuestro panel también aparecerán 
			las 4 cartas que tengamos en ese momento.</p>
			<p>Si pasamos el ratón o puntero por encima de cada una de las ciudades, en la esquina 
			inferior izquierda podremos notar una vista lateral de cada uno de los edificios de los 3 
			sectores correspondientes a la parte de la ciudad donde tengamos el cursor. Cada ciudad 
			tendrá 3 bandas horizontales con 3 sectores cada una. Con la rueda del ratón, o gestos del 
			touchpad, podemos mover verticalmente la proyección lateral de los edificios, en caso de 
			que los rascacielos sean más altos que el espacio disponible. </p>
			<p>Finalmente, en la esquina inferior derecha tendremos un chat para la partida donde 
			podremos hablar con los otros jugadores en tiempo real.</p>
			
			
			<h2>Puntuaciones</h2>
			<ol>
				<li>Edificio más alto. El jugador con el edificio más alto del tablero ganará 
				3 puntos. Si hubiera algún empate, ninguno de los jugadores sumaría ningún punto.</li>
				<li>Mayor número de edificios de los que se es dueño en una ciudad. Un jugador se 
				considerará dueño de un edificio cuando lo sea del bloque más alto (El último 
				colocado). El jugador con el mayor número de edificios en una ciudad sumará 2 
				puntos. Si hubiera algún empate dentro de una ciudad, ningún jugador sumará 
				puntos en esa ciudad.</li>
				<li>Edificios de los que se es dueño. Cada jugador sumará 1 punto por cada edificio 
				del que sea dueño, independientemente de que un edificio ya le diera puntos mediante 
				alguno de los otros métodos.</li>
			</ol>
			
			<h2></h2>
			<p></p>
		</div>
		
	</div>
	
</manhattan:manhattanPage>