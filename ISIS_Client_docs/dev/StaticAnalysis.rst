**************************
Java Static Analysis Tools
**************************

We are currently making use of 3 static analysis tools to help ensure the quality of our code: `PMD <http://pmd.sourceforge.net/>`_, `FindBugs <http://findbugs.sourceforge.net/>`_, and `CheckStyle <http://checkstyle.sourceforge.net/>`_. These particular tools are mostly intended for use with Java programs and as such our main use for them is the Eclipse GUI project, though they are also used on some Java code in the main EPICS project (specifically the IOC log server).

All of these tools have maven plugins which allow the analyses to be run as part of the maven build process, with the results being output to an XML file. Additionally, they all have Jenkins plugins which can consume the XML results files, producing reports and graphs within Jenkins and allowing you to track code quality trends over time. The Jenkins reports list all the specific violations which can be categorised in a number of ways and are listed with file name and line number as well as a detailed description of the problem.

These tools also have plugins for Eclipse, which allow you to view their results of their analyses within the Eclipse IDE. To install them, open the Eclipse marketplace (Help > Eclipse Marketplace) and search for the plugins (they should be the first result in each case).

----------
Checkstyle
----------

Checkstyle is designed to enforce a set of highly configurable coding standards. It supports a very large number of rules including ones relating to naming conventions, annotations, javadoc comments, poor coding practices, etc. That rules that Checkstyle will check for violations of may be configured in an XML file. 

The style rules imposed by Checkstyle are very demanding and it may be helpful to consider them as guidelines rather than as hard rules. At our first analysis of the Eclipse GUI project, Checkstyle showed over 30000 errors and it probably isn't worth the effort to fix all of these. However moving forward, when writing new code, it is a good idea to make at least some effort to conform to the Checkstyle 'rules'.

Checkstyle configuration is done by XML file called 'checkstyle.xml' which should be located in the same directory as the POM for all projects that are using Checkstyle. The rule set used is the default Sun ruleset with some rules disabled (by commenting them out in the XML file). In order to reduce the number of needless Checkstyle warnings, the following specific rules, which are enabled by default, have been disabled:

* ``FileTabCharacterCheck`` - using a tab character anywhere in the code
* ``RegexpSinglelineCheck`` - line has trailing white space.
* ``LineLengthCheck`` - more than 80 characters in a line

---
PMD
---

PMD focuses on potential coding problems such as unused or suboptimal code, code size and complexity, and good coding practices. Some typical rules include "Empty If Statement", "Broken Null Check", "Avoid Deeply Nested If Statements", "Switch Statements Should Have Defaults", etc. PMD will produce far fewer warnings/errors than checkstyle, however they are typically far more important, indicating technical problems with the code rather than merely stylistic ones, and should therefore be fixed whenever possible/sensible.

--------
FindBugs
--------

FindBugs detects similar types of problems as PMD, though it can detect many additional serious problems such as potential null pointer exceptions, infinite loops, and unintentional access of the internal state of an object (to name a few). Unlike PMD, which checks through the source code, FindBugs actually checks through the applications bytecode for potential issues. FindBugs tends to find a smaller number of more important issues.

For both PMD and FindBugs, there are certain rules which, while generally sensible, may not apply to the project at hand. For example, on the Eclipse GUI project, FindBugs throws up a lot of problems involving writing to a static member from an instance method, however the Activator class in most Eclipse plugins make use of this construct.

-----
Maven
-----

The tools may be run as part of a maven build by including them as plugins in the project's pom.xml. In the case of the Eclipse GUI, each plugin is included in the 'tycho.parent' pom.xml file, and is therefore used in every other project (Eclipse plugin) during the build.

* `FindBugs maven plugin <http://mojo.codehaus.org/findbugs-maven-plugin/>`_
* `CheckStyle maven plugin <http://maven.apache.org/plugins/maven-checkstyle-plugin/>`_
* `PMD maven plugin <http://maven.apache.org/plugins/maven-pmd-plugin/>`_


-------
Jenkins
-------

