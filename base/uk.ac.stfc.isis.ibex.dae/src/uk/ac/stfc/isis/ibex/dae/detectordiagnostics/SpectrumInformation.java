 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.dae.detectordiagnostics;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * This class holds information about one row in the detector diagnostics table.
 * 
 * It updates via databinding so fires property changes when it's data changes.
 */
public class SpectrumInformation extends ModelObject {
    
    private Integer spectrumNumber;
    private Double countRate;
    private Integer integral;
    private Integer maxSpecBinCount;
    
    /**
     * Gets the spectrum number.
     *
     * @return the spectrum number
     */
    public Integer getSpectrumNumber() {
        return spectrumNumber;
    }
    
    /**
     * Gets the count rate.
     *
     * @return the count rate
     */
    public Double getCountRate() {
        return countRate;
    }
    
    /**
     * Gets the integral.
     *
     * @return the integral
     */
    public Integer getIntegral() {
        return integral;
    }
    
    /**
     * Gets the max spec bin count.
     *
     * @return the max spec bin count
     */
    public Integer getMaxSpecBinCount() {
        return maxSpecBinCount;
    }
    
    /**
     * Sets the spectrum number.
     *
     * @param value the new spectrum number
     */
    public void setSpectrumNumber(Integer value) {
        firePropertyChange("spectrumNumber", this.spectrumNumber, this.spectrumNumber = value);
    }
    

    /**
     * Sets the count rate.
     *
     * @param value the new count rate
     */
    public void setCountRate(Double value) {
        firePropertyChange("countRate", this.countRate, this.countRate = value);
    }
    
    /**
     * Sets the max spec bin count.
     *
     * @param value the new max spec bin count
     */
    public void setMaxSpecBinCount(Integer value) {
        firePropertyChange("maxSpecBinCount", this.maxSpecBinCount, this.maxSpecBinCount = value);
    }
    
    /**
     * Sets the integral.
     *
     * @param value the new integral
     */
    public void setIntegral(Integer value) {
        firePropertyChange("maxSpecBinCount", this.integral, this.integral = value);
    }
   
}
