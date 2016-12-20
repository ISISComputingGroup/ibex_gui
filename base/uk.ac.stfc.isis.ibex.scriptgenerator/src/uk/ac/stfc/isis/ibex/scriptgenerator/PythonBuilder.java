
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
    private Script script;
    private DateTimeProvider dateTime;
    private static final String BLANK = "";
	
    /**
     * The default constructor.
     */
    public PythonBuilder() {
        this(new Script(), new DateTimeProvider());
    }

    /**
     * Constructor using a custom Script and DateTimeProvider. Used for testing.
     * 
     * @param dateTime
     *            The DateTimeProvider.
     * @param script
     *            The Script.
     */
    public PythonBuilder(Script script, DateTimeProvider dateTime) {
        this.script = script;
        this.dateTime = dateTime;
    }

	/**
	 * Uses set_sample_par for the "Sample Height", "Sample Width" and "Sample Geometry" settings.
	 * @param name the name of the setting, "width", "height" or "geometry"
	 * @param value a numerical value for the "width" and "height", or the type of geometry, e.g. "disc"
	 * @return
	 */
    private String buildSamplePar(String name, Object value) {
        return String.format("set_sample_par('%s', '%s')", name, value.toString());
	}

    private void generateHeader() {
        PythonString header = new PythonString();
        header.add("# Script created by ZOOM Script at " + dateTime.getDate() + " " + dateTime.getTime());
        header.add("import LSS.SANSroutines as lm");
        header.add(BLANK);
        header.add("def my_script():");
        script.setHeader(header);
    }

    private void generateSetup() {
        PythonString setup = new PythonString();
        setup.add("from genie_python.genie import *");
        setup.add("lm.setupzoom_normal()");
        setup.add(buildSamplePar("height", settings.getSampleHeight()));
        setup.add(buildSamplePar("width", settings.getSampleWidth()));
        setup.add(buildSamplePar("geometry", settings.getGeometry().toString()));
        setup.add(BLANK);
        script.setSetup(setup);
    }

    private void generateSansLoop() {
        PythonString seqLoop = new PythonString();
        seqLoop.add(String.format("for i in range(%d):", settings.getDoSans()));
        script.setSansLoop(seqLoop);
    }

    private void generateTransLoop() {
        PythonString seqLoop = new PythonString();
        seqLoop.add(String.format("for i in range(%d):", settings.getDoTrans()));
        script.setTransLoop(seqLoop);
    }

    private void generateAltHeader() {
        PythonString altHeader = new PythonString();
        altHeader.add(String.format("num_sans = %s", settings.getDoSans()));
        altHeader.add(String.format("num_trans = %s", settings.getDoTrans()));
        script.setAltHeader(altHeader);
    }

    private void generateAltLoop() {
        PythonString altLoop = new PythonString();
        altLoop.add("count = 0");
        altLoop.add("while True:");
        script.setAltLoop(altLoop);
    }

    private void generateAltConditions() {
        PythonString altSansCondition = new PythonString();
        PythonString altTransCondition = new PythonString();
        altSansCondition.add("if count < num_sans:");
        altTransCondition.add("if count < num_trans:");
        script.setAltSansCondition(altSansCondition);
        script.setAltTransCondition(altTransCondition);
    }

    private void generateAltFooter() {
        PythonString altFooter = new PythonString();
        altFooter.add("count += 1");
        altFooter.add("if count >= num_trans and count >= num_sans : break");
        altFooter.add(BLANK);
        script.setAltFooter(altFooter);
    }

    private void generateRows() {
        int collectionVal = (settings.getCollection() == CollectionMode.HISTOGRAM ? 0 : 1);
        ArrayList<PythonString> doSansRows = new ArrayList<PythonString>();
        ArrayList<PythonString> doTransRows = new ArrayList<PythonString>();
        
        for (Row row : rows) {
            if (row.getPosition().length() > 0) {
                PythonString currentSans = new PythonString();
                PythonString currentTrans = new PythonString();

                // Add SANS block for row
                currentSans.add(String.format("set_aperture('%s')", settings.getSansSize().name().toLowerCase()));
                currentSans.add(String.format(
                        "lm.dosans_normal(position='%s', waitfor=%s, waitfortype='%s', change_period=%s, title='%s', thickness=%s, rtype=%s)",
                        row.getPosition(), row.getSansWaitValue(), row.getSansWaitUnit().name().toLowerCase(),
                        row.getPeriod(), row.getSampleName(), row.getThickness(), collectionVal));
                currentSans.add(BLANK);

                // Add TRANS block for row
                currentTrans.add(String.format("set_aperture('%s')", settings.getTransSize().name().toLowerCase()));
                currentTrans.add(String.format(
                        "lm.dotrans_normal(position='%s', waitfor=%s, waitfortype='%s', change_period=%s, title='%s', thickness=%s, rtype=%s)",
                        row.getPosition(), row.getTransWaitValue(), row.getTransWaitUnit().name().toLowerCase(),
                        row.getPeriod(), row.getSampleName(), row.getThickness(), collectionVal));
                currentTrans.add(BLANK);

                doSansRows.add(currentSans);
                doTransRows.add(currentTrans);
            }
        }
        script.setDoSansRows(doSansRows);
        script.setDoTransRows(doTransRows);
    }

    /**
     * Receives and sets the rows from the view.
     * 
     * @param rows
     *            the table rows
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
     * Generates the Python script and returns it as a single auto-formatted
     * String.
     * 
     * @return The script as a String.
     */
    public String createScript() {
        generateScriptBlocks();
        script.createScript(settings.getOrder(), settings.getLoopOver());

        return script.toString();
    }

    /**
     * Calls all methods for generating the building blocks of Python code and
     * saves these as templates in the script.
     */
    private void generateScriptBlocks() {
        generateHeader();
        generateSetup();
        generateAltHeader();
        generateAltLoop();
        generateAltConditions();
        generateAltFooter();
        generateSansLoop();
        generateTransLoop();
        generateRows();
    }
}
