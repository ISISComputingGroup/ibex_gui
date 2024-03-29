
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Observable object of time channel model.
 */
public class TimeChannels extends ModelObject {

	private List<TimeRegime> timeRegimes = new ArrayList<>();
    private UpdatedValue<Collection<String>> timeChannelFileList;
    private String timeChannelFile = "";
    private String newTimeChannelFile = "";
	private CalculationMethod calculationMethod = CalculationMethod.SPECIFY_PARAMETERS;
	private TimeUnit timeUnit = TimeUnit.MICROSECONDS;
	
	/**
	 * Gets the time regimes.
	 * @return the time regimes
	 */
	public List<TimeRegime> timeRegimes() {
		return timeRegimes;
	}

	/**
	 * Sets the time regimes.
	 * @param value the time regimes
	 */
	public void setTimeRegimes(List<TimeRegime> value) {
		firePropertyChange("timeRegimes", timeRegimes, timeRegimes = value);
	}

    /**
     * Get the path for the currently set time channel file.
     * 
     * @return the file path.
     */
    public String getTimeChannelFile() {
        return timeChannelFile;
    }

    /**
     * Set the path for the current time channel file.
     * 
     * @param value the file path.
     */
    public void setTimeChannelFile(String value) {
        firePropertyChange("timeChannelFile", timeChannelFile, timeChannelFile = value);
    }

    /**
     * Get the path for the new time channel file (to be set as current file
     * once changes are applied).
     * 
     * @return the file path.
     */
    public String getNewTimeChannelFile() {
        return newTimeChannelFile;
	}

    /**
     * Set the path for the new time channel file (to be set as current file
     * once changes are applied).
     * 
     * @param value the file path.
     */
    public void setNewTimeChannelFile(String value) {
        firePropertyChange("newTimeChannelFile", newTimeChannelFile, newTimeChannelFile = value);
	}

    /**
     * Returns a list of time channel configuration files available to the
     * instrument.
     * 
     * @return the list of time channel files
     */
    public UpdatedValue<Collection<String>> getTimeChannelFileList() {
        return timeChannelFileList;
    }

    /**
     * Sets the list of time channel files and adds a listener.
     * 
     * @param files the collection of available time channel files.
     */
    public void setTimeChannelFileList(UpdatedValue<Collection<String>> files) {
        timeChannelFileList = files;

        timeChannelFileList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange("timeChannelFileList", null, null);
            }
        });
    }

    /**
     * Returns the time unit used for time channel calculations (micro- or
     * nanoseconds).
     * 
     * @return the time unit
     */
	public TimeUnit timeUnit() {
		return timeUnit;
	}

    /**
     * Sets the time unit used for time channel calculations (micro- or
     * nanoseconds).
     * 
     * @param value the time unit
     */
	public void setTimeUnit(TimeUnit value) {
		firePropertyChange("timeUnit", timeUnit, timeUnit = value);
	}

    /**
     * Returns the method used to determine time channel calculation parameters
     * (either read from file or specify parameters manually).
     * 
     * @return the calculation method
     */
	public CalculationMethod calculationMethod() {
		return calculationMethod;
	}

    /**
     * Sets the method used to determine time channel calculation parameters
     * (either read from file or specify parameters manually).
     * 
     * @param method the calculation method
     */
	public void setCalculationMethod(CalculationMethod method) {
		firePropertyChange("calculationMethod", calculationMethod, calculationMethod = method);
	}
}
