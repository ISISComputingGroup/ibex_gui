
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.ibex.epics.conversion.XMLUtil;
import uk.ac.stfc.isis.ibex.opis.desc.Descriptions;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
/**
 * Provides the descriptions of the OPIs from a file.
 *
 */
public class DescriptionsProvider extends Provider {
	
	private static final String FILENAME = "opi_info.xml";
	
	private final Descriptions descriptions;
	
	public DescriptionsProvider() {
		descriptions = importDescriptions();
	}
	
	/**
	 * This constructor is for unit-testing only.
	 * 
	 * @param descriptions the Descriptions to use
	 */
	public DescriptionsProvider(Descriptions descriptions) {
		this.descriptions = descriptions;
	}
	
    /**
     * Get all of the OPI descriptions.
     * 
     * @return Descriptions for all of the OPIs.
     */
	public Descriptions getDescriptions() {
		return descriptions;
	}
	
    /**
     * Get the description of a specific OPI given its name.
     * 
     * @param name
     *            The name of the OPI to get the description for.
     * @return The description of the named OPI.
     */
	public OpiDescription getDescription(String name) {
		if (!descriptions.getOpis().containsKey(name)) {
            return new OpiDescription("", "", "", new ArrayList<MacroInfo>());
		}
		
		return descriptions.getOpis().get(name);
	}
	
	private Descriptions importDescriptions() {
		Path path = pathToFileResource("/resources/" + FILENAME);
		StringBuilder sb = new StringBuilder();
		
        try (BufferedReader br = new BufferedReader(new FileReader(path.toOSString()))) {
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
			return new Descriptions();
		} 
		
		try {
            return XMLUtil.fromXml(sb.toString(), Descriptions.class);
        } catch (JAXBException e) {
			e.printStackTrace();
			return new Descriptions();
		}
	}

	@Override
	public Collection<String> getOpiList() {
        List<String> availableOPIs = new ArrayList<String>(descriptions.getOpis().keySet());
        Collections.sort(availableOPIs);
        return availableOPIs;
	}

	@Override
	public Path pathFromName(String name) {
		if (!descriptions.getOpis().containsKey(name)) {
			return null;
		}
		
		return pathToFileResource("/resources/" + descriptions.getOpis().get(name).getPath());
	}
	
	/**
	 * For back-compatibility - in the future this can be removed.
	 * The OPI name might be the end of the path rather than the name, in which case, the name
	 * should be guessed based on this.
	 * 
	 * @param name the name used to guess with
	 * @return the best guess for the correct name
	 */
	@SuppressWarnings("unchecked")
	public String guessOpiName(String name) {
		if (descriptions.getOpis().containsKey(name)) {
			// It is a new style name
			return name;
		}
		
		// Find the name the hard way!
		Iterator<?> it = descriptions.getOpis().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, OpiDescription> pair = (Map.Entry<String, OpiDescription>) it.next();
	        if (pair.getValue().getPath().replace("\\", "/").equalsIgnoreCase(name.replace("\\", "/"))) {
	        	return pair.getKey();
	        }
	    }
		
	    // If all else fails return an empty string
		return "";
	}
	
}
