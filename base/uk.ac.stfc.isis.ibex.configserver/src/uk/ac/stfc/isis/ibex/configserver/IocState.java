
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any kind,
 * either expressed or implied, including but not limited to the implied
 * warranties of merchantability and/or fitness for a particular purpose.
 */
package uk.ac.stfc.isis.ibex.configserver;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.epics.observing.INamed;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class to hold information about the state of an IOC.
 *
 * Implementation note: this class represents an immutable instance of the state
 * of an IOC at a particular time.
 */
public class IocState extends ModelObject implements Comparable<IocState>, INamed {

    private final ConfigServer configServer;
    private final String name;
    private final boolean isRunning;
    private final String description;

    /**
     * Instantiates a new IOC state.
     *
     * @param configServer
     *            the Config Server
     * @param name
     *            the name
     * @param isRunning
     *            whether the IOC is running
     * @param description
     *            description of the IOC
     */
    public IocState(ConfigServer configServer, String name, boolean isRunning, String description) {
        this.configServer = configServer;
        this.name = name;
        this.isRunning = isRunning;
        this.description = description;
    }

    /**
     * Gets the name of the IOC corresponding to this IOCState.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets whether this IOC is running or not.
     *
     * @return true if it is running; false otherwise
     */
    public boolean getIsRunning() {
        return isRunning;
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
     * Gets whether or not the IOC is in the current configuration or in any
     * selected components. The result is calculated at run-time so that it is
     * as up to date as possible with the actual configuration being used.
     *
     * Can't easily use listeners here because it will cause a memory leak
     * (#3425). The reason for this is that instances of this class are
     * recreated any time any item in the ioc list changes. If we register an
     * observer with the current configs observable, it will have a reference
     * back to this class and hence can never be garbage collected.
     *
     * @return true if it is in the current configuration; false otherwise.
     */
    public boolean getInCurrentConfig() {
        Configuration currentConfig = configServer.currentConfig().getValue();
        Collection<Configuration> components = configServer.componentDetails().getValue();

        Set<String> enabledComponentNames =
                currentConfig.getComponents().stream().map(comp -> comp.getName()).collect(Collectors.toSet());

        Collection<Ioc> configIocs = currentConfig.getIocs();
        components.stream().forEach(comp -> {
            if (enabledComponentNames.contains(comp.getName())) {
                configIocs.addAll(comp.getIocs());
            }
        });
        return configIocs.stream().anyMatch(ioc -> Objects.equals(ioc.getName(), name));
    }
    
    /**
     * Gets the current simulation level of the IOC.
     * 
     * @return the simulation level of the IOC
     */
    public SimLevel getSimLevel() {
    	Configuration currentConfig = configServer.currentConfig().getValue();
    	Collection<Ioc> configIocs = currentConfig.getIocs();
    	
    	for (var ioc : configIocs) {
    		if (ioc.getName().equals(name)) {
    			return ioc.getSimLevel();
    		}
    	}
    	return SimLevel.NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(IocState iocState) {
        return name.compareTo(iocState.getName());
    }
}
