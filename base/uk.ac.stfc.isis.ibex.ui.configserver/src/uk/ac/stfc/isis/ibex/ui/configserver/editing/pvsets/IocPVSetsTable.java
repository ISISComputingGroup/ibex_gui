
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvsets;


import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditablePVSet;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.CheckboxLabelProvider;

/**
 * A table displaying the "PV sets" for an IOC in the current configuration.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class IocPVSetsTable extends DataboundTable<EditablePVSet> {
	private boolean isEditable = true;
	
	/**
	 * Constructor for a PV sets table.
	 * @param parent The parent composite that this table is part of.
	 * @param style The SWT style for this composite.
	 * @param tableStyle The SWT style for the underlying table.
	 */
	public IocPVSetsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, tableStyle | SWT.BORDER);

		initialise();
	}
	
	@Override
    public void setRows(Collection<EditablePVSet> rows) {
        super.setRows(rows);
        super.refresh();
    }

	@Override
	protected void addColumns() {
		name();
		enable();
		description();
	}
	
	private void name() {
		createColumn("Name", 8, new DataboundCellLabelProvider<EditablePVSet>(observeProperty("name")) {
			@Override
			protected String stringFromRow(EditablePVSet row) {
				return row.getName();
			}
		});	
	}
	
	private void description() {
		createColumn("Description", 6, new DataboundCellLabelProvider<EditablePVSet>(observeProperty("description")) {
			@Override
			protected String stringFromRow(EditablePVSet row) {
				return row.getDescription();
			}
		});	
	}
	
	private void enable() {
	    CheckboxLabelProvider<EditablePVSet> enableStatusLabelProvider = new CheckboxLabelProvider<EditablePVSet>(observeProperty("enabled")) {  
            @Override
            protected boolean checked(EditablePVSet pvset) {
                return pvset.getEnabled();
            }
            
            @Override
            protected void setChecked(EditablePVSet pvset, boolean checked) {
                pvset.setEnabled(checked);
            }
            
            @Override
            protected boolean isEditable(EditablePVSet pvset) {
                return isEditable;
            }
        };
        
        setSortAction(() -> enableStatusLabelProvider.resetCheckBoxListenerUpdateFlags());
        
		createColumn("Enabled?", 2, enableStatusLabelProvider);
	}
	
	/**
	 * Gets whether the values in the table can be edited by the user.
	 * @return Whether the values in the table can be edited by the user.
	 */
	public boolean getIsEditable() {
		return isEditable;
	}
	
	/**
	 * Sets whether the values in the table can be edited by the user.
	 * @param isEditable true if the table can be edited by the user.
	 */
	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
