
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

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;

/**
 * The Class DeviceScreensComparitor which compares device descriptions in the
 * device screen list.
 * 
 */
public class DeviceScreensComparator extends ColumnComparator<DeviceDescription> {
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        DeviceDescription p1 = (DeviceDescription) e1;
        DeviceDescription p2 = (DeviceDescription) e2;
        int rc = 0;
        switch (this.propertyIndex) {
            case 0:
                rc = p1.getName().compareToIgnoreCase(p2.getName());
                if (rc == 0) {
                    rc = p1.getKey().compareToIgnoreCase(p2.getKey());
                }
                break;
            case 1:
                rc = p1.getKey().compareToIgnoreCase(p2.getKey());
                if (rc == 0) {
                    rc = p1.getName().compareToIgnoreCase(p2.getName());
                }
                break;
            case 2:
                rc = ((Boolean) p1.getPersist()).compareTo(p2.getPersist());
                if (rc == 0) {
                    rc = ((Boolean) p1.getPersist()).compareTo(p2.getPersist());
                }
                break;
            default:
                break;
        }
        // If ascending order, flip the direction
        if (isDescending) {
        	rc = -rc;
        }
      return rc;
    }

}
