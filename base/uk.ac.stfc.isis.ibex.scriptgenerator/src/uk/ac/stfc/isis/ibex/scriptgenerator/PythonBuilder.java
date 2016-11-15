
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

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.row.Row;

/**
 * Generates Python code based on values in the Script Generator table and the settings.
 */
public class PythonBuilder extends ModelObject {
	private int doSans = 1;
	private int doTrans = 1;
	private Collection<Row> rows;
	
	/**
	 * The default constructor.
	 */
	public PythonBuilder() {
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
	 * @param rows the table rows
	 */
	public void setRows(Collection<Row> rows) {
		this.rows = rows;
	}
	
	/**
	 * Returns the script back to the view to be displayed.
	 * @return the completed script
	 */
	public String getScript() {
        return createScript();
	}
	
	/**
     * Creates a valid Python script.
     * 
     * Currently does not create a valid script!
     */
    private String createScript() {
        StringBuilder sb = new StringBuilder();
		
		for (Row row : rows) {
            sb.append("position = " + row.getPosition() + System.lineSeparator());
		}

        return sb.toString();
	}
}
