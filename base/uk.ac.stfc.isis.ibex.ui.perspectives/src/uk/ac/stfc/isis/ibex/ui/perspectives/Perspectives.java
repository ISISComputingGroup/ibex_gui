
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class Perspectives {

	private final List<IsisPerspective> perspectives = new ArrayList<>();
	private final Map<String, String> ids = new HashMap<>();
	
	public Perspectives() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.perspectives");
		for (IConfigurationElement element : elements) {
			try {
				final Object obj = element.createExecutableExtension("class");
				IsisPerspective perspective = (IsisPerspective) obj;
				perspectives.add(perspective);
				ids.put(perspective.name(), perspective.ID());
				
				Collections.sort(perspectives);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<IsisPerspective> get() {		
		return perspectives;
	}
	
	public String getID(String perspectiveName) {
		return ids.get(perspectiveName);
	}
	
}
