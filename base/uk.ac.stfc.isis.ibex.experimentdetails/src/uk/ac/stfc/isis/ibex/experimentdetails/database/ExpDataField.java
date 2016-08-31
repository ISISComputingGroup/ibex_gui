
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

package uk.ac.stfc.isis.ibex.experimentdetails.database;

/**
 * A class to hold a single field from the experiment details table.
 * Used to construct SQL to ensure that only the correct tables and fields can be used.
 */
public class ExpDataField {
	private ExpDataTablesEnum table;
	private ExpDataFieldsEnum field;
	
	/**
	 * Constructor to create an ExpDataField Object based on a table and field.
	 * @param table The table that the field can be found in.
	 * @param field The specific field.
	 */
	public ExpDataField(ExpDataTablesEnum table, ExpDataFieldsEnum field) {
		this.table = table;
		this.field = field;
	}
	
	@Override
	public String toString() {
		return table + "." + field;
	}
}
