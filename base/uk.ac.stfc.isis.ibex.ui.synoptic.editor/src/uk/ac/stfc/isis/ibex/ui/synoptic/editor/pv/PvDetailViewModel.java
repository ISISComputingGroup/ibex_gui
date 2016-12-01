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

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.blockselector.BlockSelector;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IPVSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

/**
 * This is the view model that contains the logic for the pv details panel.
 */
public class PvDetailViewModel extends ModelObject {
    private SynopticViewModel synoptic;

    private boolean selectionVisible;
    private String pvName = "";
    private String errorText = "";
    private String pvAddress = "";

    private IO pvMode = IO.READ;

    private PV selectedPv;

    /**
     * The constructor for the view model.
     * 
     * @param synoptic
     *            The view model for the synoptic as a whole
     */
    public PvDetailViewModel(SynopticViewModel synoptic) {
        this.synoptic = synoptic;

        if (synoptic != null) {
            synoptic.addPVSelectionListener(new IPVSelectionListener() {
                @Override
                public void selectionChanged(PV oldSelection, PV newSelection) {
                    showPV(selectedPv = newSelection);
                }
            });
        }

        setSelectionVisible(false);
    }

    private synchronized void showPV(PV componentPv) {
        if (selectedPv != null) {
            setSelectionVisible(true);

            setPvName(selectedPv.displayName());

            // Use the full address to avoid confusion
            setPvAddress(selectedPv.fullAddress());

            setPvMode(selectedPv.recordType().io());
        } else {
            setSelectionVisible(false);
        }
    }

    /**
     * @return Whether the editing PV panel is visible
     */
    public boolean getSelectionVisible() {
        return selectionVisible;
    }

    /**
     * @param visible
     *            Whether the editing PV panel is visible
     */
    public void setSelectionVisible(boolean visible) {
        firePropertyChange("selectionVisible", selectionVisible, selectionVisible = visible);
    }

    /**
     * @return The name of the PV that is being edited.
     */
    public String getPvName() {
        return pvName;
    }

    /**
     * @param name
     *            The name of the PV that is being edited.
     */
    public void setPvName(String name) {
        firePropertyChange("pvName", pvName, pvName = name);
        updateModel();
    }

    /**
     * @return The error text to display.
     */
    public String getErrorText() {
        return errorText;
    }

    /**
     * @param text
     *            The error text to display.
     */
    public void setErrorText(String text) {
        firePropertyChange("errorText", errorText, errorText = text);
    }

    /**
     * @return The read/write status of the PV.
     */
    public IO getPvMode() {
        return pvMode;
    }

    /**
     * @param mode
     *            The read/write status of the PV.
     */
    public void setPvMode(IO mode) {
        firePropertyChange("pvMode", pvMode, pvMode = mode);
        updateModel();
    }

    /**
     * @return The address of the PV which is being edited.
     */
    public String getPvAddress() {
        return pvAddress;
    }

    /**
     * @param name
     *            The address of the PV which is being edited.
     */
    public void setPvAddress(String name) {
        firePropertyChange("pvAddress", pvAddress, pvAddress = name);
        validateAddress();
    }

    private synchronized void updateModel() {
        if (selectedPv != null) {
            synoptic.updateSelectedPV(pvName, pvAddress, pvMode);
        }
    }

    private void validateAddress() {
        PvValidator addressValid = new PvValidator();
        if (addressValid.validatePvAddress(pvAddress)) {
            setErrorText("");
            updateModel();
        } else {
            setErrorText(addressValid.getErrorMessage());
        }
    }

    /**
     * Open a dialog for picking a PV out of the known list.
     */
    public void openPvDialog() {
        PvSelector selectPV = new PvSelector();
        try {
            selectPV.execute(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (selectPV.isConfirmed()) {
            setPvAddress(selectPV.getPvAddress());
        }
    }

    /**
     * Open a dialog for picking a PV out of the block list.
     */
    public void openBlockDialog() {
        BlockSelector selectPV = new BlockSelector();
        try {
            selectPV.execute(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (selectPV.isConfirmed()) {
            setPvName(selectPV.getBlockName());
            setPvAddress(selectPV.getPvAddress());
        }
    }
}
