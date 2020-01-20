pipeline {

  agent any

options {
ansiColor("xterm")
}

stages {

stage ('Clonage repo GIT') {
steps {
checkout([
          $class: 'GitSCM',
          branches: [[name: '*/develop']],
          doGenerateSubmoduleConfigurations: false,
          extensions: [],
          submoduleCfg: [],
          userRemoteConfigs: [[credentialsId: '5ac9ae46-07af-42bb-8431-f9d43b564885', url: 'https://github.com/gui-caliban/ansible.git']]
        ])
       }
    }

    stage ('Invoke Ansible Playbook install_wordpress.yml on INT plateform') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'install_wordpress.yml',
          inventory: 'inventories/INT_home/hosts',
          extras: '${VERBOSE}'
        )
      }
    }

    stage ('Invoke Ansible Playbook install_wordpress.yml on PROD plateform') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'install_wordpress.yml',
          inventory: 'inventories/PROD_home/hosts',
          extras: '${VERBOSE}'
        )
      }
    }
  }
}
