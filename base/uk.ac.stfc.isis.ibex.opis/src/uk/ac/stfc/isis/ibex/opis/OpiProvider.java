
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.core.runtime.Path;

/**
 * Provides information about the available OPIs.
 *
 */
public class OpiProvider extends Provider {

	public Collection<String> getOpiList() {
		Collection<String> relativeFilePaths = new ArrayList<String>();
		Path root = pathToFileResource("/resources/");
		Iterator<File> itr = FileUtils.iterateFiles(root.toFile(), new SuffixFileFilter(".opi"), TrueFileFilter.INSTANCE);
		
		while (itr.hasNext()) {
			Path path = new Path(itr.next().getAbsolutePath());
			relativeFilePaths.add(path.makeRelativeTo(root).toString());
		}
		
		return relativeFilePaths;
	}
	
	/**
	 * This generates a path when the name is actually the end of the path.
	 * 
	 * @param name the end of the path (e.g. Eurotherm.opi)
	 * @return the constructed path
	 */
	public Path pathFromName(String name) {
		return pathToFileResource("/resources/" + name);
	}
	
}
