
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

/**
 * A factory that creates PV filters.
 */
public class PVFilterFactory {
	private final Collection<EditableIoc> availableIOCs;
	
	/**
	 * Creates the factory.
	 * @param availableIOCs A list of the IOCs that you may want to filter PVs against.
	 */
	public PVFilterFactory(Collection<EditableIoc> availableIOCs) {
		this.availableIOCs = availableIOCs;
	}
	
	/**
	 * Returns a filter that filters PVs based on where they have come from.
	 * @param type The source to filter on.
	 * @return The filter.
	 */
	public PVFilter getFilter(SourceFilters type) {
		switch (type) {
			case ACTIVE:
				return new FilterFromPVList(Configurations.getInstance().variables().activePVs);
			case ASSOCIATED:
				return new AssociatedPvFilter(availableIOCs);
			default:
				return new PVFilter();
		}
	}
	
	/**
	 * Returns a filter that filters PVs based on their interest level.
	 * @param type The interest level to filter on.
	 * @return The filter.
	 */
	public PVFilter getFilter(InterestFilters type) {
		switch (type) {
			case HIGH:
				return new FilterFromPVList(Configurations.getInstance().variables().highInterestPVs);
			case MEDIUM:
				return new FilterFromPVList(Configurations.getInstance().variables().mediumInterestPVs);
			case FACILITY:
                return new FilterFromPVList(Configurations.getInstance().variables().facilityInterestPVs);
			default:
				return new PVFilter();
		}
	}
	
}
