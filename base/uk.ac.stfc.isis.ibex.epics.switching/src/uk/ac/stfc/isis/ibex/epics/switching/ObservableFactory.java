
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

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

/**
 * This class is responsible for creation of the observable, and registering the
 * observable with a switcher.
 */
public class ObservableFactory {
    private Switcher switcher;

    /**
     * Creates a new observable factory, with the specified behaviour on instrument switch.
     * @param onSwitch the behaviour on instrument switch
     */
    public ObservableFactory(OnInstrumentSwitch onSwitch) {
        this(onSwitch, InstrumentSwitchers.getDefault());
    }

    /**
     * Creates a new observable factory, with the specified behaviour on instrument switch and
     * a specified InstrumentSwitcher instance.
     * @param onSwitch the behaviour on instrument switch
     * @param instrumentSwitchers the InstrumentSwitcher instance to use
     */
    public ObservableFactory(OnInstrumentSwitch onSwitch, InstrumentSwitchers instrumentSwitchers) {
        this(instrumentSwitchers.getObservableSwitcher(onSwitch));
    }

    /**
     * Creates a new observable factory which uses the provided Switcher as it's behaviour on
     * instrument switch.
     * @param switcher the switcher to use
     */
    public ObservableFactory(Switcher switcher) {
        this.switcher = switcher;
    }

    /**
     * Create and return a PV observable of the correct type, registering it
     * with the switcher.
     * 
     * @param <T>
     *            the type of the channel
     * @param channelType
     *            the type of the channel
     * @param address
     *            the PV address
     * @return the PV observable
     */
    public <T> SwitchableObservable<T> getSwitchableObservable(ChannelType<T> channelType,
            String address) {
        ClosableObservable<T> channelReader = getPVObservable(channelType, address);
        final SwitchableObservable<T> channel = new SwitchableObservable<>(
                channelReader);

        SwitchableObservable<T> createdObservable = new SwitchableObservable<>(
                channel);

        if (switcher != null) {
            switcher.<T>registerSwitchable(createdObservable, address, channelType);
        }
        
        createdObservable.setSwitcher(switcher);

        return createdObservable;
    }

    /**
     * Create and return a PV observable of the correct type, registering it
     * with the switcher.
     * 
     * @param <T>
     *            the type of the channel
     * @param channelType
     *            the type of the channel
     * @param address
     *            the PV address
     * @return the PV observable
     */
    public <T> ForwardingObservable<T> getForwardingObservable(ChannelType<T> channelType, String address) {
        return new ForwardingObservable<>(getPVObservable(channelType, address));
    }

    /**
     * Create and return a closeable PV observable of the correct type.
     * 
     * @param <T>
     *            the type of the channel's values
     * @param channelType
     *            the type of the channel
     * @param address
     *            the PV address
     * @return the PV observable
     */
    public <T> ClosableObservable<T> getPVObservable(ChannelType<T> channelType, String address) {
        return channelType.reader(address);
    }
}
