
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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

/**
 * The workbench window advisor for the application.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private static final int MIN_WINDOW_HEIGHT = 800;
    private static final int MIN_WINDOW_WIDTH = 1100;
    
	private static final String DIALOG_BOX_TITLE = "IBEX is already running.";
	private static final String DIALOG_QUESTION = "It appears that IBEX client is already running. "
			+ "Are you sure you want to open another instance?";
	
	private static final String TEMP_PATH = Paths.get(new PreferenceSupplier().tempFilePath(), "instance.txt").toString();
	
	private static final Logger LOG = IsisLog.getLogger(ApplicationWorkbenchWindowAdvisor.class);
	
	private static final String MULTIPLE_INSTANCES_CHECKING_ERROR = "Exception encountered while checking for multiple instances.";
	private static final String ANOTHER_INSTANCE_LOG_INFO = "Another instance of IBEX found running.";
	private static final String TEMPORARY_FILE_LOCKED_ERROR = "Could not access temporary file due to a lock. Another process is "
			+ "refusing to release the lock.";
    
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
        
        try {
    		if (clearTempFile().length > 0) {
    			LOG.info(ANOTHER_INSTANCE_LOG_INFO);
    	        if (!MessageDialog.openQuestion(Display.getDefault().getActiveShell(), DIALOG_BOX_TITLE, DIALOG_QUESTION)) {
    	            shutDown = true;
    	            PlatformUI.getWorkbench().close();
    	        }
    		}
    		if (!shutDown) {
    			writeTempFile(new String[] {Long.toString(ProcessHandle.current().pid())}, true);
    		}
		} catch (Exception e) {
			LOG.error(MULTIPLE_INSTANCES_CHECKING_ERROR);
			e.printStackTrace();
		}
    }
    
    /**
     * This function compares contents of temporary file to the list of PIDs obtained from process list
     * and overwrites the file only with those that match.
     * @return New contents of temporary file as a string array.
     * @throws IOException - if any of the operations result in IOException.
     */
    String[] clearTempFile() throws IOException {
    	String[] current = readTempFile();
    	
        ArrayList<String> procs = new ArrayList<String>();
        ProcessHandle.allProcesses()
        .forEach(process -> procs.add(Long.toString(process.pid())));
        
        ArrayList<String> newContent = new ArrayList<String>();
        
        for (String l : current) {
        	for (String s : procs) {
        		if (l.equals(s)) {
        			newContent.add(l);
        			break;
        		}
        	}
        }
        String[] newContentArr = newContent.toArray(new String[newContent.size()]);
        writeTempFile(newContentArr, false);
        
        return newContentArr;
    }
    
    /**
     * Locks the temporary file. The lock is only needed in a rare edge case
     * where someone would fire multiple instances exactly at the same time,
     * creating write racing conditions.
     * @return Lock, or empty optional if it fails to acquire lock within reasonable time.
     * @throws IOException - if any of the operations result in IOException.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    Optional<FileLock> lockTemporaryFile() throws IOException {
    	
        /**
         * The lock is only needed in a rare edge case where someone would fire multiple
         * instances exactly at the same time, creating write racing conditions.
         */
    	
        FileChannel channel = FileChannel.open(Paths.get(TEMP_PATH), StandardOpenOption.WRITE);
        FileLock lock = null;

        Instant time = Instant.now();
        while (lock == null) {
        	try {
            	lock = channel.tryLock();
        	} catch (OverlappingFileLockException e) {
        		lock = null;
        	}
        	if (lock != null) {
        		return Optional.of(lock);
        	}
        	
        	/**
        	 * This is just a security measure in case something goes terribly bad. Realistically I/O
        	 * operation of another instance would only take milliseconds. This will prevent program
        	 * from hanging if that happens.
        	 */
        	long timeElapsed = Duration.between(time, Instant.now()).toSeconds();
        	if (timeElapsed > 3) {
        		LOG.error(TEMPORARY_FILE_LOCKED_ERROR);
        		break;
        	}
        }
    	
		return Optional.empty();
    }
    
    /**
     * Helper function for writing to temporary file.
     * @param lines Lines of text to write.
     * @param append When true the lines will be appended. When false, the file will be
     * Overwritten.
     * @throws IOException - if any of the operations result in IOException.
     */
    void writeTempFile(String[] lines, boolean append) throws IOException, NoSuchElementException {
    	createTempFile();
    	String content = "";
    	for (String line : lines) {
    		content += line + '\n';
    	}
    	
    	FileLock lock = lockTemporaryFile().get();
    	FileWriter writer = new FileWriter(TEMP_PATH, append);
        writer.write(content);
        
        if (lock != null) {
            lock.release();
        }
        writer.close();
    }
    
    /**
     * Helper function for getting info from temporary file.
     * @return Lines of text in the temporary file.
     * @throws IOException - if any of the operations result in IOException.
     */
    String[] readTempFile() throws IOException {
    	createTempFile();
		String content = Files.readString(Paths.get(TEMP_PATH));
		String[] lines = content.split("\n");
		return lines;
    }
    
    /**
     * Helper function for creating temporary file. Does nothing if file already exists.
     * @throws IOException - if any of the operations result in IOException.
     */
    void createTempFile() throws IOException {
    	try {
	        Files.createDirectories(Paths.get(TEMP_PATH).getParent());
	        Files.createFile(Paths.get(TEMP_PATH));
    	} catch (FileAlreadyExistsException e) { }
    }

    /**
     * Attempts to get previous window settings, otherwise returning sensible
     * defaults.
     *
     * @return An object with parameters for width, height, x position, y
     *         position and maximised flag
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
