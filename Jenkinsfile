pipeline {
    agent any

    stages {

        stage('Check Docker') {
            steps {
                bat 'docker ps'
            }
        }

        stage('Start Grid') {
            steps {
                bat 'docker-compose up -d'
            }
        }

        stage('Wait for Grid') {
            steps {
                bat 'timeout /t 15'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Stop Grid') {
            steps {
                bat 'docker-compose down'
            }
        }
    }

    post {
        always {
            echo 'Execution completed'
        }
        success {
            echo 'Build SUCCESS'
        }
        failure {
            echo 'Build FAILED'
        }
    }
}