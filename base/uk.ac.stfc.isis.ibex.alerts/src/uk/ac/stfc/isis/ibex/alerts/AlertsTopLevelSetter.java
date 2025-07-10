/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.alerts;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * This class sets the alerts control on the blocks within the server.
 */
public class AlertsTopLevelSetter {
    private final Writable<String> messageSetter;
    private final Writable<String> emailsSetter;
    private final Writable<String> mobilesSetter;


    /**
     * Creates a setter for the top level alert details. The PVs are created at this time as
     * they need some time to connect.
     *
     * @param alertsServer
     *            The server object that creates the PVs for the alerts control.
     */
    public AlertsTopLevelSetter(AlertsServer alertsServer) {
    	messageSetter = alertsServer.setMessage();
        emailsSetter = alertsServer.setEmails();
        mobilesSetter = alertsServer.setMobiles();  
	}

    /**
     * Set the message on alert.
     *
     * @param message the message to set on the alert
     */
    public void setMessage(String message) {
    	messageSetter.uncheckedWrite(message);
	}

    /**
     * Set the mobiles on the alert.
     *
     * @param mobiles the mobiles to set on the alert
     */
    public void setMobiles(String mobiles) {
        mobilesSetter.uncheckedWrite(mobiles);
	}

    /**
     * Set the emails on the alert.
     *
     * @param emails the emails to set on the alert
     */
    public void setEmails(String emails) {
        emailsSetter.uncheckedWrite(emails);
	}
}
