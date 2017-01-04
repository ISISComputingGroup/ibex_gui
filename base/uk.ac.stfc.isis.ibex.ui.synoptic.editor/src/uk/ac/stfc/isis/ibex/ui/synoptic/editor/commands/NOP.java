
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import org.eclipse.jface.action.ContributionItem;

/**
 * A dynamic menu item that does nothing, this forces the synoptic menu items to
 * be instantiated when first selected. This means that the check for the
 * command to be enabled can be done.
 */
public class NOP extends ContributionItem {


    /**
     * The default constructor for the menu item.
     */
    public NOP() {
	}
	
	/**
     * Say the list is dynamic so as to force menu items to be refreshed each
     * time.
     * 
     * @return Whether the menu item is dynamic.
     */
	@Override
	public boolean isDynamic() {
		return true;
	}
}
