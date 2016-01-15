
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

package uk.ac.stfc.isis.ibex.beamstatus.internal;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
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
	
    /**
     * The beam status PVs are global so they do not need to be switched.
     */
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.NOTHING);

	public Synchrotron sync = new Synchrotron(); 
	public TargetStation1 ts1 = new TargetStation1();
	public TargetStation2 ts2 = new TargetStation2();

	public class Synchrotron {
        public final ForwardingObservable<Number> beamCurrent = obsFactory
                .getSwitchableObservable(new NumberWithPrecisionChannel(), SYNC.endWith(BEAM_CURRENT));
        public final ForwardingObservable<Number> beamFrequency = obsFactory
                .getSwitchableObservable(new NumberWithPrecisionChannel(), SYNC.endWith(FREQ));
		
		private Synchrotron() {
		}
	}
	
	public class TargetStation1 extends EndStation {
		
        public final ForwardingObservable<Number> methaneTemperature = obsFactory
                .getSwitchableObservable(new NumberWithPrecisionChannel(), "TG:TS1:MOD:METH:TEMP");
        public final ForwardingObservable<Number> hydrogenTemperature = obsFactory
                .getSwitchableObservable(new NumberWithPrecisionChannel(), "TG:TS1:MOD:HDGN:TEMP");
        public final ForwardingObservable<OnOff> muonKicker = obsFactory
                .getSwitchableObservable(new EnumChannel<OnOff>(OnOff.class), "AC:MUON:KICKR:STAT");

		private TargetStation1() {
			super(TS1);	
		}	
	}
	
	public class TargetStation2 extends EndStation {		
        public final ForwardingObservable<Number> coupledMethaneTemperature;
		public final ForwardingObservable<Number> coupledHydrogenTemperature;
		public final ForwardingObservable<Number> decoupledMethaneTemperature;
		public final ForwardingObservable<Number> decoupledModeratorRuntime;
		public final ForwardingObservable<Number> decoupledModeratorRuntimeLimit;
		public final ForwardingObservable<YesNo> decoupledModeratorAnnealPressure;
		public final ForwardingObservable<Number> decoupledModeratorUAHBeam;

		private TargetStation2() {
			super(TS2);
            coupledMethaneTemperature = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(),
                    TS2.endWith("CMOD:METH:TEMP"));
            coupledHydrogenTemperature = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(),
                    TS2.endWith("CMOD:HDGN:TEMP"));
            decoupledMethaneTemperature = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(),
                    "TG:TS2:DMOD:METH:TEMP");
            decoupledModeratorRuntime = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(),
                    "TG:TS2:DMOD:RTIME:DUR");
            decoupledModeratorRuntimeLimit = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(),
                    "TG:TS2:DMOD:RTLIM");
            decoupledModeratorAnnealPressure = obsFactory.getSwitchableObservable(new EnumChannel<YesNo>(YesNo.class),
                    "TG:TS2:DMOD:ANNPLOW:STAT");
            decoupledModeratorUAHBeam = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(),
                    "TG:TS2:DMOD:BEAM");
		}	
	}
	
	public class EndStation {
		private final ForwardingObservable<OnOff> beam;
		private final ForwardingObservable<Number> pps;
		private final ForwardingObservable<Number> beamCurrent;
		private final ForwardingObservable<Number> uAHToday;
		private final ForwardingObservable<String> lastBeamOff;
		private final ForwardingObservable<String> lastBeamOn;
		
		protected EndStation(PVAddress suffix) {

            beam = obsFactory.getSwitchableObservable(new EnumChannel<OnOff>(OnOff.class), suffix.endWith("BEAM:STAT"));
            pps = obsFactory.getSwitchableObservable(new NumberChannel(), suffix.endWith("FREQ"));
            beamCurrent = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), suffix.endWith("BEAM:CURR"));
            uAHToday = obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), suffix.endWith("BEAM:TOTAL"));
            lastBeamOff = obsFactory.getSwitchableObservable(new DateTimeChannel(), suffix.endWith("BEAMOFF:TIME"));
            lastBeamOn = obsFactory.getSwitchableObservable(new DateTimeChannel(), suffix.endWith("BEAMOFF:TIME"));
		}
		
		public ForwardingObservable<OnOff> beam() { return beam; }
		public ForwardingObservable<Number> pps() { return pps; }
		public ForwardingObservable<Number> beamCurrent() { return beamCurrent; }
		public ForwardingObservable<Number> uAHToday() { return uAHToday; }
		public ForwardingObservable<String> lastBeamOff() { return lastBeamOff; }
		public ForwardingObservable<String> lastBeamOn() { return lastBeamOn; }
	}
}
