#!groovy​

node ('windows && ibex_gui'){

	stage('Checkout') {
		checkout scm
	}

   stage('Preparation') { 

   }
   stage('Build') {
            bat '''
            cd build
            jenkins_build.bat"
            '''
 
   }
   stage('Test') {
        echo 'Testing..'
        junit '**/surefire-reports/TEST-*.xml'
    }
}
