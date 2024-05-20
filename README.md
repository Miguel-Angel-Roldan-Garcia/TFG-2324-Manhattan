# TFG-2324-Manhattan

Este es el repositorio para el trabajo de fin de grado de Miguel Ángel Roldán García, alumno de la titulación de Ingeniería Informática del Software en la Escuela Técnica de Ingeniería Informática de la Universidad de Sevilla.

A continuación, se propondrán algunas formas mediante las que usted puede ejecutar este proyecto en una máquina local y probarlo.

# Instrucciones de instalación

## Requisitos

1. Java 17 (Es preferible la versión 17, aunque una superior podría funcionar).
2. Maven 3.9.5 o superior.
3. Un gestor de bases de datos relacionales con driver disponible en spring data. La configuración proporcionada será para MariaDB.
4. Un cliente de Git.

## Instrucciones de preparación de bases de datos

En este repositorio, se proporcionan 3 archivos de propiedades distintos, relativos a 3 perfiles de Spring de ejemplo, "dev", "migrate" y "prod". El primero borra y carga la base de datos "manhattandb-dev" siempre al iniciarse. El segundo realiza lo mismo pero con "manhattandb" y detiene su ejecución inmediatamente después. El tercero sólo actualiza el esquema de la base de datos "manhattandb" si fuera necesario.

Será necesario crear la base de datos asociada al perfil que se vaya a usar antes de intentar lanzar el servidor. Para saber cómo hacerlo, puede consultar el manual del gestor de bases de datos que haya elegido. Sin embargo, usualmente se habrán de seguir los siguientes pasos:

1. Abrir nuestra herramienta de administración de bases de datos.
2. Abrir una conexión con un usuario con permisos de creación de bases de datos.
3. Ejecutar la siguiente consulta SQL: "CREATE OR REPLACE DATABASE \`nombre\`", donde nombre se refiere al nombre de la base de datos que se quiere crear.

En el proyecto, en src/main/resources/db/dbCreate.sql dispone de las secuencias necesarias para la creación de las 2 bases de datos necesarias para los 3 perfiles de ejemplo proporcionados.

## Instrucciones de instalación del proyecto

1. Clone el proyecto en un directorio de su elección.
2. Instalar con Maven.
3. Cambiar el archivo "*.properties" que se desee con la configuración adecuada.
4. La aplicación de Spring Boot está lista para ser ejecutada.

## Linea de comandos

1. Clonar en el directorio de su elección: git clone https://github.com/Miguel-Angel-Roldan-Garcia/TFG-2324-Manhattan.git
2. Navegar al directorio del proyecto
3. Instalar: mvn install -e
4. Propiedades: Si es necesario, ajuste la url de la base de datos y el driver de spring.datasource acorde al gestor que vaya a usar.
5. Lanzamiento. Deberá cambiar "dev" por el perfil que desee: mvn spring-boot:run -Dspring-boot.run.profiles=dev

## Instrucciones específicas por IDE

### Visual Studio Code

Las siguientes extensiones son recomendables pero no necesarias:
- Extension pack for Java
- Spring Boot Dashboard

Se recomienda ejecutar los comandos de linea de comandos en un terminal, en vez de usar la interfaz gráfica de usuario.

Instrucciones:
1. Clonar: Comando (Abrir con Ctrl+Mayus+P) "Git: clone".
2. Instalación (Dos opciones, extensiones requeridas):
    - Comando: "Maven: Execute commands..." y seleccionar "install".
    - Comando: "Explorer: Focus on maven view", en la ventana que se abre de maven, hacer click derecho en el proyecto, "run maven commands" e "install".
3. Propiedades: Si es necesario, ajuste la url de la base de datos y el driver de spring.datasource acorde al gestor que vaya a usar.
4. Lanzamiento (Dos opciones):
    - (Extension requerida) Abrir la vista de Spring Boot Dashboard, hacer click derecho en el proyecto y presionar en "run with profile...", saldrá una ventana donde elegiremos el perfil deseado.
    - Comando: "Maven: Execute commands...", seleccionar "custom..." e introducir "spring-boot:run -Dspring-boot.run.profiles=dev", cambiando dev por el perfil deseado.

### Eclipse for Java Developers

Las siguientes soluciones del marketplace son recomendadas pero no necesarias:
- Spring Boot Dashboard

Instrucciones:
1. Clonar:
    - Abrir la vista de repositorios de Git y clonar el repositorio (No lo importe todavía, sólo copie la ruta del directorio destino o cambiela)
    - Importar como "Existing Maven Projects" usando el destino de clonado copiado anteriormente.
2. Instalación: Hacer click derecho en el proyecto y ejecutar como "Maven install".
3. Propiedades: Si es necesario, ajuste la url de la base de datos y el driver de spring.datasource acorde al gestor que vaya a usar.
4. Lanzamiento: Hacer click derecho en el proyecto y "Run as". Si no se ha hecho, crearemos una nueva configuración en "Run configurations...", eligiendo una de las siguientes opciones:
    - (Solucion de marketplace requerida) En "Spring Boot App", donde especificaremos el proyecto, la clase principal que es "ManhattanApplication" y el perfil de Spring. Una vez hecho esto, solo habrá que abrir el Boot Dashboard y presionar start en esta configuración para lanzar la aplicación. 
    - En "Java Application", especificando la clase principal que es "ManhattanApplication" y en la pestaña de argumentos el perfil de Spring que se vaya a usar mediante "--spring.profiles.active=dev", cambiando dev por el perfil que se quiera.

