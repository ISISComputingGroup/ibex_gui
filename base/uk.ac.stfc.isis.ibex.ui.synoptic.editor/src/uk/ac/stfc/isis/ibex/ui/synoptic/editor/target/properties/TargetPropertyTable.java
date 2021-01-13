
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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * The Class TargetPropertyTable.
 * 
 * A table of properties for a target. These are the macros set on an OPI.
 */
public class TargetPropertyTable extends DataboundTable<Property> {
	
    /**
     * Instantiates a new target property table.
     *
     * @param parent the parent
     * @param instrument the synoptic model for the instrument
     */
    public TargetPropertyTable(Composite parent, final SynopticViewModel instrument) {
    	super(parent, SWT.NONE, SWT.FULL_SELECTION, false);
		initialise();
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
            @Override
            public void instrumentUpdated(UpdateTypes updateType) {
                if (updateType == UpdateTypes.EDIT_TARGET) {
                	setRows(instrument.getProperties());
                }
            }
        });
        
        addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				instrument.setSelectedProperty((Property) selection.getFirstElement());
			}
		});
	}

	@Override
	protected void addColumns() {
		createColumn("Key", 1, new DataboundCellLabelProvider<Property>(observeProperty("key")) {
			@Override
			protected String stringFromRow(Property row) {
				return row.getKey();
			}
		});
		
		TableViewerColumn value = createColumn("Value", 1, new DataboundCellLabelProvider<Property>(observeProperty("value")) {
			@Override
			protected String stringFromRow(Property row) {
				return row.getValue();
			}
		});
		
		value.setEditingSupport(new StringEditingSupport<Property>(viewer(), Property.class) {

			@Override
			protected String valueFromRow(Property row) {
				return row.getValue();
			}

			@Override
			protected void setValueForRow(Property row, String value) {
				row.setValue(value);
			}
		});
	}
}
