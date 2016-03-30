
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
package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class InstrumentNameSearch extends ViewerFilter {
    private String searchString = ".*";

    public void setSearchText(String s) {
        // ensure that the value can be used for matching
        searchString = ".*" + s + ".*";
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }

        InstrumentInfo instrument = (InstrumentInfo) element;
        if (instrument.name().matches(searchString)) {
            return true;
        }

        // Use uppercase checks to eliminate case sensitivity on the search
        String upSearch = searchString.toUpperCase();
        String upName = instrument.name().toUpperCase();
        if (upName.matches(upSearch)) {
            return true;
        }

        return false;
    }
}
