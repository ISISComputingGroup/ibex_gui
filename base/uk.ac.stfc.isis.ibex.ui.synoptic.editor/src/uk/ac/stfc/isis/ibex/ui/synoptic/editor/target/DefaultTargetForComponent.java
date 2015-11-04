
/**
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

/**
 * @author kvlb23
 *
 */
public class DefaultTargetForComponent {
    
    public static TargetDescription defaultTarget(ComponentType compType) {
        TargetDescription target = new TargetDescription();
        String targetName = "NONE";
        TargetType targetType = TargetType.OPI;
        
        switch (compType) {
            case UNKNOWN:
            case DAE:
            case MOVINGBEAMSTOP:
                // no sensible default(s) for the above at present
                break;
            case JAWS:
                targetName = "Slit 1";
                break;
            case CHOPPER:
            case MONITOR:
            case JULABO:
                // multiple options for the above, no default specified
                break;
            case SAMPLESTACK:
                targetName = "Sample stage";
                break;
            case CAEN:
                targetName = "Caen HV";
                break;
            case KEPCO:
                targetName = "Kepco";
                break;
            case BEAMSTOP:
                targetName = "Beam-stop";
                break;
            case MOVINGMONITOR:
                targetName = "In Out Monitor";
                break;
            case ROTATINGBENCH:
                targetName = "Rotating Bench";
                break;
            case SAMPLECHANGER:
                targetName = "Sample changer";
                break;
            case ANALYSER:
                targetName = "Analyser";
                break;
            case POLARISER:
                targetName = "Polariser";
                break;
            case EUROTHERM:
                targetName = "Eurotherm";
                break;
            case PINHOLESELECTOR:
                targetName = "Pinhole Selector";
                break;
            case GONIOMETER:
                targetName = "Goniometer";
                targetType = TargetType.COMPONENT;
                break;
            case SINGLESTAGE:
                targetName = "Single Stage";
                break;
        }

        if (targetType == TargetType.OPI && targetName != "NONE") {
            if (!targetNameInOpiList(targetName)) {
                targetName = "NONE";
            }
        }
        
        target.setName(targetName);
        target.setType(targetType);
        return target;
    }

    private static Boolean targetNameInOpiList(String targetName) {
        Collection<String> availableOPIs = Opi.getDefault().descriptionsProvider().getOpiList();
        return availableOPIs.contains(targetName);
    }

}
