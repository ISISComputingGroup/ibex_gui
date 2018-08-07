
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

import java.util.Objects;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.INamed;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class to hold information about the state of an IOC.
 */
public class IocState extends ModelObject implements Comparable<IocState>, INamed {
    
    /**
     * Note: instances of this class are recreated each time the configuration changes.
     * Need to make sure that there are no references to this class left over after a 
     * configuration change, otherwise will get a memory leak. Registering an observer
     * in an observable means you WILL have a reference back from the observable to this
     * class.
     */
    private static final ForwardingObservable<Configuration> CURRENT_CONFIG_OBSERVABLE = 
            Configurations.getInstance().server().currentConfig();

    private final String name;

    private boolean isRunning;
    private final String description;

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
    public IocState(String name, boolean isRunning, String description) {
        this.name = name;
        this.isRunning = isRunning;
        this.description = description;
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
     * Gets whether or not the IOC is in the current configuration. The result is calculated
     * at run-time so that it is as up to date as possible with the actual configuration
     * being used. Can't easily use listeners here because it will cause a memory leak (see
     * javadoc at top of class)
     * 
     * @return true if it is in the current configuration; false otherwise.
     */
    public boolean getInCurrentConfig() {
        return CURRENT_CONFIG_OBSERVABLE.getValue().getIocs().stream()
                .anyMatch(ioc -> Objects.equals(ioc.getName(), name));
    }

    @Override
    public int compareTo(IocState iocState) {
        return name.compareTo(iocState.getName());
    }
}
