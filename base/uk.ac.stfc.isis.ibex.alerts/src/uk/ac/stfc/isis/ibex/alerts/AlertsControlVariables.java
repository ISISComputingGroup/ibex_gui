/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.alerts;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Creates the various run-control variables.
 */
public class AlertsControlVariables {
	private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.CLOSE);
	private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE);
	private final AlertsPVs alertPVs = new AlertsPVs();

	/**
	 * Creates the various alert-control variables.
	 */
	public AlertsControlVariables() {
	}

	/**
	 * Gets an observable looking at the alert-control low limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the forwarding observable
	 */
	public ForwardingObservable<Double> getLowLimit(String blockName) {
		return obsFactory.getSwitchableObservable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getLowLimitPv(blockName)));
	}

	/**
	 * Gets a writable for the alert-control low limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the writable
	 */
	public Writable<Double> setLowLimit(String blockName) {
		return writeFactory.getSwitchableWritable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getLowLimitPv(blockName)));
	}

	/**
	 * Gets an observable looking at the alert-control high limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the forwarding observable
	 */
	public ForwardingObservable<Double> getHighLimit(String blockName) {
		return obsFactory.getSwitchableObservable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getHighLimitPv(blockName)));
	}

	/**
	 * Gets a writable for the alert-control high limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the writable
	 */
	public Writable<Double> setHighLimit(String blockName) {
		return writeFactory.getSwitchableWritable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getHighLimitPv(blockName)));
	}

	/**
	 * Gets an observable looking at the alert-control enable PV.
	 *
	 * @param blockName the name of the block
	 * @return the forwarding observable
	 */
	public ForwardingObservable<String> getEnabled(String blockName) {
		return obsFactory.getSwitchableObservable(new DefaultChannel(),
				InstrumentUtils.addPrefix(alertPVs.getEnablePv(blockName)));
	}

	/**
	 * Gets a writable for the alert-control enabled PV.
	 *
	 * @param blockName the name of the block
	 * @return the writable
	 */
	public Writable<String> setEnabled(String blockName) {
		return writeFactory.getSwitchableWritable(new StringChannel(),
				InstrumentUtils.addPrefix(alertPVs.getEnablePv(blockName)));
	}

	/**
	 * Gets an observable looking at the alert-control Delay-In PV.
	 *
	 * @param blockName the name of the block
	 * @return the forwarding observable
	 */
	public ForwardingObservable<Double> getDelayIn(String blockName) {
		return obsFactory.getSwitchableObservable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getDelayInPv(blockName)));
	}

	/**
	 * Gets a writable for the alert-control Delay-In PV.
	 *
	 * @param blockName the name of the block
	 * @return the writable
	 */
	public Writable<Double> setDelayIn(String blockName) {
		return writeFactory.getSwitchableWritable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getDelayInPv(blockName)));
	}

	/**
	 * Gets an observable looking at the alert-control Delay-Out PV.
	 *
	 * @param blockName the name of the block
	 * @return the forwarding observable
	 */
	public ForwardingObservable<Double> getDelayOut(String blockName) {
		return obsFactory.getSwitchableObservable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getDelayOutPv(blockName)));
	}

	/**
	 * Gets a writable for the alert-control Delay-Out PV.
	 *
	 * @param blockName the name of the block
	 * @return the writable
	 */
	public Writable<Double> setDelayOut(String blockName) {
		return writeFactory.getSwitchableWritable(new DoubleChannel(),
				InstrumentUtils.addPrefix(alertPVs.getDelayOutPv(blockName)));
	}

	/**
	 * Gets an observable looking at the alert-control message PV.
	 *
	 * @return the forwarding observable
	 */
	public ForwardingObservable<String> getMessage() {
		return obsFactory.getSwitchableObservable(new CharWaveformChannel(),
				InstrumentUtils.addPrefix(alertPVs.getMessagePv()));
	}

	/**
	 * Gets a writable for the alert-control message PV.
	 *
	 * @return the writable
	 */
	public Writable<String> setMessage() {
		return writeFactory.getSwitchableWritable(new StringChannel(),
				InstrumentUtils.addPrefix(alertPVs.getMessagePv()));
	}

	/**
	 * Gets an observable looking at the alert-control emails PV.
	 *
	 * @return the forwarding observable
	 */
	public ForwardingObservable<String> getEmails() {
		return obsFactory.getSwitchableObservable(new CharWaveformChannel(),
				InstrumentUtils.addPrefix(alertPVs.getEmailsPv()));
	}

	/**
	 * Gets a writable for the alert-control emails PV.
	 *
	 * @return the writable
	 */
	public Writable<String> setEmails() {
		return writeFactory.getSwitchableWritable(new CharWaveformChannel(),
				InstrumentUtils.addPrefix(alertPVs.getEmailsPv()));
	}

	/**
	 * Gets an observable looking at the alert-control mobiles PV.
	 *
	 * @return the forwarding observable
	 */
	public ForwardingObservable<String> getMobiles() {
		return obsFactory.getSwitchableObservable(new CharWaveformChannel(),
				InstrumentUtils.addPrefix(alertPVs.getMobilesPv()));
	}

	/**
	 * Gets a writable for the alert-control mobiles PV.
	 *
	 * @return the writable
	 */
	public Writable<String> setMobiles() {
		return writeFactory.getSwitchableWritable(new StringChannel(),
				InstrumentUtils.addPrefix(alertPVs.getMobilesPv()));
	}

	/**
	 * Gets the PVs used by the alerts control.
	 *
	 * @return the PVs
	 */	
	public AlertsPVs getPVs() {
		return alertPVs;
	}
}
