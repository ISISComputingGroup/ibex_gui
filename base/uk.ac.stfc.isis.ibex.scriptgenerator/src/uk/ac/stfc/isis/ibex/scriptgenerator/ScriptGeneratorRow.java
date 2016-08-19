
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

public class ScriptGeneratorRow extends ModelObject {
	private int position; // drop-down?
	private int trans;
	private int transWait; // drop-down
	private int sans;
	private int sansWait; // drop-down
	private int period;
	private String sampleName; 
	private int thickness; 
	private String script;
	private PythonBuilder builder;
	
	public ScriptGeneratorRow(int position, int trans, int transWait, int sans, int sansWait, int period, 
			String sampleName, int thickness, String script, PythonBuilder builder) {
		this.position = position;
		this.trans = trans;
		this.transWait = transWait;
		this.sans = sans;
		this.sansWait = sansWait;
		this.period = period;
		this.sampleName = sampleName;
		this.thickness = thickness;
		this.script = script;
		builder = new PythonBuilder();
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		firePropertyChange("position", this.position, this.position = position);
		builder.setPosition(position);
		setScript(builder.getScript());
	}

	public int getTrans() {
		return trans;
	}
	
	public void setTrans(int trans) {
		firePropertyChange("trans", this.trans, this.trans = trans);
		builder.setTrans(trans);
		setScript(builder.getScript());
	}
	
	public int getTransWait() {
		return transWait;
	}
	
	public void setTransWait(int transWait) {
		firePropertyChange("wait", this.transWait, this.transWait = transWait);
		builder.setTransWait(transWait);
		setScript(builder.getScript());
	}
	
	public String getScript() {
		return script;
	}
	
	private void setScript(String script) {
		firePropertyChange("script", this.script, this.script = script);
	}
}
