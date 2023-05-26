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
import java.util.Collections;
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
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;

/**
 * Checks for the synoptic component icons.
 * More system tests than unit tests, but written in Java rather than Python
 * due to the {@link ComponentType} and {@link ComponentIcons} dependencies,
 * in order to get the component type list and the icon files corresponding to each type.
 */
public class IconConventionsTest {
	
	private static final String ICONS_PATH = Paths.get(System.getProperty("user.dir"), "..", "uk.ac.stfc.isis.ibex.ui.devicescreens", "icons", "components").toString();
	private static final String THUMBS_PATH = Paths.get(ICONS_PATH, "thumbs").toString();
	private static final String OPI_INFO_PATH = Paths.get(System.getProperty("user.dir"), "..", "uk.ac.stfc.isis.ibex.opis", "resources", "opi_info.xml").toString();
	
	private static final String GET_ICON = "icon";
	private static final String GET_THUMBNAIL = "thumbnail";
	
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
	 * Returns a list of files found at the specified path and used by the OPIs in opi_info.
	 * @param path The path of the directory.
	 * @return The list of files, empty if none are found or path is invalid.
	 */
	private List<File> getOPIFilesList(String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			return Collections.emptyList();
		}
		
		// Check whether the current path is for icons or thumbs
		String iconOrThumbnail = null;
		if (path.equals(ICONS_PATH)) {
			iconOrThumbnail = GET_ICON;
		} else if (path.equals(THUMBS_PATH)) {
			iconOrThumbnail = GET_THUMBNAIL;
		}
		
		// Get the OPI types from opi_info, remove duplicates via HashSet
		HashSet<String> infoTypes = new HashSet<String>(getOPIKeysAndTypes().values());
		
		// Convert type names found in opi_info.xml to the icon/thumbnail file names of that type.
		HashSet<String> infoTypeFiles = new HashSet<String>();
		for (String type : infoTypes) {
			infoTypeFiles.add(typeNameToFileName(type, iconOrThumbnail));
		}
		
		List<File> filesUsedByOPIs = new ArrayList<>();
		// Add the file to the list only if used by an OPI (it exists within opi_info).
		for (File file : folder.listFiles()) {
			if (infoTypeFiles.contains(file.getName()) && (!file.isDirectory())) {
				filesUsedByOPIs.add(file);
			}
		}

		return filesUsedByOPIs;
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
	 * Convert a type name to the name of the file it corresponds to.
	 * @param typeName The name of the type.
	 * @param iconOrThumbnail Whether to get the file name of the icon or thumbnail.
	 * @return The name of the file.
	 */
	public static String typeNameToFileName (String typeName, String iconOrThumbnail) {
		ComponentType type;
		
		// If type is not found as a constant within ComponentType, leave it to UNKNOWN instead.
		try {
			type = Enum.valueOf(ComponentType.class, typeName);
		} catch (IllegalArgumentException e) {
			type = Enum.valueOf(ComponentType.class, "UNKNOWN");
		}
		
		String filename;
		if (iconOrThumbnail == GET_ICON) {
			filename = ComponentIcons.iconNameForType(type);
		} else if (iconOrThumbnail == GET_THUMBNAIL){
			filename = ComponentIcons.thumbnailNameForType(type);
		} else {
			filename = "";
		}
		
		return filename;
	}
	
	
	@Test
	public void GIVEN_icons_THEN_correct_height() throws IOException {
		List<String> errMessageList = new ArrayList<>();
		
		for (File file : getOPIFilesList(ICONS_PATH)) {
			BufferedImage image = ImageIO.read(file);
			int height = image.getHeight();
			
			if (height > ICON_MAX_HEIGHT) {
				errMessageList.add(String.format("\nHeight of %s (%s px) should be no higher than %s px", file.getName(), height, ICON_MAX_HEIGHT));
			}
		}
		
		String failMessage = "One or more icons does not conform to height conventions.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_thumbs_THEN_correct_height_and_width() throws IOException {
		List<String> errMessageList = new ArrayList<>();
		
		for (File file : getOPIFilesList(THUMBS_PATH)) {
			BufferedImage image = ImageIO.read(file);
			int height = image.getHeight();
			int width = image.getWidth();
			
			if (height != THUMB_FIXED_HEIGHT) {
				errMessageList.add(String.format("\nHeight of %s (%s px) should be %s px", file.getName(), height, THUMB_FIXED_HEIGHT));
			}
			if (width != THUMB_FIXED_WIDTH) {
				errMessageList.add(String.format("\nWidth of %s (%s px) should be %s px", file.getName(), width, THUMB_FIXED_WIDTH));
			}
		}

		String failMessage = "One or more thumbnails does not conform to size conventions.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_file_THEN_correct_type() throws IOException {
		List<String> errMessageList = new ArrayList<>();
		
		for (File file : getOPIFilesList(THUMBS_PATH)) {
			String type = Files.probeContentType(file.toPath());
			
			if (!type.equals(FILE_TYPE)) {
				errMessageList.add(String.format("\nType of %s (%s) should be %s", file.getName(), type, FILE_TYPE));
			}
		}
		for (File file : getOPIFilesList(ICONS_PATH)) {
			String type = Files.probeContentType(file.toPath());
			
			if (!type.equals(FILE_TYPE)) {
				errMessageList.add(String.format("\nType of %s (%s) should be %s", file.getName(), type, FILE_TYPE));
			}
		}
		
		String failMessage = "One or more files does not conform to file type conventions.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_types_THEN_no_undeclared_types() throws IOException {
		List<String> errMessageList = new ArrayList<>();
		
		HashSet<String> infosTypes = new HashSet<String>(getOPIKeysAndTypes().values());	
		infosTypes.removeAll(ComponentType.componentTypeList());
		
		if (!infosTypes.isEmpty()) {
			errMessageList.add(String.format("Type(s) %s are not declared in ComponentType", infosTypes.toString()));
		}
		
		String failMessage = "One or more types is used but not declared in ComponentType.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_existing_opi_THEN_use_relevant_icon() throws IOException {
		List<String> errMessageList = new ArrayList<>();
		String defaultIcon = typeNameToFileName("UNKNOWN", GET_ICON);
		
		Map<String, String> keysAndTypes = getOPIKeysAndTypes();
		for (String OPIKey : keysAndTypes.keySet()) {
			String OPIType = keysAndTypes.get(OPIKey);
			
			// if type is UNKNOWN it will use the default icon, so skip test
			if (OPIType.contentEquals("UNKNOWN")) {
				continue;
			}
			
			if (typeNameToFileName(OPIType, GET_ICON).equals(defaultIcon)) {
				errMessageList.add(String.format("\n[%s] of type [%s] is using the default icon", OPIKey, OPIType));
			}
		}

		String failMessage = String.format("One or more OPIs is using the default icon (%s).", defaultIcon);
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_declared_type_THEN_has_corresponding_icon() throws IOException {
		List<String> errMessageList = new ArrayList<>();	
		
		HashSet<String> componentTypes = new HashSet<String>(ComponentType.componentTypeList());
		componentTypes.remove("UNKNOWN");
		
		for (String type : componentTypes) {
			String defaultIcon = typeNameToFileName("UNKNOWN", GET_ICON);
			
			if (typeNameToFileName(type, GET_ICON).equals(defaultIcon)) {
				errMessageList.add(String.format("\nDeclared type [%s] does not have a corresponding icon",
												type, defaultIcon));
			}
		}
		
		String failMessage = "One or more declared types does not have a corresponding icon.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_declared_type_with_corresponding_icon_THEN_icon_exists() throws IOException {
		List<String> errMessageList = new ArrayList<>();	
		
		HashSet<String> componentTypes = new HashSet<String>(ComponentType.componentTypeList());
		componentTypes.remove("UNKNOWN");
		
		for (String type : componentTypes) {
			String iconName = typeNameToFileName(type, GET_ICON);
			String iconPath = Paths.get(ICONS_PATH, iconName).toString();
			File icon = new File(iconPath);
			
			if (!icon.exists()) {
				errMessageList.add(String.format("\nExpected icon [%s] for type [%s] was not found", iconName, type));
			}
		}
		
		String failMessage = "One or more icons for declared types was not found.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_declared_type_with_corresponding_thumbnail_THEN_thumbnail_exists() throws IOException {
		List<String> errMessageList = new ArrayList<>();	
		
		HashSet<String> componentTypes = new HashSet<String>(ComponentType.componentTypeList());
		componentTypes.remove("UNKNOWN");
		
		for (String type : componentTypes) {
			String thumbnailName = typeNameToFileName(type, GET_THUMBNAIL);
			String thumbnailPath = Paths.get(THUMBS_PATH, thumbnailName).toString();
			File thumbnail = new File(thumbnailPath);
			if (!thumbnail.exists()) {
				errMessageList.add(String.format("\nExpected thumbnail [%s] for type [%s] was not found", thumbnailName, type));
			}
		}
		
		String failMessage = "One or more thumbnails for declared types was not found.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
	@Test
	public void GIVEN_icon_THEN_has_thumbnail() throws IOException {
		List<String> errMessageList = new ArrayList<>();	
		
		List<File> icons = getOPIFilesList(ICONS_PATH);
		List<String> iconNames = new ArrayList<>();
		for (File icon : icons) {
			iconNames.add(icon.getName().split(".png")[0]);
		}
		
		for (String name : iconNames) {
			String thumbnailPath = Paths.get(THUMBS_PATH, name + "_tb.png").toString();
			File thumbnail = new File(thumbnailPath);
			if (!thumbnail.exists()) {
				errMessageList.add(String.format("\nIcon [%s.png] does not have a corresponding thumbnail", name));
			}
		}
		
		String failMessage = "One or more icons does not have a corresponding thumbnail.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
		
	
	@Test
	public void GIVEN_thumbnail_THEN_has_icon() throws IOException {
		List<String> errMessageList = new ArrayList<>();
		
		List<File> thumbnails = getOPIFilesList(THUMBS_PATH);
		List<String> thumbnailNames = new ArrayList<>();
		for (File icon : thumbnails) {
			thumbnailNames.add(icon.getName().split("_tb")[0]);
		}
		
		for (String name : thumbnailNames) {
			String iconPath = Paths.get(ICONS_PATH, name + ".png").toString();
			File icon = new File(iconPath);
			if (!icon.exists()) {
				errMessageList.add(String.format("\nThumbnail [%s.png] does not have a corresponding icon", name));
			}
		}
		
		String failMessage = "One or more thumbnails does not have a corresponding icon.";
		assertEquals(failMessage, Collections.emptyList(), errMessageList);
	}
	
}

//CHECKSTYLE:ON
