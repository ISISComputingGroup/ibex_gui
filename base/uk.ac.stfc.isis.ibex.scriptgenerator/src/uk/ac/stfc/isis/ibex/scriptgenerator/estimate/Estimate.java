
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

package uk.ac.stfc.isis.ibex.scriptgenerator.estimate;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.row.Row;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.Settings;

/**
 * The estimate settings.
 */
public class Estimate extends ModelObject {
	private Integer estCountRate;
	private Integer estMoveTime;
	private String estScriptTime;
	private Settings settings;
	private Collection<Row> rows;
	
	/**
	 * The default constructor.
	 * @param estCountRate the estimated count rate
	 * @param estMoveTime the estimated move time
	 */
	public Estimate(Integer estCountRate, Integer estMoveTime) {
		this.estCountRate = estCountRate;
		this.estMoveTime = estMoveTime;
	}
	
	/**
	 * Sets the table rows.
	 * @param rows the rows in the table
	 */
	public void setRows(Collection<Row> rows) {
		this.rows = rows;
	}
	
	/**
	 * Gets the settings.
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}
	
	/**
	 * Gets the table rows.
	 * @return the rows in the table
	 */
	public Collection<Row> getRows() {
		return rows;
	}
	
	/**
	 * Sets the settings.
	 * @param settings the settings
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
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
		Integer total = 0;
		
		Integer defaultRowTime = estMoveTime * 2;
		Integer minute = 60;
		Integer countRate = 3600 / estCountRate;
	
		for (Row row : rows) {
			if (row.getPosition() != "") {
				total += defaultRowTime;
				if (row.getTransWait() != null && row.getTrans() != null && row.getTrans() != 0) {
					switch(row.getTransWait()) {
						case FRAMES:
							if (row.getTrans() >= 25) {
								total += row.getTrans().intValue() / 25;
							}
							break;
						case MINUTES: 
							total += row.getTrans().intValue() * minute;
							System.out.print(total);
							break;
						case SECONDS: 
							total += row.getTrans().intValue();
							break;
						case MICROAMP: 
							total += row.getTrans().intValue() * countRate; 
							break;
					}
				}
				else { 
					total += defaultRowTime; 
				}
			}
		}
		
		Integer hrs = total / 3600;
		Integer mins = (total % 3600) / 60;
		Integer secs = total % 60;
		
		estScriptTime = String.format("%02d:%02d:%02d", hrs, mins, secs);
		
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
