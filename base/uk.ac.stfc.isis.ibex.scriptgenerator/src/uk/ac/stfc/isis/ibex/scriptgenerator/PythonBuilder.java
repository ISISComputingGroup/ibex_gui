
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

import java.util.Collection;
import java.time.LocalDate;
import java.time.LocalTime;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Generates Python code based on values in the Script Generator table and the settings.
 */
public class PythonBuilder extends ModelObject {
	private int doSans = 1;
	private int doTrans = 1;
	private Collection<Row> rows;
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
		
		String creationComment = "# Script created by ZOOM Script at " + LocalDate.now() + LocalTime.now() + "\n";
		String sansImport = "import LSS.SANSroutines as lm" + "\n\n";
		String functionName = "def my_script():" + "\n";
		String genieImport = "    from genie_python.genie import *";
		String zoomSetup = "    lm.setupzoom_normal()";
		
		header.append(creationComment).append(sansImport).append(functionName).append(genieImport).append(zoomSetup);
		
		return header.toString();
	}

	/**
	 * Uses set_sample_par for the "Sample Height", "Sample Width" and "Sample Geometry" settings.
	 * @param name the name of the setting, "width", "height" or "geometry"
	 * @param value a numerical value for the "width" and "height", or the type of geometry, e.g. "disc"
	 * @return
	 */
	private String setSamplePar(String name, String value) {
		String samplePar = String.format("    set_sample_par(%s, %s)", name, value);
		
		return samplePar;
	}

	/**
	 * Sets the "Do SANS:" text box value.
	 * @param doSans the do SANS value
	 */
	public void setDoSans(int doSans) {
		this.doSans = doSans;
	}
	
	/**
	 * Receives and sets the rows from the view.
	 * @param rows
	 */
	public void setRows(Collection<Row> rows) {
		this.rows = rows;
		
		createScript();
	}
	
	/**
	 * Returns the script back to the view to be displayed.
	 * @return the completed script
	 */
	public String getScript() {
		return script.toString();
	}
	
	/**
	 * Calls relevant internal methods in order to create a valid Python script.
	 */
	public void createScript() {
		script.append(generateHeader());
		
		for (Row row : rows) {
			script.append("position = " + row.getPosition());
		}
	}
}
