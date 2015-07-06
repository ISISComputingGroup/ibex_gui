
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

package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.banner.InMotionState;
import uk.ac.stfc.isis.ibex.banner.Observables;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorStateObserver;

public class InMotionModel extends Closer implements IndicatorModel {
	
	private final IndicatorStateObserver<InMotionState> inMotion;
	
	public InMotionModel(Observables observables) {
		inMotion = registerForClose(new IndicatorStateObserver<InMotionState>(observables.inMotion, new InMotionViewState()));
	}
	
	public UpdatedValue<String> text() {
		return inMotion.text();
	}

	public UpdatedValue<Color> color() {
		return inMotion.color();
	}
	
	public UpdatedValue<Boolean> availability() {
		return inMotion.availability();
	}
	
}
