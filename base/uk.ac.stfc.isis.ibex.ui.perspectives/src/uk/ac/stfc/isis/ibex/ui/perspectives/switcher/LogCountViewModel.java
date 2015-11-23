
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

package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.log.LogCounter;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class LogCountViewModel extends ModelObject {
	
	private static final String IOC_LOG = "IOC Log"; 
	
	private String text;
	private boolean hasMessages;
	
	public LogCountViewModel(final LogCounter counter) {
		counter.addPropertyChangeListener("count", new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				update(counter.getCount());
			}
		});
		
		update(counter.getCount());
	}
	
	public String getText() {
		return text;
	}
	
	public boolean hasMessages() {
		return hasMessages;
	}
	
	private void update(long count) {
		setText(textForCount(count));
		setHasMessages(count > 0);		
	}
	
	private void setText(String newText) {
		firePropertyChange("text", text, text = newText);
	}
	
	private void setHasMessages(boolean updated) {
		firePropertyChange("hasMessages", hasMessages, hasMessages = updated);
	}
	
	private String textForCount(long count) {
		return IOC_LOG + optionalCount(count);
	}

	private String optionalCount(Long count) {
		if (count == 0) {
			return "";
		}
		
		String displayedCount = count > 100 ? "100+" : count.toString();
		return " (" + displayedCount + ")";
	}
}
