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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * The view model that contains the logic for the PV list.
 */
public class PvListViewModel extends ModelObject {
    List<PV> pvList;
    PV selectedPV;

    ComponentDescription selectedComp;

    boolean deleteEnabled;
    boolean upEnabled;
    boolean downEnabled;

    /**
     * The constructor for the view model of the pv list view.
     * 
     * @param synoptic
     *            A synoptic view model that will fire changes on the components
     *            selection.
     */
    public PvListViewModel(final SynopticViewModel synoptic) {
        synoptic.addPropertyChangeListener("compSelection", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                selectedComp = synoptic.getSingleSelectedComp();
                updatePvList();
                setSelectedPV(null);
            }
        });
    }

    /**
     * Adds a PV writer/reader to the component.
     */
    public void addNewPV() {
        PV pv = new PV(selectedComp.getUniquePvName("New PV"), "NONE", IO.READ);

        // Will add at the beginning if null
        selectedComp.addPV(pv, getIndex(selectedPV) + 1);

        updatePvList();
        
        setSelectedPV(pv);

    }

    private void updatePvList() {
        if (selectedComp != null) {
            // We don't need to send the list as the ListViewer just needs to be
            // refreshed
            firePropertyChange("pvListChanged", 0, pvList = selectedComp.pvs());
        }
    }

    /**
     * Gets the currently selected PV. Can be null.
     * 
     * @return the PV
     */
    public PV getSelectedPV() {
        return selectedPV;
    }

    private int getIndex(PV selected) {
        return pvList.indexOf(selectedPV);
    }

    /**
     * Set the selected PV.
     * 
     * @param selected
     *            the PV
     */
    public void setSelectedPV(PV selected) {
        if (selected == null) {
            setDeleteEnabled(false);
            setUpEnabled(false);
            setDownEnabled(false);
        } else {
            setDeleteEnabled(true);

            int idx = getIndex(selected);
            setUpEnabled(idx != 0);
            setDownEnabled(idx != pvList.size() - 1);
        }

        firePropertyChange("pvSelection", selectedPV, selectedPV = selected);
    }

    /**
     * Moves a PV up the list.
     */
    public void promoteSelectedPV() {
        selectedComp.promotePV(selectedPV);
        updatePvList();
    }

    /**
     * Moves a PV down the list.
     */
    public void demoteSelectedPV() {
        selectedComp.demotePV(selectedPV);
        updatePvList();
    }

    /**
     * Remove the selected PV from the component.
     */
    public void removeSelectedPV() {
        if (selectedPV != null) {
            selectedComp.removePV(selectedPV);
            updatePvList();
            setSelectedPV(null);
        }
    }

    /**
     * @return true if the delete PV button is enabled.
     */
    public boolean getDeleteEnabled() {
        return deleteEnabled;
    }

    /**
     * @param enabled
     *            true to enable the delete PV button
     */
    public void setDeleteEnabled(boolean enabled) {
        firePropertyChange("deleteEnabled", deleteEnabled, deleteEnabled = enabled);
    }

    /**
     * @return true if the up PV button is enabled.
     */
    public boolean getUpEnabled() {
        return upEnabled;
    }

    /**
     * @param enabled
     *            true to enable the up PV button
     */
    public void setUpEnabled(boolean enabled) {
        firePropertyChange("upEnabled", upEnabled, upEnabled = enabled);
    }

    /**
     * @return true if the down PV button is enabled.
     */
    public boolean getDownEnabled() {
        return downEnabled;
    }

    /**
     * @param enabled
     *            true to enable the down PV button
     */
    public void setDownEnabled(boolean enabled) {
        firePropertyChange("downEnabled", downEnabled, downEnabled = enabled);
    }
}
