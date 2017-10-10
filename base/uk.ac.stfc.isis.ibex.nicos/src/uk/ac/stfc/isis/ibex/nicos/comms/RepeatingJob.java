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
package uk.ac.stfc.isis.ibex.nicos.comms;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * An eclipse Job that is scheduled to run at a set interval.
 */
public abstract class RepeatingJob extends Job {

    private boolean running = true;
    protected long repeatDelay = 0;

    /**
     * Creates a job that is run repeatedly at a set interval.
     * 
     * @param jobName
     *            The name of the job.
     * @param repeatPeriod
     *            The amount of time between jobs (in ms).
     */
    public RepeatingJob(String jobName, long repeatPeriod) {
        super(jobName);
        repeatDelay = repeatPeriod;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        if (running) {
            schedule(repeatDelay);
            return doTask(monitor);
        } else {
            return Status.CANCEL_STATUS;
        }
    }

    /**
     * Performs the task that this job is expected to do.
     * 
     * @param monitor
     *            The monitor to post job progress to.
     * @return The status of the job.
     */
    protected abstract IStatus doTask(IProgressMonitor monitor);

    @Override
    public boolean shouldSchedule() {
        return running;
    }

    /**
     * Set the job running status.
     * 
     * @param running
     *            True to start the job running, false to stop.
     */
    public void setRunning(boolean running) {
        this.running = running;
        schedule(repeatDelay);
    }
}
