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
 *
 */
public class PythonString {

    private Collection<PythonString> subBlocks;
    private Collection<String> lines;
    private static final String INDENT = "    ";

    public PythonString() {
        lines = new ArrayList<String>();
        subBlocks = new ArrayList<PythonString>();
    }

    public PythonString(PythonString other) {
        this.lines = other.lines;
        subBlocks = new ArrayList<PythonString>();
    }

    public void add(String line) {
        this.lines.add(line);
    }

    public void addAll(Collection<String> lines) {
        this.lines.addAll(lines);
    }

    public void clear() {
        this.lines.clear();
        this.subBlocks.clear();
    }

    public void addSub(PythonString sub) {
        this.subBlocks.add(sub);
    }

    @Override
    public String toString() {
        return this.flatten(0);
    }

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

    public String indent(int indentLevel, String line) {
        String indented = line;
        for (int i = 0; i < indentLevel; i++) {
            indented = INDENT + indented;
        }
        return indented;
    }

    public Collection<String> getLines() {
        return this.lines;
    }

    public Collection<PythonString> getChildren() {
        return this.subBlocks;
    }

}
