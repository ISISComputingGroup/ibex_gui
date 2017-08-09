
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private static final int MIN_WINDOW_HEIGHT = 800;
    private static final int MIN_WINDOW_WIDTH = 1100;
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
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
    }

    /**
     * Attempts to get previous window settings, otherwise returning sensible
     * defaults.
     * 
     * @return An object with parameters for width, heigh, x position, y
     *         poistion and maximised flag
     */
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
            System.out.println("No workbench.xml - using default initial window sizes");
        } catch (IOException e) {
            e.printStackTrace();
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
