
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

package uk.ac.stfc.isis.ibex.ui.blocks.presentation;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;

/**
 * This plugin creates an extension point for other plugins to hook into if they have a method for displaying historic PV data.
 */
public class Presenter extends Plugin {	
    private static final String EXTENSION_POINT_ID = "uk.ac.stfc.isis.ibex.ui.blocks.presentation";
	
	/**
	 * Get the PV history presenter.
	 * @return The PV history presenter.
	 */
	public static PVHistoryPresenter pvHistoryPresenter() {
		return Arrays.stream(Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID))
			.map(element -> {
				try {
				    return element.createExecutableExtension("class");
				} catch (CoreException e) {
					return null;  // will be filtered out below
				}
			})
			.filter(obj -> obj instanceof PVHistoryPresenter)
			.map(obj -> (PVHistoryPresenter) obj)
			.findFirst()
			.orElseGet(NullPVHistoryPresenter::new);
	}

}

