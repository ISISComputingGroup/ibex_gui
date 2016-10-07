
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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceDescriptionWrapper;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;

/**
 * The target properties widget used to set the macros for an OPI.
 * 
 * 
 */
public class TargetPropertiesWidget extends Composite {

    private static final int TABLE_HEIGHT = 150;
	
    private DeviceScreensDescriptionViewModel viewModel;
    private Table table;
    private Text valueText;
	
    /**
     * Instantiates a new widget.
     *
     * @param parent the parent
     * @param viewModel the view model
     */
    public TargetPropertiesWidget(Composite parent, DeviceScreensDescriptionViewModel viewModel) {
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

        table = new Table(controlComposite, SWT.BORDER | SWT.FULL_SELECTION);
        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdTable.minimumHeight = TABLE_HEIGHT;
        table.setLayoutData(gdTable);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn keyColumn = new TableColumn(table, SWT.NULL);
        keyColumn.setText("Name");
        TableColumn valueColumn = new TableColumn(table, SWT.NULL);
        valueColumn.setText("Value");

        table.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.setSelectedProperty(table.getSelectionIndex());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Can add double click behaviour here...
            }
        });

        Label lblValue = new Label(controlComposite, SWT.NONE);
        lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblValue.setText("Value");

        valueText = new Text(controlComposite, SWT.BORDER);
        valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        valueText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (valueText.isFocusControl()) {
                    viewModel.setSelectedPropertyValue(valueText.getText());
                }
            }
        });
        valueText.setEnabled(false);

        Label lblPropertyDescription = new Label(controlComposite, SWT.NONE);
        lblPropertyDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblPropertyDescription.setText("Description");

        Text txtDescription = new Text(controlComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
        GridData gdDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdDescription.heightHint = 70;
        txtDescription.setLayoutData(gdDescription);

        // This updates when the screens change, e.g. if a screen is deleted
        viewModel.addPropertyChangeListener("screens", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updatePropertyList(viewModel.getSelectedScreen());
                table.setSelection(-1);
                valueText.setEnabled(false);
            }
        });

        // This updates when the user switches the selected screen
        viewModel.addPropertyChangeListener("selectedScreen", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updatePropertyList(viewModel.getSelectedScreen());
                table.setSelection(-1);
                valueText.setEnabled(false);
            }
        });

        // This updates when the property selected in the table changes
        viewModel.addPropertyChangeListener("selectedProperty", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                valueText.setText(viewModel.getSelectedPropertyValue());
                txtDescription.setText(viewModel.getSelectedPropertyDescription());
                if (table.getSelectionIndex() == -1) {
                    valueText.setEnabled(false);
                } else {
                    valueText.setEnabled(true);
                }
            }
        });

        // This updates when the user changes the property value
        viewModel.addPropertyChangeListener("selectedPropertyValue", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updatePropertyList(viewModel.getSelectedScreen());
            }
        });

        // This updates when the OPI changes
        viewModel.addPropertyChangeListener("currentKey", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updatePropertyList(viewModel.getSelectedScreen());
                table.setSelection(-1);
                // Clear the property text and description
                txtDescription.setText("");
                valueText.setText("");
                valueText.setEnabled(false);
            }
        });
	}
	
    /**
     * Show the property list for target. This is the property values set which
     * correspond to macro names in the OPI for the selected target.
     * 
     * @param deviceDescription selected component
     */
    private void updatePropertyList(DeviceDescriptionWrapper deviceDescription) {
        table.removeAll();

        if (deviceDescription != null) {
            List<PropertyDescription> properties = deviceDescription.getProperties();
            for (PropertyDescription p : properties) {
                TableItem item = new TableItem(table, SWT.NULL);
                item.setText(0, p.getKey());
                item.setText(1, p.getValue());
            }

            table.setEnabled(properties.size() > 0);
        }

        table.getColumn(0).pack();
        table.getColumn(1).pack();
	}

}
