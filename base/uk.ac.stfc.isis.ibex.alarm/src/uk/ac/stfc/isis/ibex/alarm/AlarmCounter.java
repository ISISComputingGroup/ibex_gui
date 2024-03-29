
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


/**
 * 
 */
package uk.ac.stfc.isis.ibex.alarm;

import org.csstudio.alarm.beast.SeverityLevel;
import org.csstudio.alarm.beast.client.AlarmTreePV;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModelListener;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A model to provide listeners and events which interact with the alarm server.
 * Chiefly the number of active alarms.
 */
public class AlarmCounter extends ModelObject {
	private int count;

    /**
     * Instantiates a new alarm counter.
     *
     * @param alarmModel the alarm model
     */
    public AlarmCounter(final AlarmClientModel alarmModel) {
        setAlarmModel(alarmModel);
    }

    /**
     * Get count of active alarms. An alarm is also considered active if it is latched
     * but has subsequently moved out of alarm state. If there are no current alarms
     * but some unacknowledged latched alarms then we return a negative count of
     * the alarms  
     * 
     * @param alarmModel the alarm model
     * @return alarm count
     */
    public int getAlarmCount(final AlarmClientModel alarmModel) {
    	int current = 0;
        AlarmTreePV[] pvs = alarmModel.getActiveAlarms();
        int total = pvs.length; 
        for (int i = 0; i < total; ++i) {
            if (pvs[i].getCurrentSeverity() != SeverityLevel.OK) {
            	++current;
            }
        }
        return (current > 0 ? total : -total);
    }
    
    /**
     * Set the alarm model used by the alarm counter. On set count property if
     * updated.
     * 
     * @param alarmModel the alarm model; null to signal no alarm model and
     *            count will be 0.
     */
    public void setAlarmModel(final AlarmClientModel alarmModel) {
        
        if (alarmModel == null) {
            fireCountChanged(count, count = 0);
        } else {
    		alarmModel.addListener(new AlarmClientModelListener() {
    
    			@Override
    			public void newAlarmConfiguration(AlarmClientModel model) {
                    fireCountChanged(count, count = getAlarmCount(model));
    			}
    
    			@Override
    			public void serverTimeout(AlarmClientModel model) {
    			}
    
    			@Override
    			public void serverModeUpdate(AlarmClientModel model, boolean maintenanceMode) {
                    fireCountChanged(count, count = getAlarmCount(model));
    			}
    
    			@Override
    			public void newAlarmState(AlarmClientModel model, AlarmTreePV pv, boolean parentChanged) {
                    fireCountChanged(count, count = getAlarmCount(model));
    			}
    		});
            fireCountChanged(count, count = getAlarmCount(alarmModel));
        }
    }
	
	/**
     * Fire a change in the number of active alarms. Use of runnable to avoid
     * error between SWT and BEAST.
     * 
     * @param prevCount the previous count
     * @param newCount the new count
     */
    private void fireCountChanged(final int prevCount, final int newCount) {

        // If the count hasn't changed, don't do anything.
        if (prevCount == newCount) {
            return;
        }

		Display.getDefault().asyncExec(new Runnable() {  
			@Override
            public void run() {
                firePropertyChange("alarmCount", prevCount, newCount);
            }
		});
	}

	/**
     * @return count of active alarms
     */
	public int getCount() {
		return count;
	}

}
