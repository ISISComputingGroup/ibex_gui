
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

package uk.ac.stfc.isis.ibex.configserver;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;

public class EditableIocState extends IocState {
	
    private final Observer<String> descriptionObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			EditableIocState.super.setDescription(value);
		}
	};
	
	private final Writer<String> setDescription;

	public EditableIocState(
			IocState iocState, 
			ForwardingObservable<String> description,
			Writer<String> setDescription) {
		super(iocState);
		description.addObserver(descriptionObserver);
		this.setDescription = setDescription;
	}
	
	@Override
	public void setDescription(String description) {
		setDescription.write(description);
	}
}
