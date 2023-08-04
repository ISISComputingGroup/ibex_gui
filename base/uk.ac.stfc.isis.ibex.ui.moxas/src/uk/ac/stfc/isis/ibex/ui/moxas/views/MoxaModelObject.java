
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any kind,
 * either expressed or implied, including but not limited to the implied
 * warranties of merchantability and/or fitness for a particular purpose.
 */
package uk.ac.stfc.isis.ibex.ui.moxas.views;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class to hold information about the mapping between physical moxa ports and COM numbers.
 *
 */
public class MoxaModelObject extends ModelObject implements Comparable<MoxaModelObject> {

    private final String physport;
    private final String comport;

    /**
     * Instantiates a new moxa mapping pair.
     *
     * @param physport
     *            physical moxa port number for a mapping.
     * @param comport
     *            COM port for a mapping.
     */
    public MoxaModelObject(String physport,String comport) {
        this.physport = physport;
        this.comport = comport;
    }

    /**
     * Gets physical moxa port number for a mapping.
     *
     * @return the port number
     */

    public String getPhysPort() {
        return physport;
    }

    /**
     * Gets the COM port for a mapping.
     *
     * @return COM port string
     */
    public String getComPort() {
        return comport;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(MoxaModelObject moxaModelObject) {
        return physport.compareTo(moxaModelObject.getPhysPort());
    }
}
