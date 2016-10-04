
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodControlType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSetupSource;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class PeriodsViewModel extends ModelObject {

	private PeriodSettings settings;
	private UpdatedValue<Collection<String>> periodFiles;

    private boolean internalPeriod;
    private boolean hardwarePeriod;
	
    /**
     * Sets the period settings.
     * 
     * @param settings the settings.
     */
	public void setSettings(PeriodSettings settings) {
		this.settings = settings;
		
		settings.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
			}
		});	
	}
	
    /**
     * Sets the list of period files currently available to the instrument.
     * 
     * @param files the list of currently available period files.
     */
	public void setPeriodFilesList(UpdatedValue<Collection<String>> files) {
		periodFiles = files;
		
		periodFiles.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				// Fire a property change event on periodFilesList not periodFiles
				firePropertyChange("periodFilesList", null, null);
			}
		});	
	}

    /**
     * Gets the list of period files currently available to the instrument.
     * 
     * @return the list of period files.
     */
	public String[] getPeriodFilesList() {
        String[] files = valueOrEmpty(periodFiles);
        files = files.length != 0 ? files : new String[] {
                "None found in C:\\Instrument\\Settings\\config\\[Instrument Name]\\configurations\\tables\\ (file name must contain string \"period\")." };
        return addBlank(files);
	}

    /**
     * Adds a blank option to the list for displaying in a drop down menu in the
     * GUI.
     * 
     * @param files a list of files.
     * @return the list of files with a blank entry added at the beginning.
     */
    private String[] addBlank(String[] files) {
        String[] result = new String[files.length + 1];
        result[0] = " ";
        for (int i = 0; i < files.length; i++) {
            result[i + 1] = files[i];
        }
        return result;
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
	
    public PeriodSetupSource getSetupSource() {
        return settings.getSetupSource();
	}
	
    public void setSetupSource(PeriodSetupSource source) {
        settings.setSetupSource(source);
	}
	
    /**
     * Returns the path to the period file currently in use.
     * 
     * @return the file path.
     */
	public String getPeriodFile() {
		return settings.getPeriodFile();
	}
	
    /**
     * Sets a new period file in the settings (does not take effect until
     * changes are applied).
     * 
     * @param value the path to the new period file.
     */
	public void setNewPeriodFile(String value) {
        settings.setNewPeriodFile(value);
	}
	
    /**
     * Returns the enum index of the period type.
     * 
     * @return the period type.
     */
	public int getPeriodType() {
        updateWindow(PeriodControlType.values()[settings.getPeriodType().ordinal()]);
		return settings.getPeriodType().ordinal();
	}

    /**
     * Sets the period type by enum index.
     * 
     * @param index the index of the chosen type.
     */
	public void setPeriodType(int index) {
        settings.setPeriodType(PeriodControlType.values()[index]);
        updateWindow(PeriodControlType.values()[index]);
	} 

    /**
     * Gets the number of software periods.
     * 
     * @return the number of software periods.
     */
	public int getSoftwarePeriods() {
		return settings.getSoftwarePeriods();
	}
	
    /**
     * Sets the number of software periods.
     * 
     * @param value the number of software periods.
     */
	public void setSoftwarePeriods(int value) {
		settings.setSoftwarePeriods(value);
	}
	
    /**
     * Gets the number of hardware periods.
     * 
     * @return the number of hardware periods.
     */
	public double getHardwarePeriods() {
		return settings.getHardwarePeriods();
	}

    /**
     * Sets the number of hardware periods.
     * 
     * @param value the number of hardware periods.
     */
	public void setHardwarePeriods(double value) {
		settings.setHardwarePeriods(value);
	}
	
    /**
     * Gets the length of the output delay in us.
     * 
     * @return the length of the output delay.
     */
	public double getOutputDelay() {
		return settings.getOutputDelay();
	}
	
    /**
     * Sets the length of the output delay in us.
     * 
     * @param value the length of the output delay.
     */
	public void setOutputDelay(double value) {
		settings.setOutputDelay(value);
	}
	
    /**
     * Returns the currently set list of periods.
     * 
     * @return the periods.
     */
	public List<Period> periods() {
		return settings.getPeriods();
	}

    /**
     * Sets internal parameters used by the view to show/omit elements based on
     * the chosen period type.
     * 
     * @param type the period type.
     */
    public void updateWindow(PeriodControlType type) {
        setHardwarePeriod(false);
        setInternalPeriod(false);
        if (type == PeriodControlType.HARDWARE_EXTERNAL) {
            setHardwarePeriod(true);
        } else if (type == PeriodControlType.HARDWARE_DAE) {
            setHardwarePeriod(true);
            setInternalPeriod(true);
        }
    }

    /**
     * Returns whether the period is controlled internally by the DAE.
     * 
     * @return whether period is internal.
     */
    public boolean isInternalPeriod() {
        return internalPeriod;
    }

    /**
     * Set whether the chosen period is controlled internally by the DAE.
     * 
     * @param internalPeriod whether period is internal.
     */
    public void setInternalPeriod(boolean internalPeriod) {
        firePropertyChange("internalPeriod", this.internalPeriod, this.internalPeriod = internalPeriod);
    }

    /**
     * Returns whether the period is of type hardware.
     * 
     * @return whether period is hardware period.
     */
    public boolean isHardwarePeriod() {
        return hardwarePeriod;
    }

    /**
     * Set whether the chosen period is of type hardware.
     * 
     * @param hardwarePeriod whether period is hardware period.
     */
    public void setHardwarePeriod(boolean hardwarePeriod) {
        firePropertyChange("hardwarePeriod", this.hardwarePeriod, this.hardwarePeriod = hardwarePeriod);
    }
}
