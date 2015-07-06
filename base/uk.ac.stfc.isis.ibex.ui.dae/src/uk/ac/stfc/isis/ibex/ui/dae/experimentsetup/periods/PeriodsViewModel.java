
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodControlType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSetupSource;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PeriodsViewModel extends ModelObject {

	private PeriodSettings settings;
	
	public void setSettings(PeriodSettings settings) {
		this.settings = settings;
		
		settings.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
			}
		});	
	}
	
	public int getSetupSource() {
		return settings.getSetupSource().ordinal();
	}
	
	public void setSetupSource(int index) {
		settings.setSetupSource(PeriodSetupSource.values()[index]);
	}
	
	public String getPeriodFile() {
		return settings.getPeriodFile();
	}
	
	public void setPeriodFile(String value) {
		settings.setPeriodFile(value);
	}
	
	public int getPeriodType() {
		return settings.getPeriodType().ordinal();
	}
	
	public void setPeriodType(int index) {
		settings.setPeriodType(PeriodControlType.values()[index]);
	} 
	
	public int getSoftwarePeriods() {
		return settings.getSoftwarePeriods();
	}
	
	public void setSoftwarePeriods(int value) {
		settings.setSoftwarePeriods(value);
	}
	
	public double getHardwarePeriods() {
		return settings.getHardwarePeriods();
	}
	
	public void setHardwarePeriods(double value) {
		settings.setHardwarePeriods(value);
	}
	
	public double getOutputDelay() {
		return settings.getOutputDelay();
	}
	
	public void setOutputDelay(double value) {
		settings.setOutputDelay(value);
	}
	
	public List<Period> periods() {
		return settings.getPeriods();
	}
}
