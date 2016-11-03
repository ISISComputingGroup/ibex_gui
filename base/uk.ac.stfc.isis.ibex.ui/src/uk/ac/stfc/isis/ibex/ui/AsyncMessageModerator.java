
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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Moderate the number of asynchoronous actions that are placed on a thread,
 * e.g. a GUI thread. This is done by granting a task lock to a requester.
 */
public class AsyncMessageModerator {

    /**
     * When the task is being processed this is set to true; false otherwise. Is
     * atomic so it is thread safe.
     */
    private AtomicBoolean processing = new AtomicBoolean(false);

    /**
     * When the task lock can not be granted, e.g. task is not queued, this is
     * set to true; false otherwise. Is atomic so it is thread safe.
     */
    private AtomicBoolean taskLockDenied = new AtomicBoolean(false);

    /** Task to perform if process is denied. */
    private AsyncMessageModeratorTask taskRerunner;

    /**
     * Constructor.
     * 
     * @param taskRerunner task to run if a task can not be started; null for
     *            don't do anything
     */
    public AsyncMessageModerator(AsyncMessageModeratorTask taskRerunner) {
        this.taskRerunner = taskRerunner;
    }
    
    /**
     * Request the task lock so that the task can be queued. Will return true if
     * ok, false otherwise. If it returns true the caller MUST release the task
     * lock with release task lock when the task is done.
     * 
     * @return True if caller has task lock; False otherwise the change
     */
    public boolean requestTaskLock() {
        if (processing.compareAndSet(false, true)) {
            return true;
        } else {
            taskLockDenied.set(true);
            return false;
        }
    }

    /**
     * Once a task has finished this should be called to release the task lock.
     * 
     * 
     */
    public void releaseTaskLock() {
        processing.set(false);
        if (this.taskRerunner != null && taskLockDenied.get()) {
            this.taskRerunner.reQueueTask();
        }
    }

}
