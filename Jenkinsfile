pipeline {

  // agent defines where the pipeline will run.
  agent {  
    label "ibex_gui"
  }
  
  triggers {
    pollSCM('H/2 * * * *')
  }
  
  stages {  
    stage("Checkout") {
      steps {
        git 'https://github.com/ISISComputingGroup/ibex_gui.git'
      }
    }
    
    stage("Build") {
      steps {
        bat '''
            cd build
            set DEPLOY=YES
            jenkins_build.bat"
            '''
      }
    }
    
    stage("Unit Tests") {
      steps {
        junit '**/surefire-reports/TEST-*.xml'
      }
    }
    
    stage("Checkstyle") {
      steps {
        archiveCheckstyleResults()
      }
    }
    
    stage("Doxygen") {
      steps {
        bat '''
            "C:\\Program Files\\doxygen\\bin\\doxygen.exe" build\\ibex_gui_doxy.config
            '''
      }
    }
  }
  
  post {
    failure {
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: '', sendToIndividuals: true])
    }
  }
  
  // The options directive is for configuration that applies to the whole job.
  options {
    buildDiscarder(logRotator(numToKeepStr:'10'))
    timeout(time: 60, unit: 'MINUTES')
    disableConcurrentBuilds()
  }
}

def archiveCheckstyleResults() {
    step([$class: "CheckStylePublisher",
          canComputeNew: false,
          defaultEncoding: "",
          healthy: "",
          pattern: "**/target/checkstyle-result.xml",
          unHealthy: ""])
}
