
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.Collection;
import java.util.Collections;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Build a full configuration suitable for editing.
 */
public class ObservableEditableConfiguration 
extends TransformingObservable<Configuration, EditableConfiguration> {


    private static final Logger LOG = IsisLog.getLogger(ObservableEditableConfiguration.class);
    private final ConfigServer configServer;

    /**
     * Constructor for the ObservableEditableConfiguration.
     * 
     * @param config
     *            The observable for the configuration to be edited
     * @param configServer
     *            The config server
     */
    public ObservableEditableConfiguration(
	    ClosableObservable<Configuration> config,
	    ConfigServer configServer) {
	this.configServer = configServer;
	setSource(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EditableConfiguration transform(Configuration value) {

	try {
	    // close the previous editable config
	    EditableConfiguration val = this.getValue();
	    if (val != null) {
		val.close();
	    }
	    return new EditableConfiguration(
		    value, 
		    valueOrEmptyCollection(configServer.iocs()),
		    valueOrEmptyCollection(configServer.componentDetails()), 
		    valueOrEmptyCollection(configServer.pvs()));
	} catch (RuntimeException e) {
	    // Log here because the exception will be lost in the observer/observables.
	    LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
	    throw e;
	}
    }

    private static <T> Collection<T> valueOrEmptyCollection(Observable<Collection<T>> collection) {
	return collection.getValue() != null ? collection.getValue() : Collections.<T>emptyList();
    }

    /**
     * Unsubscribe from config and close editable config. 
     */
    public void cancelSubscriptionAndCloseEditableConfig() {
	EditableConfiguration val = this.getValue();
	if (val != null) {
	    val.close();
	}
	cancelSubscription();
    }
}
