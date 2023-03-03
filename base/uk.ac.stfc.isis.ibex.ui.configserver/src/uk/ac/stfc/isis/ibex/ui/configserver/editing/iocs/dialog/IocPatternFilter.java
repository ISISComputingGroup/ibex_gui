
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2022 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.dialogs.PatternFilter;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

/**
 * Class that filters the Tree View of available IOCs.
 */
public class IocPatternFilter extends PatternFilter {
	
	/**
	 * Create the filter. Set default value for the leading wild card.
	 */
    public IocPatternFilter() {
    	setIncludeLeadingWildcard(true);
    }
    
    /**
     * Check if the EditableIoc description matches the search pattern so we can also filter by parent.
     */
    @Override
    public boolean isLeafMatch(Viewer viewer, Object element) {
    	if (element instanceof EditableIoc) {
    		EditableIoc editableIoc = (EditableIoc) element;
    		return super.wordMatches(editableIoc.getName()) || super.wordMatches(editableIoc.getDescription());
    	}
    	
    	return super.isLeafMatch(viewer, element);
    }
}
