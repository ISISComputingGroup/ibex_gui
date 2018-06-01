
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

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.alarm.AlarmCounter;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A model to provide easy access to the listeners for the interaction with the
 * alarm system; chiefly the number of active alarms.
 */
public class AlarmCountViewModel extends ModelObject {
	
	private static final String ALARM = "Alarms"; 
	private static final int MAX_ALARMS = 100;
	
	private String text;
	
    /**
     * Instantiates a new alarm count view model.
     *
     * @param alarmCounter the alarm counter model to listen to
     */
	public AlarmCountViewModel(final AlarmCounter alarmCounter) {
        alarmCounter.addPropertyChangeListener("alarmCount", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				update(alarmCounter.getCount());
			}

		});
		
        update(alarmCounter.getCount());
    }
	
    /**
     * Gets the text which displays "Alarms" and the number of alarms, e.g.
     * Alarms (10).
     * 
     * @return the text
     */
	public String getText() {
		return text;
	}
	
    /**
     * Updates the model with the new alarm count.
     *
     * @param count the current alarm count
     */
	private void update(long count) {
        String textForCount = ALARM + alarmCountAsText(count);
        setText(textForCount);
	}
	
	private void setText(String newText) {
		firePropertyChange("text", text, text = newText);
	}
	
    private String alarmCountAsText(Long count) {
		if (count == 0) {
			return "";
		}
		
		String displayedCount = count > MAX_ALARMS ? Integer.toString(MAX_ALARMS) + "+" : count.toString();
		return " (" + displayedCount + ")";
	}
}
