
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

/**
 * Beam status graph for TS1 and TS2 beam current with a time range of one hour.
 */

public class BeamStatusHourlyGraphView extends BeamStatusGraphView {

    /** Number of milliseconds in an hour. */
    private static final long MILLISECONDS_IN_HOUR = 3600 * 1000;

    /** Title for the plot. */
    private static final String PLOT_TITLE = "Beam current, past hour";

    @Override
    protected long getTimeRangeInMilliseconds() {
        return MILLISECONDS_IN_HOUR;
    }

    @Override
    protected String getPlotTitle() {
        return PLOT_TITLE;
    }
}
