
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.alarm.AlarmCounter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * A button which relates to the alarm model from BEAST.
 */
public class AlarmButton extends PerspectiveButton {

	private final DataBindingContext bindingContext = new DataBindingContext();

	private final AlarmButtonViewModel model;

    private static AlarmCounter alarmCounter = Alarm.getInstance().getCounter();
	
    /**
     * @param parent where the button is stored
     * @param perspective the perspective to be used by this button
     */
	@SuppressWarnings("unchecked")
	public AlarmButton(Composite parent, MPerspective perspective, PerspectivesProvider perspectivesProvider) {
		super(parent, perspective, perspectivesProvider);
	
		model = new AlarmButtonViewModel(alarmCounter);

        bindingContext.bindValue(WidgetProperties.text().observe(this), BeanProperties.value("text").observe(model));
        bindingContext.bindValue(WidgetProperties.background().observe(this), BeanProperties.value("color").observe(model));
	}
}
