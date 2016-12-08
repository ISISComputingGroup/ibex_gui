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

import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.RecordType;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.blockselector.BlockSelector;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

/**
 * This is the view model that contains the logic for the pv details panel.
 */
public class PvDetailViewModel extends ErrorMessageProvider {
    private boolean selectionVisible;
    private String pvName = "";
    private String pvAddress = "";

    private static final String NON_UNIQUE_PV_ERROR = "%s: %s (%s) PV is not unique";

    private IO pvMode = IO.READ;

    private PV selectedPv;

    private PvListViewModel model;

    /**
     * The constructor for the view model.
     * 
     * @param pvList
     *            The view model for the PV List as a whole
     */
    public PvDetailViewModel(PvListViewModel pvList) {
        this.model = pvList;
        if (pvList != null) {
            pvList.addPropertyChangeListener("pvSelection", new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    showPV((PV) evt.getNewValue());
                }
            });
        }

        setSelectionVisible(false);
    }

    /**
     * Update the displayed PV from the backend. Directly fire the property
     * changes here as we don't want to update the model at all.
     * 
     * @param pv
     *            The PV to update the front end with
     */
    public void showPV(PV pv) {
        if (pv != null) {
            selectedPv = pv;

            setSelectionVisible(true);

            firePropertyChange("pvName", pvName, pvName = pv.displayName());

            firePropertyChange("pvAddress", pvAddress, pvAddress = pv.address());

            firePropertyChange("pvMode", pvMode, pvMode = pv.recordType().io());

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
     * @param address
     *            The address of the PV which is being edited.
     */
    public void setPvAddress(String address) {
        firePropertyChange("pvAddress", pvAddress, pvAddress = address);
        updateModel();
    }

    private void updateModel() {
        updateSelectedPV(pvName, pvAddress, pvMode);
        validatePv(selectedPv);
    }

    /**
     * Change the settings for the selected PV.
     * 
     * @param name
     *            the display name
     * @param address
     *            the underlying PV
     * @param mode
     *            whether it is read or write
     */
    public void updateSelectedPV(String name, String address, IO mode) {
        if (selectedPv == null) {
            return;
        }

        selectedPv.setDisplayName(name);
        RecordType recordType = new RecordType();
        recordType.setIO(mode);
        selectedPv.setRecordType(recordType);

        String addressToUse = address;

        selectedPv.setAddress(addressToUse);

        model.updatePvList();
    }

    private void validatePv(PV pv) {
        String name = pv.displayName();
        IO mode = pv.recordType().io();
        String address = pv.address();

        for (PV otherPv : model.getList()) {
            if (otherPv == pv) {
                continue;
            }

            if (otherPv.displayName().equals(pv.displayName())) {
                if (otherPv.recordType().io().equals(pv.recordType().io())) {
                    setError(true, String.format(NON_UNIQUE_PV_ERROR, model.getComponentName(), name, mode));
                    return;
                }
            }
        }

        PvValidator addressValidator = new PvValidator();
        boolean addressValid = addressValidator.validatePvAddress(address);
        if (!addressValid) {
            setError(true, addressValidator.getErrorMessage());            
        } else {
            setError(false, null);
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
