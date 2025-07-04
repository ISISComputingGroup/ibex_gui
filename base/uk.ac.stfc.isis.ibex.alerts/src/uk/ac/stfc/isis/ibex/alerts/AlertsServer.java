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
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * The alerts control server.
 *
 */
public class AlertsServer extends Closer {
	private AlertsControlVariables variables;
	
	/**
	 * Constructor.
	 *
	 * @param variables the alert-control variables
	 */
	public AlertsServer(AlertsControlVariables variables) {
		this.variables = variables;
	}
	
	/**
     * Gets an observable looking at the alert-control low limit PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Double> getLowLimit(String blockName) {
		return variables.getLowLimit(blockName);
	}
	
	/**
     * Gets a writable for the alert-control low limit PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<Double> setLowLimit(String blockName) {
		return variables.setLowLimit(blockName);
	}

	/**
     * Gets an observable looking at the alert-control high limit PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Double> getHighLimit(String blockName) {
		return variables.getHighLimit(blockName);
	}

	/**
     * Gets a writable for the alert-control high limit PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<Double> setHighLimit(String blockName) {
        return variables.setHighLimit(blockName);
	}

	/**
     * Gets an observable looking at the alert-control enabled PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<String> getEnabled(String blockName) {
		return variables.getEnabled(blockName);
	}
		
	/**
     * Gets a writable for the alert-control enabled PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<String> setEnabled(String blockName) {
		return variables.setEnabled(blockName);
	}
	
	/**
     * Gets an observable looking at the alert-control Delay-In PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Double> getDelayIn(String blockName) {
		return variables.getDelayIn(blockName);
	}

	/**
     * Gets a writable for the alert-control Delay-In PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<Double> setDelayIn(String blockName) {
		return variables.setDelayIn(blockName);
	}
	
	/**
     * Gets an observable looking at the alert-control Delay-Out PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Double> getDelayOut(String blockName) {
		return variables.getDelayOut(blockName);
	}


	/**
     * Gets a writable for the alert-control Delay-Out PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writable<Double> setDelayOut(String blockName) {
		return variables.setDelayOut(blockName);
	}
}
