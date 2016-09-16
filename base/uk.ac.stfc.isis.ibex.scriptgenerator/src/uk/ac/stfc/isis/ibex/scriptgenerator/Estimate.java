
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
 * The estimate settings.
 */
public class Estimate extends ModelObject {
	private Integer estCountRate;
	private Integer estMoveTime;
	private String estScriptTime = "hello world";
	
	/**
	 * The default constructor.
	 * @param estCountRate the estimated count rate
	 * @param estMoveTime the estimated move time
	 * @param estScriptTime the estimated script time
	 */
	public Estimate(Integer estCountRate, Integer estMoveTime) {
		this.estCountRate = estCountRate;
		this.estMoveTime = estMoveTime;
	}
	
	/**
	 * Gets the estimated count rate.
	 * @return the estimated count rate
	 */
	public Integer getEstCountRate() {
		return estCountRate;
	}

	/**
	 * Sets the estimated count rate.
	 * @param estCountRate the estimated count rate
	 */
	public void setEstCountRate(Integer estCountRate) {
		firePropertyChange("estCountRate", this.estCountRate, this.estCountRate = estCountRate);
	}

	/**
	 * Gets the estimated move time.
	 * @return the estimated move time
	 */
	public Integer getEstMoveTime() {
		return estMoveTime;
	}

	/**
	 * Sets the estimated move time.
	 * @param estMoveTime the estimated move time
	 */
	public void setEstMoveTime(Integer estMoveTime) {
		firePropertyChange("estMoveTime", this.estMoveTime, this.estMoveTime = estMoveTime);
	}

	/** 
	 * Gets the estimated script time.
	 * @return the estimated script time
	 */
	public String getEstScriptTime() {
		return estScriptTime;
	}

	/**
	 * Sets the estimated script time.
	 * @param estScriptTime the estimated script time
	 */
	public void setEstScriptTime(String estScriptTime) {
		firePropertyChange("estScriptTime", this.estScriptTime, this.estScriptTime = estScriptTime);
	}
}
