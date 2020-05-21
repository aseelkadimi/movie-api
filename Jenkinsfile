pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Mutation test') {
            steps {
                echo 'Mutation test ....'
            }
        }
        stage('Sonar') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://172.18.0.3:9000'
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