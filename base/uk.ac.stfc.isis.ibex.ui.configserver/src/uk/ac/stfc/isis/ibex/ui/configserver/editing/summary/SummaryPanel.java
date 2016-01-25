
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.summary;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

@SuppressWarnings("checkstyle:magicnumber")
public class SummaryPanel extends Composite {
	private Text txtName;
	private Text txtDescription;
	private Text txtDateCreated;
	private Text txtDateModified;
	private ComboViewer cmboSynoptic;
	private EditableConfiguration config;
	private DataBindingContext bindingContext;
	private UpdateValueStrategy strategy = new UpdateValueStrategy();
	private final MessageDisplayer messageDisplayer;

	public SummaryPanel(Composite parent, int style, MessageDisplayer dialog) {
		super(parent, style);
		messageDisplayer = dialog;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpSummary = new Group(this, SWT.NONE);
		grpSummary.setText("Summary");
		grpSummary.setBounds(0, 0, 70, 81);
		grpSummary.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(grpSummary, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtName = new Text(grpSummary, SWT.BORDER);
		txtName.setEditable(false);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(grpSummary, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Description:");
		
		txtDescription = new Text(grpSummary, SWT.BORDER);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDescription.setTextLimit(39);
		
		Label lblSynoptic = new Label(grpSummary, SWT.NONE);
		lblSynoptic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSynoptic.setText("Synoptic:");
		
		cmboSynoptic = new ComboViewer(grpSummary, SWT.READ_ONLY);
		cmboSynoptic.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cmboSynoptic.setContentProvider(new ArrayContentProvider());
		updateSynopticList();
		
		Label lblDateCreated = new Label(grpSummary, SWT.NONE);
		lblDateCreated.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDateCreated.setText("Date Created:");
				
		txtDateCreated = new Text(grpSummary, SWT.BORDER);
		GridData gdTxtDateCreated = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTxtDateCreated.widthHint = 120;
		gdTxtDateCreated.minimumWidth = 120;
		txtDateCreated.setLayoutData(gdTxtDateCreated);
		txtDateCreated.setEditable(false);
				
		Label lblDateModified = new Label(grpSummary, SWT.NONE);
		lblDateModified.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDateModified.setText("Date Modified:");
		
		txtDateModified = new Text(grpSummary, SWT.BORDER);
		GridData gdTxtDateModified = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTxtDateModified.minimumWidth = 120;
		gdTxtDateModified.widthHint = 120;
		txtDateModified.setLayoutData(gdTxtDateModified);
		txtDateModified.setEditable(false);
	}
	
	public void setConfig(EditableConfiguration config) {
		this.config = config;
		setBindings();
	}

	private void setBindings() {
		bindingContext = new DataBindingContext();
		
		strategy.setBeforeSetValidator(new SummaryDescriptionValidator(messageDisplayer));
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtName), BeanProperties.value("name").observe(config));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDescription), BeanProperties.value("description").observe(config), strategy, null);
		bindingContext.bindValue(WidgetProperties.selection().observe(cmboSynoptic.getCombo()), BeanProperties.value("synoptic").observe(config));		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDateCreated), BeanProperties.value("dateCreated").observe(config));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDateModified), BeanProperties.value("dateModified").observe(config));
	}
	
	private void updateSynopticList() {
		Collection<SynopticInfo> available = Synoptic.getInstance().availableSynoptics();
		String[] names = SynopticInfo.names(available).toArray(new String[0]);
		Arrays.sort(names);
		cmboSynoptic.setInput(names);
	}
}
