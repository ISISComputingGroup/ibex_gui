
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

package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.runtime.Path;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.OPIView;

public class MotorOPIView extends OPIView {
		
	private static final String MOTOR_OPI = "Motor/mymotor.opi";
	private String partName = "Motor view";
	
	public MotorOPIView() {
		setTitleToolTip("A more detailed view of the motor");
		setTitleImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/cog.png"));
	}

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.motor.views.MotorOPIView"; //$NON-NLS-1$
	
	public void setMotorName(String motorName) {
		macros().put("P", Instrument.getInstance().currentInstrument().pvPrefix());
		macros().put("MM", motorName);		
	}
	
	public void setOPITitle(String partName) {
		this.partName = partName;
	}
	
	@Override
	public void initialiseOPI() {
		super.initialiseOPI();
		super.setPartName(partName);
	}

	@Override
	protected Path opi() {
		return Opi.getDefault().opiProvider().pathFromName(MOTOR_OPI);
	}
}
