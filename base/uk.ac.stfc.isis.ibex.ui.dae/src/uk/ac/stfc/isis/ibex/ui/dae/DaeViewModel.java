
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

package uk.ac.stfc.isis.ibex.ui.dae;

import org.eclipse.core.databinding.DataBindingContext;

import uk.ac.stfc.isis.ibex.dae.Dae;
import uk.ac.stfc.isis.ibex.dae.IDae;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.detectordiagnostics.DetectorDiagnosticsViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.ExperimentSetupViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.run.RunSummaryViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.runinformation.RunInformationViewModel;

/**
 * A View Model that holds the logic for displaying general information about
 * the DAE.
 */
public class DaeViewModel extends Closer {
	
	private static final String DEFAULT_TITLE = "DAE Control Program";
	
	private RunSummaryViewModel runSummary = registerForClose(new RunSummaryViewModel());
	private ExperimentSetupViewModel experimentSetup = new ExperimentSetupViewModel();
	private RunInformationViewModel runInformation = registerForClose(new RunInformationViewModel(Dae.getInstance().observables()));
	private DetectorDiagnosticsViewModel detectorDiagnosticsViewModel = new DetectorDiagnosticsViewModel(new DataBindingContext());
	
	private UpdatedValue<String> vetos;
	private UpdatedValue<Boolean> isRunning;

	private SettableUpdatedValue<String> daeTitle = new SettableUpdatedValue<>();

    /**
     * Binds a model to this view model.
     * 
     * @param model
     *            A IDae model object that holds information about the DAE.
     */
	public void bind(IDae model) {
		runSummary.bind(model);
		experimentSetup.setModel(model.experimentSetup());
		
		vetos = registerForClose(new TextUpdatedObservableAdapter(model.vetos()));
		
		isRunning = new UpdatedObservableAdapter<>(model.isRunning());
		
		model.simulationMode().subscribe(new BaseObserver<Boolean>() {
			@Override
			public void onValue(Boolean value) {
				if (value) {
					daeTitle.setValue(DEFAULT_TITLE + " (simulation mode)");
				} else {
					daeTitle.setValue(DEFAULT_TITLE);
				}
				
			}
			
			@Override
			public void onConnectionStatus(boolean status) {
				if (!status) {
					daeTitle.setValue(DEFAULT_TITLE + " (not connected)");
				}
			}
		});
	}
	
    /**
     * Get an updated value on the running status of the DAE.
     * 
     * @return An updating boolean, true if the DAE is running.
     */
	public UpdatedValue<Boolean> isRunning() {
		return isRunning;
	}
	
    /**
     * Get a view model for the display logic of the run summary.
     * 
     * @return The run summary view model.
     */
	public RunSummaryViewModel runSummary() {
		return runSummary;
	}
	
    /**
     * Get a view model for the display logic of the experiment setup.
     * 
     * @return The experiment setup view model.
     */
	public ExperimentSetupViewModel experimentSetup() {
		return experimentSetup;
	}
	
    /**
     * Get an updated value on the status of the vetoes.
     * 
     * @return An updating string, giving the status of the DAE vetoes as
     *         human-readable text.
     */
	public UpdatedValue<String> vetos() {
		return vetos;
	}

    /**
     * Get a view model for the display logic of the run information.
     * 
     * @return The run information view model.
     */
	public RunInformationViewModel runInformation() {
		return runInformation;
	}

    /**
     * @return the detector diagnostics view model
     */
    public DetectorDiagnosticsViewModel detectorDiagnostics() {
        return detectorDiagnosticsViewModel;
    }
    
}
