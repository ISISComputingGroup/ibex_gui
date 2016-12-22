
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

package uk.ac.stfc.isis.ibex.ui.motor.views;


import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

/**
 * The viewer for an individual motor.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class MinimalMotorView extends Composite {
	
    private MinimalMotorViewModel minimalMotorViewModel;

	private DataBindingContext bindingContext = new DataBindingContext();

	private Composite motorComposite;
	private MinimalMotionIndicator indicator;
		
	private Label motorName;

	private Label value;
	private Label setpoint;

    /**
     * Constructor. Creates a new instance of the MinimalMotorView object.
     * 
     * @param parent
     *            the parent of this element
     * @param style
     *            the base style to be applied to the overview
     * @param minimalMotorViewModel
     *            the view model to be used by this view.
     */
    public MinimalMotorView(Composite parent, int style, MinimalMotorViewModel minimalMotorViewModel) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
        this.minimalMotorViewModel = minimalMotorViewModel;

		motorComposite = new Composite(this, SWT.BORDER);
		GridLayout glMotorComposite = new GridLayout(1, false);
		glMotorComposite.verticalSpacing = 2;
		glMotorComposite.marginWidth = 2;
		glMotorComposite.marginHeight = 1;
		motorComposite.setLayout(glMotorComposite);
		
		motorName = new Label(motorComposite, SWT.NONE);
		motorName.setAlignment(SWT.CENTER);
		GridData gdMotorName = new GridData(SWT.TOP, SWT.TOP, false, false, 1, 1);
		gdMotorName.minimumWidth = 80;
		gdMotorName.widthHint = 80;
		motorName.setLayoutData(gdMotorName);
		motorName.setText("Motor name");
				
		value = new Label(motorComposite, SWT.NONE);
		value.setAlignment(SWT.CENTER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        value.setText("");
		
		setpoint = new Label(motorComposite, SWT.NONE);
		setpoint.setAlignment(SWT.CENTER);
		setpoint.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        setpoint.setText("");
		
		indicator = new MinimalMotionIndicator(motorComposite, SWT.NONE);
		indicator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		setMouseListeners();
		
        bind();

	}

    /**
     * Gets the MinimalMotorViewModel used by the cell.
     * 
     * @return the motor view model used by the cell.
     */
    public MinimalMotorViewModel getViewModel() {
        return minimalMotorViewModel;
    }

    /**
     * Binds the model to the view.
     */
    private void bind() {
        
        bindingContext.bindValue(WidgetProperties.text().observe(setpoint),
                BeanProperties.value("setpoint").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.text().observe(value),
                BeanProperties.value("value").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.text().observe(motorName),
                BeanProperties.value("motorName").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.font().observe(motorName),
                BeanProperties.value("font").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.background().observe(motorName),
                BeanProperties.value("color").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.background().observe(indicator),
                BeanProperties.value("color").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.background().observe(value),
                BeanProperties.value("color").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.background().observe(setpoint),
                BeanProperties.value("color").observe(minimalMotorViewModel));

        bindingContext.bindValue(WidgetProperties.background().observe(motorComposite),
                BeanProperties.value("color").observe(minimalMotorViewModel));

	}
	
	private void setMouseListeners() {
		final MinimalMotorView self = this;
		MouseListener forwardDoubleClick = new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Event event = new Event();
				event.widget = self;
		
				self.notifyListeners(SWT.MouseDoubleClick, event);
			}
		};
		
		motorComposite.addMouseListener(forwardDoubleClick);
		motorName.addMouseListener(forwardDoubleClick);
		value.addMouseListener(forwardDoubleClick);
		setpoint.addMouseListener(forwardDoubleClick);
		indicator.addMouseListener(forwardDoubleClick);
	}
}
