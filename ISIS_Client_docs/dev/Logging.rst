*******
Logging
*******

We are using the `log4j2 <http://logging.apache.org/log4j/2.x/manual/index.html>`_ framework for our logging.

------------------------
Using the Logger in Code
------------------------
To use the logger in your plugin/class, first add the packages ``org.csstudio.isis.logger`` and ``org.apache.logging.log4j`` to the plugin's imported packages list. Next have the class get a static instance of the logger::

	private static Logger logger = IsisLog.getLogger(MyClass.class);


To do this you will need to import two classes::

	import org.csstudio.isis.logger.IsisLog;
	import org.apache.logging.log4j.Logger;


The argument to the static getLogger() method can be either a Class type or a string. If you wanted all the messages from a given plugin to be listed as coming from the same source, you could supply the name of the plugins activator class (for example) to the getLogger() call in every class in the plugin.

Finally to actually log a message, simply call the appropriate method on your ``Logger`` object::

	logger.warn("A minor issue has occurred...");


The logger has a separate method for each level of logging listed below.

--------------
Logging levels
--------------
In the preferences page, you may configure the logging level. Selecting a particular level will cause all log messages of that level and above that are generated in the program to be logged to file. In order of increasing severity, the levels are::

	TRACE < DEBUG < INFO < WARN < ERROR < FATAL

