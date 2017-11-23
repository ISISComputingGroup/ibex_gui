
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Represents a period.
 */
public class Period extends ModelObject {
	private int number;
	private PeriodType type = PeriodType.UNUSED;
	private int frames;
	private int binaryOutput;
	private String label = "";
	
    /**
     * Constructor.
     * 
     * @param number the period number
     */
	public Period(int number) {
		this.number = number;
	}
	
    /**
     * @return the period number
     */
	public int getNumber() {
		return number;
	}
	
    /**
     * @return the period type
     */
	public PeriodType getType() {
		return type;
	}
	
    /**
     * Set the period type.
     * 
     * @param value the period type
     */
	public void setType(PeriodType value) {
		firePropertyChange("type", type, type = value);
	}

    /**
     * @return the number of frames in the period
     */
	public int getFrames() {
		return frames;
	}

    /**
     * Set the number of frames in the period.
     * 
     * @param value the number of frames
     */
	public void setFrames(int value) {
		firePropertyChange("frames", frames, frames = value);
	}

    /**
     * @return the binary output
     */
	public int getBinaryOutput() {
		return binaryOutput;
	}

    /**
     * Set the binary output.
     * 
     * @param value the binary output
     */
	public void setBinaryOutput(int value) {
		firePropertyChange("binaryOutput", binaryOutput, binaryOutput = value);
	}

    /**
     * @return the period's label
     */
	public String getLabel() {
		return label;
	}

    /**
     * Set the period's label.
     * 
     * @param value the new label
     */
	public void setLabel(String value) {
		firePropertyChange("label", label, label = value);
	}
}
