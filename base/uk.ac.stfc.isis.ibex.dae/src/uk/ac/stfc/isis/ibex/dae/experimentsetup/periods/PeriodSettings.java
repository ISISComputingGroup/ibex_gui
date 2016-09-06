
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PeriodSettings extends ModelObject {
	
	private List<Period> periods = new ArrayList<>();
	private PeriodSetupSource setupSource = PeriodSetupSource.PARAMETERS;
    private String periodFile = "";
    private String newPeriodFile = "";
	private PeriodControlType periodType = PeriodControlType.SOFTWARE;
	private int softwarePeriods;
	private double hardwarePeriods;
	private double outputDelay;
	
	public List<Period> getPeriods() {
		return periods;
	}
	
	public void setPeriods(List<Period> value) {
		firePropertyChange("periods", periods, periods = value);
	}
	
	public PeriodSetupSource getSetupSource() {
		return setupSource;
	}
	
	public void setSetupSource(PeriodSetupSource value) {
		firePropertyChange("setupSource", setupSource, setupSource = value);
	}

    /**
     * Get the path for the currently set period file.
     * 
     * @return the file path.
     */
    public String getPeriodFile() {
        return periodFile;
    }

    /**
     * Set the path for the current period file.
     * 
     * @param value the file path.
     */
    public void setPeriodFile(String value) {
        firePropertyChange("periodFile", periodFile, periodFile = value);
    }

    /**
     * Get the path for the new period file (to be set as current file once
     * changes are applied).
     * 
     * @return the file path.
     */
    public String getNewPeriodFile() {
        return newPeriodFile;
	}

    /**
     * Set the path for the new period file (to be set as current file once
     * changes are applied).
     * 
     * @param value the file path.
     */
    public void setNewPeriodFile(String value) {
        firePropertyChange("newPeriodFile", newPeriodFile, newPeriodFile = value);
	}
	
	public PeriodControlType getPeriodType() {
		return periodType;
	}
	
	public void setPeriodType(PeriodControlType value) {
		firePropertyChange("periodType", periodType, periodType = value);
	}
	
	public int getSoftwarePeriods() {
		return softwarePeriods;
	}
	
	public void setSoftwarePeriods(int value) {
		firePropertyChange("softwarePeriods", softwarePeriods, softwarePeriods = value);
	}
	
	public double getHardwarePeriods() {
		return hardwarePeriods;
	}
	
	public void setHardwarePeriods(double value) {
		firePropertyChange("hardwarePeriods", hardwarePeriods, hardwarePeriods = value);
	}
	
	public double getOutputDelay() {
		return outputDelay;
	}
	
	public void setOutputDelay(double value) {
		firePropertyChange("outputDelay", outputDelay, outputDelay = value);
	}		
}
