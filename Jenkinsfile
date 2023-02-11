pipeline {
    agent any
    tools {
        maven 'M3'
        jdk 'openJDK17'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    env | grep -e PATH -e JAVA_HOME
                    which java
                    java -version
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn clean -Dmaven.test.failure.ignore=true install'
            }
//             post {
//                 success {
//                     junit 'target/surefire-reports/**/*.xml'
//                 }
//             }
        }

        stage ('Package') {
            steps {
                sshPublisher(publishers: [sshPublisherDesc(configName: 'ubuntu-house-server', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: 'echo successfully moved files! :)', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '**/*.jar')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
    }
}