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
/**
 * 
 */
public class SynopticSelectionViewModel extends ModelObject {

	private static Synoptic synoptic = Synoptic.getInstance();
	
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
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
                setEmptySynopticList();
                setEnabled(false);
			}
        }
	};
	
    public SynopticSelectionViewModel() {
        synoptic.availableSynopticsInfo().addObserver(availableSynopticsObserver);
    }

    private synchronized void setEmptySynopticList() {
        updateSynopticList(new ArrayList<SynopticInfo>());
    }

    private synchronized void updateSynopticList(Collection<SynopticInfo> value) {
        synopticList = value;
        ArrayList<String> names = new ArrayList<String>(SynopticInfo.names(synopticList));

        firePropertyChange("synopticList", synopticNamesList, synopticNamesList = names);

        setDefaultSynoptic();
    }

    private void setDefaultSynoptic() {
        for (SynopticInfo synoptic : synopticList) {
            System.out.println("synoptic:" + synoptic.name());
            if (synoptic.isDefault()) {
                setSelected(synoptic.name());
                break;
            }
        }
    }

    public String getSelected() {
        return selected;
    }

    /**
     * Sets the selected synoptic from the UI
     * 
     * @param synoptic
     *            The string name of the synoptic to set
     */
    public synchronized void setSelected(String selected) {
        synoptic.setViewerSynoptic(selected);
        firePropertyChange("selected", this.selected, this.selected = selected);
    }
	
	public ArrayList<String> getSynopticList() {
        return synopticNamesList;
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
