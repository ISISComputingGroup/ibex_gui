
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.e4.product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * The workbench window advisor for the application.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private static final int MIN_WINDOW_HEIGHT = 800;
    private static final int MIN_WINDOW_WIDTH = 1100;
    
	private static final String DIALOG_BOX_TITLE = "IBEX is already running.";
	private static final String DIALOG_QUESTION = "It appears that IBEX client is already running. "
			+ "Are you sure you want to open another instance?";
	
	private static final String TEMP_PATH = System.getProperty("user.home") + "\\AppData\\Local\\IBEX\\tmp";
	
	private static final Logger LOG = IsisLog.getLogger(ApplicationWorkbenchWindowAdvisor.class);
    
    /**
     * Setting this flag to true allows workbench to close without prompt.
     */
    protected boolean shutDown = false;

    /**
     * Constructor.
     * @param configurer the configurer
     */
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ActionBarAdvisor(configurer);
    }

    @Override
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(false);
    }

    @Override
    public void postWindowCreate() {
    	super.postWindowCreate();
        final Shell shell = getWindowConfigurer().getWindow().getShell();
        
        WindowLayout windowLayout = getPreviousWindowSettings();

        shell.setMinimumSize(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
        shell.setLocation(windowLayout.windowX, windowLayout.windowY);
        shell.setSize(windowLayout.windowWidth, windowLayout.windowHeight);
        shell.setMaximized(windowLayout.windowMaximised);
        
        // Create local application directory
        try {
			Files.createDirectories(Paths.get(TEMP_PATH));
		} catch (IOException e) {
			LOG.warn("Exception found while creating local directory.\n");
			e.printStackTrace();
		}
        
        // Check if this is not the only instance running
		File tempFolder = new File(TEMP_PATH);
		File[] files = tempFolder.listFiles();
		for (File f : files) {
			// Check if files are locked. This is only needed if deleteOnExit() didn't work (for example SIGKILL was sent)
			if (lockFile(f) != null) {
				LOG.warn("Unlocked temporary file found. It's possible last application exit was ungraceful. Deleting file.\n");
				f.delete();
			}
		}
		files = tempFolder.listFiles();
		if (files.length > 0) {
			LOG.info("Another instance of IBEX found running.\n");
	        if (!MessageDialog.openQuestion(Display.getDefault().getActiveShell(), DIALOG_BOX_TITLE, DIALOG_QUESTION)) {
	            shutDown = true;
	            PlatformUI.getWorkbench().close();
	            return;
	        }
		}
        
        // Create temporary file
		try {
			File tempFile = Files.createTempFile(Paths.get(TEMP_PATH), "ClientInstance", ".tmp").toFile();
			tempFile.deleteOnExit();
			lockFile(tempFile);
		} catch (IOException e1) {
			LOG.warn("Exception found while creating temporary file.\n");
			e1.printStackTrace();
		}
    }
    
    /**
     * This function will return file lock or null if file is already locked.
     * @param file File to lock.
     * @return
     */
    private FileLock lockFile(File file) {
		try {
		    FileChannel channel = FileChannel.open(file.toPath(),
		    		StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		    FileLock lock = channel.tryLock();
		    return lock;
		} catch (OverlappingFileLockException e1) {
			return null;
		} catch (IOException e) {
			LOG.warn("Exception found while locking file.\n");
		    e.printStackTrace();
		    return null;
		}
    }

    /**
     * Attempts to get previous window settings, otherwise returning sensible
     * defaults.
     *
     * @return An object with parameters for width, heigh, x position, y
     *         poistion and maximised flag
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private WindowLayout getPreviousWindowSettings() {
        int windowHeight = MIN_WINDOW_HEIGHT;
        int windowWidth = MIN_WINDOW_WIDTH;
        int windowX = 0;
        int windowY = 0;
        boolean windowMaximised = true;

        IPath workbenchXml;
        IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();

        workbenchXml = workspaceRoot.append(new Path(".metadata"))
                .append(new Path(".plugins"))
                .append(new Path("org.eclipse.ui.workbench"))
                .append("/workbench.xml");


        // This pattern picks out from <window height="800" maximized="true"
        // width="1100" x="0" y="0">
        Pattern patternMaximized = Pattern.compile(
                "<window height=\"(\\d+)\" maximized=\"([a-z]+)\" width=\"(\\d+)\" x=\"(-?\\d+)\" y=\"(-?\\d+)\">");

        // This pattern picks out from <window height="800" width="1100" x="0"
        // y="0">
        Pattern patternNotMaximized = Pattern
                .compile("<window height=\"(\\d+)\" width=\"(\\d+)\" x=\"(-?\\d+)\" y=\"(-?\\d+)\">");

        try {
            File file = new File(workbenchXml.toOSString());
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            boolean foundMatch = false;

            while (bufferedReader.ready() && !foundMatch) {
                String line = bufferedReader.readLine();

                Matcher matchMaximised = patternMaximized.matcher(line);
                Matcher matchNotMaximised = patternNotMaximized.matcher(line);

                if (matchMaximised.matches()) {
                    windowHeight = Integer.parseInt(matchMaximised.group(1));
                    windowMaximised = Boolean.parseBoolean(matchMaximised.group(2));
                    windowWidth = Integer.parseInt(matchMaximised.group(3));
                    windowX = Integer.parseInt(matchMaximised.group(4));
                    windowY = Integer.parseInt(matchMaximised.group(5));

                    foundMatch = true;
                } else if (matchNotMaximised.matches()) {
                    windowHeight = Integer.parseInt(matchNotMaximised.group(1));
                    windowMaximised = false;
                    windowWidth = Integer.parseInt(matchNotMaximised.group(2));
                    windowX = Integer.parseInt(matchNotMaximised.group(3));
                    windowY = Integer.parseInt(matchNotMaximised.group(4));

                    foundMatch = true;
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            IsisLog.getLogger(getClass()).info("No workbench.xml - using default initial window sizes");
        } catch (IOException e) {
            LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
        }

        return new WindowLayout(windowHeight, windowWidth, windowX, windowY, windowMaximised);
    }

    private class WindowLayout {
        public int windowHeight;
        public int windowWidth;
        public int windowX;
        public int windowY;
        public boolean windowMaximised;

        WindowLayout(int height, int width, int x, int y, boolean maximised) {
            windowHeight = height;
            windowWidth = width;
            windowX = x;
            windowY = y;
            windowMaximised = maximised;
        }
    }
}
