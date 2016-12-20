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

/**
 * Class used for constructing Python code in a tree structure, where each node
 * contains a block of code and each sub block increments the level of code
 * indentation by one.
 */
public class PythonString {

    private Collection<PythonString> subBlocks;
    private Collection<String> lines;
    private static final String INDENT = "    ";

    /**
     * Standard constructor.
     */
    public PythonString() {
        lines = new ArrayList<String>();
        subBlocks = new ArrayList<PythonString>();
    }

    /**
     * Constructs a new PythonString node based on a template, copying its
     * lines, but not its sub blocks.
     * 
     * @param template
     *            The template from which to construct this node.
     */
    public PythonString(PythonString template) {
        this.lines = new ArrayList<String>(template.lines);
        this.subBlocks = new ArrayList<PythonString>();
    }

    /**
     * Adds a line of code to the content of the current Python block.
     * 
     * @param line
     *            The line of code.
     */
    public void add(String line) {
        this.lines.add(line);
    }

    /**
     * Adds a block of code to the content of the current Python block.
     * 
     * @param lines
     *            The block of code.
     */
    public void addAll(Collection<String> lines) {
        this.lines.addAll(lines);
    }

    /**
     * Clears the current block of Python and removes all attached sub blocks.
     */
    public void clear() {
        this.lines.clear();
        this.subBlocks.clear();
    }

    /**
     * Adds a sub block of python code to the current Python block.
     * 
     * @param subBlock
     *            The block of code.
     */
    public void addSubBlock(PythonString subBlock) {
        this.subBlocks.add(subBlock);
    }

    @Override
    public String toString() {
        return this.flatten(0);
    }

    /**
     * Recursively flattens the tree structure of the Python script, producing a
     * single auto-formatted String of code.
     * 
     * @param indentLevel
     *            The level of indentation for the current recursive method
     *            call.
     * @return A single string containing the auto-formatted Python script.
     */
    private String flatten(int indentLevel) {
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            result.append(indent(indentLevel, line));
            result.append("\n");
        }
        for (PythonString sub : subBlocks) {
            result.append(sub.flatten(indentLevel + 1));
        }
        return result.toString();
    }

    /**
     * Indents a line of code to the specified level of indentation.
     * 
     * @param indentLevel
     *            The desired level of indentation.
     * @param line
     *            The line of code to be indented.
     * @return The indented line of code.
     */
    public String indent(int indentLevel, String line) {
        String indented = line;
        for (int i = 0; i < indentLevel; i++) {
            indented = INDENT + indented;
        }
        return indented;
    }
}
