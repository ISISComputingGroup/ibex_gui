
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

package uk.ac.stfc.isis.ibex.opis;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * The base class for anything that provides information relating to the OPIs.
 *
 */
public abstract class Provider {
	
	protected static final Logger LOG = IsisLog.getLogger(OpiProvider.class);
	
	/**
	 * @return a list of OPI names
	 */
	public abstract Collection<String> getOpiList();
	
	/**
	 * Gets the path associated with the OPI.
	 * 
	 * @param name the OPI's name/key in the description XML
	 * @return the path
	 */
	public abstract Path pathFromName(String name);
	
	/**
	 * @param relativePath the relative path to the OPI
	 * @return the full path to the OPI
	 */
	protected final Path pathToFileResource(String relativePath) {	
		Path path = null;
		try {
			URL url = getClass().getResource(relativePath);
			String filePath = FileLocator.resolve(url).getPath();			
			path = new Path(filePath);
		} catch (IOException e) {
			LOG.catching(e);
		}
		
		return path;
	}
}
