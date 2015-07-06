
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

package uk.ac.stfc.isis.ibex.synoptic.internal;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ClosingSwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class ObservingSynopticModel {
	
	private SynopticInfo synopticInfo;

	private final InitialisableObserver<InstrumentDescription> descriptionObserver 
		= new BaseObserver<InstrumentDescription>() {

			@Override
			public void onValue(InstrumentDescription value) {
				model.setInstrumentFromDescription(value);
			}

			@Override
			public void onError(Exception e) { }
			@Override
			public void onConnectionChanged(boolean isConnected) { }
		};
		
	private final InitialisableObserver<Configuration> configSynopticObserver 
		= new BaseObserver<Configuration>() {

			@Override
			public void onValue(Configuration value) {
				String synopticName = value.synoptic();
				SynopticInfo newSynoptic = SynopticInfo.search(variables.available.value(), synopticName);
				if (newSynoptic != null) {
					switchSynoptic(newSynoptic);
				}
			}

			@Override
			public void onError(Exception e) { }
			@Override
			public void onConnectionChanged(boolean isConnected) { }
		};
	
	private final SynopticModel model;
	private final Variables variables;
	private final ClosingSwitchableObservable<InstrumentDescription> synoptic;
	
	public ObservingSynopticModel(Variables variables, SynopticModel model) {
		this.model = model;
		this.variables = variables;
				
		this.synoptic = new ClosingSwitchableObservable<InstrumentDescription>(variables.getSynoptic(""));
		this.synoptic.subscribe(descriptionObserver);
		
		Configurations.getInstance().server().currentConfig().subscribe(configSynopticObserver);
	}
	
	public void switchSynoptic(SynopticInfo newSynoptic) {
		this.synopticInfo = newSynoptic;
		synoptic.switchTo(variables.getSynoptic(newSynoptic.pv()));
	}
	
	public SynopticInfo getSynopticInfo() {
		return this.synopticInfo;
	}
}
