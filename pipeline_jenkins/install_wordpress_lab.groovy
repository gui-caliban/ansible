pipeline {

  agent any

	options {
		ansiColor("xterm")
	}

  parameters {
    choice(name: 'VERBOSE', choices: [' ', "-v", "-vv", "-vvv", "-vvvv"], description: "Choice verbosity on logs")
    choice(name: 'ansible_environment', choices: ['INT_Ubuntu', 'INT_CentOS'], description: 'Environment where you want to play your pipeline')
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
          sh '''
               ansible-galaxy install -r requirements.yml --roles-path roles/ --force
               tree
          '''
  			}
      }

      stage ('Invoke Ansible Playbook install_wordpress.yml') {
        environment {
          ANSIBLE_FORCE_COLOR = true
        }
        steps {
          withCredentials([string(credentialsId: 'VAULT_PASSWORD', variable: 'VAULT_PASSWORD')]) {
            ansiblePlaybook (
              colorized: true,
              playbook: 'install_wordpress.yml',
              inventory: 'inventories/${ansible_environment}/hosts',
              extras: '${VERBOSE} --vault-password-file=.vault_pass.py'
            )
          }
        }
      }
    }
  }
