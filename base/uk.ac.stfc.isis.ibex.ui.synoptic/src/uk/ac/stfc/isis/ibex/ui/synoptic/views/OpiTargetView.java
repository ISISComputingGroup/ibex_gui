
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

package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import java.util.Map;

import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.OPIView;

public final class OpiTargetView extends OPIView {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.OpiTargetView"; //$NON-NLS-1$

	private final String pvPrefix = Instrument.getInstance().currentInstrument().pvPrefix();
	
	private String opiName;
	
	public void setOpi(String title, String opiName, Map<String, String> macros) {
		this.opiName = opiName;
		
		macros.put("NAME", title);
		macros.put("OPINAME", opiName);
		addMacros(macros);	
		initialiseOPI();
	}
		
	@Override
	protected Path opi() {
		Path path = Opi.getDefault().descriptionsProvider().pathFromName(opiName);
		
		if (path != null) {
			return path;
		}
		
		// This is for back-compatibility; previously the opi name in the synoptic was the path
		// At some point this can be removed.
		return Opi.getDefault().opiProvider().pathFromName(opiName);
	}
	
	private void addMacros(Map<String, String> macros) {
		MacrosInput input = macros();
		input.put("P", pvPrefix);
		for (Map.Entry<String, String> macro : macros.entrySet()) {
			input.put(macro.getKey(), macro.getValue());
		}
	}
}
