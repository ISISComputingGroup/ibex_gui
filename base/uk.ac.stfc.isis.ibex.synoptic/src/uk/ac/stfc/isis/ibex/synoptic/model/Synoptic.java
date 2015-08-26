
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

package uk.ac.stfc.isis.ibex.synoptic.model;

import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * Defines a synoptic view, displayed in IBEX and held on the blockserver as XML.
 * 
 */
public interface Synoptic {
	
	/**
	 * Get the synoptic name.
	 * 
	 * @return The synoptic name
	 */
	String name();
	
	/**
	 * Get the list of components for the synoptic.
	 * 
	 * @return A list, extending type Component
	 */
	List<? extends Component> components();
	
	/**
	 * Defines whether or not the beam is shown in the synoptic.
	 *  
	 * @return True if the beam is to be shown
	 */
	Boolean showBeam();
	
	/**
	 * Return a description of the synoptic.
	 * 
	 * @return The description of the synoptic as a Sting
	 */
	SynopticDescription getDescription();	
}
