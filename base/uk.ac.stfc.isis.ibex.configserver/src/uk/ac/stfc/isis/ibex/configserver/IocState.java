
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
package uk.ac.stfc.isis.ibex.configserver;

import java.util.ArrayList;
import java.util.Collection;


import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.ConfigEditing;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.INamed;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.model.ModelObject;


/**
 * Class to hold information about the state of an IOC.
 *
 */
public class IocState extends ModelObject implements Comparable<IocState>, INamed {
	private final String name;
	private final boolean allowControl;

	private boolean isRunning;
	private String description;
	private String inCurrentConfig;
	
    /**
     * Instantiates a new IOC state.
     *
     * @param name
     *            the name
     * @param isRunning
     *            whether the IOC is running
     * @param description
     *            description of the IOC
     * @param allowControl
     *            whether the user is allowed control it           
     */
	public IocState(String name, boolean isRunning, String description, boolean allowControl) {
		this.name = name;
		this.isRunning = isRunning;
		this.description = description;
		this.allowControl = allowControl;
		bindInCurrentConfig();    
		
	}	
	
    /**
     * Copy constructor.
     *
     * @param other
     *            the value to copy
     */
	public IocState(IocState other) {
		this(other.name, other.isRunning, other.description, other.allowControl);
	}	

	@Override
    public String getName() {
		return name;
	}
	
    /**
     * 
     * @return True if it is running; False otherwise
     */
	public boolean getIsRunning() {
		return isRunning;
	}
	
    /**
     *
     * @param isRunning
     *            true if it is running; False if not running
     */
	public void setIsRunning(boolean isRunning) {
		firePropertyChange("isRunning", this.isRunning, this.isRunning = isRunning);
	}
	
    /**
     * Gets the description.
     *
     * @return the description
     */
	public String getDescription() {
		return description;
	}
	
    /**
     *
     * @return true if user is allowed to control it; false otherwise
     */
	public boolean getAllowControl() {
		return allowControl;
	}
	
	/**
    *Gets whether or not the IOC is in the current configuration.
    * 
    * @return "Yes" if it is in the current configuration; "No" otherwise.
    */
	public String getInCurrentConfig() {
	    
	    return this.inCurrentConfig;
	}
		
	/**
	*Binds setInCurrentConfig to the class constructor.
	* 
	*/
	public void bindInCurrentConfig() {
	    setInCurrentConfig();
	}	
		
	/**
    *Sets inCurrentConfig to "Yes" if the IOC is in the current configuration; "No" otherwise.
    * 
    */
	public void setInCurrentConfig() {
	    
	    ConfigServer server = Configurations.getInstance().server();
	    ConfigEditing edit = new ConfigEditing(server);	
        ForwardingObservable<EditableConfiguration> currentConfig = edit.currentConfig();
        
        Observer<EditableConfiguration> observer = new BaseObserver<EditableConfiguration>() {
            
            @Override
            public void onValue(EditableConfiguration value) {
                final EditableConfiguration config = value; 
                
                Collection<EditableIoc> addedIocs = new ArrayList<EditableIoc>();
                Collection<EditableIoc> availableIocs = new ArrayList<EditableIoc>();
                addedIocs = config.getAddedIocs();
                availableIocs = config.getAvailableIocs();
        
                for (EditableIoc ioc : addedIocs) {
           
                    if (ioc.getName().equals(name)) {
                        firePropertyChange("inCurrentConfig", inCurrentConfig, inCurrentConfig = "Yes");
                        
                    } 
                }
                for (EditableIoc ioc : availableIocs) {
                    if (ioc.getName().equals(name)) {
                        firePropertyChange("inCurrentConfig", inCurrentConfig, inCurrentConfig = "No");
                    }
                }
            }
        };	    
	    currentConfig.addObserver(observer); 	    
	}

	@Override
	public int compareTo(IocState iocState) {
		return name.compareTo(iocState.getName());
	}
}
