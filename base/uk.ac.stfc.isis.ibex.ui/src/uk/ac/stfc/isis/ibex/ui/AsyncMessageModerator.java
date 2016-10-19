
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
 * Moderate the number of asynchoronous actions that are placed on the GUI
 * thread. This is done by providing feedback on whether an action should be
 * processed
 */
public class AsyncMessageModerator {

    /**
     * When the task is being processed this is set to true; false otherwise. Is
     * atomic so it is thread safe
     */
    private AtomicBoolean processing = new AtomicBoolean(false);

    /**
     * When the task can not be started this is set to true; false otherwise. Is
     * atomic so it is thread safe
     */
    private AtomicBoolean taskCouldNotBeStarted = new AtomicBoolean(false);

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
     * Can start task, and register task has started.
     * 
     * @return True if property change should be process; False to not process
     *         the change
     */
    public boolean canStartTask() {
        if (processing.compareAndSet(false, true)) {
            return true;
        } else {
            taskCouldNotBeStarted.set(true);
            return false;
        }
    }

    /**
     * Once a task has finished this should be called.
     * 
     * 
     */
    public void finishedTask() {
        processing.set(false);
        if (this.taskRerunner != null && taskCouldNotBeStarted.get()) {
            this.taskRerunner.rerunTask();
        }
    }

}
