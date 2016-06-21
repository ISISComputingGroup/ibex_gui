
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

import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.groups.GroupEditorViewModel;

/**
 * Models related to viewing the configuration with the same observable
 * configuration.
 */
public class ConfigurationViewModels {

    /** model that is being edited. */
    private Editing editingModel;

    /** common observables from the model for configuration. */
    private UpdatedObservableAdapter<EditableConfiguration> observableConfigModel;

    /** view model for the group. */
    private GroupEditorViewModel groupEditorViewModel;

    /**
     * Constructor.
     */
    public ConfigurationViewModels() {
        groupEditorViewModel = new GroupEditorViewModel();
    }

    /**
     * Bind the editing model to this model. 
     * Note: this must be done before changing the item being edited.
     * 
     * @param editingModel the editing model
     */
    public void bind(Editing editingModel) {
        this.editingModel = editingModel;
        setModelAsCurrentConfig();
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
     */
    public void setModelAsCurrentConfig() {
        observableConfigModel = new UpdatedObservableAdapter<>(editingModel.currentConfig());
        updateViewModels();
    }

    /**
     * Set the model to point at the named configuration.
     * 
     * @param configName name of the configuration
     */
    public void setModelAsConfig(String configName) {
        observableConfigModel = new UpdatedObservableAdapter<>(editingModel.config(configName));
        updateViewModels();
    }

    /**
     * Set the model to point at the named component.
     * 
     * @param componentName name of the component
     */
    public void setModelAsComponent(String componentName) {
        observableConfigModel = new UpdatedObservableAdapter<>(editingModel.component(componentName));
        updateViewModels();
    }

    /**
     * Set the model to point at a blank configuration.
     */
    public void setModelAsBlankConfig() {
        observableConfigModel = new UpdatedObservableAdapter<>(editingModel.blankConfig());
        updateViewModels();
    }

    /**
     * update the group editor model.
     */
    private void updateViewModels() {
        groupEditorViewModel.updateModel(this.observableConfigModel);
    }

}
