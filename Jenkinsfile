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
        stage('Deploy Docker image') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}