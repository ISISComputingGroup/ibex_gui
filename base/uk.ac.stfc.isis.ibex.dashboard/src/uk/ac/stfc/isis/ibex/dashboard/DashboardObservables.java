package uk.ac.stfc.isis.ibex.dashboard;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DateTimeChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;

public class DashboardObservables extends InstrumentVariables {

    private static final String SHUTTER_STATUS = "SHTR:STAT";    
    private static final String TIME = "CS:IOC:INSTETC_01:DEVIOS:TOD";
    private static final String USERS = "ED:SURNAME";
    
	public final InitialiseOnSubscribeObservable<ShutterStatus> shutter = reader(new EnumChannel<>(ShutterStatus.class), SHUTTER_STATUS);
	public final InitialiseOnSubscribeObservable<String> instrumentTime = reader(new DateTimeChannel(), TIME);
	public final InitialiseOnSubscribeObservable<String> users = reader(new CompressedCharWaveformChannel(), USERS);
		
	public final DaeObservables dae;

	public DashboardObservables(Channels channels) {
		super(channels);
		dae = registerForClose(new DaeObservables(channels));
	}
}
