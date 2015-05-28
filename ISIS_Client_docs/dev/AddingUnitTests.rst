**************************
Adding Unit Tests (JUnit)
**************************

It is relatively simple to add unit tests for a plugin in such a way that maven can run them as part of the build.

Here are the steps required in Eclipse:

#. Create a new Fragment Project
    * File > New > Project... > Plug-in Development > Fragment Project
    * Set the project name to <the full name of the plugin to test>.tests
    * Click "Next"
    * Click the "Browse" button next to "Plug-in ID" 
    * Select the plugin to test and click "OK"
    * Finish
    
#. In the newly created plugin, add a new Package with the same name as the plugin or something equally sensible.
    * Select the plugin
    * File > New > Package
    * Enter the name and click "Finish"
    
#. In the new Package create a class for adding test
    * Select the Package
    * File > New > Class
    
#. Add tests to the class

#. Convert the new plugin to a Maven project
    * Right-click on the plugin and select Configure > Convert to Maven Project
    * Click "Finish"
    
#. Add the new plugin to the Parent POM
    * Select the parent pom.xml file
    * On the overview tab click "Add..." under the Modules section
    * Select the new plugin from the list
    * Enable the "Update POM parent section in selected projects" option and click "OK"
    * Save it
    
#. Edit the plugin pom.xml file
    * Select the pom.xml file
    * Open the pom.xml tab
    * Change the packaging to eclipse-test-plugin
    * Remove the build section
    * Remove the groupID and version entries outside of parent
    * Save it
    
#. Running the Maven build should now also run the tests

----------
Examples
----------

An example test plugin POM::

    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <artifactId>tychodemo.bundle.tests</artifactId>
      <packaging>eclipse-test-plugin</packaging>
      <parent>
            <groupId>tychodemo</groupId>
            <artifactId>parent</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <relativePath>../tychodemo.parent</relativePath>
      </parent>
    </project>

