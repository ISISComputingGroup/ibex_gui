
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 * The Class TargetPropertyTable.
 * 
 * A table of properties for a target. These are the macros set on an OPI.
 */
public class TargetPropertyTable extends Composite {

    private static final int TABLE_HEIGHT = 150;
	
	private SynopticViewModel synopticViewModel;
    private Table table;
	
    /**
     * Instantiates a new target property table.
     *
     * @param parent the parent
     * @param instrument the synoptic model for the instrument
     */
    public TargetPropertyTable(Composite parent, final SynopticViewModel instrument) {
		super(parent, SWT.NONE);
		
		this.synopticViewModel = instrument;
		
        instrument.addPropertyChangeListener("selectedComponents", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                showPropertyList(instrument.getSingleSelectedComp());
            }
		});
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {	
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
                if (updateType == UpdateTypes.EDIT_PROPERTY || updateType == UpdateTypes.ADD_TARGET
                        || updateType == UpdateTypes.EDIT_TARGET) {
					
					int selected;
					
					switch (updateType) {
						case EDIT_PROPERTY:
                            selected = table.getSelectionIndex();
							break;
						default:
							selected = -1;
							break;
					}
                    showPropertyList(instrument.getSingleSelectedComp());
                    table.setSelection(selected);
				}
			}
		});
		
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
     * @param parent the parent
     */
	public void createControls(Composite parent) {
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
        GridLayout glControlComposite = new GridLayout(1, false);
        glControlComposite.marginHeight = 0;
        glControlComposite.marginWidth = 0;
        controlComposite.setLayout(glControlComposite);
	    controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

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

        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                synopticViewModel.setSelectedProperty(getSelectedProperty());
            }
        });
	}
	
    /**
     * Show the property list for target. This is the property values set which
     * correspond to macro names in the OPI for the selected target.
     * 
     * @param component selected component
     */
	public void showPropertyList(ComponentDescription component) {
        table.removeAll();

        if (synopticViewModel.getSingleSelectedComp() != null) {
            List<String> opiPropertyKeys = synopticViewModel.getPropertyKeys(component.target().name());
            for (String propertyKey : opiPropertyKeys) {
                TableItem item = new TableItem(table, SWT.NULL);
                Property componentProperty = getPropertyFromKey(propertyKey);
                item.setText(0, componentProperty.key());
                item.setText(1, componentProperty.value());
            }

            table.setEnabled(opiPropertyKeys.size() > 0);
        }

        table.getColumn(0).pack();
        table.getColumn(1).pack();
	}
	
    private Property getSelectedProperty() {
        String selectedProperty = table.getItem(table.getSelectionIndex()).getText(0);

        return getPropertyFromKey(selectedProperty);

    }

    private Property getPropertyFromKey(String key) {
        ComponentDescription component = synopticViewModel.getSingleSelectedComp();

        return component.target().getProperty(key, new Property(key, ""));
    }
}
