
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.ExperimentSetup;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods.PeriodsViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels.TimeChannelsViewModel;

/**
 * The view model that contains the logic for displaying how the experiment is
 * set up on the DAE.
 */
public class ExperimentSetupViewModel {

	private ExperimentSetup model;
	
	private DataAcquisitionViewModel daeSettings = new DataAcquisitionViewModel();
	private TimeChannelsViewModel timeChannels = new TimeChannelsViewModel();
	private PeriodsViewModel periodSettings = new PeriodsViewModel();

    /**
     * Sets all view models used in the experiment setup perspective to the
     * models in the backend.
     * 
     * @param model the model of the experiment setup.
     */
	public void setModel(ExperimentSetup model) {
		this.model = model;			
		daeSettings.setModel(model.daeSettings());
		daeSettings.setUpdateSettings(model.updateSettings());
        daeSettings.setComboContentProvider(
        		new DAEComboContentProvider(model.getWiringTablesDir()),
        		new DAEComboContentProvider(model.getDetectorTablesDir()),
        		new DAEComboContentProvider(model.getSpectraTablesDir()));
		daeSettings.setWiringTableList(model.wiringList());
		daeSettings.setDetectorTableList(model.detectorTables());
		daeSettings.setSpectraTableList(model.spectraTables());
		
		timeChannels.setModel(model.timeChannels());
        timeChannels.setComboContentProvider(new DAEComboContentProvider(model.getTimeChannelsDir()));
        timeChannels.setTimeChannelFileList(model.timeChannelFiles());
		
		periodSettings.setSettings(model.periodSettings());
        periodSettings.setComboContentProvider(new DAEComboContentProvider(model.getPeriodFilesDir()));
		periodSettings.setPeriodFilesList(model.periodFiles());

	}
	
    /**
     * updates the experiment setup saved in the blockserver with changes made
     * in the frontend.
     */
	public void updateDae() {
		model.sendAllSettings();
	}
	
    /**
     * Returns the view model for the time channel settings.
     * 
     * @return the time channel view model.
     */
	public TimeChannelsViewModel timeChannels() {
		return timeChannels;
	}

    /**
     * Returns the view model for the DAE settings.
     * 
     * @return the DAE view model.
     */
	public DataAcquisitionViewModel daeSettings() {
		return daeSettings;
	}

    /**
     * Returns the view model for the period settings.
     * 
     * @return the period view model.
     */
	public PeriodsViewModel periodSettings() {
		return periodSettings;
	}


}
