
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

import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

/**
 * This class is responsible for creation of the writable, and registering the
 * writable with a switcher.
 */
public class WritableFactory {
    private Switcher switcher;

    /**
     * Creates a writable factory, with the specified behaviour on instrument switch.
     * @param onSwitch the behaviour on instrument switch
     */
    public WritableFactory(OnInstrumentSwitch onSwitch) {
        this(onSwitch, InstrumentSwitchers.getDefault());
    }

    /**
     * Creates a writable factory, with the specified behaviour on instrument switch.
     * @param onSwitch the behaviour on instrument switch
     * @param instrumentSwitchers the instrument switcher
     */
    public WritableFactory(OnInstrumentSwitch onSwitch, InstrumentSwitchers instrumentSwitchers) {
        this(instrumentSwitchers.getWritableSwitcher(onSwitch));
    }

    /**
     * Creates a writable factory, using the specified switcher.
     * @param switcher the switcher
     */
    public WritableFactory(Switcher switcher) {
        this.switcher = switcher;
    }

    /**
     * Get a switchable writable for a provided PV address.
     * @param <T> the type of values to be written
     * @param channelType - the channel type
     * @param address - the PV address
     * @return the writable
     */
    public <T> Writable<T> getSwitchableWritable(ChannelType<T> channelType, String address) {
        Writable<T> channelWriter = getPVWritable(channelType, address);

        SwitchableWritable<T> createdWritable = new SwitchableWritable<>(channelWriter);
        switcher.<T>registerSwitchable(createdWritable, address, channelType);

        createdWritable.setSwitcher(switcher);

        return createdWritable;
    }

    /**
     * Gets a PV writable.
     * @param <T> the type of value to be written
     * @param channelType the channel type
     * @param address the PV address
     * @return the writable
     */
    public <T> Writable<T> getPVWritable(ChannelType<T> channelType, String address) {
        return channelType.writer(address);
    }
}

