
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

package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * A table which displays the experiment team user details and allows them to be edited.
 */
public class EditableUserDetailsTable extends UserDetailsTable {

    /**
     * Creates a table for viewing and editing user details.
     * 
     * @param parent The parent composite that contains the table.
     * @param style The style of the table viewer.
     * @param tableStyle The style of the underlying table.
     */
	public EditableUserDetailsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, tableStyle);
	}

	@Override
	public void addColumns() {
		super.addColumns();
		nameEditable();
		instituteEditable();
		roleEditable();
	}
	
	private void nameEditable() {
		name.setEditingSupport(new StringEditingSupport<UserDetails>(viewer(), UserDetails.class) {

			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getName();
			}

			@Override
			protected void setValueForRow(UserDetails row, String value) {
				row.setName(value);
			}
		});
	}
	
	private void instituteEditable() {
		institute.setEditingSupport(new StringEditingSupport<UserDetails>(viewer(), UserDetails.class) {

			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getInstitute();
			}

			@Override
			protected void setValueForRow(UserDetails row, String value) {
				row.setInstitute(value);
			}
		});
	}
	
	private void roleEditable() {
		role.setEditingSupport(new RoleEditingSupport(viewer()));		
	}
}
