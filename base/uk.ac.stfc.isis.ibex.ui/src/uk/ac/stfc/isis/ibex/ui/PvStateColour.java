
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Contains standard colours used to display the status of PVs and blocks.
 */
public final class PvStateColour {
    
    /**
     * Private constructor for utility class.
     */
    private PvStateColour() {
    }

    /**
     * The colour white.
     */
    public static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    
    /**
     * The colour grey.
     */
    public static final Color GREY = SWTResourceManager.getColor(240, 240, 240);
    
    /**
     * The colour magenta.
     */
    public static final Color MAGENTA = SWTResourceManager.getColor(SWT.COLOR_MAGENTA);
    
    /**
     * The colour red.
     */
    public static final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
    
    /**
     * The colour orange.
     */
    public static final Color ORANGE = SWTResourceManager.getColor(255, 128, 0);
}
