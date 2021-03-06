
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
 * An enum to improve readability as to which table is which.
 */
public enum ExpDataTablesEnum {
    
    /**
     * A table containing information about users.
     */
	USER_TABLE("user"),
	
	/**
	 * A table containing information about roles.
	 */
	ROLE_TABLE("role"),
	
	/**
     * A table containing information about experiments.
     */
	EXPERIMENT_TABLE("experiment"),
	
	/**
     * A table containing information about experiment teams.
     */
	EXPERIMENT_TEAMS_TABLE("experimentteams");

    private final String representation;
    
    /**
     * Instantiates a new experiment data tables enum.
     *
     * @param representation the representation
     */
    ExpDataTablesEnum(String representation) {
    	this.representation = representation;
    }
    
    @Override
    public String toString() {
    	return this.representation;
    }
}