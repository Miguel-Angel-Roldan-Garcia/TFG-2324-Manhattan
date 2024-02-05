<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="manhattan" tagdir="/WEB-INF/tags"%>

<manhattan:manhattanPage>

	<div class="info-main-container">
		<div class="info-title-container">
			<h1 class="title-text">Sobre el TFG Manhattan</h1>
		</div>
		
		<div class="info-text-container">
			<h2>Descripción</h2>
			<p>Este proyecto es el TFG (Trabajo de Fin de Grado) de Miguel Ángel Roldán García, 
			alumno de la Universidad de Sevilla y en el grado de Ingeniería del Software de 
			la Escuela Técnica de Ingeniería Informática. Está basado en el clásico juego de 
			mesa Manhattan, de Andreas Seyfarth.</p>
			
			<p>Este proyecto pretende, en su última versión, proporcionar una forma de jugar 
			en red a Manhattan. Además, se ofrecerá la posibilidad de jugar contra una 
			Inteligencia Artificial.</p>
			
			<p>Para intentar no ocasionar ningún daño de propiedad intelectual, el acceso al 
			proyecto se limitará mediante una clave privada que será otorgada sólo a aquellos  
			que sea estrictamente necesario de cara a su evaluación.</p>
			
			<p>El proyecto está construido sobre las siguientes tecnologías: </p>
			<ul>
				<li>SpringBoot 3.2.1</li>
				<li>Phaser 3.60</li>
			</ul>
			
		</div>
		
	</div>
	
</manhattan:manhattanPage>