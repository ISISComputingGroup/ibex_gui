
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

import org.apache.logging.log4j.Logger;
import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.Path;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.targets.OpiTarget;
import uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView;

/**
 * The Class MotorOPIView which is the opi view generated from opening an opi
 * from the motors page.
 */
public class MotorOPIView extends OpiTargetView {
		
	private static final String MOTOR_OPI = "Motor/mymotor.opi";
	private String motorName;
    private static final Logger LOG = IsisLog.getLogger(MotorOPIView.class);
	
    /**
     * Instantiates a new motor opi view.
     */
	public MotorOPIView() {
		setTitleToolTip("A more detailed view of the motor");
		setTitleImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/cog.png"));
	}

    /** The ID for the view. */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.motor.views.MotorOPIView"; //$NON-NLS-1$

	@Override
	protected Path opi() {
		return Opi.getDefault().opiProvider().pathFromName(MOTOR_OPI);
	}
	
	/**
	 * Display an OPI Target in the Motors perspective Screen.
	 * 
	 * @param title Title to give to the OPI.
	 * @param motorName Name of the motor being opened.
	 */
    public void displayOpi(String title, String motorName) {
    	this.motorName = motorName;
        try {
			OpiTargetView.displayOpi(new OpiTarget(title, MOTOR_OPI), ID);
		} catch (OPIViewCreationException e) {
			LOG.error("Unable to create motor OPI");
		}
    }

	@Override
	public MacrosInput macros() {
		MacrosInput macros = super.macros();
		macros.put("P", Instrument.getInstance().currentInstrument().pvPrefix());
		macros.put("MM", motorName);	
		return macros;
	}
}
