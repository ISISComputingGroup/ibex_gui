#!groovy

pipeline {

  // agent defines where the pipeline will run.
  agent {  
    label "ndhspare53"
  }
  
  triggers {
    pollSCM('H/2 * * * *')
  }
  
  stages {  
    stage("Checkout") {
      steps {
        echo "Branch: ${env.BRANCH_NAME}"
        checkout scm
      }
    }
    
    stage("Build") {
      steps {
        script {
            env.GIT_COMMIT = bat(returnStdout: true, script: '@git rev-parse HEAD').trim()
            env.GIT_BRANCH = bat(returnStdout: true, script: '@git rev-parse --abbrev-ref HEAD').trim()
            echo "git commit: ${env.GIT_COMMIT}"
            echo "git branch: ${env.GIT_BRANCH}"
            if (env.GIT_BRANCH.startsWith("Release")) {
                env.IS_RELEASE = "YES"
            }
            else {
                env.IS_RELEASE = "NO"
            }
        }
        
        bat '''
            cd build
            set DEPLOY=${env.IS_RELEASE}
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
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: 'icp-buildserver@lists.isis.rl.ac.uk', sendToIndividuals: true])
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
          unHealthy: "",
          usePreviousBuildAsReference: true])
}
