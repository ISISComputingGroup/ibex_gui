***************
Getting Started 
***************

We have two books which should serve as a reasonable introduction to the Eclipse RCP platform: 'Eclipse Rich Client Platform' by McAffer, Lemieux and Aniszczyk, and 'Eclipse 4 Application Development' by Vogel. Most of the contents of the latter book can be found on the `Vogella website <http://www.vogella.com/tutorials/EclipseRCP/article.html>`_ along with a large number of additional Eclipse RCP and JFace `tutorials <http://www.vogella.com/tutorials/eclipse.html>`_.

Note that the most recent version of Eclipse RCP is v4, however we are using v3 for our control system software.


------------------------
Building Our GUI Locally
------------------------

These are the steps needed to configure Eclipse so that the Client/GUI can be edited and built locally:

#. Check out source code from: https://svn.isis.rl.ac.uk/CSStudio/trunk/ISIS/ (e.g. to C:\\Instrument\\Dev\\Client)
#. Create a workspace directory  (e.g. C:\\Instrument\\Dev\\Client\\workspace)
#. Start Eclipse IDE and select the workspace you created
#. From the menu bar choose: File->Import->General->Existing Projects into Workspace. Choose "Select root directory" and browse to find the plug-ins and features that were downloaded. (e.g. C:\\Instrument\\Dev\\Client\\ISIS). Choose "Finish".
#. Expand the target platform folder (labelled as ``org.csstudio.isis.targetplatform``), double click on the target file and choose "Set as Target Platform". This may take some time as parts of CSStudio are downloaded. It may also be required to update the Locations in use should some packages appear to be missing.
#. To run the application from within Eclipse: open "repository.product" from the ``org.csstudio.isis.tycho.repository`` folder, select "Launch an Eclipse application"