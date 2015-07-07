
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

import java.util.List;

import uk.ac.stfc.isis.ibex.dae.Dae;
import uk.ac.stfc.isis.ibex.dae.IDae;
import uk.ac.stfc.isis.ibex.dae.spectra.UpdatableSpectrum;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.ExperimentSetupViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.run.RunSummaryViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.runinformation.RunInformationViewModel;

public class DaeViewModel extends Closer {
	
	private IDae model;
	
	private RunSummaryViewModel runSummary = registerForClose(new RunSummaryViewModel());
	private ExperimentSetupViewModel experimentSetup = new ExperimentSetupViewModel();
	private RunInformationViewModel runInformation = registerForClose(new RunInformationViewModel(Dae.getInstance().observables()));
	
	private UpdatedValue<String> vetos;
	private UpdatedValue<Boolean> isRunning;
	
	public void bind(IDae model) {
		this.model = model;
		
		runSummary.bind(model);
		experimentSetup.setModel(model.experimentSetup());
		
		vetos = registerForClose(new TextUpdatedObservableAdapter(model.vetos()));
		
		isRunning = new UpdatedObservableAdapter<>(model.isRunning());
	}
	
	public UpdatedValue<Boolean> isRunning() {
		return isRunning;
	}
	
	public RunSummaryViewModel runSummary() {
		return runSummary;
	}
	
	public ExperimentSetupViewModel experimentSetup() {
		return experimentSetup;
	}
	
	public List<? extends UpdatableSpectrum> spectra() {
		return model.spectra().spectra();
	}
	
	public UpdatedValue<String> vetos() {
		return vetos;
	}

	public RunInformationViewModel runInformation() {
		return runInformation;
	}
}
