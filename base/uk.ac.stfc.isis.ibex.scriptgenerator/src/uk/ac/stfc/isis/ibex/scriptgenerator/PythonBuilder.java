
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

package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDate;
import java.time.LocalTime;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Generates Python code based on values in the Script Generator table and the settings.
 */
public class PythonBuilder extends ModelObject {
	private Collection<Row> rows;
	private Settings settings;
	private StringBuilder script;
	
	/**
	 * The default constructor.
	 */
	public PythonBuilder() {
	}
	
	/**
	 * Generates necessary header code needed for each script.
	 * @return the header block of Python code
	 */
	private String generateHeader() {
		StringBuilder header = new StringBuilder();
		
		String creationComment = "# Script created by ZOOM Script at " + LocalDate.now() + " " +  LocalTime.now() + "\n";
		String sansImport = "import LSS.SANSroutines as lm" + "\n\n";
		String functionName = "def my_script():" + "\n";
		String genieImport = "    from genie_python.genie import *" + "\n";
		String zoomSetup = "    lm.setupzoom_normal()" + "\n";
		
		header.append(creationComment).append(sansImport).append(functionName).append(genieImport).append(zoomSetup);
		
		return header.toString();
	}

	/**
	 * Uses set_sample_par for the "Sample Height", "Sample Width" and "Sample Geometry" settings.
	 * @param name the name of the setting, "width", "height" or "geometry"
	 * @param value a numerical value for the "width" and "height", or the type of geometry, e.g. "disc"
	 * @return
	 */
	private String buildSamplePar(String name, String value) {	
		String samplePar = String.format("    set_sample_par('%s', '%s') \n", name, value);
		
		return samplePar;
	}
	
	/**
	 * Builds a string used for the do sans loop.
	 * @return the sans loop string
	 */
	private String buildSans() {
		StringBuilder sans = new StringBuilder();
		StringBuilder rowData = new StringBuilder();
		boolean populatedRow = false;
		String collectionMode = null;
		
		// Based on the collection mode's two settings, set the rtype value to one of its two possibilities 
		if (settings.getCollection().toString() != null) {
			collectionMode = (settings.getCollection().toString() == "Histogram") ? "rtype='0'" : "rtype='1'"; 
		}
		
		for (Row row: rows) {
			if (row.getPosition() != null) {
				rowData.append(String.format("    set_aperture('%s')\n", settings.getSansSize()));
				if (row.getSans() != null && row.getSansWait() != null) {
					rowData.append(String.format("    lm.dosans_normal(position='%s', title='%s', %s='%s', thickness='%s', %s)\n", 
							row.getPosition(), row.getSampleName(), row.getSansWait() /*row.getSansWait().toString().toLowerCase()*/, row.getSansWait(), row.getThickness(), collectionMode));
				} else {
					rowData.append(String.format("    lm.dosans_normal(position='%s', title='%s', uamps='0', thickness='%s', %s)\n", 
							row.getPosition(), row.getSampleName(), row.getThickness(), collectionMode));
				} 
			populatedRow = true;
			}
		}
		
		if (populatedRow) {
			sans.append(String.format("\nfor i in range(%d):\n", settings.getDoSans()));
			sans.append(rowData);
		}
		
		return sans.toString();
	}
	
	/**
	 * Builds a string used for the do trans loop.
	 * @return the trans loop
	 */
	private String buildTrans() {
		StringBuilder trans = new StringBuilder();
		StringBuilder rowData = new StringBuilder();
		boolean populatedRow = false;
		String collectionMode = null;
		
		// Based on the collection mode's two settings, set the rtype value to one of its two possibilities  
		if (settings.getCollection().toString() != null) {
			collectionMode = (settings.getCollection().toString() == "Histogram") ? "rtype='0'" : "rtype='1'"; 
		}
		
		for (Row row: rows) {
			if (row.getPosition() != null) {
				rowData.append(String.format("    set_aperture('%s')\n", settings.getTransSize()));
				if (row.getTrans() != null && row.getTransWait() != null) {
					rowData.append(String.format("    lm.dotrans_normal(position='%s', title='%s', %s='%s', thickness='%s', %s)\n", 
							row.getPosition(), row.getSampleName(), row.getTransWait() /*row.getSansWait().toString().toLowerCase()*/, row.getTransWait(), row.getThickness(), collectionMode));
				} else {
					rowData.append(String.format("    lm.dotrans_normal(position='%s', title='%s', uamps='0', thickness='%s', %s)\n", 
							row.getPosition(), row.getSampleName(), row.getThickness(), collectionMode));
				} 
			populatedRow = true;
			}
		}
		
		if (populatedRow) {
			trans.append(String.format("\nfor i in range(%d):\n", settings.getDoTrans()));
			trans.append(rowData);
		}
		
		return trans.toString();
	}
	
	/**
	 * Receives and sets the rows from the view.
	 * @param rows the table rows
	 */
	public void setRows(Collection<Row> rows) {
		this.rows = rows;
		
		createScript();
	}
	
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	/**
	 * Returns the script back to the view to be displayed.
	 * @return the completed script
	 */
	public String getScript() {
		createScript();
		
		return script.toString();
	}
	
	/**
	 * Calls internal build script methods and adds these returned strings to the main script.
	 */
	public void createScript() {
		script = new StringBuilder();
		
		script.append(generateHeader());
		
		script.append(buildSamplePar("width", settings.getSampleWidth().toString()));
		script.append(buildSamplePar("height", settings.getSampleHeight().toString()));
		script.append(buildSamplePar("geometry", settings.getGeometry().toString()));
		
		script.append(buildSans());
		script.append(buildTrans());
	}
}
