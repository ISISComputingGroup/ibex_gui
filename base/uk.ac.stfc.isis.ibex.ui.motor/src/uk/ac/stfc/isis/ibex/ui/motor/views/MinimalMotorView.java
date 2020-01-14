
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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;

/**
 * The viewer for an individual motor.
 */
public class MinimalMotorView extends Composite {
	
	SimpleMinimalView simpleView;
	AdvancedMinimalView advancedView;

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

        final StackLayout layout = new StackLayout();
        setLayout(layout);
        
        simpleView = new SimpleMinimalView(this, SWT.NONE, minimalMotorViewModel);
        advancedView = new AdvancedMinimalView(this, SWT.BORDER, minimalMotorViewModel);

        layout.topControl = simpleView;
        this.requestLayout();

        minimalMotorViewModel.addPropertyChangeListener("advancedMinimalMotorView", new PropertyChangeListener() {
        	@Override
        	public void propertyChange(PropertyChangeEvent evt) {
                layout.topControl = minimalMotorViewModel.isAdvancedMinimalMotorView() == false ? simpleView : advancedView;
                MinimalMotorView.this.requestLayout();
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
    
	public void setMouseListeners(List<MouseListener> mouseListeners) {
		for (MouseListener listener : mouseListeners) {
			simpleView.addMouseListener(listener);
			advancedView.addMouseListener(listener);
		}
	}
}
