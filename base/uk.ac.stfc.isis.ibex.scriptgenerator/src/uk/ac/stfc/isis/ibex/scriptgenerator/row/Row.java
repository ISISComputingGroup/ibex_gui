
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

package uk.ac.stfc.isis.ibex.scriptgenerator.row;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A row of data to be inserted into a ScriptGeneratorTable.
 */
public class Row extends ModelObject {
	private String position;
	private Double transWaitValue;
	private WaitUnit transWaitUnit; 
    private Double sansWaitValue;
	private WaitUnit sansWaitUnit; 
	private Double period;
	private String sampleName; 
	private Double thickness; 
	private boolean wasNull;
	
	/**
	 * The default constructor.
	 * @param position the position
	 * @param transWaitValue the trans wait time value
	 * @param transWaitUnit the unit of measurement for trans
	 * @param sansWaitValue the sans wait time value
	 * @param sansWaitUnit the unit of measurement for sans
	 * @param period the period
	 * @param sampleName the name of the sample
	 * @param thickness the thickness of the sample
	 */
	public Row(String position, Double transWaitValue, WaitUnit transWaitUnit, Double sansWaitValue, WaitUnit sansWaitUnit, Double period, 
			String sampleName, Double thickness) {
		this.position = position;
		this.transWaitValue = transWaitValue;
		this.transWaitUnit = transWaitUnit;
        this.sansWaitValue = sansWaitValue;
		this.sansWaitUnit = sansWaitUnit;
		this.period = period;
		this.sampleName = sampleName;
		this.thickness = thickness;
		this.wasNull = false;
	}
	
	/**
	 * Secondary constructor for creating a blank row.
	 */
	public Row() {
		this.position = "";
		this.sampleName = "";
		this.wasNull = true;
	}
	
	/**
	 * Gets the position.
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	
	/**
	 * Sets the position.
	 * @param position the position
	 */
	public void setPosition(String position) {
		this.wasNull = false;
		firePropertyChange("position", this.position, this.position = position);
	}

	/**
	 * Gets the trans value.
	 * @return the trans value
	 */
	public Double getTransWaitValue() {
		return transWaitValue;
	}
	
	/**
	 * Sets the trans value.
	 * @param transWaitValue the trans value
	 */
	public void setTransWaitValue(Double transWaitValue) {
		this.wasNull = false;
        firePropertyChange("transWaitValue", this.transWaitValue, this.transWaitValue = transWaitValue);
	}
	
	/**
	 * Gets the trans wait.
	 * @return the trans wait combo selection
	 */
	public WaitUnit getTransWaitUnit() {
		return transWaitUnit;
	}
	
	/**
     * Sets the trans wait.
     * 
     * @param transWaitUnit
     *            the trans wait
     */
    public void setTransWaitUnit(WaitUnit transWaitUnit) {
		this.wasNull = false;
        firePropertyChange("transWaitUnit", this.transWaitUnit, this.transWaitUnit = transWaitUnit);
	}
	
	/**
	 * Gets the sans value.
	 * @return the sans value.
	 */
    public Double getSansWaitValue() {
        return sansWaitValue;
	}
	
	/** Sets the sans value.
	 * @param sans the sans value
	 */
    public void setSansWaitValue(Double sans) {
		this.wasNull = false;
        firePropertyChange("sansWaitValue", this.sansWaitValue, this.sansWaitValue = sans);
	}
	
	/**
	 * Gets the sans wait.
	 * @return the sans wait.
	 */
	public WaitUnit getSansWaitUnit() {
		return sansWaitUnit;
	}
	
	/**
	 * Sets the sans wait.
	 * @param sansWait the sans wait
	 */
	public void setSansWait(WaitUnit sansWait) {
		this.wasNull = false;
        firePropertyChange("sansWaitUnit", this.sansWaitUnit, this.sansWaitUnit = sansWait);
	}
	
	/** 
	 * Gets the period.
	 * @return the period
	 */
	public Double getPeriod() {
		return period;
	}
	
	/** 
	 * Sets the period.
	 * @param period the period
	 */
	public void setPeriod(Double period) {
		this.wasNull = false;
		firePropertyChange("period", this.period, this.period = period);
	}
	
	/** 
	 * Gets the sample name.
	 * @return the sample name
	 */
	public String getSampleName() {
		return sampleName;
	}
	
	/** 
	 * Sets the sample name.
	 * @param sampleName the sample name
	 */
	public void setSampleName(String sampleName) {
		this.wasNull = false;
		firePropertyChange("sampleName", this.sampleName, this.sampleName = sampleName);
	}
	
	/**
	 * Gets the sample thickness.
	 * @return the sample thickness
	 */
	public Double getThickness() {
		return thickness;
	}
	
	/**
	 * Sets the sample thickness.
	 * @param thickness the sample thickness
	 */
	public void setThickness(Double thickness) {
		this.wasNull = false;
		firePropertyChange("thickness", this.thickness, this.thickness = thickness);
	}
	
	/**
	 * Returns a boolean for whether a cell's contents were previously null.
	 * @return whether the cell was null.
	 */
	public boolean wasNull() {
		return wasNull;
	}
}
