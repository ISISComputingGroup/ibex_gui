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
package uk.ac.stfc.isis.ibex.alarm;

/**
 * This class is responsible for queuing a delayed update to the alarms view.
 * 
 * Updates are queued when a configuration finishes changing.
 */
public final class AlarmReloadManager {

    private static final int MILLISECONDS_PER_SECOND = 1000;

    /**
     * Have to wait for the alarm server to actually have updated values.
     * 
     * The blockserver finishes it's operations before the alarm server is
     * ready.
     */
    private static final int DELAY_IN_SECONDS = 40;

    /**
     * The instance of this singleton.
     */
    private static AlarmReloadManager instance;

    /**
     * Private constructor, use getInstance() instead.
     */
    private AlarmReloadManager() {
    }

    /**
     * Gets the instance of this singleton.
     * 
     * @return the instance of this singleton
     */
    public static synchronized AlarmReloadManager getInstance() {
        if (instance == null) {
            instance = new AlarmReloadManager();
        }
        return instance;
    }

    /**
     * The runnable used by each thread.
     */
    private final Runnable reloadRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(MILLISECONDS_PER_SECOND * DELAY_IN_SECONDS);
                reloadAlarms();
            } catch (InterruptedException e) {
                return;
            }
        }
    };

    private Thread thread;

    /**
     * Queues a delayed update to the alarms view. The delay is defined by
     * <code>DELAY_IN_SECONDS</code>. At most one update will be queued at any
     * time, with newer updates cancelling older ones.
     */
    public void queueDelayedUpdate() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }

        thread = new Thread(reloadRunnable);
        thread.start();
    }

    /**
     * Reloads the alarms perspective.
     */
    private void reloadAlarms() {
        Alarm.getInstance().reload();
    }

}
