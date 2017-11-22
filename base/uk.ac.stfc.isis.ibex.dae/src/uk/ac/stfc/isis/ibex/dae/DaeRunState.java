
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.dae;

/**
 * Enum of the states that the DAE could be in.
 */
public enum DaeRunState {
    /**
     * The DAE is starting up.
     */
	PROCESSING,
    /**
     * The DAE is ready to start a run.
     */
	SETUP,
    /**
     * The DAE is running and taking data.
     */
	RUNNING,
    /**
     * The run has been paused, data is no longer being taken.
     */
	PAUSED,
    /**
     * The DAE is waiting for a block that is in run control.
     */
	WAITING,
    /**
     * The DAE is vetoing data due to a hardware signal.
     */
	VETOING,
    /**
     * The DAE is in the process of ending a run. (It may take some time to pull
     * data out of the DAE and save it)
     */
	ENDING,
    /**
     * The DAE is in the process of saving the run data to file.
     */
	SAVING,
    /**
     * The DAE is resuming a run after being paused.
     */
	RESUMING,
    /**
     * The DAE is in the process of pausing a run.
     */
	PAUSING,
    /**
     * The DAE is in the process of beginning a run.
     */
	BEGINNING,
    /**
     * The DAE is in the process of aborting a run.
     */
	ABORTING,
    /**
     * The DAE is creating a snapshot of data in memory. (like an end but
     * doesn't increase the run number)
     */
	UPDATING,
    /**
     * The DAE is storing the snapshot to disk.
     */
	STORING,
    /**
     * The state of the DAE is unknown.
     */
	UNKNOWN,
}
