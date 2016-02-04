
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

package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.alarm.AlarmCounter;
import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.LogCounter;
import uk.ac.stfc.isis.ibex.ui.UI;

/**
 * The perspective buttons used, with appropriate behaviours for IBEX.
 */
public class PerspectiveButton extends CLabel {
	
	protected static final Color FOCUSSED = SWTResourceManager.getColor(220, 235, 245);
	protected static final Color DEFOCUSSED = SWTResourceManager.getColor(247, 245, 245);
    protected static final Color ACTIVE = SWTResourceManager.getColor(120, 170, 210);

    private static final Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
    private static final Font ACTIVE_FONT = SWTResourceManager.getFont("Arial", 12, SWT.BOLD);
	
	protected static LogCounter logCounter = Log.getInstance().getCounter();
	protected static AlarmCounter alarmCounter = Alarm.getInstance().getCounter();
	
    private String buttonPerspective;

    /**
     * Main button behaviours for IBEX.
     * 
     * @param parent the parent of the button
     * @param perspective the perspective related to the button
     */
	public PerspectiveButton(Composite parent, final String perspective) {
		super(parent, SWT.SHADOW_OUT);
		
        this.buttonPerspective = perspective;

		addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseDown(MouseEvent e) {
				UI.getDefault().switchPerspective(perspective);
				mouseClickAction();
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				mouseDown(e);
			}
		});

		addMouseTrackListener(new MouseTrackAdapter() {			
			@Override
			public void mouseExit(MouseEvent e) {
                mouseExitAction();
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				mouseEnterAction();
			}
        });
		
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new IPerspectiveListener() {

            @Override
            public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
                //
            }

            @Override
            public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
                setBackground(setColourToUse());
            }
        });

        setBackground(DEFOCUSSED);
	}

    /**
     * Things to do when the mouse is clicked.
     */
	protected void mouseClickAction() {
		// Restart counting of log messages when not observing
		// the log message viewer
		logCounter.start();
	}
	
    /**
     * Set the background colour of the button when the mouse enters it.
     */
	protected void mouseEnterAction() {
        if (setColourToUse() == DEFOCUSSED) {
            setBackground(FOCUSSED);
        } else {
            setBackground(setColourToUse());
        }
	}
	
    /**
     * Set the background colour of the button when the mouse exits it.
     */
	protected void mouseExitAction() {
        setBackground(setColourToUse());
	}

    /**
     * The background of the button is dependent on whether it is the active
     * perspective or not. The font is updated as appropriate.
     * 
     * @return colour to use for the background
     */
    protected Color setColourToUse() {
        String currentPerspective = new PerspectiveModel().getCurrentPerspective();
        Color toUse = DEFOCUSSED;
        setFont(BUTTON_FONT);
        if (buttonPerspective.equals(currentPerspective)) {
            toUse = ACTIVE;
            setFont(ACTIVE_FONT);
        }
        return toUse;
    }
}
