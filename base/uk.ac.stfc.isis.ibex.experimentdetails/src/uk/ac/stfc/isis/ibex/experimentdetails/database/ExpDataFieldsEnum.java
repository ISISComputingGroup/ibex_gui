
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
 * An enum to improve the readability of the column names in the database.
 */
public enum ExpDataFieldsEnum {
    /**
     * The ID of the experiment (same as the RB number).
     */
	EXPERIMENT_ID("experimentID"),
	
	/**
	 * The ID of the user.
	 */
	USER_ID("userID"),
	
	/**
	 * The ID of the role.
	 */
	ROLE_ID("roleID"),
	
	/**
	 * The name of a person.
	 */
	NAME("name"),
	
	/**
	 * The organisation a person is associated with.
	 */
	ORGANISATION("organisation"),
	
	/**
	 * The priority.
	 */
    PRIORITY("priority"),
    
    /**
     * The starting date of the experiment.
     */
    STARTDATE("startDate"),
    
    /**
     * The duration of the experiment.
     */
    DURATION("duration");

    private final String representation;
    
    ExpDataFieldsEnum(String representation) {
    	this.representation = representation;
    }
    
    @Override
    public String toString() {
    	return this.representation;
    }
}