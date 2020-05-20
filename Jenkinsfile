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
        stage('Docker image') {
            steps {
                echo 'Creating docker image..'
            }
        }
        stage('Deploy Docker image') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}