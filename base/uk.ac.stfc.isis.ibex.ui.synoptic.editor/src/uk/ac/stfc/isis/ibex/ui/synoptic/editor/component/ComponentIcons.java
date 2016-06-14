
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

public final class ComponentIcons {
	
	private ComponentIcons() { }
	
	public static Image thumbnailForComponent(ComponentDescription component) {
		return thumbnailForType(component.type());
	}

	public static Image thumbnailForType(ComponentType type) {
		switch (type) {
		case JAWS:
			return icon("thumb/jaws_tb.png");
		case CHOPPER:
			return icon("thumb/chopper_tb.png");
		case MONITOR:
			return icon("thumb/monitor_tb.png");
		case SAMPLESTACK:
			return icon("thumb/sample_stack_tb.png");
		case DAE:
			return icon("thumb/dae_tb.png");
        case DANFSYIK:
		case KEPCO:
        case TDK_LAMBDA_GENESYS:
		case CAEN:
			return icon("thumb/caen_tb.png");
		case BEAMSTOP:
			return icon("thumb/beamstop_tb.png");
		case MOVINGMONITOR:
		case MOVINGBEAMSTOP:
			return icon("thumb/moving_monitor_tb.png");
		case SAMPLECHANGER:
			return icon("thumb/sample_changer_tb.png");
		case ANALYSER:
			return icon("thumb/analyser_tb.png");
		case POLARISER:
			return icon("thumb/polariser_tb.png");
		case ROTATINGBENCH:
			return icon("thumb/rotating_b_tb.png");
		case JULABO:
			return icon("thumb/julabo_tb.png");
		case EUROTHERM:
			return icon("thumb/euro_tb.png");
		case PINHOLESELECTOR: 
			return icon("thumb/pinhole_selector_tb.png");
		case SINGLESTAGE:
			return icon("thumb/single_stage_tb.png");
		case LAKESHORE:
                return icon("thumb/lakeshore_tb.png");
		default:
			return icon("thumb/cog_tb.png");
		}
	}

	private static Image icon(String fileName) {
		return ResourceManager.getPluginImage(
				"uk.ac.stfc.isis.ibex.ui.synoptic.editor", "icons/" + fileName);
	}
}
