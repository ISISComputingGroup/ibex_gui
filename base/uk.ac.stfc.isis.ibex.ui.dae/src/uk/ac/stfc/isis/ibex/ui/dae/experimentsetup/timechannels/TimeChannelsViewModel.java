
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.CalculationMethod;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeChannels;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegime;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeUnit;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class TimeChannelsViewModel extends ModelObject {

	private TimeChannels model;
	
	public void setModel(TimeChannels timeChannels) {
		model = timeChannels;
		
		model.addPropertyChangeListener("timeRegimes", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
			}
		});
		
		model.addPropertyChangeListener("timeUnit", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
				}
			});

        model.addPropertyChangeListener("timeChannelFile", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
            }
        });

        model.addPropertyChangeListener("timeChannelFileList", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
            }
        });

        model.addPropertyChangeListener("calculationMethod", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
            }
        });
	}
	
	public List<TimeRegime> timeRegimes() {		
		return model.timeRegimes();
	}

	public String getTimeChannelFile() {
		return model.timeChannelFile();
	}
	
	public void setTimeChannelFile(String value) {
		model.setTimeChannelFile(value);
	}

    /**
     * Returns a string array containing paths to all time channel configuration
     * files available to this instrument.
     * 
     * @return the file paths as string array
     */
    public String[] getTimeChannelFileList() {
        return valueOrEmpty(model.timeChannelFileList());
    }

    /**
     * Sets the list of time channel configuration files available.
     * 
     * @param files a collection containing the available time channel files
     */
    public void setTimeChannelFileList(UpdatedValue<Collection<String>> files) {
        model.setTimeChannelFileList(files);
    }

    public CalculationMethod getCalculationMethod() {
        return model.calculationMethod();
	}

    public void setCalculationMethod(CalculationMethod value) {
		model.setCalculationMethod(value);
	}
	
	public int getTimeUnit() {
		return model.timeUnit().ordinal();
	}
	
	public void setTimeUnit(int index) {
		TimeUnit value = TimeUnit.values()[index];
		model.setTimeUnit(value);
	}

    /**
     * Used to convert an UpdatedValue with type string collection into a string
     * array.
     * 
     * @param updated the UpdatedValue object to be converted
     * @return the String array
     */
    private String[] valueOrEmpty(UpdatedValue<Collection<String>> updated) {
        Collection<String> value = updated.getValue();
        return value != null ? value.toArray(new String[0]) : new String[0];
    }
}
