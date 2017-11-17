
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

public class Spectrum extends ModelObject {

	private int number;
	private int period;
	private double[] xData = new double[2];
	private double[] yData = new double[2];
	private int spectrumYAxisTypeSelectionIndex = 0;
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int value) {
		firePropertyChange("number", number, number = value);
	}
	
	public int getPeriod() {
		return period;
	}
	
	public void setPeriod(int value) {
		firePropertyChange("period", period, period = value);
	}
	
	public double[] xData() {
		return xData;
	}
		
	public double[] yData() {
		return yData;
	}
	
	public void setTypeSelectionIndex(int type) {
		System.out.println("Set the thing to " + type);
		firePropertyChange("typeSelectionIndex", spectrumYAxisTypeSelectionIndex, spectrumYAxisTypeSelectionIndex = type);
	}
	
	public int getTypeSelectionIndex() {
		return spectrumYAxisTypeSelectionIndex;
	}

	
	protected void setXData(double[] value) {
		firePropertyChange("xData", xData, xData = value);
	}
	
	protected void setYData(double[] value) {
		System.out.println("Setting data, " + spectrumYAxisTypeSelectionIndex);
		if (spectrumYAxisTypeSelectionIndex == 0) {
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
