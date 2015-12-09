
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

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

/**
 * This class is responsible for creating and setting the new source for the
 * observable. It takes the old PV address, replaces the old prefix with the new
 * prefix, and then sets this as a source on the observable.
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ObservablePrefixChangingSwitcher extends PrefixChangingSwitcher {

    private class SwitchableInformation {

        public SwitchableInitialiseOnSubscribeObservable switchable;
        public String pvAddress;
        public ChannelType channelType;

        public SwitchableInformation(SwitchableInitialiseOnSubscribeObservable switchable, String pvAddress,
                ChannelType channelType) {
            this.switchable = switchable;
            this.pvAddress = pvAddress;
            this.channelType = channelType;
        }
    }

    private class DummySwitcherProvider extends SwitcherProvider {

        private Switcher switcher;

        public DummySwitcherProvider(Switcher switcher) {
            this.switcher = switcher;
        }

        @Override
        public Switcher getObservableSwitcher(OnSwitchBehaviour behaviour) {
            return switcher;
        };
    }

    protected Collection<SwitchableInformation> switchables;

    /**
     * This effectively overrides the super class method in Switcher to register
     * switchables.
     * 
     * @param switchable
     *            The switchable to register
     * @param pvAddress
     *            The PV Address
     * @param channelType
     */
    public <T> void registerSwitchable(SwitchableInitialiseOnSubscribeObservable<T> switchable, String pvAddress,
            ChannelType<T> channelType) {
        switchables.add(new SwitchableInformation(switchable, pvAddress, channelType));
    }

    @Override
    public void switchInstrument(InstrumentInfo instrumentInfo) {
        for (SwitchableInformation switchableInformation : switchables) {
            // Create a dummy switcher provider that just recycles the switcher
            // we already have
            SwitcherProvider switcherProvider = new DummySwitcherProvider(switchableInformation.switchable.getSwitcher());
            
            // Create a new observable factory, with the old channel type,
            // switching behaviour and switcher
            ObservableFactory obsFactory = new ObservableFactory(switchableInformation.channelType,
                    OnSwitchBehaviour.SWITCHING, switcherProvider);
            
            String switcherPvAddress = switchableInformation.pvAddress.replace(pvPrefix, instrumentInfo.pvPrefix());

            // Close the old switchable
            switchableInformation.switchable.close();
            // Set the new source
            switchableInformation.switchable.setSource(obsFactory.getPVObserverable(switcherPvAddress));

            // The immediate super class sets the new pvPrefix
            super.switchInstrument(instrumentInfo);
        }
    }

}
