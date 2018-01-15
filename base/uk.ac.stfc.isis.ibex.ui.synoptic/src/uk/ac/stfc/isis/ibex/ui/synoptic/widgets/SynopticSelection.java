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

package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Contains the widget showing the synoptic drop down menu for switching, refresh button, and bread crumb trail.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SynopticSelection extends Composite {
	
	private static final Color BACKGROUND = SWTResourceManager.getColor(240, 240, 240);
	
	// The synoptic drop down menu selector
	private Combo synopticCombo;
	
    private Button refreshButton;

	public SynopticSelection(Composite parent, int style, final SynopticSelectionViewModel model) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginRight = -2;
		gridLayout.marginLeft = 0;
		gridLayout.marginTop = -6;
		gridLayout.marginBottom = -6;

		setLayout(gridLayout);
		
		GridData gdSynopticCombo = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gdSynopticCombo.widthHint = 120;
		synopticCombo = new Combo(this, SWT.READ_ONLY);
		synopticCombo.setLayoutData(gdSynopticCombo);
		
		Button loadDefaultButton = new Button(this, SWT.NONE);
		loadDefaultButton.setText("Load Default");
		loadDefaultButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		loadDefaultButton.setBackground(BACKGROUND);
		
        refreshButton = new Button(this, SWT.NONE);
		refreshButton.setText("Refresh Synoptic");
		refreshButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		refreshButton.setBackground(BACKGROUND);
		
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.refreshSynoptic();
			}
		});
		
		bind(model);
	}
	
	private void bind(SynopticSelectionViewModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		
        bindingContext.bindList(SWTObservables.observeItems(synopticCombo),
                BeanProperties.list(SynopticSelectionViewModel.SYNOPTIC_LIST).observe(model));
        bindingContext.bindValue(WidgetProperties.selection().observe(synopticCombo),
                BeanProperties.value(SynopticSelectionViewModel.SELECTED).observe(model));
        bindingContext.bindValue(WidgetProperties.enabled().observe(synopticCombo),
                BeanProperties.value(SynopticSelectionViewModel.ENABLED).observe(model));
        bindingContext.bindValue(WidgetProperties.enabled().observe(refreshButton),
                BeanProperties.value(SynopticSelectionViewModel.ENABLED).observe(model));

	}

}
