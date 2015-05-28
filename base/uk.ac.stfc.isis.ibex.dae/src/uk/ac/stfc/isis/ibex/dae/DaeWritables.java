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
	private static final String HARDWARE_PERIODS ="HARDWAREPERIODS:SP";
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
