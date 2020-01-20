pipeline {

  agent any

  options {
    ansiColor("xterm")
  }

  parameters {
    choice(name: 'VERBOSE', choices: ['Yes', "No"], description: "Choice Yes to have a verbose mode on logs")
    choice(name: 'ansible_environment', choices: ['DEV, INT, PROD'], description: 'Environment where you want to play your pipeline')
  }

  stages {

    stage("Log level") {
      steps {
        script {
          if (VERBOSE == 'Yes') {
              LOG_LEVEL = '-vvvv'
          } else {
              LOG_LEVEL = ''
          }
        }
      }
    }

    // stage ('Clonage repo GIT') {
    //   steps {
    //     checkout([
    //       $class: 'GitSCM',
    //       branches: [[name: '*/develop']],
    //       doGenerateSubmoduleConfigurations: false,
    //       extensions: [],
    //       submoduleCfg: [],
    //       userRemoteConfigs: [[credentialsId: '5ac9ae46-07af-42bb-8431-f9d43b564885', url: 'https://github.com/gui-caliban/ansible.git']]
    //     ])
    //   }
    // }

    stage ('Invoke Ansible Playbook install_wordpress.yml on ${ansible_environment} plateform') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'install_wordpress.yml',
          inventory: 'inventories/${ansible_environment}/hosts',
          extras: '${LOG_LEVEL}'
        )
      }
    }
  }
}
