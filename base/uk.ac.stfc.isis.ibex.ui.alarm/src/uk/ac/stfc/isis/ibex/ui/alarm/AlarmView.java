
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

import org.apache.logging.log4j.Logger;
import org.csstudio.alarm.beast.ui.alarmtree.AlarmTreeView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.logger.IsisLog;


/**
 * The Class AlarmView which is the view which contains the alarm tree.
 */
public class AlarmView extends AlarmTreeView {

    private static final Logger LOG = IsisLog.getLogger(AlarmView.class);

    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.alarm"; //$NON-NLS-1$

    private static final String REFRESH_BUTTON_TOOLTIP_TEXT = "Refresh this view";
    private static final String REFRESH_ICON_LOCATION = "icons/refresh.png";
	
    /**
     * Instantiates a new alarm view.
     */
	public AlarmView() {
		setPartName("TestView");
	}

	@Override
	public void setFocus() {
        // Do nothing.
	}

    @Override
    public void createPartControl(final Composite parent) {
        super.createPartControl(parent);

        IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
        toolbar.add(new Separator());
        toolbar.add(new RefreshAction());
    }

    private class RefreshAction extends Action {

        RefreshAction() {
            setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ID, REFRESH_ICON_LOCATION)); // $NON-NLS-1$
            setToolTipText(REFRESH_BUTTON_TOOLTIP_TEXT);
        }

        /**
         * Called when the refresh button was pressed.
         */
        @Override
        public void run() {
            LOG.info("Manual reload of alarms view triggered.");
            Alarm.getInstance().reload();
        }

    }
}
