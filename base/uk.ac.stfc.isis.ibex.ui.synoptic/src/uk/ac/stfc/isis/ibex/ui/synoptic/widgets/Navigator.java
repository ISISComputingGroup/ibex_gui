
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.NavigationPresenter;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;

public class Navigator extends Composite {

	private final SynopticPresenter presenter;
	
	private final Button previous;
	private final Button up;
	private final Button next;
	
	private final Label gotoLabel;
	private final Combo gotoCombo;
	
	public Navigator(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.horizontalSpacing = 7;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		previous = new Button(this, SWT.NONE);
		previous.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/navigate_left.png"));
		previous.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		previous.setText("Prev");
		
		up = new Button(this, SWT.NONE);
		up.setSelection(true);
		up.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		up.setText("Up");
		
		next = new Button(this, SWT.NONE);
		next.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/navigate_right.png"));
		next.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		next.setText("Next");
		
		gotoLabel = new Label(this, SWT.NONE);
		GridData gd_gotoLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_gotoLabel.horizontalIndent = 10;
		gotoLabel.setLayoutData(gd_gotoLabel);
		gotoLabel.setText("Go to:");
		
		gotoCombo = new Combo(this, SWT.READ_ONLY);
		GridData gd_gotoCombo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_gotoCombo.widthHint = 120;
		gotoCombo.setLayoutData(gd_gotoCombo);
		new Label(this, SWT.NONE);
		
		presenter = Activator.getDefault().presenter();
		setTargets(presenter.getTargets());
		
		setModel(presenter.navigator());
	}
	
	private void setModel(final NavigationPresenter navigator) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(previous), BeanProperties.value("hasPrevious").observe(navigator));
		bindingContext.bindValue(WidgetProperties.enabled().observe(up), BeanProperties.value("hasUp").observe(navigator));
		bindingContext.bindValue(WidgetProperties.enabled().observe(next), BeanProperties.value("hasNext").observe(navigator));
		
		bindingContext.bindList(WidgetProperties.items().observe(gotoCombo), BeanProperties.list("targets").observe(presenter));

		previous.setToolTipText(navigator.nameOfPrevious());
		up.setToolTipText(navigator.nameOfUp());
		next.setToolTipText(navigator.nameOfNext());
		
		navigator.addPropertyChangeListener("currentNode", new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				previous.setToolTipText(navigator.nameOfPrevious());
				up.setToolTipText(navigator.nameOfUp());
				
				// Next button has text direction right to left in order to place
				// image on the right hand side. This causes parentheses to be 
				// incorrectly formatted.
				next.setToolTipText(navigator.nameOfNext().replace("(", "- ").replace(')', ' '));
			}
		});
		
		previous.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				navigator.previous();
			}
		});
		
		up.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				navigator.up();
			}
		});
		
		next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				navigator.next();
			}
		});
	}

	private void setTargets(List<String> targetNames) {
		gotoCombo.setItems(targetNames.toArray(new String[0]));
		gotoCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String item = gotoCombo.getText();
				presenter.navigateTo(item);
			}
		});
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		previous.setBackground(color);
		up.setBackground(color);
		next.setBackground(color);
		gotoLabel.setBackground(color);
	}
}
