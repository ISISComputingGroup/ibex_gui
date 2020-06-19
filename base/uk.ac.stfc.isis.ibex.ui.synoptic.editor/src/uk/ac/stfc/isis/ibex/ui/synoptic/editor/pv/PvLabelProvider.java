
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

import org.eclipse.jface.viewers.LabelProvider;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;

/**
 * Label provider for the PV type from synoptic. To display the value in PV details panel.
 *
 */
public class PvLabelProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		if (element instanceof PV) {
			PV pv = (PV) element;
			String display = pv.displayName();
			display += " (" + pv.recordType().io().name() + ")";
			return display;
		} else {
			return "#UNKNOWN OBJECT TYPE";
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
}
