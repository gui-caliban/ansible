pipeline {

  agent any

	options {
		ansiColor("xterm")
	}

  parameters {
    choice(name: 'VERBOSE', choices: [' ', "-v", "-vv", "-vvv", "-vvvv"], description: "Choice verbosity on logs")
    choice(name: 'ansible_environment', choices: ['INT', 'DEV', 'PROD'], description: 'Environment where you want to play your pipeline')
  }

  stages {

  		stage ('Clonage repo GIT') {
  			steps {
  				checkout([
            $class: 'GitSCM',
            branches: [[name: '*/${GIT_BRANCH}']],
            doGenerateSubmoduleConfigurations: false,
            extensions: [],
            submoduleCfg: [],
            userRemoteConfigs: [[credentialsId: 'd35a7e7a-3b40-4772-a2a3-cc2795010ce1', url: 'https://github.com/gui-caliban/ansible.git']]
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
            inventory: 'inventories/${ansible_environment}/hosts',
            extras: '${VERBOSE}'
          )
        }
      }
    }
  }
