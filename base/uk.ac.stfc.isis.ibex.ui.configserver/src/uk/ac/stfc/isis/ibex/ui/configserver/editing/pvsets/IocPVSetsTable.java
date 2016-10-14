
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


import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditablePVSet;
import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

@SuppressWarnings("checkstyle:magicnumber")
public class IocPVSetsTable extends DataboundTable<EditablePVSet> {
	private boolean isEditable = true;
	
	public IocPVSetsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, EditablePVSet.class, tableStyle | SWT.BORDER);

		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		enable();
		description();
	}
	
	private void name() {
		TableViewerColumn desc = createColumn("Name", 8);
		desc.setLabelProvider(new DataboundCellLabelProvider<EditablePVSet>(observeProperty("name")) {
			@Override
			protected String valueFromRow(EditablePVSet row) {
				return row.getName();
			}
		});	
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<EditablePVSet>(observeProperty("description")) {
			@Override
			protected String valueFromRow(EditablePVSet row) {
				return row.getDescription();
			}
		});	
	}
	
	private void enable() {
		TableViewerColumn enabled = createColumn("Enabled?", 2);
		IObservableMap[] stateProperties = {observeProperty("enabled")};
		enabled.setLabelProvider(new CheckboxLabelProvider<EditablePVSet>(stateProperties) {	
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
		});	
	}
	
	public boolean getIsEditable() {
		return isEditable;
	}
	
	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
