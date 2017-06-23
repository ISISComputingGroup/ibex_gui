/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.dae.detectordiagnostics;

import java.util.List;

/**
 * Bindings that need to be made to a Detector diagnostics view model.
 */
public interface IDetectorDiagnosticsViewModelBinding {

    /**
     * Updates the list of spectrum numbers.
     * 
     * @param spectrumNumbersList the list of spectrum numbers
     */
    void updateSpectrumNumbers(List<Integer> spectrumNumbersList);

    /**
     * Updates the list of count rates.
     * 
     * @param countRatesList the list of count rates
     */
    void updateCountRates(List<Double> countRatesList);

    /**
     * Updates the list of maximum counts.
     * 
     * @param maxSpecBinCount the list of maximum counts
     */
    void updateMaxSpecBinCount(List<Integer> maxSpecBinCount);

    /**
     * Updates the list of integrals.
     * 
     * @param integralsList the list of integrals
     */
    void updateIntegrals(List<Integer> integralsList);

    /**
     * Sets the integral time range from.
     *
     * @param value the new integral time range from
     */
    void setIntegralTimeRangeFrom(String value);

    /**
     * Sets the integral time range to.
     *
     * @param value the new integral time range to
     */
    void setIntegralTimeRangeTo(String value);

    /**
     * Sets the max frames.
     *
     * @param value the new max frames
     */
    void setMaxFrames(Integer value);

    /**
     * Sets the diagnostics enabled. (This is the readback to set use the
     * enabledDiagnostics property)
     *
     * @param isEnabled
     *            the new diagnostics enabled
     */
    void setDiagnosticsEnabled(boolean isEnabled);

    /**
     * Update the number of spectra that should be displayed.
     * 
     * @param size
     *            the new number of spectra
     */
    void updateSpectraCount(int size);

    /**
     * Sets the number of spectra.
     *
     * @param value
     *            the new number of spectra
     */
    void setNumberOfSpectra(Integer value);

    /**
     * Sets the starting spectrum number.
     *
     * @param value
     *            the new starting spectrum number
     */
    void setStartingSpectrumNumber(Integer value);

    /**
     * Sets the period.
     * 
     * @param value
     *            the period
     */
    void setPeriod(Integer value);

    /**
     * Sets the spectra type.
     * 
     * @param type
     *            the type
     */
    void setSpectraType(Integer type);
}
