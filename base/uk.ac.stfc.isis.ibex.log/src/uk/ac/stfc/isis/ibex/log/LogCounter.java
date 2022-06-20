/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.activemq.message.IMessageConsumer;
import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.UI;

/**
 * A Counter that consumes log messages and counts how many there have been.
 */
public class LogCounter extends ModelObject implements IMessageConsumer<LogMessage>, PropertyChangeListener {

    private static final String MAJOR = "MAJOR";
    private static final String MINOR = "MINOR";

    private static IPreferenceStore preferenceStore = Log.getDefault().getPreferenceStore();

    private MessageCounter counter = new MessageCounter();
    private boolean running = true;
    
    private static final String SWITCH_TO_OR_FROM_IOC_LOG_PROPERTY = "switchToOrFromIOCLog";
    
    /**
     * Create a new log constructor and add it as observed by {@link uk.ac.stfc.isis.ibex.ui.UI}.
     */
    public LogCounter() {
    	UI.getDefault().addStopWaitPropertyChangeListener(this);
    }

    /**
     * Starts the counter running.
     */
    public void start() {
        running = true;
    }

    /**
     * Stops the counter.
     */
    public void stop() {
        running = false;
    }

    /**
     * Get how many messages have been counted.
     * 
     * @return The number of counted messages.
     */
    public long getCount() {
        return counter.countsForSeverity(MAJOR) + optionalMinorCount();
    }

    /**
     * Resets the counter to zero.
     */
    public void resetCount() {
        long grandTotal = counter.totalCount();
        counter = new MessageCounter();
        fireCountChanged(grandTotal);
    }

    /**
     * Count the messages as they come in, if they have the correct severity.
     */
    @Override
    public void newMessage(LogMessage logMessage) {
        if (!running) {
            return;
        }

        long totalToDate = counter.totalCount();
        counter.countMessage(logMessage);
        fireCountChanged(totalToDate);
    }

    private void fireCountChanged(long previousTotal) {
        firePropertyChange("count", previousTotal, counter.totalCount());
    }

    private long optionalMinorCount() {
        return includeMinorSeverityCount() ? counter.countsForSeverity(MINOR) : 0;
    }

    private boolean includeMinorSeverityCount() {
        return preferenceStore.getBoolean(PreferenceConstants.P_MINOR_MESSAGE);
    }

    @Override
    public void clearMessages() {
        this.resetCount();
    }

    /**
     * Handle a switchToOrFromIOCLogProperty by calling {@link #start()}.
     * 
     * @param evt The property change event, only handles {@link switchToOrFromIOCLogProperty}.
     */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Objects.equals(evt.getPropertyName(), SWITCH_TO_OR_FROM_IOC_LOG_PROPERTY)) {
			start();
		}
	}
}
