//CHECKSTYLE:OFF

/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2020 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.tests;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;;

/**
 * Tests for the synoptic component icons.
 */
public class IconConventionsTest {
	
	private static final String ICONS_PATH = Paths.get(System.getProperty("user.dir"), "..", "uk.ac.stfc.isis.ibex.ui.devicescreens", "icons", "components").toString();
	private static final String THUMBS_PATH = Paths.get(ICONS_PATH, "thumbs").toString();
	private static final String OPI_INFO_PATH = Paths.get(System.getProperty("user.dir"), "..", "uk.ac.stfc.isis.ibex.opis", "resources", "opi_info.xml").toString();
	
	// Icon thumbs image files have height of 28px and a width of 28px including margins.
	private static final int THUMB_FIXED_HEIGHT = 28;
	private static final int THUMB_FIXED_WIDTH = 28;
	
	// Synoptic icon image files have a maximum height of 100px.
	private static final int ICON_MAX_HEIGHT = 100;
	
	// The icon image should be a .png file.
	private static final String FILE_TYPE = "image/png";
	
	// Synoptic icon image files have a maximum width of (icon width + 20px) including margins.
	// The fill colour of the icons is 0xAAA19C. The border color is black.
	// The icon background should be transparent.
	

	/**
	 * Returns a list of File objects found in the specified path.
	 * @param path The path of the directory.
	 * @return The list of files.
	 */
	private List<File> getFilesList(String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			System.err.println(String.format("Specified path [%s] was not found.", path));
		}
		List<File> listOfFiles = new ArrayList<>();
		
		// Get only the types from opi_info and remove duplicates
		HashSet<String> infoTypes = new HashSet<String>(getOPIKeysAndTypes().values());
		HashSet<String> infoFiles = new HashSet<String>();
		// Convert type names found in opi_info.xml to the file names these types correspond to.
		for (String type : infoTypes) {
			infoFiles.add(typeNameToFileName(type));
		}
		
		// Add the file to the list only if used by an OPI (it exists within opi_info).
		for (File file : folder.listFiles()) {
			if (infoFiles.contains(file.getName()) && (!file.isDirectory())) {
				listOfFiles.add(file);
			}
		}

		return listOfFiles;
	}
	
	/**
	 * Returns the BufferedImage found at given path.
	 * @param path The path of the file.
	 * @return The BufferedImage found at the path.
	 * @throws IOException If file could not be read (e.g. incorrect path).
	 */
	private BufferedImage readImage(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		return image;
	}
	
	/**
	 * Returns a map of keys and types from current OPIs in opi_info.xml.
	 * @return The key and type of current OPIs, empty if an exception occurred while parsing the info file, null if invalid tag name.
	 */
	private Map<String, String> getOPIKeysAndTypes() {
		
		Map<String, String> keysAndTypes= new HashMap<String, String>();
		
		try {
			File inputFile = new File(OPI_INFO_PATH);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("entry");
		         
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String OPIKey = eElement.getElementsByTagName("key").item(0).getTextContent();
					String OPIType = eElement.getElementsByTagName("type").item(0).getTextContent();
					keysAndTypes.put(OPIKey, OPIType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return keysAndTypes;	
	}
	
	/**
	 * Gets a set of types that currently exist in the opi_info.xml file but are not declared in ComponentType.
	 * @return A set of types that are found in opi_info.xml but are not declared in ComponentType, empty list if all are declared.
	 */
	private HashSet<String> getUndeclaredTypes() {
		HashSet<String> infosTypes = new HashSet<String>(getOPIKeysAndTypes().values());
		HashSet<String> componentTypes = new HashSet<String>(ComponentType.componentTypeList());
		
		infosTypes.removeAll(componentTypes);
		
		return infosTypes;
	}
	
	/**
	 * Convert a type name to the name of the file it corresponds to.
	 * @param typeName The name of the type.
	 * @return The name of the file.
	 */
	private String typeNameToFileName (String typeName) {
		ComponentType type;
		
		// If type is not found as a constant within ComponentType, leave it to UNKNOWN instead.
		try {
			type = Enum.valueOf(ComponentType.class, typeName);
		} catch (IllegalArgumentException e) {
			type = Enum.valueOf(ComponentType.class, "UNKNOWN");
		}
		String filename = ComponentIcons.iconNameForType(type);
		
		return filename;
	}
	
	
	@Test
	public void GIVEN_icons_THEN_correct_height() throws IOException {
		boolean allPass = true;
		
		//ComponentIcons.iconForType("MOTION_SET_POINTS_FEW");	
		for (File file : getFilesList(ICONS_PATH)) {
			BufferedImage image = readImage(file);
			int height = image.getHeight();
			
			if (height > ICON_MAX_HEIGHT) {
				allPass = false;
				System.err.println(String.format("Height of %s (%s px) should be no higher than %s px.", file.getName(), height, ICON_MAX_HEIGHT));
			}
		}

		String errMessage = "One or more files does not adhere to conventions, please check the console log for details.";
		assertEquals(errMessage, true, allPass);
	}
	
	@Test
	public void GIVEN_thumbs_THEN_correct_height_and_width() throws IOException {
		boolean allPass = true;
		
		for (File file : getFilesList(THUMBS_PATH)) {
			BufferedImage image = readImage(file);
			int height = image.getHeight();
			int width = image.getWidth();
			
			if (height != THUMB_FIXED_HEIGHT) {
				allPass = false;
				System.err.println(String.format("Height of %s (%s px) should be %s px.", file.getName(), height, THUMB_FIXED_HEIGHT));
			}
			if (width != THUMB_FIXED_WIDTH) {
				allPass = false;
				System.err.println(String.format("Width of %s (%s px) should be %s px.", file.getName(), width, THUMB_FIXED_WIDTH));
			}
		}

		String errMessage = "One or more files does not adhere to conventions, please check the console log for details.";
		assertEquals(errMessage, true, allPass);
	}
	
	@Test
	public void GIVEN_thumbs_THEN_correct_type() throws IOException {
		boolean allPass = true;
		
		for (File file : getFilesList(THUMBS_PATH)) {
			String type = Files.probeContentType(file.toPath());
			
			if (!type.equals(FILE_TYPE)) {
				allPass = false;
				System.err.println(String.format("Type of %s (%s) should be %s.", file.getName(), type, FILE_TYPE));
			}
		}
		
		String errMessage = "One or more files does not adhere to conventions, please check the console log for details.";
		assertEquals(errMessage, true, allPass);
	}
	
	@Test
	public void GIVEN_icons_THEN_correct_type() throws IOException {
		boolean allPass = true;
		
		for (File file : getFilesList(ICONS_PATH)) {
			String type = Files.probeContentType(file.toPath());
			
			if (!type.equals(FILE_TYPE)) {
				allPass = false;
				System.err.println(String.format("Type of %s (%s) should be %s.", file.getName(), type, FILE_TYPE));
			}
		}
		
		String errMessage = "One or more files does not adhere to conventions, please check the console log for details.";
		assertEquals(errMessage, true, allPass);
	}
	
	@Test
	public void GIVEN_types_THEN_no_undeclared_types() throws IOException {
		boolean pass = true;
		
		HashSet<String> undeclaredTypes = getUndeclaredTypes();
		if (!undeclaredTypes.isEmpty()) {
			pass = false;
			System.err.println(String.format("Type(s) are used but not declared in ComponentType %s", undeclaredTypes.toString()));
		}
		
		String errMessage = "One or more types is not declared as a ComponentType, please check the console log for details.";
		assertEquals(errMessage, true, pass);
	}
	
	@Test
	public void GIVEN_existing_opi_THEN_use_relevant_icon() throws IOException {
		boolean allPass = true;
		
		Map<String, String> keysAndTypes = getOPIKeysAndTypes();
		for (String OPIKey : keysAndTypes.keySet()) {
			String OPIType = keysAndTypes.get(OPIKey);
			String defaultIcon = typeNameToFileName("UNKNOWN");
			
			if (typeNameToFileName(OPIType).equals(defaultIcon)) {
				allPass = false;
				System.err.println(String.format("[%s] of type [%s] is using the default icon (%s).",
												OPIKey, OPIType, defaultIcon));
			}
		}

		String errMessage = "One or more OPIs are using the default icon, please check the console log for details.";
		assertEquals(errMessage, true, allPass);
	}
	
	@Test
	public void GIVEN_declared_type_THEN_has_corresponding_icon() throws IOException {
		boolean allPass = true;
			
		HashSet<String> componentTypes = new HashSet<String>(ComponentType.componentTypeList());
		componentTypes.remove("UNKNOWN");
		
		for (String type : componentTypes) {
			String defaultIcon = typeNameToFileName("UNKNOWN");
			
			if (typeNameToFileName(type).equals(defaultIcon)) {
				allPass = false;
				System.err.println(String.format("Declared type [%s] does not have a corresponding icon.",
												type, defaultIcon));
			}
		}
		
		String errMessage = "One or more declared types does not have a corresponding icon, please check the console log for details.";
		assertEquals(errMessage, true, allPass);
	}
	
}

//CHECKSTYLE:ON
