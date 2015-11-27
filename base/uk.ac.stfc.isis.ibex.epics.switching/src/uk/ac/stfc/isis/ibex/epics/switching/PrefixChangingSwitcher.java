
/**
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.epics.switching;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * This is an abstract class for all the prefix changing switchers. It holds the
 * old pv prefix, which is updated when switchInstrument is called. This means
 * subclasses should make sure they first make use of the old pv prefix, then
 * call super.switchInstrument(...).
 */
public abstract class PrefixChangingSwitcher extends Switcher {

    String pvPrefix = "";

    @Override
    public void switchInstrument(InstrumentInfo instrumentInfo) {
        pvPrefix = instrumentInfo.pvPrefix();
    }

}
