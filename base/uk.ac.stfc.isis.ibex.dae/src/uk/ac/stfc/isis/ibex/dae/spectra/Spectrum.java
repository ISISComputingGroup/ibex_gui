
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

package uk.ac.stfc.isis.ibex.dae.spectra;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The view model for a single spectrum plot.
 */
public class Spectrum extends ModelObject {

	private int number;
	private int period;
	private double[] xData = new double[2];
	private double[] yData = new double[2];
	
	private int spectrumYAxisTypeSelectionIndex = 0;
	
	/**
	 * Gets the spectrum number.
	 * @return the spectrum number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Sets the spectrum number.
	 * @param value the new spectrum number
	 */
	public void setNumber(int value) {
		firePropertyChange("number", number, number = value);
	}
	
	/**
	 * Gets the period of this spectrum.
	 * @return the period
	 */
	public int getPeriod() {
		return period;
	}
	
	/**
	 * The period of this spectrum.
	 * @param value the new period
	 */
	public void setPeriod(int value) {
		firePropertyChange("period", period, period = value);
	}
	
	/**
	 * The X data of this spectrum.
	 * @return the X data.
	 */
	public double[] xData() {
		return xData;
	}
		
	/**
	 * The Y data of this spectrum.
	 * @return the Y data.
	 */
	public double[] yData() {
		return yData;
	}
	
	/**
	 * Sets the selection index of the Y axis type.
	 * 
	 * This is an index into the {@link SpectrumYAxisTypes} enum.
	 */
	public void setTypeSelectionIndex(int type) {
		firePropertyChange("typeSelectionIndex", spectrumYAxisTypeSelectionIndex, spectrumYAxisTypeSelectionIndex = type);
	}
	
	/**
	 * Gets the selection index of the Y axis type.
	 * 
	 * This is an index into the {@link SpectrumYAxisTypes} enum.
	 * 
	 * @return the type selection index
	 */
	public int getTypeSelectionIndex() {
		return spectrumYAxisTypeSelectionIndex;
	}


	/**
	 * Sets the X data array for this spectrum.
	 * @param value the data to set.
	 */
	public void setXData(double[] value) {
		firePropertyChange("xData", xData, xData = value);
	}
	
	/**
	 * Sets the Y data array for this spectrum.
	 * @param value the data to set.
	 */
	public void setYData(double[] value) {
		if (SpectrumYAxisTypes.values()[spectrumYAxisTypeSelectionIndex] == SpectrumYAxisTypes.ABSOLUTE_COUNTS) {
			for (int i = 0; i<value.length; i++) {
				try {
					value[i] *= Math.abs(xData[i+1] - xData[i]);
				} catch (ArrayIndexOutOfBoundsException e){
					value[i] = 0;
				}
			}
		}
		firePropertyChange("yData", yData, yData = value);
	}
}
