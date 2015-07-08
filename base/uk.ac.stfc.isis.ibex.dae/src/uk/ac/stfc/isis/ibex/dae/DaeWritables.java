
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

import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.Writables;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

public class DaeWritables extends Writables {

	private static final String BEGIN_RUN = "BEGINRUN";
	private static final String END_RUN = "ENDRUN";
	private static final String PAUSE_RUN = "PAUSERUN";
	private static final String RESUME_RUN = "RESUMERUN";
	private static final String ABORT_RUN = "ABORTRUN";
	private static final String RECOVER_RUN = "RECOVERRUN";
	private static final String SAVE_RUN = "SAVERUN";

	private static final String TITLE = "TITLE:SP";
	private static final String DAE_SETTINGS = "DAESETTINGS:SP";
	private static final String HARDWARE_PERIODS = "HARDWAREPERIODS:SP";
	private static final String UPDATE_SETTINGS = "UPDATESETTINGS:SP";	
	private static final String TIME_CHANNEL_SETTINGS = "TCBSETTINGS:SP";	

	public final Writable<String> begin = writable(new StringChannel(), daeAddress(BEGIN_RUN));
	public final Writable<String> end = writable(new StringChannel(), daeAddress(END_RUN));
	public final Writable<String> pause = writable(new StringChannel(), daeAddress(PAUSE_RUN));
	public final Writable<String> resume = writable(new StringChannel(), daeAddress(RESUME_RUN));
	public final Writable<String> abort = writable(new StringChannel(), daeAddress(ABORT_RUN));
	public final Writable<String> recover = writable(new StringChannel(), daeAddress(RECOVER_RUN));
	public final Writable<String> save = writable(new StringChannel(), daeAddress(SAVE_RUN));
	
	public final Writable<String> title = writable(new CharWaveformChannel(), daeAddress(TITLE));
	public final Writable<String> daeSettings = writable(new CharWaveformChannel(), daeAddress(DAE_SETTINGS));
	public final Writable<String> hardwarePeriods = writable(new CharWaveformChannel(), daeAddress(HARDWARE_PERIODS));
	public final Writable<String> updateSettings = writable(new CharWaveformChannel(), daeAddress(UPDATE_SETTINGS));
	public final Writable<String> timeChannelSettings = writable(new CompressedCharWaveformChannel(), daeAddress(TIME_CHANNEL_SETTINGS));
	
	public DaeWritables(Channels channels) {
		super(channels);
	}
	
	private static String daeAddress(String daeSuffix) {
		return "DAE:" + daeSuffix;
	}
}
