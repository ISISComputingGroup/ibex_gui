
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

package uk.ac.stfc.isis.ibex.ui.devicescreens;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;

/**
 * Given a component type, returns an icon appropriate for that type.
 */
public final class ComponentIcons {
	
    /**
     * An empty constructor required for check style.
     */
	private ComponentIcons() { }
	
    /**
     * Gets the appropriate icon for a given component type.
     * 
     * @param componentType the component type
     * @return the icon corresponding to the component type
     */
    public static Image iconForType(ComponentType componentType) {
        String iconName = componentType == ComponentType.GONIOMETER ? "ALF Gonio" : getIconNameForType(componentType);
        return icon(iconName + ".png");
    }

    /**
     * Gets the appropriate thumbnail icon for a given component type.
     * 
     * @param componentType the input component type
     * @return the thumbnail icon corresponding to the component type
     */
    public static Image thumbnailForType(ComponentType componentType) {
        String iconName = getIconNameForType(componentType);
        return icon("thumbs/" + iconName + "_tb.png");
    }

    private static Image icon(String fileName) {
        return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.devicescreens", "icons/components/" + fileName);
    }

    private static String getIconNameForType(ComponentType componentType) {
        switch (componentType) {
            case JAWS:
                return "jaws";
            case CHOPPER:
                return "chopper";
            case MONITOR:
                return "monitor";
            case SAMPLESTACK:
                return "sample_stack";
            case DAE:
                return "dae";
            case DANFSYIK:
            case KEPCO:
            case TDK_LAMBDA_GENESYS:
            case CAEN:
                return "powersupply";
            case BEAMSTOP:
                return "beamstop";
            case MOVINGMONITOR:
            case MOVINGBEAMSTOP:
                return "moving_monitor";
            case LINEARSAMPLECHANGER:
            case SAMPLECHANGER:
                return "sample_changer";
            case ANALYSER:
                return "analyser";
            case POLARISER:
                return "polariser";
            case ROTATINGBENCH:
                return "rotating_bench";
            case JULABO:
                return "julabo";
            case EUROTHERM:
                return "euro";
            case PINHOLESELECTOR:
                return "pinhole_selector";
            case SINGLESTAGE:
                return "single_stage";
            case LINKAM95:
                return "linkam95";
            case ATTENUATOR:
                return "attenuator";
            case MERCURY:
                return "mercury";
            case CRYVALVE:
                return "iriscryovalve";
            case MUON_FRONT_END:
                return "muon_front_end";
            default:
                return "cog";
        }
	}
}
