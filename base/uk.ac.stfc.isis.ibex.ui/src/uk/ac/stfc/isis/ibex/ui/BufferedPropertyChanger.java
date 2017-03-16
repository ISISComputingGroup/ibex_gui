 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 *
 */
public class BufferedPropertyChanger extends ModelObject {

    private class PropertyChangeDescriptor {
        private String propertyName;
        private Object oldValue;
        private Object newValue;

        public PropertyChangeDescriptor(String propertyName, Object oldValue, Object newValue) {
            this.propertyName = propertyName;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public void fire() {
            firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    private PropertyChangeDescriptor lastUpdate;
    private boolean stop;
    private final int period;

    /**
     * Fires a property change every <code>period</code> milliseconds.
     * 
     * @param period
     *            number of milliseconds between each call
     */
    public BufferedPropertyChanger(int period) {
        this.period = period;
    }

    /**
     * Registers a property change.
     * 
     * @param propertyName
     *            see firePropertyChange
     * @param oldValue
     *            see firePropertyChange
     * @param newValue
     *            see firePropertyChange
     */
    public void add(String propertyName, Object oldValue, Object newValue) {
        lastUpdate = new PropertyChangeDescriptor(propertyName, oldValue, newValue);
    }

    /**
     * Starts this queue. Will fire the last property change received every
     * <code>period</code> milliseconds.
     */
    public void start() {
        stop = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!stop) {
                    doTask();
                }
            }

        }).start();
    }

    /**
     * Stops this queue from firing any further property changes until it is
     * restarted.
     */
    public void stop() {
        stop = true;
    }

    private void doTask() {
        if (lastUpdate != null) {
            lastUpdate.fire();
            lastUpdate = null;
        }

        sleepAndStopIfInterrupted();
    }

    private void sleepAndStopIfInterrupted() {
        try {
            Thread.sleep(period);
        } catch (InterruptedException e) {
            stop = true;
        }
    }
}
