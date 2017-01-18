
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

@SuppressWarnings("checkstyle:magicnumber")
public class IocSettingPanel extends Composite {
	private EditableIoc ioc;
	private ComboViewer iocCombo;
	private Label lbDescTarget;
	private IIocDependentPanel target;
	
	public IocSettingPanel(Composite parent, int style, IIocPanelCreator panelFactory) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblIoc = new Label(this, SWT.NONE);
		lblIoc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIoc.setText("IOC:");
		
		iocCombo = new ComboViewer(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = 100;
		iocCombo.getCombo().setLayoutData(gridData);
		iocCombo.setContentProvider(ArrayContentProvider.getInstance());
		
		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setText("Description:");
		
		lbDescTarget = new Label(this, SWT.NONE);
		lbDescTarget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		target = panelFactory.factory(this);
		Composite targetPanel = (Composite) target;
		targetPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
	}
	
	public void setConfig(EditableConfiguration config) {
		iocCombo.setLabelProvider(new LabelProvider() {
			@Override
            public String getText(Object element) {
				Ioc ioc = (Ioc) element;
				return ioc.getName();
			};
		});
		Collection<EditableIoc> iocs = config.getEditableIocs();
		iocCombo.setInput(iocs);
		
		iocCombo.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				StructuredSelection selection = (StructuredSelection) arg0.getSelection();
				ioc = (EditableIoc) selection.getFirstElement();
				lbDescTarget.setText(ioc.getDescription());
				target.setIoc(ioc);
			}
		});

		if (iocs.size() > 0) {
			iocCombo.setSelection(new StructuredSelection(iocs.iterator().next()));
		}
	}
}
