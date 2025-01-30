pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {
        stage('Prepare workspace') {
            steps {
                cleanWs()
                checkout scm
                sh "ls"
            }
        }

        stage('Prepare environment variables') {
            steps {
                script {
                    env.BACKEND_WORKDIR="${WORKSPACE}/backend"
                }
                // withCredentials([file(credentialsId: 'application-secret', variable: 'prodCredentials')]) {
                //     script {
                //         sh 'sudo cp $prodCredentials ./src/main/resources/application-secret.yml'
                //     }
                // }
                sh "echo work directory is ${env.BACKEND_WORKDIR}"
            }
        }

        stage('Start notification') {
            steps {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
            }
        }

        // stage('Run test db container') {
        //     steps {
        //         sh "docker stop test-db | true"
        //         sh "docker pull rudeh1253/meta-metamong-oracle-db"
        //         sh "docker run --name test-db -p 1521:1521 -dit --rm rudeh1253/meta-metamong-oracle-db"
        //     }
        // }
        
        stage('Test') {
            steps {
                dir('backend') {
                    sh "ls"
                    sh "chmod 755 ./mvnw"
                    sh "sudo ./mvnw clean test"
                }
            }
        }

        stage('Build front-end') {
            steps {
                dir('frontend') {
                    sh "mkdir build | true"
                    sh "chmod 755 build"
                    sh "docker build -t frontbuilder ."
                    sh "docker run --name fb --rm -v ${WORKSPACE}/frontend/build:/src/dist frontbuilder"
                    sh "ls"
                    sh "ls build"
                    sh "mkdir ${env.BACKEND_WORKDIR}/src/main/resources/static | true"
                    sh "sudo mv build/* ${env.BACKEND_WORKDIR}/src/main/resources/static"
                }
            }
        }

        stage('Build back-end') {
            steps {
                dir('backend') {
                    sh "sudo ./mvnw clean package -Dmaven.test.skip=true"
                }
            }
        }

        stage('Dockerize') {
            steps {
                dir('backend') {
                    sh "sudo docker image build -t metamong-backend:${env.BUILD_ID} ."
                    sh "sudo docker tag metamong-backend:${env.BUILD_ID} hansoo0614/metamong-backend:${env.BUILD_ID}"
                }
            }
        }

        // stage('Docker Push') {
        //     steps {
        //         withCredentials([string(credentialsId: 'docker_hub_access_token', variable: 'dockerHubAccesstoken')]) {
        //             sh "echo ${dockerHubAccesstoken} | sudo docker login --username rudeh1253 --password-stdin"
        //             sh "sudo docker push rudeh1253/sample-hub:${env.BUILD_ID}"
        //         }
        //     }
        // }

        // stage('Deploy') {
        //     steps {
        //         withCredentials([file(credentialsId: 'node_credential', variable: 'nodeInfo'),
        //                 file(credentialsId: 'docker_shell_script', variable: 'deployShellFile')]) {
        //             sh "sudo chown jenkins ${deployShellFile}"
        //             sh "sudo chown jenkins ${nodeInfo}"
        //             sh "sh ${deployShellFile} \$(cat ${nodeInfo}) ${env.BUILD_ID}"
        //         }
        //     }
        // }

        stage('Health Check') {
            steps {
                echo "Health Check new deployment"
            }
        }

        stage('Convert Blue or Green') {
            steps {
                echo "Convert traffic"
                echo "Stop old version container"
            }
        }

        stage('Clear') {
            steps {
                echo "Clear old images"
            }
        }
    }
}