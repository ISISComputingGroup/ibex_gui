
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

package uk.ac.stfc.isis.ibex.experimentdetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The details for one user.
 *
 */
public class UserDetails extends ModelObject {
	
	private String name;
	private String institute;
    private Role role;
	private String associatedExperimentID;
	private Date associatedExperimentStartDate;
	
	private static final DateFormat DF = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Constructor for the user's details.
	 * @param name The full name of the user.
	 * @param institute The institute the user works for.
	 * @param role The role of the user.
	 * @param associatedExperimentID The experiment ID that the user is working on.
	 * @param associatedExperimentStartDate The start date of the user's experiment.
	 */
    public UserDetails(String name, String institute, Role role, String associatedExperimentID,
            Date associatedExperimentStartDate) {
		this.name = name;
		this.institute = institute;
        this.role = role;
		this.associatedExperimentID = associatedExperimentID;
		this.associatedExperimentStartDate = associatedExperimentStartDate;
	}
	
    /**
     * Constructor for a default user that is not associated with an experiment.
     * @param name The full name of the user.
	 * @param institute The institute the user works for.
	 * @param role The role of the user.
     */
    public UserDetails(String name, String institute, Role role) {
		this.name = name;
		this.institute = institute;
        this.role = role;
	}

    /**
     * Get the user's full name.
     * @return The user's full name.
     */
	public String getName() {
		return name;
	}

	/**
	 * Set the user's name.
	 * @param name The new name to give the user.
	 */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	/**
	 * Get the institute that the user works for.
	 * @return The institute the user works for.
	 */
	public String getInstitute() {
		return institute;
	}

	/**
	 * Set the institute that the user works for.
	 * @param institute The institute to assign the user.
	 */
	public void setInstitute(String institute) {
		firePropertyChange("institute", this.institute, this.institute = institute);
	}

	/**
	 * Get the user's role in the experiment.
	 * @return The user's role in the experiment.
	 */
    public Role getRole() {
        return role;
	}

    /**
     * Set the user's role in the experiment.
     * @param role The new role of the user.
     */
    public void setRole(Role role) {
        firePropertyChange("role", this.role, this.role = role);
	}
	
    /**
     * Get the experiment id that the user is working on.
     * @return The experiment ID.
     */
	public String getAssociatedExperimentID() {
		return associatedExperimentID;
	}
	
	/**
	 * Get the start date for the experiment.
	 * @return the start date for the experiment.
	 */
	public Date getAssociatedExperimentStartDate() {
		return associatedExperimentStartDate;
	}
	
	/**
	 * Get the string representation of the start date for the experiment.
	 * @return The string representation.
	 */
	public String getAssociatedExperimentStartDateString() {
		return DF.format(associatedExperimentStartDate);
	}

    /**
     * Finds the highest role and sets it.
     * 
     * @param roleToPrimary
     *            the role to try to set as primary
     */
    public void setPrimaryRole(Role roleToPrimary) {
        if (roleToPrimary == Role.PI) {
            role = roleToPrimary;
        } else if (role != Role.PI && roleToPrimary == Role.CONTACT) {
            role = roleToPrimary;
        } else if (role != Role.CONTACT && role != Role.PI && roleToPrimary == Role.USER) {
            role = roleToPrimary;
        }
	}
}
