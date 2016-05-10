
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegime;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeUnit;

public class TimeChannelsPanel extends Composite {
	private Group timeRegimesGroup;
	private List<TimeRegimeView> timeRegimeViews = new ArrayList<TimeRegimeView>();
	
	private TimeChannelsViewModel viewModel;
	private Combo timeUnit;
	private DataBindingContext bindingContext;
	
	private static final Display DISPLAY = Display.getCurrent();
	
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public TimeChannelsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite parameters = new Composite(this, SWT.NONE);
		parameters.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		parameters.setLayout(new GridLayout(2, false));
		
		Label lblTimeUnit = new Label(parameters, SWT.NONE);
		lblTimeUnit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTimeUnit.setText("Time Unit:");
		
		timeUnit = new Combo(parameters, SWT.DROP_DOWN | SWT.READ_ONLY);
		timeUnit.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		timeUnit.setItems(TimeUnit.allToString().toArray(new String[0]));
		
		timeRegimesGroup = new Group(this, SWT.NONE);
		timeRegimesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		timeRegimesGroup.setText("Time Regimes");
		timeRegimesGroup.setLayout(new GridLayout(3, false));		
	}
	
	public void setModel(final TimeChannelsViewModel viewModel) {
		this.viewModel = viewModel;
		
		bindingContext = new DataBindingContext();	
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(timeUnit), BeanProperties.value("timeUnit").observe(viewModel));
		
		updateTimeRegimes();
		viewModel.addPropertyChangeListener("timeRegimes", new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateTimeRegimes();
			}
		});
	}
	
	private void updateTimeRegimes() {
		DISPLAY.asyncExec(new Runnable() {
			@Override
			public void run() {
				clearExistingTimeRegimeViews();

				GridData timeRegimeGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				int count = 1;
				
				for (TimeRegime timeRegime : viewModel.timeRegimes()) {
					TimeRegimeView view = new TimeRegimeView(timeRegimesGroup, SWT.NONE);
					view.setLayoutData(timeRegimeGridData);					
					
					view.setModel(timeRegime);
					view.setTitle(String.format("Time regime %d", count));
					
					count++;
					timeRegimeViews.add(view);					
				}
				
				timeRegimesGroup.layout();
			}
		});

	}

	private void clearExistingTimeRegimeViews() {
		for (TimeRegimeView view : timeRegimeViews) {
			view.dispose();
		}
		timeRegimeViews.clear();
	}
}
