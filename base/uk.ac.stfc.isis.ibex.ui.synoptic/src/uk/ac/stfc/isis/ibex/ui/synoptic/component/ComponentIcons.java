
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

package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;

public final class ComponentIcons {
	
	private ComponentIcons() { }
	
	public static Image iconForComponent(Component component) {
		switch (component.type()) {
			case JAWS:
				return icon("jaws.png");
			case CHOPPER:
				return icon("chopper.png");
			case MONITOR:
				return icon("monitor.png");
			case SAMPLESTACK:
				return icon("sample_stack.png");
			case DAE:
				return icon("dae.png");
			case KEPCO:
			case CAEN:
				return icon("caen.png");
			case BEAMSTOP:
				return icon("beamstop.png");
			case MOVINGMONITOR:
			case MOVINGBEAMSTOP:
				return icon("moving_monitor.png");
			case SAMPLECHANGER:
				return icon("sample_changer.png");
			case ANALYSER:
				return icon("analyser.png");
			case POLARISER:
				return icon("polariser.png");
			case ROTATINGBENCH:
				return icon("rotating_bench.png");
            case JULABO:
				return icon("julabo.png");
			case EUROTHERM:
				return icon("euro.png");
			case GONIOMETER:
				return icon("ALF Gonio.png");
//			case PINHOLESELECTOR:
//				return icon("pinhole_selector.png");
			case SINGLESTAGE:
				return icon("single_stage.png");
			default:
				return icon("cog.png");
		}
	}
	
	private static Image icon(String fileName) {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/" + fileName);
	}
}
