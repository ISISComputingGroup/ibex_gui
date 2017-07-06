
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

package uk.ac.stfc.isis.ibex.model;

/**
 * An abstract class for a generic action which may or may not be able to be
 * executed.
 */
public abstract class Action extends ModelObject {
	
	private boolean canExecute;
	
    /**
     * Execute the action.
     */
	public abstract void execute();
	
    /**
     * Get whether or not the action can be executed.
     * 
     * @return True if the action can be executed.
     */
	public boolean getCanExecute() {
		return canExecute;
	}
	
    /**
     * Set whether or not the action can be executed.
     * 
     * @param canExecute
     *            True if the action can be executed.
     */
	protected void setCanExecute(boolean canExecute) {
		firePropertyChange("canExecute", this.canExecute, this.canExecute = canExecute);
	}
}
