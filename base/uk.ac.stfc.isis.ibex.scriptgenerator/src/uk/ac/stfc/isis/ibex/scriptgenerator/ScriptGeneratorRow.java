
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

package uk.ac.stfc.isis.ibex.scriptgenerator;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A row of data to be inserted into a ScriptGeneratorTable.
 */
public class ScriptGeneratorRow extends ModelObject {
	protected Double position;
	protected Double trans;
	protected Double transWait; 
	protected Double sans;
	protected Double sansWait; 
	protected Double period;
	protected String sampleName; 
	protected Double thickness; 
	private boolean wasNull;
	
	public ScriptGeneratorRow(Double position, Double trans, Double transWait, Double sans, Double sansWait, Double period, 
			String sampleName, Double thickness, String script) {
		this.position = position;
		this.trans = trans;
		this.transWait = transWait;
		this.sans = sans;
		this.sansWait = sansWait;
		this.period = period;
		this.sampleName = sampleName;
		this.thickness = thickness;
		this.wasNull = false;
	}
	
	public ScriptGeneratorRow() {
		this.sampleName = "";
		this.wasNull = true;
	}
	
	public Double getPosition() {
		return position;
	}
	
	public void setPosition(Double position) {
		this.wasNull = false;
		firePropertyChange("position", this.position, this.position = position);
	}

	public Double getTrans() {
		return trans;
	}
	
	public void setTrans(Double trans) {
		this.wasNull = false;
		firePropertyChange("trans", this.trans, this.trans = trans);
	}
	
	public Double getTransWait() {
		return transWait;
	}
	
	public void setTransWait(Double transWait) {
		this.wasNull = false;
		firePropertyChange("transWait", this.transWait, this.transWait = transWait);
	}
	
	public Double getSans() {
		return sans;
	}
	
	public void setSans(Double sans) {
		this.wasNull = false;
		firePropertyChange("sans", this.sans, this.sans = sans);
	}
	
	public Double getSansWait() {
		return sansWait;
	}
	
	public void setSansWait(Double sansWait) {
		this.wasNull = false;
		firePropertyChange("sansWait", this.sansWait, this.sansWait = sansWait);
	}
	
	public Double getPeriod() {
		return period;
	}
	
	public void setPeriod(Double period) {
		this.wasNull = false;
		firePropertyChange("period", this.period, this.period = period);
	}
	
	public String getSampleName() {
		return sampleName;
	}
	
	public void setSampleName(String sampleName) {
		this.wasNull = false;
		firePropertyChange("sampleName", this.sampleName, this.sampleName = sampleName);
	}
	
	public Double getThickness() {
		return thickness;
	}
	
	public void setThickness(Double thickness) {
		this.wasNull = false;
		firePropertyChange("thickness", this.thickness, this.thickness = thickness);
	}
	
	public boolean wasNull() {
		return wasNull;
	}
}
