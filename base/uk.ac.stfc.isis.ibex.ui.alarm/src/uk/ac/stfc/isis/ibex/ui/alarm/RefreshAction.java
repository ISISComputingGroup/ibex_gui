 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.alarm;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * This class defines the properties and behaviour of the refresh button which
 * has been added to the Alarms perspective.
 */
public class RefreshAction extends Action {

    /**
     * Logger.
     */
    private static final Logger LOG = IsisLog.getLogger(RefreshAction.class);

    /**
     * Tooltip text of the refresh button.
     */
    private static final String REFRESH_BUTTON_TOOLTIP_TEXT = "Refresh this view";

    /**
     * Location of the refresh button's image.
     */
    private static final String REFRESH_ICON_LOCATION = "icons/refresh.png";

    /**
     * Creates the button and sets it's image and tooltip text.
     */
    public RefreshAction() {
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(AlarmView.ID, REFRESH_ICON_LOCATION));
        setToolTipText(REFRESH_BUTTON_TOOLTIP_TEXT);
    }

    /**
     * Called when the refresh button is pressed.
     */
    @Override
    public void run() {
        LOG.info("Manual reload of alarms view triggered.");
        Alarm.getInstance().reload();
    }
}
