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
    timeout(time: 60, unit: 'MINUTES')
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
        echo "Branch: ${env.BRANCH_NAME}"
        checkout scm
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
            echo "git commit: ${env.GIT_COMMIT}"
            echo "git branch: ${env.BRANCH_NAME} ${env.GIT_BRANCH}"
            if (env.BRANCH_NAME.startsWith("Release")) {
                env.IS_RELEASE = "YES"
                env.IS_DEPLOY = "NO"
                env.IS_E4 = "YES"
            }
            else if (env.GIT_BRANCH == "origin/master_E3_maint") {
                env.IS_RELEASE = "NO"
                env.IS_DEPLOY = "YES"
                env.IS_E4 = "NO"
            }
            else if (env.GIT_BRANCH == "origin/master") {
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
      }
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
