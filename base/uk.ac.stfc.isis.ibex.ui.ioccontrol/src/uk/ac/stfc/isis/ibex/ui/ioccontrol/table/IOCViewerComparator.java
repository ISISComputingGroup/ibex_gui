
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

import java.util.Comparator;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import uk.ac.stfc.isis.ibex.configserver.IocState;

/**
 * Provides labels for components within the ioc editor.
 */
public class IOCViewerComparator extends ViewerComparator {
	/**
	 * @param comparator The comparator to use for sorting.
	 */	
	public IOCViewerComparator(Comparator<? super String> comparator) {
		super(comparator);
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		String name1 = this.getLabel(viewer, e1);
		String name2 = this.getLabel(viewer, e2);
		if (name1 == "Running") {
			return -1;
		}
		if (name2 == "Running") {
			return 1;
		}
		if (name1 == "In Config") {
			return -1;
		}
		if (name2 == "In Config") {
			return 1;
		}
		// use the comparator to compare the strings
		return getComparator().compare(name1.toLowerCase(), name2.toLowerCase());
	}
	
	private String getLabel(Viewer viewer, Object e1) {
		String name1;
		if (viewer == null || !(viewer instanceof ContentViewer)) {
			name1 = e1.toString();
		} else {
			if (e1 instanceof IOCList) {
				name1 = IOCList.class.cast(e1).name;
				
			} else {
				name1 = IocState.class.cast(e1).getName();
			}
		}
		if (name1 == null) {
			name1 = "";
		}
		return name1;
	}
	
}
