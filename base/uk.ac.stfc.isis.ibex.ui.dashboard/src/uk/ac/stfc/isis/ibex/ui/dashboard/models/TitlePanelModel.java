
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

package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.BooleanWritableObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * The model for title & users in the dashboard.
 */
public class TitlePanelModel extends Closer {
	
	private final UpdatedObservableAdapter<String> title;
	private final UpdatedObservableAdapter<String> users;
	
	/**
	 * An observable for whether to display the title on the web dashboard.
	 */
	public final BooleanWritableObservableAdapter displayTitle;

	
	/**
	 * Create the model.
	 * @param title an observable on the title
	 * @param users an observable on the users
	 * @param displayTitle is an observable and writer on the displayTitle
	 * @param displayTitleSetter is an observable and writer on the displayTitle visibility
	 */
	public TitlePanelModel(ForwardingObservable<String> title, ForwardingObservable<String> users, ForwardingObservable<Boolean> displayTitle, Writable<Long> displayTitleSetter) {
		this.title = registerForClose(new TextUpdatedObservableAdapter(title));
		this.users = registerForClose(new TextUpdatedObservableAdapter(users));
        this.displayTitle = registerForClose(new BooleanWritableObservableAdapter(displayTitleSetter, displayTitle));
        


	}
	
	/**
	 * Gets the title.
	 * @return the title
	 */
	public UpdatedValue<String> title() {
		return title;
	}

	/**
	 * Gets the users.
	 * @return the users
	 */
	public UpdatedValue<String> users() {
		return users;
	}
	
	/**
	 * Gets the displayTitle.
	 * @return the displayTitle
	 */
	public BooleanWritableObservableAdapter displayTitle() {
		return displayTitle;
	}


}
