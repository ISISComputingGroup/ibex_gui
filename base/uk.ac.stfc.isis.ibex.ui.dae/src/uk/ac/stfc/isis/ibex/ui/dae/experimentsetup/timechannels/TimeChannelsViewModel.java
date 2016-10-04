
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.CalculationMethod;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeChannels;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegime;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeUnit;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Model of time channel settings read by the GUI.
 */
public class TimeChannelsViewModel extends ModelObject {

    private TimeChannels model;
    private String instrumentName;

    /**
     * Binds Listeners to the time channel settings model used to update the
     * GUI.
     * 
     * @param timeChannels the time channel settings model
     */
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

    /**
     * A list of all time regimes used to manually define time channel settings.
     * 
     * @return the list of time regimes
     */
    public List<TimeRegime> timeRegimes() {
        return model.timeRegimes();
    }


    /**
     * Returns the path to the time channel file currently in use.
     * 
     * @return the file path.
     */
    public String getTimeChannelFile() {
        return model.getTimeChannelFile();
    }

    /**
     * Sets a new time channel file in the settings (does not take effect until
     * changes are applied).
     * 
     * @param value the path to the new time channel file.
     */
    public void setNewTimeChannelFile(String value) {
        model.setNewTimeChannelFile(value);
    }

    /**
     * Returns a string array containing paths to all time channel configuration
     * files available to this instrument.
     * 
     * @return the file paths as string array
     */
    public String[] getTimeChannelFileList() {
        String[] items = valueOrEmpty(model.getTimeChannelFileList());
        items = items.length != 0 ? items : new String[] {
                "None found in C:\\Instrument\\Settings\\config\\" + instrumentName
                        + "\\configurations\\tcb\\ (file name must contain \"tcb\")." };
        return addBlank(items);
    }

    /**
     * Sets the list of time channel configuration files available.
     * 
     * @param files a collection containing the available time channel files
     */
    public void setTimeChannelFileList(UpdatedValue<Collection<String>> files) {
        model.setTimeChannelFileList(files);
    }

    /**
     * Returns the method used to determine time channel calculation parameters
     * (either read from file or specify parameters manually).
     * 
     * @return the calculation method
     */
    public CalculationMethod getCalculationMethod() {
        return model.calculationMethod();
    }

    /**
     * Sets the method used to determine time channel calculation parameters
     * (either read from file or specify parameters manually).
     * 
     * @param method the calculation method
     */
    public void setCalculationMethod(CalculationMethod method) {
        model.setCalculationMethod(method);
    }

    /**
     * Returns the time unit used for time channel calculations (micro- or
     * nanoseconds). Converts from time unit value to list position for binding
     * to a dropdown selection.
     * 
     * @return the index of the time unit
     */
    public int getTimeUnit() {
        return model.timeUnit().ordinal();
    }

    /**
     * Sets the time unit used for time channel calculations (micro- or
     * nanoseconds). Converts from list position to time unit value.
     * 
     * @param index the index of the time unit
     */
    public void setTimeUnit(int index) {
        TimeUnit value = TimeUnit.values()[index];
        model.setTimeUnit(value);
    }

    /**
     * Returns a string array from a string collection, or an empty array if the
     * input is null.
     * 
     * @param updated the string collection.
     * @return the resulting array.
     */
    private String[] valueOrEmpty(UpdatedValue<Collection<String>> updated) {
        Collection<String> value = updated.getValue();
        return value != null ? value.toArray(new String[0]) : new String[0];
    }

    /**
     * Adds a blank option to the list for displaying in a drop down menu in the
     * GUI.
     * 
     * @param files a list of files.
     * @return the list of files with a blank entry added at the beginning.
     */
    private String[] addBlank(String[] tables) {
        String[] result = new String[tables.length + 1];
        result[0] = " ";
        for (int i = 0; i < tables.length; i++) {
            result[i + 1] = tables[i];
        }
        return result;
    }

    /**
     * Binds an observer to the observable holding the name of the current
     * instrument.
     * 
     * @param instrumentName the name of the current instrument.
     */
    public void bindInstName(ForwardingObservable<String> instrumentName) {
        this.instrumentName = instrumentName.getValue();
        instrumentName.addObserver(instrumentAdapter);
    }

    private final BaseObserver<String> instrumentAdapter = new BaseObserver<String>() {

        @Override
        public void onValue(String value) {
            setInstrumentName(value);
        }

        @Override
        public void onError(Exception e) {
            // handle
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            // handle
        }

    };

    /**
     * Updates the local variable holding the instrument name when the value
     * changes.
     * 
     * @param instrumentName the name of the new instrument.
     */
    public void setInstrumentName(String instrumentName) {
        firePropertyChange("instrumentName", this.instrumentName, this.instrumentName = instrumentName);
    }
}
