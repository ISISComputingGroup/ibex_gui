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

package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;

/**
 * Contains the display logic for the SynopticSelection widget.
 */
public class SynopticSelectionViewModel extends ModelObject {
    
	/**
	 * Property name for binding to synoptic list changes.
	 */
    public static final String SYNOPTIC_LIST = "synopticList";

	/**
	 * Property name for binding to selected synoptic changes.
	 */
    public static final String SELECTED = "selected";
    

	/**
	 * Property name for binding to synoptic control enablement changes.
	 */
    public static final String ENABLED = "enabled";

    /**
     * Time for databinding to wait to avoid synchronicity issues.
     */
    public static final int DATABINDING_WAIT_TIME_MS = 100;

	private static Synoptic synoptic = Synoptic.getInstance();
	
    private SynopticInfo lastSetDefaultSynoptic = new SynopticInfo("", "", false);

    private Collection<SynopticInfo> synopticList = new ArrayList<>();
    private ArrayList<String> synopticNamesList;

    private boolean enabled;
	private String selected;
	
	private final BaseObserver<Collection<SynopticInfo>> availableSynopticsObserver = new BaseObserver<Collection<SynopticInfo>>() {
		@Override
		public void onValue(Collection<SynopticInfo> value) {
            updateSynopticList(value);
            if (synopticList != null) {
                setEnabled(true);
            }
		}

		@Override
		public void onError(Exception e) {
            setEmptySynopticList();
            setEnabled(false);
            lastSetDefaultSynoptic = new SynopticInfo("", "", false);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
                setEmptySynopticList();
                setEnabled(false);
                lastSetDefaultSynoptic = new SynopticInfo("", "", false);
			}
        }
	};
	
	/**
	 * Get the view model for the synoptic selector.
	 */
    public SynopticSelectionViewModel() {
        synoptic.availableSynopticsInfo().subscribe(availableSynopticsObserver);
    }

    /**
     * Put an empty list in the currently available synoptic list.
     */
    private void setEmptySynopticList() {
        updateSynopticList(new ArrayList<SynopticInfo>());
    }

    /**
     * Put a new set of synoptics in the list of available synoptics.
     * 
     * @param value The new set of available synoptics.
     */
    private void updateSynopticList(Collection<SynopticInfo> value) {
        synopticList = value;
        ArrayList<String> names = new ArrayList<String>(SynopticInfo.names(synopticList));

        firePropertyChange(SYNOPTIC_LIST, synopticNamesList, synopticNamesList = names);

        // Updating the list above causes the selection to change, and
        // databinding to update the selected synoptic. If we just wait a moment
        // here we can avoid it overwriting our setting of the default synoptic.
        // But there must be a better way to do this...
        try {
            Thread.sleep(DATABINDING_WAIT_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setDefaultSynoptic();
    }

    private void setDefaultSynoptic() {
        for (SynopticInfo synoptic : synopticList) {
            if (synoptic.isDefault()) {
                setDefaultIfChanged(synoptic);
                break;
            }
        }
    }

    private void setDefaultIfChanged(SynopticInfo newDefaultSynoptic) {
        if (!newDefaultSynoptic.name().equals(lastSetDefaultSynoptic.name())) {
            setSelected(newDefaultSynoptic.name());
            lastSetDefaultSynoptic = newDefaultSynoptic;
        }
    }
    
    /**
     * Set the selected synoptic to be the synoptic identified as the latest default.
     */
    public void setLastDefault() {
    	setSelected(lastSetDefaultSynoptic.name());
    }

    /**
     * @return The name of the currently selected synoptic.
     */
    public String getSelected() {
        return selected;
    }

    /**
     * Sets the selected synoptic from the UI.
     * 
     * @param selected
     *            The string name of the synoptic to set
     */
    public void setSelected(String selected) {
        synoptic.setViewerSynoptic(selected);
        firePropertyChange(SELECTED, this.selected, this.selected = selected);
    }
	
    /**
     * @return A list of names of currently available synoptics
     */
	public ArrayList<String> getSynopticList() {
        return synopticNamesList;
	}
	
	/**
	 * Refresh the currently selected synoptic.
	 */
	public void refreshSynoptic() {
        SynopticInfo synopticToRefresh = synoptic.currentObservingViewerModel().getSynopticInfo();
		synoptic.setViewerSynoptic(synopticToRefresh);
	}

    /**
     * @return whether the display items should be enabled or not
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     *            set whether the display items should be enabled or not
     */
    public void setEnabled(boolean enabled) {
        firePropertyChange(ENABLED, this.enabled, this.enabled = enabled);
    }
	
}
