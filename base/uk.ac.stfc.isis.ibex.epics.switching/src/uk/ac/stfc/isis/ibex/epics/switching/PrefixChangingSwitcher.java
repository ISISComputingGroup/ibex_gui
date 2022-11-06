
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
 * This is an abstract class for all the prefix changing switchers. It holds the
 * old pv prefix, which is updated when switchInstrument is called. This means
 * subclasses should make sure they first make use of the old pv prefix, then
 * call super.switchInstrument(...) to set the new pv prefix.
 */
public abstract class PrefixChangingSwitcher extends Switcher {

	/**
	 * The channels switched by this class.
	 */
    protected Collection<SwitchableInformation> switchableInfoList;

    /**
     * The current PV prefix.
     */
    protected String pvPrefix = "";

    /**
     * Information about a channel which can be switched.
     */
    protected class SwitchableInformation {

    	/**
    	 * The switchable.
    	 */
        public final Switchable switchable;
        
        /**
         * The PV address.
         */
        public String pvAddress;
        
        /**
         * The channel type to be switched.
         */
        public final ChannelType<?> channelType;

        /**
         * Creates new switchable information.
         * @param switchable the switchable
         * @param pvAddress the pv address
         * @param channelType the channel type
         */
        public SwitchableInformation(Switchable switchable, String pvAddress, ChannelType<?> channelType) {
            this.switchable = switchable;
            this.pvAddress = pvAddress;
            this.channelType = channelType;
        }
    }

    /**
     * Creates a new switcher.
     */
    public PrefixChangingSwitcher() {
        switchables = new CopyOnWriteArrayList<>();
        switchableInfoList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void switchInstrument(InstrumentInfo instrumentInfo) {
        pvPrefix = instrumentInfo.pvPrefix();
    }

    /**
     * This overrides the super class method in Switcher to register
     * switchables.
     * 
     * @param switchable
     *            The switchable to register
     * @param pvAddress
     *            The PV Address
     * @param channelType
     */
    @Override
    public <T> void registerSwitchable(Switchable switchable, String pvAddress, ChannelType<T> channelType) {
        switchableInfoList.add(new SwitchableInformation(switchable, pvAddress, channelType));
        super.registerSwitchable(switchable, pvAddress, channelType);
    }

}
