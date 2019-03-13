
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

/**
 * A filter for filtering/searching by instrument name. This should be case
 * insensitive, and the search text should match to any part of the instrument
 * name.
 * 
 */
public class InstrumentNameSearch extends ViewerFilter {
    private String searchString = "";

    /**
     * Sets the text to search for.
     * @param s the text to search for
     */
    public void setSearchText(String s) {
        searchString = s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        InstrumentInfo instrument = (InstrumentInfo) element;
        return searchString == null || instrument.name().toUpperCase().contains(searchString.toUpperCase());
    }
}
