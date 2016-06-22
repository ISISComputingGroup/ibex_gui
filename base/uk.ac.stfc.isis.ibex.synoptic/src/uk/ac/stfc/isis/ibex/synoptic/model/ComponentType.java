
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.targets.PerspectiveTarget;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.ViewTarget;

/**
 * The type of components available to the synoptic.
 * Basically, the things that appear on a beam-line.
 */
public enum ComponentType {
	UNKNOWN,
	JAWS,
	CHOPPER,
	MONITOR,
	SAMPLESTACK,
	DAE (new PerspectiveTarget("DAE")),
    DANFSYIK,
	CAEN,
	KEPCO,
    TDK_LAMBDA_GENESYS,
	BEAMSTOP, 
	MOVINGMONITOR,
	ROTATINGBENCH,
	SAMPLECHANGER,
	MOVINGBEAMSTOP,
	ANALYSER,
	POLARISER,
	JULABO,	
	EUROTHERM,
	PINHOLESELECTOR,
	GONIOMETER (new ViewTarget("Goniometer")), 
	SINGLESTAGE,
    LAKESHORE,
    ATTENUATOR;
	
	private Target target;

	ComponentType() {
	}
	
	ComponentType(Target target) {
		this.target = target;
	}
	
	public Target target() {
		return target;
	}

    /**
     * @return a string list of the enum entries
     */
    public static List<String> componentTypeList() {
        ComponentType[] types = values();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            list.add(types[i].name());
        }

        return list;
    }

    /**
     * @return an alphabetised list of the enum entries
     */
    public static List<String> componentTypeAlphaList() {
        List<String> list = componentTypeList();
        
        // remove unknown
        list.remove(ComponentType.UNKNOWN.toString());
        Collections.sort(list);
        
        // add unknown back at the beginning
        list.add(0, ComponentType.UNKNOWN.toString());
        return list;
    }
}
