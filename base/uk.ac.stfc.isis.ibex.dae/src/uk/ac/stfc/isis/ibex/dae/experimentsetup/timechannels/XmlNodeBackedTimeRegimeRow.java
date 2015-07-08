
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import org.w3c.dom.Node;

public class XmlNodeBackedTimeRegimeRow extends TimeRegimeRow {

	private Node from;
	private Node to;
	private Node step;
	private Node mode;
	
	public XmlNodeBackedTimeRegimeRow(Node from, Node to, Node step, Node mode) {
		this.from = from;
		this.to = to;
		this.step = step;
		this.mode = mode;
		
		super.setFrom(Double.parseDouble(from.getTextContent()));
		super.setTo(Double.parseDouble(to.getTextContent()));
		super.setStep(Double.parseDouble(step.getTextContent()));
		
		int index = Integer.parseInt(mode.getTextContent());
		super.setMode(TimeRegimeMode.values()[index]);
	}
	
	@Override
	public void setFrom(double value) {
		super.setFrom(value);		
		setDouble(from, value);
	}
	
	@Override
	public void setTo(double value) {
		super.setTo(value);
		setDouble(to, value);
	}
	
	@Override
	public void setStep(double value) {
		super.setStep(value);
		setDouble(step, value);
	}
	
	@Override
	public void setMode(TimeRegimeMode value) {
		super.setMode(value);
		setInt(mode, value.ordinal());
	}
	
	private static void setDouble(Node node, double value) {
		node.setTextContent(String.format("%f", value));
	}
	
	private static void setInt(Node node, int value) {
		node.setTextContent(String.format("%d", value));
	}
}
