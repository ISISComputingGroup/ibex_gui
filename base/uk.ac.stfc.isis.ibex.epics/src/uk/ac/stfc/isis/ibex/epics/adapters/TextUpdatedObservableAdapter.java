
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

package uk.ac.stfc.isis.ibex.epics.adapters;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;

public class TextUpdatedObservableAdapter extends UpdatedObservableAdapter<String> {

	private static final String UNKNOWN = "Unknown";
	private static final String EMPTY = "";
	
	public TextUpdatedObservableAdapter(Observable<String> observable) {
		super(new ForwardingObservable<>(observable));
	}
	
	public TextUpdatedObservableAdapter(
			ForwardingObservable<String> observable) {
		super(observable);
	}
	
	@Override
	public void setValue(String value) { 
		super.setValue(Strings.isNullOrEmpty(value) ? EMPTY : value);	
	}

	@Override
	protected void error(Exception e) {
		setValue(UNKNOWN);
	}

	@Override
	protected void connectionChanged(boolean isConnected) {
		if (!isConnected) {
			setValue(UNKNOWN);
		}
	}
}
