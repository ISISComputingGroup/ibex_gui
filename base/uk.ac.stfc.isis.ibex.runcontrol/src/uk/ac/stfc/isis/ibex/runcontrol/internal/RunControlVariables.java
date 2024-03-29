
/*
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

package uk.ac.stfc.isis.ibex.runcontrol.internal;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Creates the various run-control variables. 
 */
public class RunControlVariables {
    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.CLOSE);
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE);
	private final RunControlAddresses runControlAddresses = new RunControlAddresses();
	
	/**
	 * Creates the various run-control variables. 
	 */
    public RunControlVariables() {
	}
	
	/**
	 * Gets an observable looking at the run-control low limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the forwarding observable
	 */
	public ForwardingObservable<Double> blockRunControlLowLimit(String blockName) {
        return obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getLowLimitPv(blockName)));
	}
	
	/**
     * Gets an observable looking at the run-control high limit PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Double> blockRunControlHighLimit(String blockName) {
        return obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getHighLimitPv(blockName)));
	}
	
	/**
     * Gets an observable looking at the run-control high limit PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Boolean> blockRunControlSuspendIfInvalid(String blockName) {
        return obsFactory.getSwitchableObservable(new BooleanChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getSuspendOnInvalidPv(blockName)));
	}
	
	/**
     * Gets an observable looking at the run-control in range PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<String> blockRunControlInRange(String blockName) {
        return obsFactory.getSwitchableObservable(new DefaultChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getInRangePv(blockName)));
	}
	
	/**
     * Gets an observable looking at the run-control enabled PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<String> blockRunControlEnabled(String blockName) {
        return obsFactory.getSwitchableObservable(new DefaultChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getEnablePv(blockName)));
	}
	
	/**
	 * Gets a writable for the run-control low limit setter PV.
	 *
	 * @param blockName the name of the block
	 * @return the writable
	 */
	public Writable<Double> blockRunControlLowLimitSetter(String blockName) {
        return writeFactory.getSwitchableWritable(new DoubleChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getLowLimitPv(blockName)));
	}
	
	/**
     * Gets a writable for the run-control high limit setter PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<Double> blockRunControlHighLimitSetter(String blockName) {
        return writeFactory.getSwitchableWritable(new DoubleChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getHighLimitPv(blockName)));
	}
	
	/**
     * Gets a writable for the run-control enabled setter PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<String> blockRunControlEnabledSetter(String blockName) {
        return writeFactory.getSwitchableWritable(new StringChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getEnablePv(blockName)));
	}
	
	/**
     * Gets a writable for the run-control enabled setter PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<String> blockRunControlSuspendIfInvalidSetter(String blockName) {
        return writeFactory.getSwitchableWritable(new StringChannel(),
                InstrumentUtils.addPrefix(runControlAddresses.getSuspendOnInvalidPv(blockName)));
	}

}
