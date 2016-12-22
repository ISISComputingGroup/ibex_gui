 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.scriptgenerator.settings.Order;

/**
 * Holds building blocks of genie_python scripts and assembles them into a
 * script based on run order settings.
 */
public class Script {

    private PythonString script = new PythonString();

    private PythonString header = new PythonString();
    private PythonString setup = new PythonString();
    private PythonString sansLoop = new PythonString();
    private PythonString transLoop = new PythonString();
    private PythonString altHeader = new PythonString();
    private PythonString altLoop = new PythonString();
    private PythonString altSansCondition = new PythonString();
    private PythonString altTransCondition = new PythonString();
    private PythonString altFooter = new PythonString();

    private ArrayList<PythonString> doSansRows = new ArrayList<PythonString>();
    private ArrayList<PythonString> doTransRows = new ArrayList<PythonString>();

    /**
     * @param header
     *            the header to set
     */
    public void setHeader(PythonString header) {
        this.header = header;
    }

    /**
     * @param setup
     *            the setup to set
     */
    public void setSetup(PythonString setup) {
        this.setup = setup;
    }

    /**
     * @param sansLoop
     *            the seqLoop to set
     */
    public void setSansLoop(PythonString sansLoop) {
        this.sansLoop = sansLoop;
    }

    /**
     * @param transLoop
     *            the seqLoop to set
     */
    public void setTransLoop(PythonString transLoop) {
        this.transLoop = transLoop;
    }

    /**
     * @param altHeader
     *            the altHeader to set
     */
    public void setAltHeader(PythonString altHeader) {
        this.altHeader = altHeader;
    }

    /**
     * @param altLoop
     *            the altLoop to set
     */
    public void setAltLoop(PythonString altLoop) {
        this.altLoop = altLoop;
    }

    /**
     * @param altSansCondition
     *            the altSansCondition to set
     */
    public void setAltSansCondition(PythonString altSansCondition) {
        this.altSansCondition = altSansCondition;
    }

    /**
     * @param altTransCondition
     *            the altTransCondition to set
     */
    public void setAltTransCondition(PythonString altTransCondition) {
        this.altTransCondition = altTransCondition;
    }

    /**
     * @param altFooter
     *            the altFooter to set
     */
    public void setAltFooter(PythonString altFooter) {
        this.altFooter = altFooter;
    }

    /**
     * @param doSansRows
     *            the doSansRows to set
     */
    public void setDoSansRows(ArrayList<PythonString> doSansRows) {
        this.doSansRows = doSansRows;
    }

    /**
     * @param doTransRows
     *            the doTransRows to set
     */
    public void setDoTransRows(ArrayList<PythonString> doTransRows) {
        this.doTransRows = doTransRows;
    };

    @Override
    public String toString() {
        return script.toString();
    }

    /**
     * Assembles the script from code building blocks according to ordering
     * settings.
     * 
     * @param order
     *            The order in which to run data collection.
     * @param loopOverEach
     *            Whether to loop over every run individually.
     */
    public void createScript(Order order, boolean loopOverEach) {
        script = new PythonString(header);
        script.addSubBlock(setup);
        switch (order) {
            case SANS:
                buildSequential(true, loopOverEach);
                break;
            case TRANS:
                buildSequential(false, loopOverEach);
                break;
            case ALTSANS:
                buildAlternating(true, loopOverEach);
                break;
            case ALTTRANS:
                buildAlternating(false, loopOverEach);
                break;
            default:
                break;
        }
    }
    
    /**
     * Assembles the python code for running SANS and TRANS in sequential order.
     * 
     * @param sansFirst
     *            specifies whether SANS data will be collected first (TRANS
     *            first if false)
     * @param loopOverEach
     *            Whether to loop over every run individually.
     */
    public void buildSequential(boolean sansFirst, boolean loopOverEach) {
        PythonString firstLoop;
        PythonString secondLoop;
        Collection<PythonString> firstRows;
        Collection<PythonString> secondRows;
        // Determine SANS / TRANS order
        if (sansFirst) {
            firstLoop = sansLoop;
            secondLoop = transLoop;
            firstRows = doSansRows;
            secondRows = doTransRows;
        } else {
            firstLoop = transLoop;
            secondLoop = sansLoop;
            firstRows = doTransRows;
            secondRows = doSansRows;
        }

        boolean prependLoop = true;
        PythonString parentLoop = new PythonString();
        // Add run instructions for all entries of first type
        for (PythonString row : firstRows) {
            // Add loop per row if looping over each run
            if (prependLoop) {
                parentLoop = new PythonString(firstLoop);
                script.addSubBlock(parentLoop);
            }
            parentLoop.addSubBlock(row);
            prependLoop = loopOverEach;
        }

        prependLoop = true;
        // Add run instructions for all entries of second type
        for (PythonString row : secondRows) {
            // Add loop per row if looping over each run
            if (prependLoop) {
                parentLoop = new PythonString(secondLoop);
                script.addSubBlock(parentLoop);
            }
            parentLoop.addSubBlock(row);
            prependLoop = loopOverEach;
        }
    }

    /**
     * Assembles the Python code for running SANS and TRANS in alternating
     * order.
     * 
     * @param sansFirst
     *            specifies whether SANS data will be collected first (TRANS
     *            first if false)
     * @param loopOverEach
     *            Whether to loop over every run individually.
     */
    public void buildAlternating(boolean sansFirst, boolean loopOverEach) {
        PythonString firstCondition = new PythonString(altTransCondition);
        PythonString secondCondition = new PythonString(altSansCondition);
        ArrayList<PythonString> firstRows;
        ArrayList<PythonString> secondRows;
        // Determine SANS / TRANS order
        if (sansFirst) {
            firstRows = doSansRows;
            secondRows = doTransRows;
            firstCondition = new PythonString(altSansCondition);
            secondCondition = new PythonString(altTransCondition);
        } else {
            firstRows = doTransRows;
            secondRows = doSansRows;
            firstCondition = new PythonString(altTransCondition);
            secondCondition = new PythonString(altSansCondition);
        }

        boolean prependLoop = true;
        PythonString parentLoop = new PythonString();
        for (int i = 0; i < doSansRows.size(); i++) {
            // Add header once at the beginning
            if (i == 0) {
                script.addSubBlock(altHeader);
            }
            // Add loop per row if looping over each run
            if (prependLoop) {
                // Add break clause if in between rows
                if (i > 0) {
                    parentLoop.addSubBlock(new PythonString(altFooter));
                }
                parentLoop = new PythonString(altLoop);
                script.addSubBlock(parentLoop);
            }
            PythonString currentFirstCondition = new PythonString(firstCondition);
            PythonString currentSecondCondition = new PythonString(secondCondition);
            parentLoop.addSubBlock(currentFirstCondition);
            parentLoop.addSubBlock(currentSecondCondition);
            currentFirstCondition.addSubBlock(firstRows.get(i));
            currentSecondCondition.addSubBlock(secondRows.get(i));

            prependLoop = loopOverEach;
        }
        // Add break clause at the end
        parentLoop.addSubBlock(new PythonString(altFooter));
    }

}
