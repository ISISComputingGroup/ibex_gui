
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

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.ElapsedTimeChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.FloatArrayChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.IntegerChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

public class DaeObservables extends InstrumentVariables {
	
	private static final PVAddress DAE = PVAddress.startWith("DAE");
	
	public final InitialiseOnSubscribeObservable<String> instrumentName = reader(new StringChannel(), DAE.endWith("INSTNAME"));
	public final InitialiseOnSubscribeObservable<DaeRunState> runState = reader(new EnumChannel<>(DaeRunState.class), DAE.endWith("RUNSTATE_STR"));
	public final InitialiseOnSubscribeObservable<String> runNumber = reader(new StringChannel(), DAE.endWith("RUNNUMBER"));
	public final InitialiseOnSubscribeObservable<String> title = reader(new CharWaveformChannel(), DAE.endWith("TITLE"));
	public final InitialiseOnSubscribeObservable<String> users = reader(new CharWaveformChannel(), DAE.endWith("USERNAME"));
	
	public final InitialiseOnSubscribeObservable<Integer> goodFrames = reader(new IntegerChannel(), DAE.endWith("GOODFRAMES"));
	public final InitialiseOnSubscribeObservable<Integer> rawFrames = reader(new IntegerChannel(), DAE.endWith("RAWFRAMES"));
	public final InitialiseOnSubscribeObservable<Integer> monitorCounts = reader(new IntegerChannel(), DAE.endWith("MONITORCOUNTS"));
	public final InitialiseOnSubscribeObservable<Number> goodCurrent = reader(new NumberChannel(), DAE.endWith("GOODUAH"));

	public final InitialiseOnSubscribeObservable<String> runTime = reader(new ElapsedTimeChannel(), DAE.endWith("RUNDURATION"));
	public final InitialiseOnSubscribeObservable<Integer> currentPeriod = reader(new IntegerChannel(), DAE.endWith("PERIOD"));
	public final InitialiseOnSubscribeObservable<Integer> totalPeriods = reader(new IntegerChannel(), DAE.endWith("NUMPERIODS"));

	public final InitialiseOnSubscribeObservable<Boolean> inStateTransition = reader(new BooleanChannel(), DAE.endWith("STATETRANS"));
		
	public final InitialiseOnSubscribeObservable<String> daeSettings = reader(new CharWaveformChannel(), DAE.endWith("DAESETTINGS"));
	public final InitialiseOnSubscribeObservable<String> hardwarePeriods = reader(new CharWaveformChannel(), DAE.endWith("HARDWAREPERIODS"));
	public final InitialiseOnSubscribeObservable<String> updateSettings = reader(new CharWaveformChannel(), DAE.endWith("UPDATESETTINGS"));
	public final InitialiseOnSubscribeObservable<String> timeChannelSettings = reader(new CompressedCharWaveformChannel(), DAE.endWith("TCBSETTINGS"));

	public final InitialiseOnSubscribeObservable<String> detectorTables = reader(new CompressedCharWaveformChannel(), DAE.endWith("DETECTORTABLES"));
	public final InitialiseOnSubscribeObservable<String> spectraTables = reader(new CompressedCharWaveformChannel(), DAE.endWith("SPECTRATABLES"));
	public final InitialiseOnSubscribeObservable<String> wiringTables = reader(new CompressedCharWaveformChannel(), DAE.endWith("WIRINGTABLES"));
	public final InitialiseOnSubscribeObservable<String> periodFiles = reader(new CompressedCharWaveformChannel(), DAE.endWith("PERIODFILES"));

	public final InitialiseOnSubscribeObservable<String> vetos = reader(new CharWaveformChannel(), DAE.endWith("VETOSTATUS"));
	
	public final InitialiseOnSubscribeObservable<Number> beamCurrent = reader(new NumberChannel(), DAE.endWith("BEAMCURRENT"));
	public final InitialiseOnSubscribeObservable<Double> totalDaeCounts = reader(new DoubleChannel(), DAE.endWith("TOTALDAECOUNTS"));
	public final InitialiseOnSubscribeObservable<Integer> daeMemoryUsed = reader(new IntegerChannel(), DAE.endWith("DAEMEMORYUSED"));
	public final InitialiseOnSubscribeObservable<String> timingSource = reader(new StringChannel(), DAE.endWith("DAETIMINGSOURCE"));
	public final InitialiseOnSubscribeObservable<String> rbNumber = reader(new StringChannel(), DAE.endWith("RBNUMBER"));
	public final InitialiseOnSubscribeObservable<Double> countRate = reader(new DoubleChannel(), DAE.endWith("COUNTRATE"));
	public final InitialiseOnSubscribeObservable<Double> eventMode = reader(new DoubleChannel(), DAE.endWith("EVENTMODEFRACTION"));
	public final InitialiseOnSubscribeObservable<String> startTime = reader(new StringChannel(), DAE.endWith("STARTTIME"));
	public final InitialiseOnSubscribeObservable<Integer> runDuration = reader(new IntegerChannel(), DAE.endWith("RUNDURATION"));
	public final InitialiseOnSubscribeObservable<Integer> timeChannels = reader(new IntegerChannel(), DAE.endWith("NUMTIMECHANNELS"));
	public final InitialiseOnSubscribeObservable<Integer> spectra = reader(new IntegerChannel(), DAE.endWith("NUMSPECTRA"));
	public final InitialiseOnSubscribeObservable<String> isisCycle = reader(new StringChannel(), DAE.endWith("ISISCYCLE"));
	
	public final InitialiseOnSubscribeObservable<Integer> periodGoodFrames = reader(new IntegerChannel(), DAE.endWith("GOODFRAMES_PD"));
	public final InitialiseOnSubscribeObservable<Integer> periodRawFrames = reader(new IntegerChannel(), DAE.endWith("RAWFRAMES_PD"));
	public final InitialiseOnSubscribeObservable<Integer> periodDuration = reader(new IntegerChannel(), DAE.endWith("RUNDURATION_PD"));
	public final InitialiseOnSubscribeObservable<String> periodType = reader(new StringChannel(), DAE.endWith("PERIODTYPE"));
	public final InitialiseOnSubscribeObservable<Integer> periodSequence = reader(new IntegerChannel(), DAE.endWith("PERIODSEQ"));

	public final InitialiseOnSubscribeObservable<Integer> monitorSpectrum = reader(new IntegerChannel(), DAE.endWith("MONITORSPECTRUM"));
	public final InitialiseOnSubscribeObservable<Double> monitorFrom = reader(new DoubleChannel(), DAE.endWith("MONITORFROM"));
	public final InitialiseOnSubscribeObservable<Double> monitorTo = reader(new DoubleChannel(), DAE.endWith("MONITORTO"));
	public final InitialiseOnSubscribeObservable<Double> npRatio = reader(new DoubleChannel(), DAE.endWith("NPRATIO"));

	public DaeObservables(Channels channels) {
		super(channels);
	}
	
    public InitialiseOnSubscribeObservable<float[]> spectrumXData(int number, int period) {
        return reader(new FloatArrayChannel(), spectrumData(number, period, "X"));
	}
	
    public InitialiseOnSubscribeObservable<Integer> spectrumXDataLength(int number, int period) {
        return reader(new IntegerChannel(), spectrumDataLength(number, period, "X"));
    }

    public InitialiseOnSubscribeObservable<float[]> spectrumYData(int number, int period) {
        return reader(new FloatArrayChannel(), spectrumData(number, period, "Y"));
	}
	
    public InitialiseOnSubscribeObservable<Integer> spectrumYDataLength(int number, int period) {
        return reader(new IntegerChannel(), spectrumDataLength(number, period, "Y"));
    }

	private static String spectrumData(int spectrum, int period, String axis) {
		return String.format("DAE:SPEC:%d:%d:%s", period, spectrum, axis);	
	}

    private static String spectrumDataLength(int spectrum, int period, String axis) {
        return spectrumData(spectrum, period, axis) + ".NORD";
    }
}
