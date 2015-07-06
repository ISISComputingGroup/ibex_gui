
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

package uk.ac.stfc.isis.ibex.rotatingbench;

import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.rotatingbench.internal.Variables;

public class RotatingBenchModel extends ModelAdapter implements IRotatingBench {
	
	private final UpdatedValue<Double> angle;
	private final UpdatedValue<Double> angleSP;
	private final SameTypeWriter<Double> writeAngleSP = new SameTypeWriter<>();
	private final UpdatedValue<Double> angleSPRBV;
	private final UpdatedValue<Status> status;
	private final UpdatedValue<LiftStatus> liftStatus;
	private final UpdatedValue<Command> command;
	
	private final SameTypeWriter<String> writeCommand = new SameTypeWriter<>();
	
	public RotatingBenchModel(Variables bench) {
		angle = adapt(bench.angle);
		angleSP = adapt(bench.angleSPValue);
		writeAngleSP.writeTo(bench.angleSP);
		
		angleSPRBV = adapt(bench.angleSPRBV);
		status = adapt(bench.status);
		liftStatus = adapt(bench.liftStatus);
		
		command = adapt(bench.checkHV);
		writeCommand.writeTo(bench.writeCheckHV);
	}

	@Override
	public UpdatedValue<Double> angle() {
		return angle;
	}
	
	@Override
	public UpdatedValue<Status> status() {
		return status;
	}
	
	@Override
	public UpdatedValue<LiftStatus> liftStatus() {
		return liftStatus;
	}

	@Override
	public UpdatedValue<Command> command() {
		return command;
	}

	@Override
	public UpdatedValue<Double> angleSP() {
		return angleSP;
	}
	
	@Override
	public void setAngleSP(double value) {
		writeAngleSP.write(value);
	}

	@Override
	public UpdatedValue<Double> angleSPRBV() {
		return angleSPRBV;
	}

	@Override
	public void setCommand(boolean value) {
		writeCommand.write(value ? "YES" : "NO");
	}
}
