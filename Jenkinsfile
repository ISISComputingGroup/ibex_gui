#!groovy

pipeline {

  // agent defines where the pipeline will run.
  agent {  
    label "ndw1757"
  }
  
  triggers {
    pollSCM('H/2 * * * *')
  }
  
  // The options directive is for configuration that applies to the whole job.
  options {
    buildDiscarder(logRotator(numToKeepStr:'10'))
    timeout(time: 240, unit: 'MINUTES')
    disableConcurrentBuilds()
    timestamps()
	skipDefaultCheckout(true)
    office365ConnectorWebhooks([[
                    name: "Office 365",
                    notifyBackToNormal: true,
                    startNotification: false,
                    notifyFailure: true,
                    notifySuccess: false,
                    notifyNotBuilt: false,
                    notifyAborted: false,
                    notifyRepeatedFailure: true,
                    notifyUnstable: true,
                    url: "${env.MSTEAMS_URL}"
            ]]
    )
  }

  stages {  
    stage("Checkout") {
      steps {
        timeout(time: 2, unit: 'HOURS') {
          retry(5) {
            echo "Branch: ${env.BRANCH_NAME}"
            checkout scm
	  }
	}
      }
    }
	  
    stage("Build") {
      steps {
        script {
            // env.BRANCH_NAME is only supplied to multi-branch pipeline jobs
            if (env.BRANCH_NAME == null) {
                env.BRANCH_NAME = ""
	    }
            env.GIT_BRANCH = scm.branches[0].name
            env.GIT_COMMIT = bat(returnStdout: true, script: '@git rev-parse HEAD').trim()
            echo "git commit: ${env.GIT_COMMIT}"
            echo "git branch: ${env.BRANCH_NAME} (${env.GIT_BRANCH})"
            if (env.BRANCH_NAME.startsWith("Release")) {
                env.IS_RELEASE = "YES"
                env.IS_DEPLOY = "NO"
                env.IS_E4 = "YES"
            }
            else if (env.GIT_BRANCH == "refs/heads/master_E3_maint") {
                env.IS_RELEASE = "NO"
                env.IS_DEPLOY = "YES"
                env.IS_E4 = "NO"
            }
            else if (env.GIT_BRANCH == "refs/heads/master") {
                env.IS_RELEASE = "NO"
                env.IS_DEPLOY = "YES"
                env.IS_E4 = "YES"
            }
            else {
                env.IS_RELEASE = "NO"
                env.IS_DEPLOY = "NO"
                env.IS_E4 = "YES"
            }
        }
        
        bat """
            cd build
            set GIT_COMMIT=${env.GIT_COMMIT}
            set GIT_BRANCH=${env.BRANCH_NAME}
            set RELEASE=${env.IS_RELEASE}
            set DEPLOY=${env.IS_DEPLOY}
            jenkins_build.bat
            """
      }
    }
    
    stage("OPI Checker") {
      steps {
        bat """
            set PYTHON3=C:\\Instrument\\Apps\\Python3\\python.exe
            %PYTHON3% .\\base\\uk.ac.stfc.isis.ibex.opis\\check_opi_format.py -strict 
        """
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
    always {
	    archiveArtifacts artifacts: 'build/*.log', caseSensitive: false
	    junit '**/surefire-reports/TEST-*.xml,**/test-reports/TEST-*.xml'
      logParser ([
            projectRulePath: 'parse_rules',
            parsingRulesPath: '',
            showGraphs: true, 
            unstableOnWarning: true, 
            useProjectRule: true,
        ])
    }
    cleanup {
            echo "***"
            echo "*** Any Office365connector Matched status FAILURE message below means"
            echo "*** an earlier Jenkins step failed not the Office365connector itself"
            echo "*** Search log file for  ERROR  to locate true cause"
            echo "***"
    }
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
