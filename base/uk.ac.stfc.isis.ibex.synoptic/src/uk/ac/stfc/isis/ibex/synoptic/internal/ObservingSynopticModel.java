
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

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ClosingSwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

/**
 * A class for linking the PV observables used to define the synoptic with the SynopticModel.
 *
 */
public class ObservingSynopticModel {
	
	private SynopticInfo synopticInfo;

	private final InitialisableObserver<SynopticDescription> descriptionObserver 
		= new BaseObserver<SynopticDescription>() {

			@Override
			public void onValue(SynopticDescription value) {
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
				
				if (synopticName.isEmpty()) {
					// There is no default specified
					return;
				}
				
				SynopticInfo newSynoptic = SynopticInfo.search(variables.available.getValue(), synopticName);
				if (newSynoptic == null) {
					// If cannot find synoptic use the default even if it is wrong for the configuration		
					newSynoptic = SynopticInfo.search(variables.available.getValue(), variables.default_synoptic.getValue().name());
					
					// If still null do nothing
					if (newSynoptic == null) {
						return;
					}
						
				}
				
				switchSynoptic(newSynoptic);
			}

			@Override
			public void onError(Exception e) { }
			@Override
			public void onConnectionChanged(boolean isConnected) { }
		};
		
	private final InitialisableObserver<String> synopticSchemaObserver
		= new BaseObserver<String>() {

			@Override
			public void onValue(String value) {
				// Set the schema
				try {
					XMLUtil.setSchema(value);
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Exception e) { }
			@Override
			public void onConnectionChanged(boolean isConnected) { }
		};
	
	private final SynopticModel model;
	private final Variables variables;
	private final ClosingSwitchableObservable<SynopticDescription> synopticObservable;
	
	public ObservingSynopticModel(Variables variables, SynopticModel model) {
		this.model = model;
		this.variables = variables;
				
		this.synopticObservable = new ClosingSwitchableObservable<SynopticDescription>(variables.getSynopticDescription(""));
		this.synopticObservable.addObserver(descriptionObserver);
				
		this.variables.synopticSchema.addObserver(synopticSchemaObserver);
		
		Configurations.getInstance().server().currentConfig().addObserver(configSynopticObserver);
	}
	
	public void switchSynoptic(SynopticInfo newSynoptic) {
		this.synopticInfo = newSynoptic;
		InitialiseOnSubscribeObservable<SynopticDescription> synopticDescriptionObservable = variables.getSynopticDescription(newSynoptic.pv());
		
		UpdatedValue<SynopticDescription> config = new UpdatedObservableAdapter<>(synopticDescriptionObservable);
		
		if (Awaited.returnedValue(config, 2)) {
			synopticObservable.switchTo(synopticDescriptionObservable);
		}
	}
	
	public SynopticInfo getSynopticInfo() {
		return this.synopticInfo;
	}
}
