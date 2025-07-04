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

package uk.ac.stfc.isis.ibex.configserver.displaying;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObserverSetDefaultOnInvalid;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Contains the functionality to display a Block's alerts settings in a GUI.
 */
public class DisplayAlerts extends ModelObject implements Closable {
    private final Block block;
    
    /**
     * Indicates whether the alerts is enabled.
     */
    private Boolean enabled;

    /**
     * The current low limit alerts setting.
     */
    private Double lowlimit;

    /**
     * The current high limit alerts setting.
     */
    private Double highlimit;

    /**
     * The current delay in alerts setting.
     */
    private Double delayin;

    /**
     * The current delay out alerts setting.
     */
    private Double delayout;
    
    private final BaseObserver<Double> lowLimitAdapter = new ObserverSetDefaultOnInvalid<Double>(value -> setLowLimit(value), null);
    private final BaseObserver<Double> highLimitAdapter = new ObserverSetDefaultOnInvalid<Double>(value -> setHighLimit(value), null);
    private final BaseObserver<Double> delayInAdapter = new ObserverSetDefaultOnInvalid<Double>(value -> setDelayIn(value), null);
    private final BaseObserver<Double> delayOutAdapter = new ObserverSetDefaultOnInvalid<Double>(value -> setDelayOut(value), null);

    private final BaseObserver<String> enabledAdapter = new BaseObserver<String>() {
        @Override
        public void onValue(String value) {
            if (Objects.equals(value, "YES")) {
                setEnabled(true);
            } else if (Objects.equals(value, "NO")) {
            	setEnabled(false);
            } else {
            	// By default we assume the alert is enabled
            	setEnabled(true);
            }
        }
    };

    private final Set<Subscription> subscriptions;
    private final Set<ForwardingObservable<?>> sources;

    /**
     * Instantiates a new DisplayAlerts.
     *
     * @param block the block
     * @param alertsServer the wrapper of observables holding the block's alert setting values
     */
    public DisplayAlerts(Block block, AlertsServer alertsServer) {
        this.block = block;

        sources = Sets.newHashSet(
        		alertsServer.getEnabled(block.getName()),
        		alertsServer.getLowLimit(block.getName()),
        		alertsServer.getHighLimit(block.getName()),
        		alertsServer.getDelayIn(block.getName()),
        		alertsServer.getDelayOut(block.getName())
        );

        subscriptions = Sets.newHashSet(
        		alertsServer.getEnabled(block.getName()).subscribe(enabledAdapter),
        		alertsServer.getLowLimit(block.getName()).subscribe(lowLimitAdapter),
        		alertsServer.getHighLimit(block.getName()).subscribe(highLimitAdapter),
        		alertsServer.getDelayIn(block.getName()).subscribe(delayInAdapter),
        		alertsServer.getDelayOut(block.getName()).subscribe(delayOutAdapter)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void close() {
    	subscriptions.forEach(Subscription::cancelSubscription);
    	subscriptions.clear();
    	sources.forEach(ForwardingObservable::close);
    	sources.clear();
    }

    /**
     * @return the block's name
     */
    public String getName() {
        return block.getName();
    }
      

    /**
     * @return whether alert is currently enabled.
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled flag for the alert on the block.
     * @param enabled the enabled value.
     */
    public synchronized void setEnabled(Boolean enabled) {
        firePropertyChange("enabled", this.enabled, this.enabled = enabled);
    }

    /**
     * @return the current low limit for run-control.
     */
    public Double getLowLimit() {
        return lowlimit;
    }

    /**
     * Sets the low limit for the alerts on the block.
     * @param limit the low limit value.
     */
    public synchronized void setLowLimit(Double limit) {
        firePropertyChange("lowlimit", this.lowlimit, this.lowlimit = limit);
    }

    /**
     * @return the current high limit for alert on the block.
     */
    public Double getHighLimit() {
        return highlimit;
    }

    /**
     * Sets the high limit for the alerts on the block.
     * @param limit the high limit value.
     */
    public synchronized void setHighLimit(Double limit) {
        firePropertyChange("highlimit", this.highlimit, this.highlimit = limit);
    }

    /**
     * @return the current delay in for the alert on the block.
     */
    public Double getDelayIn() {
        return delayin;
    }

    /**
     * Sets the delay in for the alert on the block.
     * @param delayin the delay in value.
     */
    public synchronized void setDelayIn(Double delayin) {
        firePropertyChange("delayin", this.delayin, this.delayin = delayin);
    }

    /**
     * @return the current alerts delay out for the block.
     */
    public Double getDelayOut() {
        return delayout;
    }

    /**
     * Sets the delay out for the alerts on the block.
     * @param delayout the delay out value.
     */
    public synchronized void setDelayOut(Double delayout) {
        firePropertyChange("delayout", this.delayout, this.delayout = delayout);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
    	if (other == null || !(other instanceof DisplayAlerts)) {
    		return false;
    	}
    	return Objects.equals(((DisplayAlerts) other).getName(), getName());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
    	return Objects.hash(getName());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
    	return String.format("Alerts for Block (name=%s)", getName());
    }
}
