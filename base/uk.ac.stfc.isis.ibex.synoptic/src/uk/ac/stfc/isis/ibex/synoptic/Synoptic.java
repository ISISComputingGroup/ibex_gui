
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

package uk.ac.stfc.isis.ibex.synoptic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.synoptic.internal.ObservingSynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

public class Synoptic extends Closer implements BundleActivator {

	public static final Logger LOG = IsisLog.getLogger(Synoptic.class);
	
	private static Synoptic instance;
	private static BundleContext context;

	private SynopticModel viewerModel;
	
	private ObservingSynopticModel viewerModelObserver;	

	private SynopticModel editorModel;

	private final Variables variables;
	
	public Synoptic() {
		instance = this;
		
		variables = registerForClose(new Variables(Instrument.getInstance().channels()));	
		
		viewerModel = new SynopticModel(variables);
		viewerModelObserver = new ObservingSynopticModel(variables, viewerModel);
		
		editorModel = new SynopticModel(variables);
	}
	
	public static Synoptic getInstance() {
		return instance;
	}
	
	public SynopticModel currentViewerModel() {
		return viewerModel;
	}
	
	public SynopticModel edit() {
		return editorModel;
	}
	
	public void setViewerSynoptic(SynopticInfo info) {
		viewerModelObserver.switchSynoptic(info);
	}
	
	public void setViewerSynoptic(String synopticName) {
		for (SynopticInfo synoptic : availableSynoptics()) {
			if (synoptic.name().equals(synopticName)) {
				viewerModelObserver.switchSynoptic(synoptic);
			}
		}
	}
	
	public SynopticModel getBlankModel() {
		return new SynopticModel(variables);
	}
	
	public Collection<SynopticInfo> availableSynoptics() {
		return variables.available.getValue();
	}
	
	public Collection<SynopticInfo> availableEditableSynoptics() {
		ArrayList<SynopticInfo> all = new ArrayList<>(variables.available.getValue());
		SynopticInfo noneSynoptic = null;
		for (SynopticInfo s : all) {
			if (s.name().equals(Variables.NONE_SYNOPTIC_NAME)) {
				noneSynoptic = s;
			}
		}
		
		if (noneSynoptic != null) {
			all.remove(noneSynoptic);
		}
		
		return all;
	}
	
	public int getSynopticNumber() {
		// No synoptic loaded
		if (getSynopticInfo() == null) {
			return -1;
		}
		
		String currentSynopticName = getSynopticInfo().name();
		
		ArrayList<String> availableSynoptics = new ArrayList<String>(SynopticInfo.names(availableSynoptics()));
		
		return availableSynoptics.indexOf(currentSynopticName);
	}
	
	public SynopticInfo getSynopticInfo() {
		return viewerModelObserver.getSynopticInfo();
	}
	
	public InitialiseOnSubscribeObservable<SynopticDescription> synoptic(SynopticInfo synoptic) {
		return variables.getSynopticDescription(synoptic.pv());
	}
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Synoptic.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Synoptic.context = null;
		close();
	}
	

	/**
	 * The writables below were added for greying out the menu items - they are not used otherwise
	 */
	public Writable<Collection<String>> delete() {
		return editorModel.deleteSynoptics();
	}
	
	public Writable<String> editSynoptic() {
		return editorModel.setSynoptic();
	}
}
