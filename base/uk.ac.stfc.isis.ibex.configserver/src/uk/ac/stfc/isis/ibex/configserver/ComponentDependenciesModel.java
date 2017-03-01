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
package uk.ac.stfc.isis.ibex.configserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;

/**
 * Model observing and holding information on all component dependencies for
 * this instrument.
 */
public class ComponentDependenciesModel {

    private final ConfigServer server;

    private Map<String, Collection<String>> dependencies = new HashMap<>();
    private Map<String, ForwardingObservable<Collection<String>>> observerRegister = new HashMap<>();
    private Observer<Collection<ConfigInfo>> componentAdapter = new BaseObserver<Collection<ConfigInfo>>() {
        @Override
        public void onValue(Collection<ConfigInfo> value) {
            updateObservers(value);
        };
    };

    /**
     * Constructor for the component dependency model.
     * 
     * @param server The config server.
     */
    public ComponentDependenciesModel(ConfigServer server) {
        this.server = server;
        server.componentsInfo().addObserver(componentAdapter);
    }

    /**
     * Finds the dynamic blockserver PV of a given component based on its name.
     * 
     * @param component The name of the component
     * @return The PV associated to the component
     */
    private String getPV(String component) {
        String pv = "";
        for (ConfigInfo compInfo : server.componentsInfo().getValue()) {
            if (compInfo.name().equals(component)) {
                pv = compInfo.pv();
                break;
            }
        }
        return pv;
    }

    /**
     * Updates the observers on component dependencies when components change.
     * 
     * @param updatedComponents The current list of components
     */
    private void updateObservers(Collection<ConfigInfo> updatedComponents) {
        Collection<String> names = ConfigInfo.names(updatedComponents);
        closeUnusedObservables(names);

        observerRegister.clear();
        for (final String name : names) {
            String pv = getPV(name);
            server.dependencies(pv).addObserver(new BaseObserver<Collection<String>>() {
                @Override
                public void onValue(Collection<String> value) {
                    updateDependency(name, value);
                };
            });
            observerRegister.put(name, server.dependencies(pv));
        }
    }

    /**
     * Checks whether components still exist and removes associated observables
     * if not.
     * 
     * @param updatedComponents The current list of components
     */
    private void closeUnusedObservables(Collection<String> updatedComponents) {
        for (String key : observerRegister.keySet()) {
            if (!updatedComponents.contains(key)) {
                observerRegister.get(key).close();
            }
        }
    }

    /**
     * Updates the list of configurations dependent on a given component, or
     * removes it from the list if there are none.
     * 
     * @param name The name of the component
     * @param value The list of dependent configurations
     */
    private void updateDependency(String name, Collection<String> value) {
        if (!value.isEmpty()) {
            dependencies.put(name, value);
        } else {
            dependencies.remove(name);
        }
    }

    /**
     * @return The map of all component dependencies.
     */
    public Map<String, Collection<String>> getDependencies() {
        return dependencies;
    }
}
