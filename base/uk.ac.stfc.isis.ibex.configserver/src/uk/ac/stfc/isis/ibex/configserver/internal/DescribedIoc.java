
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

import com.google.common.base.Strings;

/*
 * An ioc with an updating description (which is initially blank)
 */
public class DescribedIoc extends EditableIoc {
	
	private final UpdatedValue<String> description;
	private String descriptionText;
	
	private final PropertyChangeListener updatedDesc = new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			setDescription(Strings.nullToEmpty(description.getValue()));
		}
	};
	
	public DescribedIoc(EditableIoc ioc, UpdatedValue<String> description) {
		super(ioc);
		
		this.description = description;
		description.addPropertyChangeListener(updatedDesc, true);
	}
	
	public String getDescription() {
		return descriptionText;
	}

	private void setDescription(String text) {
		firePropertyChange("description", this.descriptionText, this.descriptionText = text);
	}
}
