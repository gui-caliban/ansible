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

    stage ('Invoke Ansible Playbook install_wordpress.yml') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'install_wordpress.yml',
          inventory: 'inventories/hosts',
          extras: '${VERBOSE}'
        )
      }
    }
  }
}
