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

import java.util.Set;

import com.google.common.collect.Sets;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Contains the functionality to display a top level alerts settings in a GUI.
 */
public class TopLevelAlertSettings extends ModelObject implements Closable {

    /**
     * The current message alerts setting.
     */
    private String message;

    /**
     * The current emails alerts setting.
     */
    private String emails;

    /**
     * The current mobiles alerts setting.
     */
    private String mobiles;
    
    private final BaseObserver<String> messageObserver = new BaseObserver<String>() {
        @Override
		public void onValue(String value) {
			setMessage(value);
		}
    };

	private final BaseObserver<String> emailsObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			setEmails(value);
		}
	};
	
	private final BaseObserver<String> mobilesObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			setMobiles(value);
		}		
	};
    private final Set<Subscription> subscriptions;
    private final Set<ForwardingObservable<?>> sources;

    /**
     * Instantiates a new TopLevelAlertSettings.
     *
     * @param alertsServer the wrapper of observables holding the top level alert setting values
     */
    public TopLevelAlertSettings(AlertsServer alertsServer) {
         sources = Sets.newHashSet(
        		alertsServer.getMessage(),
        		alertsServer.getEmails(),
        		alertsServer.getMobiles()
        );

        subscriptions = Sets.newHashSet(
        		alertsServer.getMessage().subscribe(messageObserver),
        		alertsServer.getEmails().subscribe(emailsObserver),
        		alertsServer.getMobiles().subscribe(mobilesObserver)
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
     * @return the message configured for the alert
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message for the alert.
     * @param message the message to set.
     */
    public synchronized void setMessage(String message) {
        firePropertyChange("message", this.message, this.message = message);
    }

	/**
	 * @return the emails configured for the alert.
	 */
	public String getEmails() {
		return emails;
	}
	
    /**
     * Sets the emails for the alert.
     * @param emails the emails to set.
     */
    public synchronized void setEmails(String emails) {
        firePropertyChange("emails", this.emails, this.emails = emails);
    }
	
	/**
	 * @return the mobiles configured for the alert.
	 */
	public String getMobiles() {
		return emails;
	}
	
    /**
     * Sets the mobiles for the alert.
     * @param mobiles the mobiles to set.
     */
    public synchronized void setMobiles(String mobiles) {
        firePropertyChange("mobiles", this.mobiles, this.mobiles = mobiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public String toString() {
		return String.format("Global alerts configurations ([message=%s]; [emails=%s] ; [mobiles=%s])", getMessage(),
				getEmails(), getMobiles());
	}
}
