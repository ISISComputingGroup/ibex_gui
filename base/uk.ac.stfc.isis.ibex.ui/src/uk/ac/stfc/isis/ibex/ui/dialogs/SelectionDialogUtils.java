
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.dialogs;

import java.util.Arrays;

/**
 * A class for SelectionDialog Utilities.
 *
 */
public class SelectionDialogUtils {

        /**
         * The constructor for the utilities class.
         */
        public SelectionDialogUtils() {
            
        }
        
        /**
         * Sorts the given selection as required.
         * 
         * @param selection
         *             The selection to be sorted.
         * @return
         *              The sorted selection.
         */
        public String[] sortSelected(String[] selection) {
            Arrays.sort(selection, String.CASE_INSENSITIVE_ORDER);
            return selection;
        }
}
