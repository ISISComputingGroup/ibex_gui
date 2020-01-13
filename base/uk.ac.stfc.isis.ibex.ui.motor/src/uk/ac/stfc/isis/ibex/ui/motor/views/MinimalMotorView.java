
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.motor.Motors;
import uk.ac.stfc.isis.ibex.motor.internal.MotorTableSettings;

/**
 * The viewer for an individual motor.
 */
public class MinimalMotorView extends Composite {

	private MinimalMotorViewModel minimalMotorViewModel;

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
        super(parent, SWT.BORDER);
 
        this.minimalMotorViewModel = minimalMotorViewModel;
        Composite this_object = this;
        final StackLayout layout = new StackLayout();
        setLayout(layout);
        
        SimpleMinimalView simpleView = new SimpleMinimalView(this, SWT.BORDER, minimalMotorViewModel);
        AdvancedMinimalView advancedView = new AdvancedMinimalView(this, SWT.BORDER, minimalMotorViewModel);
        
        layout.topControl = simpleView;
        this.requestLayout();
        this.layout();

        minimalMotorViewModel.addPropertyChangeListener("advancedMinimalMotorView", new PropertyChangeListener() {
        	@Override
        	public void propertyChange(PropertyChangeEvent evt) {
                layout.topControl = minimalMotorViewModel.isAdvancedMinimalMotorView() == false ? simpleView : advancedView;
                this_object.requestLayout();
        	}
        });
	}

    /**
     * Gets the MinimalMotorViewModel used by the cell.
     * 
     * @return the motor view model used by the cell.
     */
    public MinimalMotorViewModel getViewModel() {
        return minimalMotorViewModel;
    }
}
