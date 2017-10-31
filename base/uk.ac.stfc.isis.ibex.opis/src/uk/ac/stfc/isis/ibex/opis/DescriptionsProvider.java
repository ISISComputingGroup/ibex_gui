
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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
    private static final String CATEGORY_UNKNOWN = "Uncategorised devices";
    private static final String CATEGORY_ALL = "All devices";
	
	private final Descriptions descriptions;
	
	private final Map<String, List<String>> opiCategories;
    	
	/**
	 * Provides information from opi_info.xml.
	 */
	public DescriptionsProvider() {
		descriptions = importDescriptions();
		opiCategories = constructOpiCategories();
	}
	
	/**
	 * This constructor is for unit-testing only.
	 * 
	 * @param descriptions the Descriptions to use
	 */
	public DescriptionsProvider(Descriptions descriptions) {
		this.descriptions = descriptions;
		opiCategories = constructOpiCategories();
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
		try {
            return XMLUtil.fromXml(new FileReader(path.toOSString()), Descriptions.class);
        } catch (JAXBException | IOException e) {
			e.printStackTrace();
			return new Descriptions();
		}
	}

	@Override
	public Collection<String> getOpiList() {
        List<String> availableOPIs = new ArrayList<String>(descriptions.getOpis().keySet());
        Collections.sort(availableOPIs, String.CASE_INSENSITIVE_ORDER);

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
	
    /**
     * Get a Map of lists, where each key in the map is a category and each value is a list of items in that category.
     * @return the map
     */
	public Map<String, List<String>> getOpiCategories() {
	    return opiCategories;
	}

	/**
     * Constructs a Map of lists, where each key in the map is a category and each value is a list of items in that category.
     * 
     * Makes both the map and the lists unmodifiable to prevent accidentally changing it since an instance of this class might
     * be shared among multiple other classes.
     * 
     * @return the map
     */
    private Map<String, List<String>> constructOpiCategories() {
        Map<String, List<String>> map = new HashMap<>();

        for (String opiName : getOpiList()) {
            
            addOpiToMap(map, CATEGORY_ALL, opiName);
            
            OpiDescription desc = getDescription(opiName);

            if (desc.getCategories() == null || desc.getCategories().isEmpty()) {
                // If it has no category then put it in the unknown category
                addOpiToMap(map, CATEGORY_UNKNOWN, opiName);
                continue;
            }

            for (String category : desc.getCategories()) {
                addOpiToMap(map, category, opiName);
            }
        }
        
        // Make map and lists unmodifiable to prevent accidentally changing since they are shared.
        Map<String, List<String>> returnedMap = new HashMap<>();
        for (String key : map.keySet()) {
            returnedMap.put(key, Collections.unmodifiableList(map.get(key)));
        }    
        return Collections.unmodifiableMap(returnedMap);
    }
    
    /**
     * Adds an OPI name-category pair to a map. If the category doesn't yet exist it is created, 
     * if the category does exist then the opi is appended to that category.
     * 
     * @param map the map to add to
     * @param category the category of this opi
     * @param name the name of this opi
     */
    private void addOpiToMap(Map<String, List<String>> map, String category, String name) {
        if (map.get(category) == null) {
            // If the category doesn't exist yet create it
            ArrayList<String> list = new ArrayList<>();
            list.add(name);
            map.put(category, list);
        } else {
            // The category already exists, add this opi to it.
            map.get(category).add(name);
        }
    }
	
}
