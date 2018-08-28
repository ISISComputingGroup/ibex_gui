
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

import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.LongChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Holds the Writables for the DAE.
 */
public class DaeWritables {

	private static final String BEGIN_RUN = "BEGINRUN";
	private static final String END_RUN = "ENDRUN";
	private static final String PAUSE_RUN = "PAUSERUN";
	private static final String RESUME_RUN = "RESUMERUN";
	private static final String ABORT_RUN = "ABORTRUN";
	private static final String RECOVER_RUN = "RECOVERRUN";
	private static final String SAVE_RUN = "SAVERUN";

	private static final String TITLE = "TITLE:SP";
    private static final String DISPLAY_TITLE = "TITLE:DISPLAY";
	private static final String DAE_SETTINGS = "DAESETTINGS:SP";
	private static final String HARDWARE_PERIODS = "HARDWAREPERIODS:SP";
	private static final String UPDATE_SETTINGS = "UPDATESETTINGS:SP";	
    private static final String TIME_CHANNEL_SETTINGS = "TCBSETTINGS:SP";

    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);

    /**
     * PV to write to to begin a run.
     */
    public final Writable<String> begin;
    /**
     * PV to write to to end the current run.
     */
    public final Writable<String> end;
    /**
     * PV to write to to pause the current run.
     */
    public final Writable<String> pause;
    /**
     * PV to write to to resume the currently paused run.
     */
    public final Writable<String> resume;
    /**
     * PV to write to to abort the current run.
     */
    public final Writable<String> abort;
    /**
     * PV to write to to cancel the abort of the current run.
     */
    public final Writable<String> recover;
    /**
     * PV to write to to save a snapshot of data from the current run to disk
     * without ending the run.
     */
    public final Writable<String> save;
    /**
     * The PV that contains the title of the current experiment.
     */
    public final Writable<String> title;
    /**
     * A PV that, when set, will ensure the title and users are displayed on the
     * web dashboard.
     */
    public final Writable<Long> displayTitle;
    /**
     * The settings for the DAE.
     */
    public final Writable<String> daeSettings;
    /**
     * The hardware periods for the DAE.
     */
    public final Writable<String> hardwarePeriods;
    /**
     * The update settings for the DAE.
     */
    public final Writable<String> updateSettings;
    /**
     * The time channel settings for the DAE.
     */
    public final Writable<String> timeChannelSettings;
	
    /**
     * Constructor for the writables class. Creates the writables for all the
     * DAE PVs.
     */
    public DaeWritables() {
        begin = writeFactory.getSwitchableWritable(new StringChannel(), daeAddress(BEGIN_RUN));
        end = writeFactory.getSwitchableWritable(new StringChannel(), daeAddress(END_RUN));
        pause = writeFactory.getSwitchableWritable(new StringChannel(), daeAddress(PAUSE_RUN));
        resume = writeFactory.getSwitchableWritable(new StringChannel(), daeAddress(RESUME_RUN));
        abort = writeFactory.getSwitchableWritable(new StringChannel(), daeAddress(ABORT_RUN));
        recover = writeFactory.getSwitchableWritable(new StringChannel(), daeAddress(RECOVER_RUN));
        save = writeFactory.getSwitchableWritable(new StringChannel(), daeAddress(SAVE_RUN));
        title = writeFactory.getSwitchableWritable(new CharWaveformChannel(), daeAddress(TITLE));
        displayTitle = writeFactory.getSwitchableWritable(new LongChannel(), daeAddress(DISPLAY_TITLE));
        daeSettings = writeFactory.getSwitchableWritable(new CharWaveformChannel(), daeAddress(DAE_SETTINGS));
        hardwarePeriods = writeFactory.getSwitchableWritable(new CharWaveformChannel(), daeAddress(HARDWARE_PERIODS));
        updateSettings = writeFactory.getSwitchableWritable(new CharWaveformChannel(), daeAddress(UPDATE_SETTINGS));
        timeChannelSettings = writeFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                daeAddress(TIME_CHANNEL_SETTINGS));

	}
	
    private static String daeAddress(String suffix) {
        StringBuilder sb = new StringBuilder();
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append("DAE:");
        sb.append(suffix);
        return sb.toString();
	}
}
