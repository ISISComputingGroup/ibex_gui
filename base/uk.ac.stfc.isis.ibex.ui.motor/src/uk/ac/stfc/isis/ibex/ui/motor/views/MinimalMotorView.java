
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

import java.util.Optional;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.motor.Motor;

/**
 * The viewer for an individual motor.
 */
public class MinimalMotorView extends Composite {
	
	private Optional<Composite> standardView = Optional.empty();
	private Optional<Composite> advancedView = Optional.empty();

	private MinimalMotorViewModel minimalMotorViewModel;
	
	private final StackLayout stackLayout;
	
	private DataBindingContext bindingContext = new DataBindingContext();

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
        super(parent, SWT.NONE);
       
        this.minimalMotorViewModel = minimalMotorViewModel;

        stackLayout = new StackLayout();
        stackLayout.marginHeight = 2;
        stackLayout.marginWidth = 2;
        setLayout(stackLayout);
        
        setActiveView(minimalMotorViewModel.isAdvancedMinimalMotorView());

        final var activeViewListener = minimalMotorViewModel.addUiThreadPropertyChangeListener("advancedMinimalMotorView", 
        		event -> setActiveView(minimalMotorViewModel.isAdvancedMinimalMotorView()));
        
        addDisposeListener(disposeEvent -> minimalMotorViewModel.removePropertyChangeListener("advancedMinimalMotorView", activeViewListener));
        
        bind();
	}
    
    private void bind() {
      	
        bindingContext.bindValue(WidgetProperties.background().observe(this),
                BeanProperties.value("borderColor").observe(this.getViewModel()));
	}
    
    private void setActiveView(boolean advanced) {
    	Composite newTopControl;
		if (minimalMotorViewModel.isAdvancedMinimalMotorView()) {
			newTopControl = advancedView.orElseGet(MinimalMotorView.this::createNewAdvancedView);
		} else {
			newTopControl = standardView.orElseGet(MinimalMotorView.this::createNewStandardView);
		}

		if (!Objects.equal(newTopControl, stackLayout.topControl)) {
			stackLayout.topControl = newTopControl;
			requestLayout();
		}
    }

    /**
     * Gets the MinimalMotorViewModel used by the cell.
     * 
     * @return the motor view model used by the cell.
     */
    public MinimalMotorViewModel getViewModel() {
        return minimalMotorViewModel;
    }
	
	/** Listens for clicks on a motor in the table, and makes a call to open the OPI for that motor. */
	public MouseListener motorSelection = new MouseAdapter() {
		@Override
		public void mouseDown(MouseEvent e) {
			if (e.widget instanceof MotorInfoView) {
				MotorInfoView minimal = (MotorInfoView) e.widget;
                openMotorView(minimal.getViewModel().getMotor());
			}
		}
	};
	
	/**
	 * Creates a new standard view and registers necessary listeners.
	 * @return the created view
	 */
	private Composite createNewStandardView() {
		Composite view = new MotorInfoStdView(this, SWT.NONE, minimalMotorViewModel);
		view.addMouseListener(motorSelection);
		standardView = Optional.of(view);
		return view;
	}
	
	/**
	 * Creates a new advanced view and registers necessary listeners.
	 * @return the created view
	 */
	private Composite createNewAdvancedView() {
		Composite view = new MotorInfoAdvView(this, SWT.NONE, minimalMotorViewModel);
		view.addMouseListener(motorSelection);
		advancedView = Optional.of(view);
		return view;
	}
	
	/**
	 * Opens the motor OPI for a particular motor.
	 * @param motor The motor to show
	 */
	private static void openMotorView(Motor motor) {
//		try {
//    		// Display OPI motor view
//			String description = motor.getDescription();
//			String secondaryID = Strings.isNullOrEmpty(description) ? motor.name() : description;
//			secondaryID = secondaryID.replace(":", "");
//
//			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//			IViewPart view = page.showView(MotorOPIView.ID, secondaryID, IWorkbenchPage.VIEW_ACTIVATE);
//
//			((MotorOPIView) view).displayOpi(secondaryID, motor.motorAddress());
//
//		} catch (PartInitException e) {
//			LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(TableOfMotorsView.class), e.getMessage(), e);
//		}
	}
}
