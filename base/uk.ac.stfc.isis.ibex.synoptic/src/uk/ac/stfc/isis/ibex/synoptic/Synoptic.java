
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

import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * The singleton that contains information on the synoptics.
 */
public class Synoptic extends Closer implements BundleActivator {

    /**
     * The logger to log synoptic error messages.
     */
	public static final Logger LOG = IsisLog.getLogger(Synoptic.class);
	
	private static Synoptic instance;
	private static BundleContext context;

	private SynopticModel viewerModel;
	
	private ObservingSynopticModel viewerModelObserver;	

	private SynopticModel editorModel;

	private final Variables variables;
	
    /**
     * The constructor for this singleton, created by eclipse when the class is
     * first used.
     */
	public Synoptic() {
		instance = this;
		
        variables = new Variables();
		
		viewerModel = new SynopticModel(variables);
		viewerModelObserver = new ObservingSynopticModel(variables, viewerModel);
		
		editorModel = new SynopticModel(variables);
	}
	
    /**
     * @return The instance of this singleton.
     */
	public static Synoptic getInstance() {
		return instance;
	}
	
    /**
     * Get the model for viewing the current synoptic.
     * 
     * @return The current synoptic model.
     */
	public SynopticModel currentViewerModel() {
		return viewerModel;
	}
	
    /**
     * Gets a model for editing synoptics.
     * 
     * @return The model for editing synoptics.
     */
	public SynopticModel edit() {
		return editorModel;
	}
	
    /**
     * Sets the synoptic that is being viewed based on a synoptic info class.
     * 
     * @param info
     *            The detailed information of the synoptic to display
     */
	public void setViewerSynoptic(SynopticInfo info) {
		viewerModelObserver.switchSynoptic(info);
	}
	
    /**
     * Sets the synoptic that is being viewed based on the name of the synoptic.
     * 
     * @param synopticName
     *            The name of the synoptic to view.
     */
	public void setViewerSynoptic(String synopticName) {
		for (SynopticInfo synoptic : availableSynoptics()) {
			if (synoptic.name().equals(synopticName)) {
				viewerModelObserver.switchSynoptic(synoptic);
			}
		}
	}
	
    /**
     * Get a blank synoptic model for editing.
     * 
     * @return A blank synoptic model.
     */
	public SynopticModel getBlankModel() {
		return new SynopticModel(variables);
	}

    /**
     * Gets the schema for what the blockserver expects from synoptics.
     * 
     * @return The string containing the schema.
     */
    public String getSchema() {
        return variables.synopticSchema.getValue();
    }

    /**
     * Provides the information about available synoptics.
     * 
     * @return The collection of information objects for the available
     *         synoptics.
     */
	public ForwardingObservable<Collection<SynopticInfo>> availableSynopticsInfo() {
		return variables.available;
	}

    /**
     * Provides a collection of available synoptics.
     * 
     * @return The available synoptics.
     */
	public Collection<SynopticInfo> availableSynoptics() {
		return variables.available.getValue();
	}
	
    /**
     * @return SynopticInformation for the default Synoptic.
     */
    public SynopticInfo getDefaultSynoptic() {
        SynopticInfo defaultSynoptic = null;
        //TODO this appears to be null on first access. I.e. when you open the config this should probably be bound
        Collection<SynopticInfo> infos = variables.available.getValue();
        if (infos == null) {
        	return null;
        }
		for (SynopticInfo info : infos) {
            if (info.isDefault()) {
                defaultSynoptic = info;
                break;
            }
        }
        return defaultSynoptic;
    }

    /**
     * Provides a collection of synoptics that can be edited.
     * 
     * @return The collection of editable synoptics.
     */
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
	
    /**
     * @return The model observing the current synoptic.
     */
	public ObservingSynopticModel currentObservingViewerModel() {
		return viewerModelObserver;
	}
	
    /**
     * Gets an observable tied to the synoptic description for a given synoptic
     * info.
     * 
     * @param synoptic
     *            The detailed synoptic info.
     * @return An observer on the synoptic description.
     */
	public ForwardingObservable<SynopticDescription> synoptic(SynopticInfo synoptic) {
		return variables.getSynopticDescription(synoptic.pv());
	}
	
    /**
     * @return The bundle context for the activator.
     */
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Synoptic.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Synoptic.context = null;
		close();
	}
	

	/**
     * The writables below were added for greying out the menu items - they are
     * not used otherwise.
     * 
     * @return A writable collection of synoptic names that can be deleted
     */
	public Writable<Collection<String>> delete() {
		return editorModel.deleteSynoptics();
	}
}
