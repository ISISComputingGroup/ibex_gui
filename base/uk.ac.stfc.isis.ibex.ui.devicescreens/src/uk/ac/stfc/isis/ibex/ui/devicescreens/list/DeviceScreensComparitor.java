
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2016 Science &
 * Technology Facilities Council. All rights reserved.
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
package uk.ac.stfc.isis.ibex.ui.devicescreens.list;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;

/**
 * The Class DeviceScreensComparitor which compares device descriptions in the
 * device screen list.
 * 
 */
public class DeviceScreensComparitor extends ViewerComparator {

    /**
     * The Enum SortedOnType which is what the list is sorted on.
     */
    enum SortedOnType {
        TYPE,
        NAME
    };

    private SortedOnType sortedOn = SortedOnType.NAME;
    private boolean isDescending = true;

    /**
     * Gets the direction that the list is sorted in.
     *
     * @return the direction
     */
    public int getDirection() {
        return isDescending ? SWT.DOWN : SWT.UP;
    }

    /**
     * Sets the column which is sorted over.
     *
     * @param sortedOn the column sorted on
     */
    public void setColumn(SortedOnType sortedOn) {

        if (this.sortedOn.equals(sortedOn)) {
            // Same column as last sort; toggle the direction
            this.isDescending = !this.isDescending;
        } else {
            // New column; do an ascending sort
            this.sortedOn = sortedOn;
            this.isDescending = true;
        }
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        DeviceDescription p1 = (DeviceDescription) e1;
        DeviceDescription p2 = (DeviceDescription) e2;
        int rc = 0;
        switch (this.sortedOn) {
            case NAME:
                rc = p1.getName().compareTo(p2.getName());
                if (rc == 0) {
                    rc = p1.getKey().compareTo(p2.getKey());
                }
                break;
            case TYPE:
                rc = p1.getKey().compareTo(p2.getKey());
                if (rc == 0) {
                    rc = p1.getName().compareTo(p2.getName());
                }
                break;
            default:
                break;
        }
        // If ascending order, flip the direction
        if (!isDescending) {
        rc = -rc;
      }
      return rc;
    }

}
