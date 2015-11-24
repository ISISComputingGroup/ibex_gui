
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class AlarmButton extends PerspectiveButton {

	private FlashingButton flash;
	private final Display display = Display.getDefault();
	private final DataBindingContext bindingContext = new DataBindingContext();
	
	private final AlarmCountViewModel model;
	
	public AlarmButton(Composite parent, final String perspective) {
		super(parent, perspective);
		
		flash = new FlashingButton(this, display);
		flash.setDefaultColour(this.getBackground());
	
		model = new AlarmCountViewModel(logCounter);
		bindingContext.bindValue(WidgetProperties.text().observe(this), BeanProperties.value("text").observe(model));
		model.addPropertyChangeListener("hasMessages", new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateFlashing();
			}
		});
	}
	
	@Override
	protected void mouseEnterAction() {
		flash.stop();
		super.mouseEnterAction();
	}
	
	@Override
	protected void mouseExitAction() {
		super.mouseExitAction();
		updateFlashing();
	}
	
	@Override
	protected void mouseClickAction() {
		logCounter.stop();
		logCounter.resetCount();
	}
	
	private void updateFlashing() {
		if (model.hasMessages()) {
			flash.start();
		} else {
			flash.stop();
		}
	}
}
