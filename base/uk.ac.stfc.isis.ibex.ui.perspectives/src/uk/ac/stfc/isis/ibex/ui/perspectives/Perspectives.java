
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

package uk.ac.stfc.isis.ibex.ui.perspectives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.preferences.Preferences;

/**
 * Class for holding all of the ISIS perspectives.
 */
public class Perspectives {

	private final List<IsisPerspective> perspectives = new ArrayList<>();
	private IPreferenceStore store = Preferences.getDefault().getPreferenceStore();
	
	/**
	 * Default constructor for the class, will gather all of the ISIS perspectives into a list.
	 */
	public Perspectives() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.perspectives");
		for (IConfigurationElement element : elements) {
			try {
				final Object obj = element.createExecutableExtension("class");
				IsisPerspective perspective = (IsisPerspective) obj;
				perspectives.add(perspective);

				store.setDefault(perspective.id(), perspective.isVisibleDefault());
				
				Collections.sort(perspectives);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get all the ISIS perspectives.
	 * @return A list of all perspectives
	 */
	public List<IsisPerspective> get() {		
		return perspectives;
	}
	
	/**
	 * Get the perspectives that are visible to the user.
	 * @return A list of visible perspectives
	 */
	public List<IsisPerspective> getVisible() {
		List<IsisPerspective> newList = new ArrayList<>();
		
		for (IsisPerspective perspective : perspectives) {
			if (store.getBoolean(perspective.id())) {
				newList.add(perspective);
			}
		}
		
		return newList;
    }
}
