
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTable;

/**
 * Holds the table of motors.
 */
public class MotorsOverview extends Composite {
	private Composite motorComposite;
	
	private List<MinimalMotorView> minimalViews = new LinkedList<>();
	
	private final List<MouseListener> mouseListeners = new ArrayList<>();
	
    private static final Color BACKGROUND_COLOUR = SWTResourceManager.getColor(SWT.COLOR_WHITE);

    private static final Color GREY_COLOUR = SWTResourceManager.getColor(192, 192, 192);

    public static final int HEIGHT_DIMENSION = 72;
    public static final int WIDTH_DIMENSION = 107;
    public static final int MARGIN = 3;

    /**
     * Constructor for the motors overview.
     * 
     * @param parent - The parent of this element
     * @param style - The base style to be applied to the overview
     */
    public MotorsOverview(Composite parent, int style) {
		super(parent, style);
        setBackground(GREY_COLOUR);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		motorComposite = new Composite(this, SWT.NONE);
        motorComposite.setBackground(BACKGROUND_COLOUR);
	}
	
    /**
     * 
     * @param motorsTable - The table of motors to be displayed in the view
     * @param controllerIndexOffset - The offset from 1 of the controller
     *            numbers (e.g. tab starting at controller 9 has offset of 8).
     */
    public void setMotors(MotorsTable motorsTable) {
    	
    	GridLayout motorsGrid = new GridLayout(motorsTable.getNumMotors(), true);
    	motorsGrid.verticalSpacing = MARGIN;
    	motorsGrid.horizontalSpacing = MARGIN;
    	motorsGrid.marginWidth = MARGIN;
    	motorsGrid.marginHeight = MARGIN;
		motorComposite.setLayout(motorsGrid);	
		
		resetViews();
		
		for (Motor motor : motorsTable.motors()) {
			addMinimalView(motor);
		}
	}
	
	private void addMinimalView(Motor motor) {
        MinimalMotorViewModel model = new MinimalMotorViewModel();

        model.setMotor(motor);
        
        MinimalMotorView view = new MinimalMotorView(motorComposite, SWT.NONE, model);
		view.setLayoutData(viewLayout());
		
		minimalViews.add(view);

		for (MouseListener listener : mouseListeners) {
			view.addMouseListener(listener);
		}
	}
	
	private void resetViews() {
		for (MinimalMotorView view : minimalViews) {
			view.dispose();
		}
		minimalViews.clear();
	}
	
	private static GridData viewLayout() {
		final GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd.minimumHeight = HEIGHT_DIMENSION;
        gd.minimumWidth = WIDTH_DIMENSION;
        gd.widthHint = WIDTH_DIMENSION;
        gd.heightHint = HEIGHT_DIMENSION;
		
		return gd;
	}
}
