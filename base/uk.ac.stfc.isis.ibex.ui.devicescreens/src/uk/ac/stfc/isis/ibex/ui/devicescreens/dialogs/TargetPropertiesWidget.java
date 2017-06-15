
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.devicescreens.models.TargetPropertiesViewModel;

/**
 * The target properties widget used to set the macros for an OPI.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TargetPropertiesWidget extends Composite {

    /** The minimum table height. */
    private static final int TABLE_HEIGHT = 150;
	
    /** The view model. */
    private TargetPropertiesViewModel viewModel;

    /** The properties table. */
    private TargetPropertiesTable table;

    private Text valueText;

    private Text txtDescription;
	
    /**
     * Instantiates a new widget.
     *
     * @param parent the parent
     * @param viewModel the view model
     */
    public TargetPropertiesWidget(Composite parent, TargetPropertiesViewModel viewModel) {
		super(parent, SWT.NONE);
		
        this.viewModel = viewModel;
		
		GridLayout compositeLayout = new GridLayout(1, false);
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		
		setLayout(compositeLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);
	}
	
    /**
     * Creates the controls in the composite.
     * 
     * Because the behaviour logic is quite complicated simple data-binding was
     * not used. Instead property change listeners were used.
     *
     * @param parent the parent
     */
	public void createControls(Composite parent) {
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
        GridLayout glControlComposite = new GridLayout(2, false);
        glControlComposite.marginHeight = 0;
        glControlComposite.marginWidth = 0;
        controlComposite.setLayout(glControlComposite);
        controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Label lblProperties = new Label(controlComposite, SWT.NONE);
        lblProperties.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblProperties.setText("Properties");

        // Build up the table
        table = new TargetPropertiesTable(controlComposite, SWT.NONE, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION);
        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdTable.minimumHeight = TABLE_HEIGHT;
        table.setLayoutData(gdTable);

        Label lblValue = new Label(controlComposite, SWT.NONE);
        lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblValue.setText("Value");

        valueText = new Text(controlComposite, SWT.BORDER);
        valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblPropertyDescription = new Label(controlComposite, SWT.NONE);
        lblPropertyDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblPropertyDescription.setText("Description");

        txtDescription = new Text(controlComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
        GridData gdDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdDescription.heightHint = 70;
        txtDescription.setLayoutData(gdDescription);

        bind();

	}

    private void bind() {
        DataBindingContext bindingContext = new DataBindingContext();
        
        bindingContext.bindValue(SWTObservables.observeText(valueText, SWT.Modify),
                BeanProperties.value("valueText").observe(viewModel));
        bindingContext.bindValue(SWTObservables.observeEnabled(valueText),
                BeanProperties.value("valueTextEnabled").observe(viewModel));

        bindingContext.bindValue(SWTObservables.observeText(txtDescription),
                BeanProperties.value("descriptionText").observe(viewModel));

        bindingContext.bindValue(SWTObservables.observeEnabled(table),
                BeanProperties.value("tableEnabled").observe(viewModel));

        viewModel.addPropertyChangeListener("properties", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                table.setRows(viewModel.getProperties());
            }
        });

        table.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                viewModel.setTableSelection(table.firstSelectedRow());
            }
        });
    }

}
