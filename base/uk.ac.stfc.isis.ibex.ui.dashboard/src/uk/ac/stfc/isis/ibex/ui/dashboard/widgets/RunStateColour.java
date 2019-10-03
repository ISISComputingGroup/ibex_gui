
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

package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A class holding the various run state colours.
 */
public final class RunStateColour {
	
	/** The colour YELLOW. */
	public static final Color YELLOW = SWTResourceManager.getColor(255, 255, 0);
	
	/** The colour LIGHT_GREEN. */
	public static final Color LIGHT_GREEN = SWTResourceManager.getColor(144, 238, 144);
	
	/** The colour GREEN. */
	public static final Color GREEN = SWTResourceManager.getColor(124, 204, 124);
	
	/** The colour LIGHT_BLUE. */
	public static final Color LIGHT_BLUE = SWTResourceManager.getColor(173, 216, 230);
	
	/** The colour BLUE. */
	public static final Color BLUE = SWTResourceManager.getColor(153, 191, 204);
	
	/** The colour RED. */
	public static final Color RED = SWTResourceManager.getColor(255, 0, 0);
	
	/** The colour DARK_RED. */
	public static final Color DARK_RED = SWTResourceManager.getColor(204, 0, 0);
	
	/** The colour GOLDEN_ROD. */
	public static final Color GOLDEN_ROD = SWTResourceManager.getColor(218, 165, 32);
	
	/** The colour ROYAL_BLUE. */
	public static final Color ROYAL_BLUE = SWTResourceManager.getColor(65, 105, 225);

    private RunStateColour() {
    }
}
