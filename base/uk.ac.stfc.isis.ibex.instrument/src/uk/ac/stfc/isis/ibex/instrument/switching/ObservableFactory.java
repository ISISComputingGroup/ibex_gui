
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

package uk.ac.stfc.isis.ibex.instrument.switching;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosingSwitchableObservable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class ObservableFactory<T> {

    private ChannelType<T> channelType;
    private Switcher switcher;

    public ObservableFactory(ChannelType<T> channelType, OnSwitchBehaviour onSwitch) {
        
        this.channelType = channelType;

        SwitcherFactory switcherFactory = new SwitcherFactory();
        switcher = switcherFactory.getObservableSwitcer(onSwitch);
    }

    public SwitchableInitialiseOnSubscribeObservable<T> getPVObserverable(String address) {
        ClosableCachingObservable<T> channelReader = channelType.reader(address);
        final ClosingSwitchableObservable<T> channel = new ClosingSwitchableObservable<>(channelReader);

        SwitchableInitialiseOnSubscribeObservable<T> createdObservable = new SwitchableInitialiseOnSubscribeObservable<>(
                channel);
        switcher.registerSwitchable(createdObservable);
        
        return createdObservable;
    }

}
