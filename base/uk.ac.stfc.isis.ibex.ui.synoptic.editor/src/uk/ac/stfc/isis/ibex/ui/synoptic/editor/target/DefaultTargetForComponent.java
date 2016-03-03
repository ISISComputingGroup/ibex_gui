
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

/**
 * @author kvlb23
 *
 */
public final class DefaultTargetForComponent {
    
    private DefaultTargetForComponent() {
    }

    public static Collection<TargetDescription> defaultTarget(ComponentType compType) {
        Map<String, TargetType> targetNameList = new HashMap<>();

        switch (compType) {
            case UNKNOWN:
            case MOVINGBEAMSTOP:
                // no sensible default(s) for the above at present
                break;
            case DAE:
                targetNameList.put("DAE", TargetType.COMPONENT);
                break;
            case GONIOMETER:
                targetNameList.put("Goniometer", TargetType.COMPONENT);
                break;
            case JAWS:
                targetNameList.put("Slit 1", TargetType.OPI);
                break;
            case CHOPPER:
                targetNameList.put("Mk3 Chopper", TargetType.OPI);
                targetNameList.put("SKF G5 Chopper", TargetType.OPI);
                break;
            case MONITOR:
                targetNameList.put("Monitor", TargetType.OPI);
                break;
            case JULABO:
                targetNameList.put("Julabo FL300", TargetType.OPI);
                targetNameList.put("Julabo FP50", TargetType.OPI);
                break;
            case SAMPLESTACK:
                targetNameList.put("Sample stage", TargetType.OPI);
                break;
            case CAEN:
                targetNameList.put("Caen HV", TargetType.OPI);
                break;
            case KEPCO:
                targetNameList.put("Kepco", TargetType.OPI);
                break;
            case BEAMSTOP:
                targetNameList.put("Beam-stop", TargetType.OPI);
                break;
            case MOVINGMONITOR:
                targetNameList.put("In Out Monitor", TargetType.OPI);
                break;
            case ROTATINGBENCH:
                targetNameList.put("Rotating Bench", TargetType.OPI);
                break;
            case SAMPLECHANGER:
                targetNameList.put("Sample changer", TargetType.OPI);
                break;
            case ANALYSER:
                targetNameList.put("Analyser", TargetType.OPI);
                break;
            case POLARISER:
                targetNameList.put("Polariser", TargetType.OPI);
                break;
            case EUROTHERM:
                targetNameList.put("Eurotherm", TargetType.OPI);
                break;
            case PINHOLESELECTOR:
                targetNameList.put("Pinhole Selector", TargetType.OPI);
                break;
            case SINGLESTAGE:
                targetNameList.put("Single Stage", TargetType.OPI);
                break;
            default:
                // no sensible default(s) at present
                break;

        }
        
        Collection<TargetDescription> targetDescriptions = new ArrayList<>();

        for (String targetName : targetNameList.keySet()) {
            if (targetNameInOpiList(targetName) && targetNameList.get(targetName) == TargetType.OPI) {
                targetDescriptions.add(new TargetDescription(targetName, targetNameList.get(targetName)));
            }
        }
        
        if (targetDescriptions.size() == 0) {
            targetDescriptions.add(new TargetDescription("NONE", TargetType.OPI));
        }

        return targetDescriptions;
    }

    private static Boolean targetNameInOpiList(String targetName) {
        Collection<String> availableOPIs = Opi.getDefault().descriptionsProvider().getOpiList();
        return availableOPIs.contains(targetName);
    }

}
