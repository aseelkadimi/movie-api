pipeline {
    agent any
    tools {
            maven 'Maven 3.6.3'
            jdk 'jdk8'
        }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Sonar') {
            steps {
                withSonarQubeEnv('SonarQube') {
                  sh 'mvn sonar:sonar'
                }
            }
        }
        stage("Docker build") {
             steps {
                  sh "docker build -t aseelkadimi/movie-api ."
             }
        }
        stage("Docker push") {
             steps {
                withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'dockerPass', usernameVariable: 'dockerUser')]) {
                    sh "docker login -u $dockerUser -p $dockerPass"
                    sh "docker push $dockerUser/movie-api"
                }

             }
        }
        stage('Deploy Docker image') {
            steps {
                echo 'docker run -p 8081:8080 aseelkadimi/movie-api'
            }
        }
    }
}