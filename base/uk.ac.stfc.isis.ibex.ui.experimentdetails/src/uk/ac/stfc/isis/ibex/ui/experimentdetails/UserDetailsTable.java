
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

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;

/**
 * A table that gives details of users.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class UserDetailsTable extends DataboundTable<UserDetails> {

	protected TableViewerColumn name;
	protected TableViewerColumn institute;
	protected TableViewerColumn role;
	
	/**
	 * Constructor for the table.
	 * @param parent The parent composite that contains the table
	 * @param style The style of the table viewer.
	 * @param tableStyle The style of the underlying table.
	 */
	public UserDetailsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, UserDetails.class, tableStyle);
		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		institute();
		role();
	}

	private void name() {
		name = createColumn("Name", 4);
		name.setLabelProvider(new DataboundCellLabelProvider<UserDetails>(observeProperty("name")) {
			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getName();
			}
		});		
	}
	
	private void institute() {
		institute = createColumn("Institute", 4);
		institute.setLabelProvider(new DataboundCellLabelProvider<UserDetails>(observeProperty("institute")) {
			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getInstitute();
			}
		});		
	}
	
	private void role() {
		role = createColumn("Role", 2);	
		role.setLabelProvider(new DataboundCellLabelProvider<UserDetails>(observeProperty("role")) {
			@Override
			protected String valueFromRow(UserDetails row) {
                Role role = row.getRole();
				return role == null ? "" : role.toString();
			}
		});	
	}
	
	@Override
	protected ColumnComparator comparator() {
		return new UserDetailsComparator();
	}
	
}
