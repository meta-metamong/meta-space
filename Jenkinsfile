pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    environment {
        MAIL_HOST='smtp.gmail.com'
        MAIL_USERNAME=credentials('mail_username')
        MAIL_PASSWORD=credentials('mail_password')
        JDBC_URL=credentials('jdbc_url')
        JDBC_USERNAME=credentials('jdbc_username')
        JDBC_PASSWORD=credentials('jdbc_password')
        CLIENT_DOMAIN=credentials('client_domain')
        CLIENT_ORIGIN=credentials('client_origin')
    }

    stages {
        stage('Prepare workspace') {
            steps {
                cleanWs()
                checkout scm
                sh "ls"
                sh "pwd"
            }
        }

        stage('Prepare environment variables') {
            steps {
                script {
                    env.BACKEND_WORKDIR="${WORKSPACE}/backend"
                }
                // withCredentials([string(credentialsId: 'mail_username', variable: 'mailUsername'),
                //         string(credentialsId: 'mail_password', variable: 'mailPassword'),
                //         string(credentialsId: 'jdbc_url', variable: 'jdbcUrl'),
                //         string(credentialsId: 'jdbc_username', variable: 'jdbcUsername'),
                //         string(credentialsId: 'jdbc_password', variable: 'jdbcPassword'),
                //         string(credentialsId: 'client_domain', variable: 'clientDomain'),
                //         string(credentialsId: 'client_origin', variable: 'clientOrigin')]) {
                //     sh "export MAIL_USERNAME=${mailUsername}"
                //     sh "export MAIL_PASSWORD=${mailPassword}"
                //     sh "export MAIL_HOST=smtp.gmail.com"
                //     sh "export JDBC_URL=${jdbcUrl}"
                //     sh "export JDBC_USERNAME=${jdbcUsername}"
                //     sh "export JDBC_PASSWORD=${jdbcPassword}"
                //     sh "export CLIENT_DOMAIN=${clientDomain}"
                //     sh "export CLIENT_ORIGIN=${clientOrigin}"
                // }
                sh "env"
                sh "echo work directory is ${env.BACKEND_WORKDIR}"
            }
        }

        stage('Start notification') {
            steps {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
            }
        }

        stage('Run test db container') {
            steps {
                sh "docker stop test-db | true"
                sh "docker pull rudeh1253/meta-metamong-oracle-db:latest-schema"
                sh "docker run --name test-db -p 1521:1521 -dit --rm rudeh1253/meta-metamong-oracle-db:latest-schema"
            }
        }
        
        stage('Test') {
            steps {
                dir('backend') {
                    sh "ls"
                    sh "chmod 755 ./mvnw"
                    sh "sudo ./mvnw clean test"
                    sh "docker container stop test-db"
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
                    sh "sudo docker image build -t metamong-backend ."
                    sh "sudo docker tag metamong-backend hansoo0614/metamong-backend:${env.BUILD_ID}"
                    sh "sudo docker tag metamong-backend hansoo0614/metamong-backend:latest"
                }
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([string(credentialsId: 'docker_hub_access_token', variable: 'dockerHubAccesstoken')]) {
                    sh "echo ${dockerHubAccesstoken} | sudo docker login --username hansoo0614 --password-stdin"
                    sh "sudo docker push hansoo0614/metamong-backend:${env.BUILD_ID}"
                    sh "sudo docker push hansoo0614/metamong-backend:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([string(credentialsId: 'worker_node_ip', variable: 'workerNodeIp'),
                        file(credentialsId: 'docker-compose-env-file', variable: 'envFile'),
                        string(credentialsId: 'docker_hub_access_token', variable: 'dockerHubAccesstoken')]) {
                    sh "scp docker-compose.yml ubuntu@${workerNodeIp}:~"
                    sh "ssh ubuntu@${workerNodeIp} \"env\""
                    sh "ssh ubuntu@${workerNodeIp} \"echo ${dockerHubAccesstoken} | sudo docker login --username hansoo0614 --password-stdin\""
                    sh "ssh ubuntu@${workerNodeIp} \"sudo docker pull hansoo0614/metamong-backend:latest\""
                    sh "ssh ubuntu@${workerNodeIp} \"sudo docker-compose --profile blue --env-file ~/envs up\""
                    // sh "docker-compose -H 'ssh://ubuntu@${workerNodeIp}' up"
                }
                withCredentials([]) {
    // some block
}
            }
        }

        // stage('Health Check') {
        //     steps {
        //         echo "Health Check new deployment"
        //     }
        // }

        stage('Convert Blue or Green') {
            steps {
                echo "Convert traffic"
                echo "Stop old version container"
            }
        }

        stage('Clear') {
            steps {
                sh "docker image prune"
            }
        }
    }
}