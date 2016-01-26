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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;

/**
 * Contains the display logic for the SynopticSelection widget.
 */
/**
 * 
 */
public class SynopticSelectionViewModel extends ModelObject {

	private static Synoptic synoptic = Synoptic.getInstance();
	private static final ForwardingObservable<DisplayConfiguration> CONFIG = 
			Configurations.getInstance().display().displayCurrentConfig();
	
	private static final String RECOMMENDED_STRING = " (recommended)";
	
	private String recommendedSynoptic;
    private Collection<SynopticInfo> synopticList;
    private ArrayList<String> synopticListWithRecommended;

    private boolean enabled;
	
	private String selected;
	
	private final BaseObserver<DisplayConfiguration> configObserver = new BaseObserver<DisplayConfiguration>() {
		@Override
		public void onValue(DisplayConfiguration value) {
		    recommendedSynoptic = value.defaultSynoptic();
            updateSynopticList();
		}

		@Override
		public void onError(Exception e) {
		    clearRecommended();
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
			    clearRecommended();
			}
		}
		
		private void clearRecommended() {
		    recommendedSynoptic = null;
            updateSynopticList();
		}
	};	
	
	private final BaseObserver<Collection<SynopticInfo>> availableSynopticsObserver = new BaseObserver<Collection<SynopticInfo>>() {
		@Override
		public void onValue(Collection<SynopticInfo> value) {
            synopticList = value;
            updateSynopticList();
            if (synopticList != null) {
                setEnabled(true);
            }
		}

		@Override
		public void onError(Exception e) {
            setEmptySynopticList();
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
                setEmptySynopticList();
			}
        }

        private void setEmptySynopticList() {
            synopticList = new ArrayList<SynopticInfo>();
            updateSynopticList();
            setEnabled(false);
        }
	};
	
	private final PropertyChangeListener currentSynopticObserver = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
            setSelected((SynopticInfo) evt.getNewValue());
		}
	};
	
	public SynopticSelectionViewModel() {
        CONFIG.addObserver(configObserver);
        synoptic.availableSynopticsInfo().addObserver(availableSynopticsObserver);
		synoptic.currentObservingViewerModel().addPropertyChangeListener("synopticInfo", currentSynopticObserver);

        setSelected(synoptic.currentObservingViewerModel().getSynopticInfo());
	}
	
    /**
     * Sets the selected synoptic, adding the recommended tag
     * 
     * @param synoptic
     */
    private void setSelected(SynopticInfo synoptic) {
        if (synoptic != null) {
            String synopticName = synoptic.name();
            if (synopticName.equals(recommendedSynoptic)) {
                synopticName = recommendedSynoptic + RECOMMENDED_STRING;
            }
            firePropertyChange("selected", selected, selected = synopticName);
        }
    }

    /**
     * Sets the selected synoptic from the UI, removing the recommended tag
     * 
     * @param synoptic
     */
	public void setSelected(String selected) {
		if (selected.equals(recommendedSynoptic + RECOMMENDED_STRING)) {
            synoptic.setViewerSynoptic(recommendedSynoptic);
        } else {
            synoptic.setViewerSynoptic(selected);
		}
		firePropertyChange("selected", this.selected, this.selected = selected);
	}

    private void updateSynopticList() {
        ArrayList<String> names = new ArrayList<String>(SynopticInfo.names(synopticList));

        if (recommendedSynoptic != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                if (name.equals(recommendedSynoptic)) {
                    names.set(i, name + RECOMMENDED_STRING);
                    break;
                }
            }
        }

        firePropertyChange("synopticList", synopticListWithRecommended, synopticListWithRecommended = names);
    }

	public String getSelected() {
		return selected;
	}
	
	public ArrayList<String> getSynopticList() {
        return synopticListWithRecommended;
	}
	
	public int getSynopticNumber() {
		// No synoptic loaded
		if (synoptic.getSynopticInfo() == null) {
			return -1;
		}
		
		String currentSynopticName = synoptic.getSynopticInfo().name();
		
        ArrayList<String> availableSynoptics = new ArrayList<String>(SynopticInfo.names(synoptic.availableSynoptics()));
		
		return availableSynoptics.indexOf(currentSynopticName);
	}
	
	public void refreshSynoptic() {
		SynopticInfo synopticToRefresh = synoptic.getSynopticInfo();
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
        firePropertyChange("enabled", this.enabled, this.enabled = enabled);
    }
	
}
