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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpDataFieldsCreator {
	private static List<ExpDataFieldsEnum> experimentteamsFields = new ArrayList<ExpDataFieldsEnum>(
			Arrays.asList(ExpDataFieldsEnum.USER_ID, ExpDataFieldsEnum.EXPERIMENT_ID, ExpDataFieldsEnum.ROLE_ID));

	private static List<ExpDataFieldsEnum> userFields = new ArrayList<ExpDataFieldsEnum>(
			Arrays.asList(ExpDataFieldsEnum.USER_ID, ExpDataFieldsEnum.NAME, ExpDataFieldsEnum.ORGANISATION));
	
	private static List<ExpDataFieldsEnum> roleFields = new ArrayList<ExpDataFieldsEnum>(
			Arrays.asList(ExpDataFieldsEnum.ROLE_ID, ExpDataFieldsEnum.NAME, ExpDataFieldsEnum.PRIORITY));
	
	private static List<ExpDataFieldsEnum> experimentFields = new ArrayList<ExpDataFieldsEnum>(
			Arrays.asList(ExpDataFieldsEnum.USER_ID, ExpDataFieldsEnum.EXPERIMENT_ID, ExpDataFieldsEnum.ROLE_ID, 
				ExpDataFieldsEnum.STARTDATE, ExpDataFieldsEnum.DURATION));

    private ExpDataFieldsCreator() {
    }

    public static ExpDataField getField(ExpDataTablesEnum table, ExpDataFieldsEnum field) throws IllegalArgumentException {

    	if (!getFieldList(table).contains(field)) {
    		throw new IllegalArgumentException("Unknown experiment details field: " + field);
    	}
    	
		return new ExpDataField(table, field);
		
    }

    private static List<ExpDataFieldsEnum> getFieldList(ExpDataTablesEnum table) {
	switch (table) {
		case USER_TABLE:
		    return userFields;
		case ROLE_TABLE:
		    return roleFields;
		case EXPERIMENT_TABLE:
		    return experimentFields;
		case EXPERIMENT_TEAMS_TABLE:
		    return experimentteamsFields;
		default:
			return new ArrayList<>();
		}
    }
}
