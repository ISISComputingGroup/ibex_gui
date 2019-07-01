/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.blocks.presentation;

import java.util.stream.Stream;

/**
 * A class which holds the name of a plot and its axes.
 */
public class AxisList {
    private String plotName;
    private Stream<String> axisNames;
    
    /**
     * Create an axis list.
     * @param plotName The name of the plot the axis is associated with.
     * @param axisNames A stream containing all of the axis names.
     */
    public AxisList(String plotName, Stream<String> axisNames) {
        this.plotName = plotName;
        this.axisNames = axisNames;
    }
    
    /**
     * @return the plotName
     */
    public String getPlotName() {
        return plotName;
    }
    /**
     * @return the axisNames
     */
    public Stream<String> getAxisNames() {
        return axisNames;
    }
    
}
