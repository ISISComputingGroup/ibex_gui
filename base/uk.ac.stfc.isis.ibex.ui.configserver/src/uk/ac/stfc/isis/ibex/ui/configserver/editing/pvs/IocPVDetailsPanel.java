
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Panel displaying the details of a selected default PV value.
 */
public class IocPVDetailsPanel extends Composite {
	private final MessageDisplayer messageDisplayer;
	private Text name;
	private Text value;
	private DataBindingContext bindingContext;
	private IocAvailablePVsTable availablePVTable;
	private UpdateValueStrategy strategy = new UpdateValueStrategy();
	private Collection<AvailablePV> pvs;

    private static final int TABLE_HEIGHT = 100;

    /**
     * Constructor for the PV value details panel.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     * @param messageDisplayer
     *            The dialog used for displaying error messages.
     */
	public IocPVDetailsPanel(Composite parent, int style, MessageDisplayer messageDisplayer) {
		super(parent, style);
		this.messageDisplayer = messageDisplayer;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpSelectedPv = new Group(this, SWT.NONE);
		grpSelectedPv.setText("Selected PV");
		grpSelectedPv.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(grpSelectedPv, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		name = new Text(grpSelectedPv, SWT.BORDER);
		name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		name.setEnabled(false);
		
		Label lblValue = new Label(grpSelectedPv, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValue.setText("Value");
		
		value = new Text(grpSelectedPv, SWT.BORDER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		value.setEnabled(false);
		
		availablePVTable = new IocAvailablePVsTable(grpSelectedPv, SWT.NONE, 0);
        GridData gdAvailablePVTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gdAvailablePVTable.heightHint = TABLE_HEIGHT;
        availablePVTable.setLayoutData(gdAvailablePVTable);
		new Label(grpSelectedPv, SWT.NONE);
		new Label(grpSelectedPv, SWT.NONE);
		availablePVTable.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
            public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size() > 0) {
					AvailablePV pv = (AvailablePV) selection.getFirstElement();
					name.setText(pv.getName());
				}
			}
		});
	}
	
    /**
     * Sets the default PV value.
     * 
     * @param pv
     *            The default PV value
     * @param viewModel
     *            The IOC being edited
     */
    public void setPV(PVDefaultValue pv, EditableIoc ioc) {
		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		if (pv == null) {
			setEnabled(false);
			setTextEnabled(false);
	
			return;
		}
		
        setEnabled(ioc.isEditable());
        setTextEnabled(ioc.isEditable());
		
		bindingContext = new DataBindingContext();
        strategy.setBeforeSetValidator(new PVNameValidator(ioc, pv, messageDisplayer));
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("name").observe(pv), strategy, null); 
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(value), BeanProperties.value("value").observe(pv));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("filter").observe(availablePVTable), 
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
		
		updateAvailablePVs();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		availablePVTable.setEnabled(enabled);
	}
	
	private void setTextEnabled(boolean enabled) {
		name.setEnabled(enabled);
		value.setEnabled(enabled);
		if (!enabled) {
			name.setText("");
			value.setText("");
		}
	}
	
    /**
     * Sets the PV values.
     * 
     * @param pvs
     *            The PV values.
     */
	public void setPVs(Collection<AvailablePV> pvs) { 
		this.pvs = pvs;
		updateAvailablePVs();
	}
	
	private void updateAvailablePVs() {
		if (pvs != null) {
		   	availablePVTable.setRows(pvs);
		}
	}
}
