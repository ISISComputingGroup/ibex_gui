
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

package uk.ac.stfc.isis.ibex.beamstatus;

import uk.ac.stfc.isis.ibex.beamstatus.internal.BeamStatusObservables;
import uk.ac.stfc.isis.ibex.beamstatus.internal.EnumConverter;
import uk.ac.stfc.isis.ibex.beamstatus.internal.NumberConverter;
import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Holds the Observables for the beam status display.
 */
public class Observables extends ModelAdapter {
	
	public final Synchrotron sync;
	public final TargetStation1 ts1;
	public final TargetStation2 ts2;

	public Observables(BeamStatusObservables status) {
		sync = new Synchrotron(status.sync);
		ts1 = new TargetStation1(status.ts1);
		ts2 = new TargetStation2(status.ts2);
	}

	public final class Synchrotron {
		public final UpdatedValue<String> beamCurrent;
		public final UpdatedValue<String> beamFrequency;
		
		private Synchrotron(BeamStatusObservables.Synchrotron sync) {
			beamCurrent = adaptNumber(sync.beamCurrent);
			beamFrequency = adaptNumber(sync.beamFrequency);
		}
	}
	
	public final class TargetStation1 extends EndStation {
		
		public final UpdatedValue<String> methaneTemperature;
		public final UpdatedValue<String> hydrogenTemperature;
		public final UpdatedValue<String> muonKicker;
		
		private TargetStation1(BeamStatusObservables.TargetStation1 ts1) {
			super(ts1);
			methaneTemperature = adaptNumber(ts1.methaneTemperature);
			hydrogenTemperature = adaptNumber(ts1.hydrogenTemperature);
			muonKicker = adaptEnum(ts1.muonKicker);
		}
	}

	public final class TargetStation2 extends EndStation {
		
		public final UpdatedValue<String> coupledMethaneTemperature;
		public final UpdatedValue<String> coupledHydrogenTemperature;
		public final UpdatedValue<String> decoupledMethaneTemperature;
		public final UpdatedValue<String> decoupledModeratorRuntime;
		public final UpdatedValue<String> decoupledModeratorRuntimeLimit;
		public final UpdatedValue<String> decoupledModeratorAnnealPressure;
		public final UpdatedValue<String> decoupledModeratorUAHBeam;
		
		private TargetStation2(BeamStatusObservables.TargetStation2 ts2) {
			super(ts2);
			coupledMethaneTemperature = adaptNumber(ts2.coupledMethaneTemperature);
			coupledHydrogenTemperature = adaptNumber(ts2.coupledHydrogenTemperature);
			decoupledMethaneTemperature = adaptNumber(ts2.decoupledMethaneTemperature);
			decoupledModeratorRuntime = adaptNumber(ts2.decoupledModeratorRuntime);
			decoupledModeratorRuntimeLimit = adaptNumber(ts2.decoupledModeratorRuntimeLimit);
			decoupledModeratorAnnealPressure = adaptEnum(ts2.decoupledModeratorAnnealPressure);
			decoupledModeratorUAHBeam = adaptNumber(ts2.decoupledModeratorUAHBeam);
		}
	}
	
    public class EndStation {
		private final UpdatedValue<String> beam;
		private final UpdatedValue<String> pps;
		private final UpdatedValue<String> beamCurrent;
		private final UpdatedValue<String> uAHToday;
		private final UpdatedValue<String> lastBeamOff;
		private final UpdatedValue<String> lastBeamOn;
		
        public EndStation(BeamStatusObservables.EndStation endStation) {
			beam = adaptEnum(endStation.beam());
			pps = adaptNumber(endStation.pps());
			beamCurrent = adaptNumber(endStation.beamCurrent());
			uAHToday = adaptNumber(endStation.uAHToday());
			lastBeamOff = adapt(endStation.lastBeamOff());
			lastBeamOn = adapt(endStation.lastBeamOn());
		}
		
		public UpdatedValue<String> beam() { 
			return beam; }
		public UpdatedValue<String> pps() { 
			return pps; }
		public UpdatedValue<String> beamCurrent() { 
			return beamCurrent; }
		public UpdatedValue<String> uAHToday() { 
			return uAHToday; }
		public UpdatedValue<String> lastBeamOff() { 
			return lastBeamOff; }
		public UpdatedValue<String> lastBeamOn() { 
			return lastBeamOn; }
	}

	private UpdatedValue<String> adaptNumber(ForwardingObservable<Number> observable) {
		return adapt(convert(observable, new NumberConverter()));
	}

	private <E extends Enum<E>> UpdatedValue<String> adaptEnum(ForwardingObservable<E> observable) {
		return adapt(convert(observable, new EnumConverter<E>()));
	}
}
