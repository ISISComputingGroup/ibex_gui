
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.motor.displayoptions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Stores all the display options (for example accessibility palettes).
 */
public final class DisplayPreferences extends ModelObject {
	
	private static DisplayPreferences instance;
	
    private static final Color NORMAL_VISION_MOVING_COLOR = SWTResourceManager.getColor(160, 250, 170);
    private static final Color NORMAL_VISION_INTERMEDIATE_COLOR = SWTResourceManager.getColor(255, 191, 0);
    private static final Color NORMAL_VISION_STOPPED_COLOR = SWTResourceManager.getColor(255, 200, 200);
    private static final Color NORMAL_VISION_DISABLED_COLOR = SWTResourceManager.getColor(200, 200, 200);
    private static final Color NORMAL_VISION_UNNAMED_COLOR = SWTResourceManager.getColor(220, 220, 220);
    private static final Color NORMAL_VISION_OUTSIDE_TOLERANCE_BORDER_COLOR = SWTResourceManager.getColor(255, 0, 0);
    private static final Color NORMAL_VISION_IN_TOLERANCE_BORDER_COLOR = SWTResourceManager.getColor(127, 127, 127);

    private static final Color ACCESSIBLE_MOVING_COLOR = SWTResourceManager.getColor(0, 255, 0);
    private static final Color ACCESSIBLE_INTERMEDIATE_COLOR = SWTResourceManager.getColor(255, 87, 0);
    private static final Color ACCESSIBLE_STOPPED_COLOR = SWTResourceManager.getColor(120, 120, 255);
    private static final Color ACCESSIBLE_DISABLED_COLOR = SWTResourceManager.getColor(200, 200, 200);
    private static final Color ACCESSIBLE_UNNAMED_COLOR = SWTResourceManager.getColor(220, 220, 220);
    private static final Color ACCESSIBLE_OUTSIDE_TOLERANCE_BORDER_COLOR = SWTResourceManager.getColor(255, 0, 0);
    private static final Color ACCESSIBLE_IN_TOLERANCE_BORDER_COLOR = SWTResourceManager.getColor(255, 255, 255);

    /**
     * Private constructor for this utility singleton class.
     */
    private DisplayPreferences() {
    }
    
    /**
     * Get the instance of this singleton.
     * @return The instance of this singleton.
     */
    public static DisplayPreferences getInstance() {
    	if (instance == null) {
    		instance = new DisplayPreferences();
    	}
    	return instance;
    }

    private static final Map<ColourOption, MotorPalette> PALETTE_OPTIONS;
    static {
        PALETTE_OPTIONS = new HashMap<>();
        
        PALETTE_OPTIONS.put(ColourOption.NORMAL_VISION, new MotorPalette(
                NORMAL_VISION_MOVING_COLOR, NORMAL_VISION_STOPPED_COLOR,
                NORMAL_VISION_DISABLED_COLOR, NORMAL_VISION_UNNAMED_COLOR, NORMAL_VISION_INTERMEDIATE_COLOR,
                NORMAL_VISION_IN_TOLERANCE_BORDER_COLOR, NORMAL_VISION_OUTSIDE_TOLERANCE_BORDER_COLOR));
        
        PALETTE_OPTIONS.put(ColourOption.ACCESSIBLE, new MotorPalette(
        		ACCESSIBLE_MOVING_COLOR, ACCESSIBLE_STOPPED_COLOR, 
        		ACCESSIBLE_DISABLED_COLOR, ACCESSIBLE_UNNAMED_COLOR, ACCESSIBLE_INTERMEDIATE_COLOR,
        		ACCESSIBLE_IN_TOLERANCE_BORDER_COLOR, ACCESSIBLE_OUTSIDE_TOLERANCE_BORDER_COLOR));
    }

    private MotorPalette currentMotorBackgroundPalette =
            PALETTE_OPTIONS.get(ColourOption.NORMAL_VISION);

    /**
     * Get the current motor background palette.
     * 
     * @return the current motor background palette.
     */
    public MotorPalette getMotorBackgroundPalette() {
        return currentMotorBackgroundPalette;
    }

    /**
     * Set a new motor background palette.
     * 
     * @param paletteKey the new motor background palette
     */
    public void setMotorBackgroundPalette(ColourOption paletteKey) {
        MotorPalette newMotorBackgroundPalette = PALETTE_OPTIONS.get(paletteKey);
        firePropertyChange("motorBackgroundPalette", currentMotorBackgroundPalette, currentMotorBackgroundPalette = newMotorBackgroundPalette);
    }
}
