
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
package uk.ac.stfc.isis.ibex.ui.configserver;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.ObservableEditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.groups.GroupEditorViewModel;

/**
 * Models related to viewing the configuration with the same observable
 * configuration.
 */
public class ConfigurationViewModels {

    private static final int MAX_SECONDS_TO_WAIT_FOR_VALUE = 10;

    /** model that is being edited. */
    private Editing editingModel;

    /** common observables from the model for configuration. */
    private UpdatedObservableAdapter<EditableConfiguration> observableConfigModel;

    /** view model for the group. */
    private GroupEditorViewModel groupEditorViewModel;

    private Optional<ObservableEditableConfiguration> configModel = Optional.empty();

    private static final Logger LOG = IsisLog.getLogger(ConfigurationViewModels.class);

    /**
     * Constructor.
     */
    public ConfigurationViewModels() {
	groupEditorViewModel = new GroupEditorViewModel();
	LOG.info("Created configurationviewmodels");
    }

    /**
     * Bind the editing model to this model. 
     * Note: this must be done before changing the item being edited.
     *
     * @param editingModel the editing model
     */
    public void bind(Editing editingModel) {
	this.editingModel = editingModel;
	setAsObservableConfigModel(editingModel.currentConfig());
    }

    /**
     * Get the model for group being edited.
     *
     * @return the group editor view model
     */
    public GroupEditorViewModel groupEditorViewModel() {
	return groupEditorViewModel;
    }

    /**
     * Get the configuration model.
     *
     * @return configuration model
     */
    public UpdatedObservableAdapter<EditableConfiguration> getConfigModel() {
	return observableConfigModel;
    }

    /**
     * Set the model to point at the current configuration.
     *
     * @return configuration model
     * @throws TimeoutException - if more than MAX_SECONDS_TO_WAIT_FOR_VALUE elapsed
     *                          before the current config was accessible.
     */
    public EditableConfiguration getCurrentConfig() throws TimeoutException {
	return currentValueFromObservable(editingModel.currentConfig());
    }

    /**
     * Set the model to point at the named configuration.
     *
     * @param configName name of the configuration
     * @return configuration model
     * @throws TimeoutException - if more than MAX_SECONDS_TO_WAIT_FOR_VALUE elapsed
     *                          before the config was accessible.
     */
    public EditableConfiguration getConfig(String configName) throws TimeoutException {
	return currentValueFromObservable(editingModel.config(configName));
    }

    /**
     * Set the model to point at the named component.
     *
     * @param componentName name of the component
     * @return configuration model
     * @throws TimeoutException - if more than MAX_SECONDS_TO_WAIT_FOR_VALUE elapsed
     *                          before the component was accessible.
     */
    public EditableConfiguration getComponent(String componentName) throws TimeoutException {
	return currentValueFromObservable(editingModel.component(componentName));
    }

    /**
     * Set the model to point at a blank configuration.
     * 
     * @return configuration model
     * @throws TimeoutException - if more than MAX_SECONDS_TO_WAIT_FOR_VALUE elapsed
     *                          before the blank config was accessible.
     */
    public EditableConfiguration getBlankConfig() throws TimeoutException {
	return currentValueFromObservable(editingModel.blankConfig());
    }

    /**
     * This method takes an observable, waits for it to have a correct value, and
     * then returns that value
     * 
     * @param <T>        - the type of value
     * @param observable - the observable to monitor
     * @return - the current value
     * @throws TimeoutException - if more than MAX_SECONDS_TO_WAIT_FOR_VALUE elapsed
     *                          before the value was accessible.
     */
    private EditableConfiguration currentValueFromObservable(ObservableEditableConfiguration observable)
	    throws TimeoutException {
	// Required because the groups model uses the "global" state of this class - so
	// need to ensure we keep updating the state even
	// if things don't directly need it to be updated.
	setAsObservableConfigModel(observable);

	UpdatedObservableAdapter<EditableConfiguration> updateAdapter = new UpdatedObservableAdapter<>(observable);

	if (Awaited.returnedValue(updateAdapter, MAX_SECONDS_TO_WAIT_FOR_VALUE)) {
	    EditableConfiguration value = updateAdapter.getValue();
	    updateAdapter.close();
	    return value;
	} else {
	    updateAdapter.close();
	    throw new TimeoutException("Could not get value from " + observable + " after waiting "
		    + MAX_SECONDS_TO_WAIT_FOR_VALUE + " seconds.");
	}
    }

    /**
     * Changes this class to point at a different type of configuration object to
     * edit.
     * 
     * @param model - the new model to use.
     * @return an updated observable observing the new model
     */
    private void setAsObservableConfigModel(ObservableEditableConfiguration model) {
	// Close old observable adapter and model if they were set.
	configModel.ifPresent(ObservableEditableConfiguration::cancelSubscriptionAndCloseEditableConfig);
	Optional.ofNullable(observableConfigModel).ifPresent(UpdatedObservableAdapter::close);

	configModel = Optional.ofNullable(model);
	observableConfigModel = new UpdatedObservableAdapter<>(model);
	groupEditorViewModel.updateModel(observableConfigModel);
    }

}
