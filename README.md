# Practica 01 - Integracion con Pipelines en Jenkins
----------------------------------------------------------------------------

	## Descripcion de la solucion: 
	
		Para este proyecto se considera el modelo de desarrollo Trunk Based Development, en donde se realizaran pequeños cambios en la aplicacion en la rama master. Salvo desarrollos mas grandes , en cuyo caso se realizarian en una nueva rama.
		Debido al modelo de desarrollo, habra una gran variedad de commits a lo largo del ciclo de vida de la aplicacion. Por ello se consideran la creacion de los siguientes jobs:
			
			1) Cada dia a las 22.00h se ejecutaran los test de integracion y E2E. Ademas de los test, esta tarea realizara un analisis de calidad y seguridad del codigo mediante la utilizacion de "SonarCloud". Esto se realiza a esta hora para evitar la sobrecarga de jobs, en caso que uno de los stages requiera un 
			gran esfuerzo. A su vez esto ayudara a que al dia siguiente, se pueda revisar a primera hora si las nuevas funcionalidades han podido crear alguna vulnerabilidad en la aplicacion o posibles BUGS.
			
			2) Para la creacion de plan de marcha atras en caso que haya ido mal alguna nueva funcionalidad o para tener un control con todos los nuevos cambios, se programa una tarea que se ejecute cada dia a las 01.00h con los cambios realizados a lo largo del dia anterior.
			
			3) En cuanto a la publicacion de releases estables se ha preparado una tarea que se encargara de subir el nuevo release a NEXUS. El repositorio de NEXUS esta configurado para que solo se pueda subir una misma version de release, evitando que se pueda subir una misma release mas de una vez.
			
			4) Creacion de una tarea adicional que se encargara de descargar el ejecutable actual de la aplicacion.
			
	
	## Descripcion de los jobs:
	
		1) JOB: NIGHTLY
			
				Stages:
					> Preparation --> Clonacion del repositorio.
					
					> Build app --> Se construye la aplicacion mediante docker-compose build.
					
					> Run app --> Ejecucion de la aplicacion mediante docker-compose up.
					
					> Integration Test --> Se ejecutan los test de integracion con una espera de 30 segundos para permitir al docker-compose levantar la base de datos.
					
					> E2E Test --> Se ejecutan los test end-to-end (rest assured) para las APIs.
					
					> Application Analysis with Sonarqube --> Realizacion de analisis de calidad y seguridad con SonarCloud.
					
					> Post --> Se guardarn los ficheros logs de la aplicacion y la base de datos y se ejecuta docker-compose down para terminar los docker creados.
					
					> Build image Docker --> Se crea la imagen docker de la aplicacion.
					
					> Push image to DockerHub --> SE realiza el push de la imagen creada a dockerhub.
					
				
		2) JOB: Nexus-SNAPSHOT
				
				Stages:
					> Preparation --> clonacion del repositorio.
					
					> Build Java --> Se crea el ejcutable java mediante maven.
					
					> Getting pom version for SNAPSHOT --> Recuperamos la version del fichero pom.
					
					> Publish SNAPSHOT on Nexus --> Guardamos el ejecutable PSHOT en el repositorio de artefactos nexus.
		
		
		3) JOB: Nexus-RELEASE
		
				Stages: 
					> Preparation --> clonacion del repositorio.
					
					> Build Java --> Se crea el ejcutable java mediante maven.
					
					> Getting pom version for RELEASE --> Recuperamos la version del fichero pom para la RELEASE.
					
					> Publish RELEASE on Nexus --> Guardamos el ejecutable del RELEASE en el repositorio de artefactos nexus
		
		
		4) JOBS: JenkinsFile
			
				Stages:
					> Preparation --> Clonacion del repositorio.
					
					> Build app --> Se construye la aplicacion mediante docker-compose build.
					
					> Run app --> Ejecucion de la aplicacion mediante docker-compose up.
					
					> Integration Test --> Se ejecutan los test de integracion con una espera de 15 segundos para permitir al docker-compose levantar la base de datos.
					
					> E2E Test --> Se ejecutan los test end-to-end (rest assured) para las APIs.
					
					> Post --> Se guardarn los ficheros logs de la aplicacion y la base de datos y se ejecuta docker-compose down para terminar los docker creados.
					
					> Create executable jar --> Se crea el ejecutable java de la aplicacion.
					
					> Save Java File --> Se archiva el artefacto junto a los logs de la palicaciony la base de datos en jenkins.
					
	
    ## Descripcion de las Aplicaciones:

        1) Jenkins: Se utiliza jenkins para la ejecucion de los jobs, utilizando un pipeline que clonara un repositorio en concreto. Se ejecuta en local en el puerot 9090.

        2) Nexus: Repositorio de artefactos para guardar las versiones RELEASE y SNAPSHOT. Se ejecuta en local en el puerto 8081.

        3) SonarCloud: Se utiliza sonarcloud en vez de sonarqube debido al performance. Se ejecuta en cloud.

        4) Docker: Para las configuraciones docker, se ejecutan la palicacion y la BBDD mediante docker-compose en los peurtos 8080 y 3306 respectivamente.
					
