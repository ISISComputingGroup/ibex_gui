
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

import uk.ac.stfc.isis.ibex.epics.writing.BaseWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

/**
 * This class is responsible for creation of the observable, and registering the
 * observable with a switcher.
 */
public class WritableFactory {
    private Switcher switcher;

    public WritableFactory(OnInstrumentSwitch onSwitch, SwitcherProvider switcherProvider) {
        switcher = switcherProvider.getWritableSwitcher(onSwitch);
    }

    public WritableFactory(OnInstrumentSwitch onSwitch) {
        this(onSwitch, new SwitcherProvider());
    }

    public <T> Writable<T> getSwitchableWritable(ChannelType<T> channelType, String address) {
        BaseWritable<T> channelWriter = channelType.writer(address);

        SwitchingWritable<T> createdWritable = new SwitchingWritable<>(channelWriter);
        switcher.<T> registerSwitchable(createdWritable, address, channelType);

        createdWritable.setSwitcher(switcher);

        return createdWritable;
    }

    public <T> Writable<T> getPVWritable(ChannelType<T> channelType, String address) {
        return channelType.writer(address);
    }
}

