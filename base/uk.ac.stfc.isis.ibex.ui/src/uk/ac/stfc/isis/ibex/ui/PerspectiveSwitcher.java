
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

package uk.ac.stfc.isis.ibex.ui;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.LogCounter;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class PerspectiveSwitcher {

	private static final Logger LOG = IsisLog.getLogger(PerspectiveSwitcher.class);
	
	private final IWorkbench workbench;	   
	private final IWorkbenchWindow workbenchWindow;

	private final Display display = Display.getDefault();

	private static LogCounter counter = Log.getInstance().getCounter();
	
	private static final Runnable DO_NOTHING = new Runnable() {	
		@Override
		public void run() {	
			// Do nothing
		}
	};
	
	public static final String LOG_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.ui.log.perspective";
	
	public PerspectiveSwitcher(IWorkbench workbench, IWorkbenchWindow workbenchWindow) {
		this.workbench = workbench;
		this.workbenchWindow = workbenchWindow;
	}
		
	public Runnable switchTo(final String perspectiveID) {
		return isCurrentPerspective(perspectiveID) 
					? DO_NOTHING : makeTheSwitch(perspectiveID);
    }
	
	private Runnable makeTheSwitch(final String perspectiveID) {
		return runInDisplayThread(new Runnable() {
    		public void run() {
    			try {
    				LOG.info("Switching to: " + perspectiveID);
    				
    				//Restart counter if switching to or from IOC log
    				if (isLogPerspective(perspectiveID) || isCurrentPerspective(LOG_PERSPECTIVE_ID)) {
						counter.start();
					}
    					
                	workbench.showPerspective(perspectiveID, workbenchWindow);
                } catch (WorkbenchException e) {
                	LOG.warn("Workbench Error: " + e.getMessage());
                }
            }
        }); 		
	}	
	
	private Runnable runInDisplayThread(final Runnable runnable) {
		return new Runnable() {		
			@Override
			public void run() {
				display.syncExec(runnable);
			}
		};
	}
	
	private boolean isLogPerspective(String perspectiveID) {
		return perspectiveID.equals(LOG_PERSPECTIVE_ID);
	}
	
	private boolean isCurrentPerspective(String perspectiveID) {
		IPerspectiveDescriptor activePerspective = workbenchWindow.getActivePage().getPerspective();		
	    return activePerspective != null && activePerspective.getId().equals(perspectiveID); 
	}
}
