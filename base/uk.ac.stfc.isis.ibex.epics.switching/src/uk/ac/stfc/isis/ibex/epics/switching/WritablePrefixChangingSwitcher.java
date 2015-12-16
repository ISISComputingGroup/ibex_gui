
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
 * This class is responsible for creating and setting the new source for the
 * writable. It takes the old PV address, replaces the old prefix with the new
 * prefix, and then sets this as a source on the observable.
 * 
 */
public class WritablePrefixChangingSwitcher extends PrefixChangingSwitcher {

    public WritablePrefixChangingSwitcher() {
        super();
    }

    @Override
    public void switchInstrument(InstrumentInfo instrumentInfo) {
        System.out.println("being called!!!!");
        for (SwitchableInformation switchableInfo : switchableInfoList) {
            System.out.println("doing a switch!!!!");
            // Create a new observable factory, with the old switcher
            WritableFactory wrtFactory = new WritableFactory(switchableInfo.switchable.getSwitcher());

            String switchedPvAddress;

            if (!pvPrefix.isEmpty()) {
                switchedPvAddress = switchableInfo.pvAddress.replaceFirst(pvPrefix, instrumentInfo.pvPrefix());
            } else {
                switchedPvAddress = switchableInfo.pvAddress;
            }

            System.out.println("new pv address!!!!" + switchedPvAddress);

            // Set the new source - also takes care of closing the old
            // observable
            switchableInfo.switchable
                    .setSource(wrtFactory.getPVWritable(switchableInfo.channelType, switchedPvAddress));

            switchableInfo.pvAddress = switchedPvAddress;
        }
        // The immediate super class sets the new pvPrefix
        super.switchInstrument(instrumentInfo);
    }

}
