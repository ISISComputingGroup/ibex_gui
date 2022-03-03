
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.configserver.IocState;

/**
 * Provides labels for components within the ioc editor.
 */
public class IOCStatusProvider extends ColumnLabelProvider {
	private static final int COLOR = 255;
	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ArrayList<?>) {
			return null;
		}
		if (IocState.class.cast(element).getIsRunning()) {
			return "Running";
		} else {
			return "Stopped";
		}	 
	}
	
	@Override
	public Color getForeground(Object element) {
		if (element instanceof IocState) {
			
		
			if (IocState.class.cast(element).getIsRunning()) {
				return new Color(0, COLOR, 0);
			} else {
				return new Color(COLOR, 0, 0); 
			}	 
		}
		return null;
	}
	

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
}
