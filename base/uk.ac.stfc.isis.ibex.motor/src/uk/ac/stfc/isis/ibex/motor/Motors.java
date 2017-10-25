
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

/**
 * The activator class controls the plug-in life cycle.
 */
public class Motors extends Plugin {
	private static Motors instance;
	
    /** The number of controllers to gather data for. */
    private static final int NUMBER_CONTROLLERS = 24;
	/** The number of motors for each controller. */
    public static final int NUMBER_MOTORS = 8;

    /**
     * Get the instance of this singleton.
     * 
     * @return The instance of this singleton.
     */
    public static Motors getInstance() {
    	return instance; 
    }
	
    private List<Controller> controllers = new ArrayList<Controller>();
    
    /**
     * The constructor that creates the motor tables.
     */
	public Motors() {
        super();
		instance = this;
		
        for (int i = 1; i < NUMBER_CONTROLLERS + 1; i++) {
            controllers.add(new Controller(i, NUMBER_MOTORS));
		}
	}
    
	/**
     * Getter for all the controllers.
     * 
     * @return List of controllers
     */
    public List<Controller> getControllers() {
        return controllers;
	}

}
