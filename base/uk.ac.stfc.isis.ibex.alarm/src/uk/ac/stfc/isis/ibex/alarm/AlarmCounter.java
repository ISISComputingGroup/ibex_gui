
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

import org.csstudio.alarm.beast.client.AlarmTreePV;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModelListener;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A model to provide listeners and events which interact with the alarm server.
 */
public class AlarmCounter extends ModelObject {
	private int count;

    public AlarmCounter(final AlarmClientModel alarmModel) {
        count = 0;

		alarmModel.addListener(new AlarmClientModelListener() {

			@Override
			public void newAlarmConfiguration(AlarmClientModel model) {
			}

			@Override
			public void serverTimeout(AlarmClientModel model) {
			}

			@Override
			public void serverModeUpdate(AlarmClientModel model, boolean maintenance_mode) {
			}

			@Override
			public void newAlarmState(AlarmClientModel model, AlarmTreePV pv, boolean parent_changed) {
                fireCountChanged(count, count = alarmModel.getActiveAlarms().length);
			}
		});
    }

	public void resetCount() {
		count = 0;
	}
	
	/**
     * Use of runnable to avoid error between SWT and BEAST
     * 
     * @param prevCount the previous count
     * @param newCount the new count
     */
    private void fireCountChanged(final int prevCount, final int newCount) {
		Display.getDefault().asyncExec( new Runnable() {  
			@Override
            public void run() {
                firePropertyChange("alarmCount", prevCount, newCount);
            }
		});
	}

	/**
	 * @return count value
	 */
	public int getCount() {
		return count;
	}

}
