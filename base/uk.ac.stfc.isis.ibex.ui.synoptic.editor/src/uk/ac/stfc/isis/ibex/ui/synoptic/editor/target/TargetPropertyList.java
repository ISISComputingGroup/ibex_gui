
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetPropertyList extends Composite {
	
	private SynopticViewModel synopticViewModel;
    private Table table;
	
    public TargetPropertyList(Composite parent, final SynopticViewModel instrument) {
		super(parent, SWT.NONE);
		
		this.synopticViewModel = instrument;
		
		instrument.addComponentSelectionListener(new IComponentSelectionListener() {			
			@Override
			public void selectionChanged(List<ComponentDescription> oldSelection, List<ComponentDescription> newSelection) {
				if (newSelection != null && newSelection.size() == 1) {
					showPropertyList(newSelection.iterator().next());
				} else {
					showPropertyList(null);
				}
			}
		});
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {	
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
                if (updateType == UpdateTypes.EDIT_PROPERTY) {
					
					int selected;
					
					switch (updateType) {
						case EDIT_PROPERTY:
                            selected = table.getSelectionIndex();
							break;
						default:
							selected = -1;
							break;
					}
                    showPropertyList(instrument.getFirstSelectedComponent());
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
	
	public void createControls(Composite parent) {
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
        GridLayout gl_controlComposite = new GridLayout(2, false);
        gl_controlComposite.marginHeight = 0;
        gl_controlComposite.marginWidth = 0;
        controlComposite.setLayout(gl_controlComposite);
	    controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        table = new Table(controlComposite, SWT.BORDER | SWT.FULL_SELECTION);
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gd_table.minimumHeight = 150;
        table.setLayoutData(gd_table);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn keyColumn = new TableColumn(table, SWT.NULL);
        keyColumn.setText("Name");
        TableColumn valueColumn = new TableColumn(table, SWT.NULL);
        valueColumn.setText("Value");

        table.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                synopticViewModel
                        .setSelectedProperty(getSelectedProperty(synopticViewModel.getFirstSelectedComponent()));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Can add double click behaviour here...
            }
        });
	}
	
	public void showPropertyList(ComponentDescription component) {
        table.removeAll();

        if (synopticViewModel.getFirstSelectedComponent() != null) {
            for (Property property : component.target().properties()) {
                TableItem item = new TableItem(table, SWT.NULL);
                item.setText(0, property.key());
                item.setText(1, property.value());
            }
        }

        table.getColumn(0).pack();
        table.getColumn(1).pack();
	}
	
    public Property getSelectedProperty(ComponentDescription component) {
        String selectedProperty = table.getItem(table.getSelectionIndex()).getText(0);

        for (Property property : component.target().properties()) {
            if (selectedProperty.equals(property.key())) {
                return property;
            }
        }

        return null;
	}
}
