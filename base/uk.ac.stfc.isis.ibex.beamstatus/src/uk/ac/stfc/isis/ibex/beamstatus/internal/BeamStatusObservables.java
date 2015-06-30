package uk.ac.stfc.isis.ibex.beamstatus.internal;

import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.instrument.channels.DateTimeChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithPrecisionChannel;

/**
 * Holds all the PV connections for the beam status UI.
 *
 */
public class BeamStatusObservables extends Closer {
	
	private static final PVAddress SYNC = PVAddress.startWith("AC").append("SYNCH");
	private static final PVAddress TS1 = PVAddress.startWith("AC").append("TS1");
	private static final PVAddress TS2 = PVAddress.startWith("AC").append("TS2");

	private static final String BEAM_CURRENT = "BEAM:CURR";
	private static final String FREQ = "FREQ";
	
	public Synchrotron sync = new Synchrotron(); 
	public TargetStation1 ts1 = new TargetStation1();
	public TargetStation2 ts2 = new TargetStation2();

	public class Synchrotron {
		public final InitialiseOnSubscribeObservable<Number> beamCurrent = reader(new NumberWithPrecisionChannel(), SYNC.endWith(BEAM_CURRENT));
		public final InitialiseOnSubscribeObservable<Number> beamFrequency = reader(new NumberWithPrecisionChannel(), SYNC.endWith(FREQ));
		
		private Synchrotron() {
		}
	}
	
	public class TargetStation1 extends EndStation {
		
		public final InitialiseOnSubscribeObservable<Number> methaneTemperature = reader(new NumberWithPrecisionChannel(), "TG:TS1:MOD:METH:TEMP");		
		public final InitialiseOnSubscribeObservable<Number> hydrogenTemperature = reader(new NumberWithPrecisionChannel(), "TG:TS1:MOD:HDGN:TEMP");	
		public final InitialiseOnSubscribeObservable<OnOff> muonKicker = reader(new EnumChannel<OnOff>(OnOff.class), "AC:MUON:KICKR:STAT");				

		private TargetStation1() {
			super(TS1);	
		}	
	}
	
	public class TargetStation2 extends EndStation {		
		public final InitialiseOnSubscribeObservable<Number> coupledMethaneTemperature;
		public final InitialiseOnSubscribeObservable<Number> coupledHydrogenTemperature;
		public final InitialiseOnSubscribeObservable<Number> decoupledMethaneTemperature;
		public final InitialiseOnSubscribeObservable<Number> decoupledModeratorRuntime;
		public final InitialiseOnSubscribeObservable<Number> decoupledModeratorRuntimeLimit;
		public final InitialiseOnSubscribeObservable<YesNo> decoupledModeratorAnnealPressure;
		public final InitialiseOnSubscribeObservable<Number> decoupledModeratorUAHBeam;

		private TargetStation2() {
			super(TS2);
			coupledMethaneTemperature = reader(new NumberWithPrecisionChannel(), TS2.endWith("CMOD:METH:TEMP"));
			coupledHydrogenTemperature = reader(new NumberWithPrecisionChannel(), TS2.endWith("CMOD:HDGN:TEMP"));
			decoupledMethaneTemperature = reader(new NumberWithPrecisionChannel(), "TG:TS2:DMOD:METH:TEMP");
			decoupledModeratorRuntime = reader(new NumberWithPrecisionChannel(), "TG:TS2:DMOD:RTIME:DUR");
			decoupledModeratorRuntimeLimit = reader(new NumberWithPrecisionChannel(), "TG:TS2:DMOD:RTLIM");
			decoupledModeratorAnnealPressure = reader(new EnumChannel<YesNo>(YesNo.class), "TG:TS2:DMOD:ANNPLOW:STAT");
			decoupledModeratorUAHBeam = reader(new NumberWithPrecisionChannel(), "TG:TS2:DMOD:BEAM");		
		}	
	}
	
	public class EndStation {
		private final InitialiseOnSubscribeObservable<OnOff> beam;
		private final InitialiseOnSubscribeObservable<Number> pps;
		private final InitialiseOnSubscribeObservable<Number> beamCurrent;
		private final InitialiseOnSubscribeObservable<Number> uAHToday;
		private final InitialiseOnSubscribeObservable<String> lastBeamOff;
		private final InitialiseOnSubscribeObservable<String> lastBeamOn;
		
		protected EndStation(PVAddress suffix) {
			beam = reader(new EnumChannel<OnOff>(OnOff.class), suffix.endWith("BEAM:STAT"));
			pps = reader(new NumberChannel(), suffix.endWith("FREQ"));
			beamCurrent = reader(new NumberWithPrecisionChannel(), suffix.endWith("BEAM:CURR"));
			uAHToday = reader(new NumberWithPrecisionChannel(), suffix.endWith("BEAM:TOTAL"));
			lastBeamOff = reader(new DateTimeChannel(), suffix.endWith("BEAMOFF:TIME"));
			lastBeamOn = reader(new DateTimeChannel(), suffix.endWith("BEAMOFF:TIME"));
		}
		
		public InitialiseOnSubscribeObservable<OnOff> beam() { return beam; }
		public InitialiseOnSubscribeObservable<Number> pps() { return pps; }
		public InitialiseOnSubscribeObservable<Number> beamCurrent() { return beamCurrent; }
		public InitialiseOnSubscribeObservable<Number> uAHToday() { return uAHToday; }
		public InitialiseOnSubscribeObservable<String> lastBeamOff() { return lastBeamOff; }
		public InitialiseOnSubscribeObservable<String> lastBeamOn() { return lastBeamOn; }
	}

	private <T> InitialiseOnSubscribeObservable<T> reader(ChannelType<T> channelType, String address) {
		return autoInitialise(registerForClose(channelType.reader(address)));
	}
	
	private static <T> InitialiseOnSubscribeObservable<T> autoInitialise(CachingObservable<T> observable) {
		return new InitialiseOnSubscribeObservable<>(observable);
	}
}
