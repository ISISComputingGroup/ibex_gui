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

public class ExperimentDataFields {
	private static List<ExperimentDataTags> tables = new ArrayList<ExperimentDataTags>(Arrays.asList(
			ExperimentDataTags.TAG_USER_TABLE, ExperimentDataTags.TAG_ROLE_TABLE, 
			ExperimentDataTags.TAG_EXPERIMENT_TABLE, ExperimentDataTags.TAG_EXPERIMENT_TEAMS_TABLE));
	
	private static List<ExperimentDataTags> experimentteams_fields = new ArrayList<ExperimentDataTags>
			(Arrays.asList(ExperimentDataTags.TAG_USER_ID, ExperimentDataTags.TAG_EXPERIMENT_ID, ExperimentDataTags.TAG_ROLE_ID));

	private static List<ExperimentDataTags> user_fields = new ArrayList<ExperimentDataTags>
		(Arrays.asList(ExperimentDataTags.TAG_USER_ID, ExperimentDataTags.TAG_NAME, ExperimentDataTags.TAG_ORGANISATION));
	
	private static List<ExperimentDataTags> role_fields = new ArrayList<ExperimentDataTags>
		(Arrays.asList(ExperimentDataTags.TAG_ROLE_ID, ExperimentDataTags.TAG_NAME, ExperimentDataTags.TAG_PRIORITY));
	
	private static List<ExperimentDataTags> experiment_fields = new ArrayList<ExperimentDataTags>
		(Arrays.asList(ExperimentDataTags.TAG_USER_ID, ExperimentDataTags.TAG_EXPERIMENT_ID, ExperimentDataTags.TAG_ROLE_ID));

    private ExperimentDataFields() {
    }

    public static String getAsString(ExperimentDataTags table, ExperimentDataTags field) throws IllegalArgumentException {
    	if (!tables.contains(table)){
    		throw new IllegalArgumentException("Unknown experiment details table" + table);
    	}
    	if (!getFieldList(table).contains(field)) {
    		throw new IllegalArgumentException("Unknown experiment details field" + field);
    	}
    	
		return table + "." + field;
		
    }

    private static List<ExperimentDataTags> getFieldList(ExperimentDataTags table) {
	switch (table) {
		case TAG_USER_TABLE:
		    return user_fields;
		case TAG_ROLE_TABLE:
		    return role_fields;
		case TAG_EXPERIMENT_TABLE:
		    return experiment_fields;
		case TAG_EXPERIMENT_TEAMS_TABLE:
		    return experimentteams_fields;
		default:
			return new ArrayList<>();
		}
    }
}
