
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.alarm.AlarmCounter;
import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.LogCounter;
import uk.ac.stfc.isis.ibex.ui.UI;

public class PerspectiveButton extends CLabel {
	
	protected static final Color FOCUSSED = SWTResourceManager.getColor(220, 235, 245);
	protected static final Color DEFOCUSSED = SWTResourceManager.getColor(247, 245, 245);
	
	protected static LogCounter logCounter = Log.getInstance().getCounter();
	protected static AlarmCounter alarmCounter = Alarm.getInstance().getCounter();
	
	public PerspectiveButton(Composite parent, final String perspective) {
		super(parent, SWT.SHADOW_OUT);
		
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
		
		setBackground(DEFOCUSSED);
	}

	protected void mouseClickAction() {
		// Restart counting of log messages when not observing
		// the log message viewer
		logCounter.start();
	}
	
	protected void mouseEnterAction() {
		setBackground(FOCUSSED);
	}
	
	protected void mouseExitAction() {
		setBackground(DEFOCUSSED);
	}
}
