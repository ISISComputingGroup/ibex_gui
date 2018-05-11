
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

package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.alarm.AlarmCounter;

/**
 * A button which relates to the alarm model from BEAST.
 */
public class AlarmButton extends PerspectiveButton {

    protected static final Color ALARM = SWTResourceManager.getColor(250, 150, 150);

	private FlashingButton flash;
	private final Display display = Display.getDefault();
	private final DataBindingContext bindingContext = new DataBindingContext();
	
    private static final Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
    private static final Font ACTIVE_FONT = SWTResourceManager.getFont("Arial", 12, SWT.BOLD);

	private final AlarmCountViewModel model;
    private String buttonPerspective;

    private static AlarmCounter alarmCounter = Alarm.getInstance().getCounter();
	
    /**
     * @param parent where the button is stored
     * @param perspective the perspective to be used by this button
     */
	public AlarmButton(Composite parent, final String perspective) {
		super(parent, perspective);
		
        this.buttonPerspective = perspective;

		flash = new FlashingButton(this, display);
		flash.setDefaultColour(this.getBackground());
	
		model = new AlarmCountViewModel(alarmCounter);

        bindingContext.bindValue(WidgetProperties.text().observe(this), BeanProperties.value("text").observe(model));
        updateFlashing();

		model.addPropertyChangeListener("hasMessages", new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateFlashing();
			}
		});

        // update flashing when count changes so that if the number goes up or
        // down a new alarm is issued
        model.addPropertyChangeListener("text", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        updateFlashing();
                    }
                });
            }
        });
	}
	
    @Override
	protected void mouseEnterAction() {
		flash.stop();
		super.mouseEnterAction();
	}

    @Override
	protected void mouseClickAction() {
	}
	
    @Override
    protected Color setColourToUse() {
        String currentPerspective = new PerspectiveModel().getCurrentPerspective();
        Color toUse = DEFOCUSSED;
        setFont(BUTTON_FONT);
        if (buttonPerspective.equals(currentPerspective)) {
            toUse = ACTIVE;
            setFont(ACTIVE_FONT);
        }
        if (model.hasMessages()) {
            toUse = ALARM;
        }
        return toUse;
    }

    /**
     * make sure that the button is flashing when required.
     */
	private void updateFlashing() {
        String currentPerspective = new PerspectiveModel().getCurrentPerspective();
        if (!buttonPerspective.equals(currentPerspective)) {
            if (model.hasMessages()) {
                flash.start();
            } else {
                flash.stop();
            }
        }
	}
}
