
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

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

/**
 * Abstract class for the switchers, which are responsible for performing the
 * switching behaviour on any observables registered with them.
 */
public abstract class Switcher {

	/**
	 * The collection of switchables (observables & writables) switched by this class.
	 */
    protected Collection<Switchable> switchables;

    /**
     * Creates a new switcher.
     */
    public Switcher() {
        /**
         * CopyOnWriteArrayList avoids concurrent access problems.
         */
        switchables = new CopyOnWriteArrayList<>();
    }

    /**
     * Switches the instrument.
     * @param instrumentInfo the new instrument
     */
    public abstract void switchInstrument(InstrumentInfo instrumentInfo);

    /**
     * Register a switchable with this switcher.
     * @param <T> the type of the switchable
     * @param switchable the switchable
     * @param pvAddress the PV address
     * @param channelType the channel type
     */
    public <T> void registerSwitchable(Switchable switchable, String pvAddress, ChannelType<T> channelType) {
        switchables.add(switchable);
    }

    /**
     * Removes a switchable from this switcher.
     * @param switchableInitialiseOnSubscribeObservable the switchable to remove
     */
    public void unregsiterSwitchable(Switchable switchableInitialiseOnSubscribeObservable) {
        switchables.remove(switchableInitialiseOnSubscribeObservable);
    }

    /**
     * Just used for testing so far.
     *
     * @return a collection of switchable observables
     */
    public Collection<Switchable> getSwitchables() {
        return switchables;
    }

}
