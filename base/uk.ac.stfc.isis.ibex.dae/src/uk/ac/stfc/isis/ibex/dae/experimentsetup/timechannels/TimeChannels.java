
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class TimeChannels extends ModelObject {

	private List<TimeRegime> timeRegimes = new ArrayList<>();
    private UpdatedValue<Collection<String>> timeChannelFileList;
	private String timeChannelFile = "";
	private CalculationMethod calculationMethod = CalculationMethod.SPECIFY_PARAMETERS;
	private TimeUnit timeUnit = TimeUnit.MICROSECONDS;
	
	public List<TimeRegime> timeRegimes() {
		return timeRegimes;
	}

	public void setTimeRegimes(List<TimeRegime> value) {
		firePropertyChange("timeRegimes", timeRegimes, timeRegimes = value);
	}
	
	public String timeChannelFile() {
		return timeChannelFile;
	}

	public void setTimeChannelFile(String value) {
		firePropertyChange("timeChannelFile", timeChannelFile, timeChannelFile = value);
	}

    /**
     * Returns a list of time channel configuration files available to the
     * instrument.
     * 
     * @return the list of time channel files
     */
    public UpdatedValue<Collection<String>> timeChannelFileList() {
        return timeChannelFileList;
    }

    /**
     * Sets the list of time channel files and adds a listener.
     * 
     * @param files
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

	public TimeUnit timeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit value) {
		firePropertyChange("timeUnit", timeUnit, timeUnit = value);
	}

	public CalculationMethod calculationMethod() {
		return calculationMethod;
	}
	
	public void setCalculationMethod(CalculationMethod value) {
		firePropertyChange("calculationMethod", calculationMethod, calculationMethod = value);
	}
}
