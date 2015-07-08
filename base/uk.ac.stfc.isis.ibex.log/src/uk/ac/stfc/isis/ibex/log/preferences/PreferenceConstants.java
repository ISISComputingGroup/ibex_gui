
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.log.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public final class PreferenceConstants {
	public static final String P_JMS_ADDRESS = "jmsAddress";	
	public static final String P_JMS_PORT = "jmsPort";
	public static final String P_JMS_TOPIC = "jmsTopic";
	public static final String P_SQL_ADDRESS = "sqlAddress";
	public static final String P_SQL_PORT = "sqlPort";
	public static final String P_SQL_USERNAME = "sqlUsername";
	public static final String P_SQL_PASSWORD = "sqlPassword";
	public static final String P_SQL_SCHEMA = "sqlSchema";	
	public static final String P_MINOR_MESSAGE = "msgMinor";
	
	public static final String DEFAULT_JMS_ADDRESS = "localhost";
	public static final Integer DEFAULT_JMS_PORT = 61616;
	public static final String DEFAULT_JMS_TOPIC = "iocLogs";
	public static final String DEFAULT_SQL_ADDRESS = "localhost";
	public static final Integer DEFAULT_SQL_PORT = 3306;
	public static final String DEFAULT_SQL_USERNAME = "msg_report";
	public static final String DEFAULT_SQL_PASSWORD = "$msg_report";
	public static final String DEFAULT_SQL_SCHEMA = "msg_log";
	public static final Boolean DEFAULT_MINOR_MESSAGE = false;	
	
	private PreferenceConstants() { }
}
