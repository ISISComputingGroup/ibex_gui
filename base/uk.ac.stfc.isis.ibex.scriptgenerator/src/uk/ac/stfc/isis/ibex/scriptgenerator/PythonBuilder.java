
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
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.CollectionMode;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.SansSettings;

/**
 * Generates Python code based on values in the Script Generator table and the settings.
 */
public class PythonBuilder extends ModelObject {
	private Collection<Row> rows;
    private SansSettings settings;
	private StringBuilder script;
    private static final String INDENT = "    ";
    private DateTimeProvider dateTime;
	
    /**
     * The default constructor.
     */
    public PythonBuilder() {
        this(new DateTimeProvider());
    }
	
    /**
     * Creates a PythonBuilder with a custom DateTimeProvider.
     * 
     * @param dateTime
     *            the DateTimeProvider
     */
    public PythonBuilder(DateTimeProvider dateTime) {
        this.dateTime = dateTime;
	}
	
	/**
	 * Generates necessary header code needed for each script.
	 * @return the header block of Python code
	 */
    private String generateHeader() {
		StringBuilder header = new StringBuilder();
		
        String creationComment =
                "# Script created by ZOOM Script at " + dateTime.getDate() + " " + dateTime.getTime() + "\n";
		String sansImport = "import LSS.SANSroutines as lm" + "\n\n";
		String functionName = "def my_script():" + "\n";
        String genieImport = "from genie_python.genie import *" + "\n";
        String zoomSetup = "lm.setupzoom_normal()" + "\n";
		
        header.append(creationComment).append(sansImport).append(functionName).append(indent(genieImport))
                .append(indent(zoomSetup));
		
		return header.toString();
	}

	/**
	 * Uses set_sample_par for the "Sample Height", "Sample Width" and "Sample Geometry" settings.
	 * @param name the name of the setting, "width", "height" or "geometry"
	 * @param value a numerical value for the "width" and "height", or the type of geometry, e.g. "disc"
	 * @return
	 */
	private String buildSamplePar(String name, String value) {	
        String samplePar = String.format("set_sample_par('%s', '%s')\n", name, value);
		
		return samplePar;
	}

    /**
     * Builds the python code for running SANS and TRANS in sequential order.
     * 
     * @param sansFirst
     *            specifies whether SANS data will be collected first (TRANS
     *            first if false)
     * @return the script code as String.
     */
    private String buildSequential(boolean sansFirst) {
        StringBuilder result = new StringBuilder();
        StringBuilder sansData = new StringBuilder();
        StringBuilder transData = new StringBuilder();
        boolean populatedRow = false;

        for (Row row : rows) {
            if (row.getPosition().length() > 0) {
                sansData.append(buildSans(row));
                transData.append(buildTrans(row));
                populatedRow = true;
            }
        }
        // Return if no data read
        if (!populatedRow) {
            return result.toString();
        }

        if (sansFirst) {
            result.append(String.format("for i in range(%d):\n", settings.getDoSans()));
            result.append(sansData);
            result.append(String.format("for i in range(%d):\n", settings.getDoTrans()));
            result.append(transData);
        } else {
            result.append(String.format("for i in range(%d):\n", settings.getDoTrans()));
            result.append(transData);
            result.append(String.format("for i in range(%d):\n", settings.getDoSans()));
            result.append(sansData);
        }
        return result.toString();
    }

    /**
     * Builds the python code for running SANS and TRANS in alternating order.
     * 
     * @param sansFirst
     *            specifies whether SANS data will be collected first (TRANS
     *            first if false)
     * @return the script code as String.
     */
    private String buildAlternating(boolean sansFirst) {
        StringBuilder result = new StringBuilder();
        StringBuilder rowData = new StringBuilder();
        boolean populatedRow = false;

        // Get sans and trans data for every row.
        if (sansFirst) {
            for (Row row : rows) {
                if (row.getPosition().length() > 0) {
                    rowData.append("if count < num_sans:\n");
                    rowData.append(buildSans(row));
                    rowData.append("if count < num_trans:\n");
                    rowData.append(buildTrans(row));
                    populatedRow = true;
                }
            }
        } else {
            for (Row row : rows) {
                if (row.getPosition().length() > 0) {
                    rowData.append("if count < num_trans:\n");
                    rowData.append(buildTrans(row));
                    rowData.append("if count < num_sans:\n");
                    rowData.append(buildSans(row));
                    populatedRow = true;
                }
            }
        }
        // Return if no data read
        if (!populatedRow) {
            return result.toString();
        }

        result.append(String.format("num_sans = %s\n", settings.getDoSans()));
        result.append(String.format("num_trans = %s\n", settings.getDoTrans()));
        result.append("count = 0\n");
        result.append("while True:\n");
        result.append(indent(rowData.toString()));
        result.append(indent("count += 1\n"));
        result.append(indent("if count >= num_trans and count >= num_sans : break\n"));

        return result.toString();
    }

	/**
     * Builds a string used for the do_sans loop from a single row.
     * 
     * @return the code block for the do_sans python command.
     */
    private String buildSans(Row row) {
        StringBuilder sansData = new StringBuilder();
        String collectionMode = null;

        // Based on the collection mode's two settings, set the rtype value to
        // one of its two possibilities
        if (settings.getCollection().toString() != null) {
            collectionMode = (settings.getCollection() == CollectionMode.HISTOGRAM) ? "rtype=0" : "rtype=1";
        }
        if (row.getPosition().length() == 0) {
            return sansData.toString();
        }
        sansData.append(indent(String.format("set_aperture('%s')\n", settings.getSansSize().name().toLowerCase())));
        if (row.getSansWaitValue() != null && row.getSansWaitUnit() != null) {
            sansData.append(indent(String.format(
                    "lm.dosans_normal(position='%s', waitfor=%s, waitfortype='%s', change_period=%s, title='%s', thickness=%s, %s)\n\n",
                    row.getPosition(), row.getSansWaitValue(), row.getSansWaitUnit().name().toLowerCase(),
                    row.getPeriod(), row.getSampleName(), row.getThickness(), collectionMode)));
        }
        return sansData.toString();
    }
	
	/**
     * Builds a string used for the do_trans loop from a single row.
     * 
     * @return the code block for the do_trans python command.
     */
    private String buildTrans(Row row) {
        StringBuilder transData = new StringBuilder();
        String collectionMode = null;

        // Based on the collection mode's two settings, set the rtype value to
        // one of its two possibilities
        if (settings.getCollection().toString() != null) {
            collectionMode = (settings.getCollection().toString() == "Histogram") ? "rtype=0" : "rtype=1";
        }

        if (row.getPosition().length() > 0) {
            transData.append(
                    indent(String.format("set_aperture('%s')\n", settings.getTransSize().name().toLowerCase())));
            if (row.getTransWaitValue() != null && row.getTransWaitUnit() != null) {
                transData.append(indent(String.format(
                        "lm.dotrans_normal(position='%s', waitfor=%s, waitfortype='%s', change_period=%s, title='%s', thickness=%s, %s)\n\n",
                        row.getPosition(), row.getTransWaitValue(), row.getTransWaitUnit().name().toLowerCase(),
                        row.getPeriod(), row.getSampleName(), row.getThickness(), collectionMode)));
            }
        }
        return transData.toString();
    }
	
	/**
	 * Receives and sets the rows from the view.
	 * @param rows the table rows
	 */
	public void setRows(Collection<Row> rows) {
		this.rows = rows;
	}
	
    /**
     * Sets the SANS settings used in the script.
     * 
     * @param settings
     *            The settings
     */
    public void setSettings(SansSettings settings) {
		this.settings = settings;
	}
	
	/**
	 * Returns the script back to the view to be displayed.
	 * @return the completed script
	 */
	public String getScript() {
		return script.toString();
	}
	
	/**
     * Calls internal build script methods and adds these returned strings to
     * the main script.
     * 
     * @return The Script.
     */
    public String createScript() {
		script = new StringBuilder();
		
		script.append(generateHeader());
		
        script.append(indent(buildSamplePar("height", settings.getSampleHeight().toString())));
        script.append(indent(buildSamplePar("width", settings.getSampleWidth().toString())));
        script.append(indent(buildSamplePar("geometry", settings.getGeometry().toString())));
        script.append(indent("\n"));
        switch (settings.getOrder()) {
            case SANS:
                script.append(indent(buildSequential(true)));
                break;
            case TRANS:
                script.append(indent(buildSequential(false)));
                break;
            case ALTSANS:
                script.append(indent(buildAlternating(true)));
                break;
            case ALTTRANS:
                script.append(indent(buildAlternating(false)));
                break;

            default:
                break;
        }
        return script.toString();
	}

    /**
     * Indents a single- or multiline block of python code.
     * 
     * @param block
     *            The block of code.
     * @return The indented block of code.
     */
    private String indent(String block) {
        return block.replaceAll("(?m)^", INDENT);
    }
}
