
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument.custom;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.internal.CustomInstrumentInfo;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

/**
 * A view model for the definition of a new custom instrument info object.
 * 
 */
public class CustomIntrumentViewModel extends ErrorMessageProvider {
    private String pvPrefix = "";
    private String instrumentName;

    public CustomIntrumentViewModel(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public String getPvPrefix() {
        return pvPrefix;
    }

    public void setPvPrefix(String pvPrefix) {
        validate(pvPrefix);
        firePropertyChange("pvPrefix", this.pvPrefix, this.pvPrefix = pvPrefix);
    }

    public InstrumentInfo getSelectedInstrument() {
        return new CustomInstrumentInfo(instrumentName, pvPrefix);
    }

    public void validate() {
        validate(pvPrefix);
    }

    private void validate(String pvPrefix) {
        PvValidator prefixValidator = new PvValidator();
        if (!prefixValidator.validatePvAddress(pvPrefix)) {
            setError(true, prefixValidator.getErrorMessage());
        } else {
            clearError();
        }
    }
}
