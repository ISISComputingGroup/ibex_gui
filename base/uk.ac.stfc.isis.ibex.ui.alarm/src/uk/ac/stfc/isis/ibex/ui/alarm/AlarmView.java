
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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.csstudio.alarm.beast.ui.alarmtree.Activator;
import org.csstudio.alarm.beast.ui.alarmtree.GUI;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;


/**
 * The Class AlarmView which is the view which contains the alarm tree.
 */
public class AlarmView extends ViewPart {
	
    final public static String ID = "uk.ac.stfc.isis.ibex.ui.alarm"; //$NON-NLS-1$

    private AlarmClientModel model;

    private GUI gui = null;

    @Override
    public void createPartControl(final Composite parent)
    {
        try {
			model = AlarmClientModel.getInstance();
		} catch (Exception e) {
            Logger.getLogger(Activator.ID).log(Level.SEVERE, "Cannot load alarm model", e); //$NON-NLS-1$
		}
        // Have model, create GUI
        gui = new GUI(parent, model, getViewSite());
        IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
        model.setConfigurationName("Instrument", null);
        toolbar.add(new RefreshAction());
    }

    @Override
    public void setFocus()
    {
        if (gui != null)
            gui.setFocus();
    }
}
