
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

package uk.ac.stfc.isis.ibex.motor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTableSettings;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTable;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Motors extends Plugin {
	private static Motors instance;
	private static BundleContext context;
	
	/** The number of motor tables to show. */
	private static final int NUMBER_MOTOR_TABLES = 3;
	/** The number of controllers (crates) in each table. */
	private static final int NUMBER_CONTROLLERS = 8;
	/** The number of motors for each controller. */
	private static final int NUMBER_MOTORS = 8;

    public static Motors getInstance() { 
    	if (instance == null) {
    		instance = new Motors();
    	}
    	return instance;
    }

	private MotorsTableSettings settingsModel = new MotorsTableSettings();
    public MotorsTableSettings getMotorSettingsModel() {
    	return settingsModel;
    }
    
    private List<MotorsTable> motorsTableList = new ArrayList<MotorsTable>();
    
	public Motors() {
		super();
		instance = this;
		
		for (int i = 0; i < NUMBER_MOTOR_TABLES; i++) {
			int controllerStart = i * NUMBER_MOTORS + 1;
			motorsTableList.add(new MotorsTable(Instrument.getInstance(), NUMBER_CONTROLLERS, NUMBER_MOTORS, controllerStart));
		}
	}
    
	/**
	 * Getter for list of all the motor tables.
	 * @return List of motor tables
	 */
	public List<MotorsTable> getMotorsTablesList() {
		return motorsTableList;
	}
			
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Motors.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Motors.context = null;
		for (MotorsTable motorsTable : motorsTableList) {
			motorsTable.close();
		}
	}

}
