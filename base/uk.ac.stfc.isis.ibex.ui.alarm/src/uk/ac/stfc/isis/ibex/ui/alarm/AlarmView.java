
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

package uk.ac.stfc.isis.ibex.ui.alarm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.phoebus.applications.alarm.client.AlarmClient;
import org.phoebus.applications.alarm.ui.tree.AlarmTreeView;

import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;


/**
 * The Class AlarmView which is the view which contains the alarm tree.
 */
public class AlarmView extends ViewPart {

	/**
	 * The ID for this class.
	 */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.alarm"; //$NON-NLS-1$

    @Override
    public void createPartControl(final Composite parent) {
    	FXCanvas fxCanvas = new FXCanvas(parent, SWT.NONE);
    	fxCanvas.setScene(new Scene(new AlarmTreeView(new AlarmClient("127.0.0.1:12345", "kafka_topic", null))));
    }
	
    @Override
    public void setFocus() {
    	// noop
    }
}
