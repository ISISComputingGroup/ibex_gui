
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/**
 * A panel in the edit block dialog displaying details about the block's name and PV.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockDetailsPanel extends Composite {
	
	private final Text name;
	private final Text pvAddress;
	private final Button visible;
	private final Button local;
	private final Button btnPickPV;

	/**
     * Standard constructor.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param viewModel The viewModel for the block details.
     */
	public BlockDetailsPanel(Composite parent, int style, final BlockDetailsViewModel viewModel) {
		super(parent, style);
		
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpBlock = new Group(this, SWT.NONE);
		grpBlock.setText("Selected block");
		grpBlock.setLayout(new GridLayout(4, false));
		
		Label lblName = new Label(grpBlock, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Text(grpBlock, SWT.BORDER);
		GridData gdName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdName.widthHint = 280;
		name.setLayoutData(gdName);
		
		visible = new IBEXButtonBuilder(grpBlock, SWT.CHECK)
				.customLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1))
				.text("Visible")
				.build();
		
		local = new IBEXButtonBuilder(grpBlock, SWT.CHECK)
				.text("Local")
				.build();
		
		Label lblPvAddress = new Label(grpBlock, SWT.NONE);
		lblPvAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPvAddress.setText("PV address:");
		
		pvAddress = new Text(grpBlock, SWT.BORDER);
		GridData gdPvAddress = new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1);
		gdPvAddress.minimumWidth = 380;
		pvAddress.setLayoutData(gdPvAddress);
		
		
		btnPickPV = new IBEXButtonBuilder(grpBlock, SWT.NONE)
				.customLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1))
				.text("Select PV")
				.listener(evt -> {
					viewModel.openPvDialog();
				})
				.build();
		
		setModel(viewModel);
	}
	
	private void setModel(BlockDetailsViewModel viewModel) {
		DataBindingContext bindingContext = new DataBindingContext();
		
        bindingContext.bindValue(WidgetProperties.enabled().observe(name),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(pvAddress),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(visible),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(local),
                BeanProperties.value("enabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnPickPV),
                BeanProperties.value("enabled").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name),
                BeanProperties.value("name").observe(viewModel)); 
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(pvAddress),
                BeanProperties.value("pvAddress").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(local),
                BeanProperties.value("local").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(visible),
                BeanProperties.value("visible").observe(viewModel));
        
	}
}
