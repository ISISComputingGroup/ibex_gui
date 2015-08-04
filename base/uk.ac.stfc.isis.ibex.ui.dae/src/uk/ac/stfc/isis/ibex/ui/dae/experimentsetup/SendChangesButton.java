
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.model.Action;

/**
 * The button used for handling sending changes to the DAE. 
 * It binds to the Begin action which means it is only enabled in SETUP and when
 * the client has write permission for the instrument. 
 */
public class SendChangesButton extends Button {
	
	/**
	 * Constructor.
	 * 
	 * @param parent the UI parent
	 * @param style the SWT style
	 * @param beginAction the begin action to bind to
	 */
	public SendChangesButton(Composite parent, int style, Action beginAction) {
		super(parent, style);

		if (beginAction != null) {
			bind(beginAction);
		}
	}

	@Override
	protected void checkSubclass() {
		// Allow sub-classing
	}

	private void bind(final Action action) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(this), BeanProperties.value("canExecute").observe(action));
	}
}