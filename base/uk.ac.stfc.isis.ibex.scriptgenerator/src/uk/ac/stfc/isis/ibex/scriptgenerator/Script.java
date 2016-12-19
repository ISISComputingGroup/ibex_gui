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
 *
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
    private static final String INDENT = "    ";


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
     * @param seqLoop
     *            the seqLoop to set
     */
    public void setSansLoop(PythonString seqLoop) {
        this.sansLoop = seqLoop;
    }

    /**
     * @param seqLoop
     *            the seqLoop to set
     */
    public void setTransLoop(PythonString seqLoop) {
        this.transLoop = seqLoop;
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

    public void createScript(Order order, boolean loopOverEach) {
        script = new PythonString(header);
        script.addSub(setup);
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
     */
    public void buildSequential(boolean sansFirst, boolean loopOverEach) {
        PythonString firstLoop;
        PythonString secondLoop;
        Collection<PythonString> firstRows;
        Collection<PythonString> secondRows;
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
        for (PythonString row : firstRows) {
            if (prependLoop) {
                parentLoop = new PythonString(firstLoop);
                script.addSub(parentLoop);
            }
            parentLoop.addSub(row);
            prependLoop = loopOverEach;
        }

        prependLoop = true;
        for (PythonString row : secondRows) {
            if (prependLoop) {
                parentLoop = new PythonString(secondLoop);
                script.addSub(parentLoop);
            }
            parentLoop.addSub(row);
            prependLoop = loopOverEach;
        }
    }

    /**
     * Builds the python code for running SANS and TRANS in alternating order.
     * 
     * @param sansFirst
     *            specifies whether SANS data will be collected first (TRANS
     *            first if false)
     */
    public void buildAlternating(boolean sansFirst, boolean loopOverEach) {
        PythonString firstCondition = new PythonString(altTransCondition);
        PythonString secondCondition = new PythonString(altSansCondition);
        ArrayList<PythonString> firstRows = doTransRows;
        ArrayList<PythonString> secondRows = doSansRows;
        if (sansFirst) {
            firstRows = doSansRows;
            secondRows = doTransRows;
            firstCondition = new PythonString(altSansCondition);
            secondCondition = new PythonString(altTransCondition);
        }


        boolean prependLoop = true;
        PythonString parentLoop = new PythonString();
        for (int i = 0; i < doSansRows.size(); i++) {
            if (i == 0) {
                script.addSub(altHeader);
            }
            if (prependLoop) {
                if (i > 0) {
                    parentLoop.addSub(new PythonString(altFooter));
                }
                parentLoop = new PythonString(altLoop);
                script.addSub(parentLoop);
            }
            PythonString currentFirstCondition = new PythonString(firstCondition);
            PythonString currentSecondCondition = new PythonString(secondCondition);
            parentLoop.addSub(currentFirstCondition);
            parentLoop.addSub(currentSecondCondition);
            currentFirstCondition.addSub(firstRows.get(i));
            currentSecondCondition.addSub(secondRows.get(i));

            prependLoop = loopOverEach;
        }
        parentLoop.addSub(new PythonString(altFooter));
//
//        // Get sans and trans data for every row.
//        if (sansFirst) {
//            for (Row row : rows) {
//                StringBuilder currentRow = new StringBuilder();
//                if (row.getPosition().length() > 0) {
//                    currentRow.append("if count < num_sans:\n");
//                    currentRow.append(buildSans(row));
//                    currentRow.append("if count < num_trans:\n");
//                    currentRow.append(buildTrans(row));
//                    populatedRow = true;
//                }
//                // If loop over every run set, add loop for each row
//                if (settings.getLoopOver()) {
//                    currentRow = addAltLoop(currentRow);
//                }
//                rowData.append(currentRow);
//            }
//        } else {
//            for (Row row : rows) {
//                StringBuilder currentRow = new StringBuilder();
//                if (row.getPosition().length() > 0) {
//                    currentRow.append("if count < num_trans:\n");
//                    currentRow.append(buildTrans(row));
//                    currentRow.append("if count < num_sans:\n");
//                    currentRow.append(buildSans(row));
//                    populatedRow = true;
//                }
//                // If loop over every run set, add loop for each row
//                if (settings.getLoopOver()) {
//                    currentRow = addAltLoop(currentRow);
//                }
//                rowData.append(currentRow);
//            }
//        }
//        // Return blank if no rows read
//        if (!populatedRow) {
//            return result.toString();
//        }
//
//        // If loop over every run not set, add loop once
//        if (!settings.getLoopOver()) {
//            rowData = addAltLoop(rowData);
//        }
//
//        result.append(String.format("num_sans = %s\n", settings.getDoSans()));
//        result.append(String.format("num_trans = %s\n", settings.getDoTrans()));
//        result.append(rowData);
//
//        return result.toString();
    }

}
