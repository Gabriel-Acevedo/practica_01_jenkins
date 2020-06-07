node {
    def mvnHome = tool 'M3'
    stage("Preparation"){
        git 'https://github.com/Gabriel-Acevedo/practica_01_jenkins.git'
    }
    stage("Build app"){
        sh "docker-compose build"
    }
    stage("Run app"){
        sh "docker-compose up -d"
    }
    stage("Unit Test"){
        sh "sleep 15; mvn test -Dtest=ControllerUnitTest"
    }
    stage("E2E Test"){
        sh "sleep 15;  mvn test -Dtest=ControllerIntegrationTest; mvn test -Dtest=ControllerE2ETest"
    }
    stage("Post"){

        sh "docker-compose logs"
        sh "docker-compose logs > all-logs.txt"
        archive "all-logs.txt"

        sh "docker-compose logs application > application-logs.txt"
        archive "application-logs.txt"

        sh "docker-compose logs mysqldb > mysqldb-logs.txt"
        archive "mysqldb-logs.txt"

        sh "docker-compose down"
        junit '**/target/surefire-reports/*.xml'
    }
    stage("Create executable jar"){
        sh "'${mvnHome}/bin/mvn' clean package -DskipTests"
    }
    stage("Save Java File"){
       archiveArtifacts "**/target/*.jar" 
    }
}