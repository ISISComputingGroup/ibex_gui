
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
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
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
    private ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
	
    public final InitialiseOnSubscribeObservable<String> instrumentName = obsFactory.getSwitchableObservable(
new StringChannel(), addPrefix(DAE.endWith("INSTNAME")));
    public final InitialiseOnSubscribeObservable<DaeRunState> runState = obsFactory
            .getSwitchableObservable(new EnumChannel<>(DaeRunState.class), addPrefix(DAE.endWith("RUNSTATE_STR")));
    public final InitialiseOnSubscribeObservable<String> runNumber = obsFactory
            .getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("RUNNUMBER")));
    public final InitialiseOnSubscribeObservable<String> title = obsFactory
            .getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("TITLE")));
    public final InitialiseOnSubscribeObservable<String> users = obsFactory
            .getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("USERNAME")));
	
    public final InitialiseOnSubscribeObservable<Integer> goodFrames = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("GOODFRAMES")));
    public final InitialiseOnSubscribeObservable<Integer> rawFrames = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("RAWFRAMES")));
    public final InitialiseOnSubscribeObservable<Integer> monitorCounts = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("MONITORCOUNTS")));
    public final InitialiseOnSubscribeObservable<Number> goodCurrent = obsFactory
            .getSwitchableObservable(new NumberChannel(), addPrefix(DAE.endWith("GOODUAH")));

    public final InitialiseOnSubscribeObservable<String> runTime = obsFactory
            .getSwitchableObservable(new ElapsedTimeChannel(), addPrefix(DAE.endWith("RUNDURATION")));
    public final InitialiseOnSubscribeObservable<Integer> currentPeriod = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("PERIOD")));
    public final InitialiseOnSubscribeObservable<Integer> totalPeriods = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("NUMPERIODS")));

    public final InitialiseOnSubscribeObservable<Boolean> inStateTransition = obsFactory
            .getSwitchableObservable(new BooleanChannel(), addPrefix(DAE.endWith("STATETRANS")));
		
    public final InitialiseOnSubscribeObservable<String> daeSettings = obsFactory
            .getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("DAESETTINGS")));
    public final InitialiseOnSubscribeObservable<String> hardwarePeriods = obsFactory
            .getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("HARDWAREPERIODS")));
    public final InitialiseOnSubscribeObservable<String> updateSettings = obsFactory
            .getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("UPDATESETTINGS")));
    public final InitialiseOnSubscribeObservable<String> timeChannelSettings = obsFactory
            .getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(DAE.endWith("TCBSETTINGS")));

    public final InitialiseOnSubscribeObservable<String> detectorTables = obsFactory
            .getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(DAE.endWith("DETECTORTABLES")));
    public final InitialiseOnSubscribeObservable<String> spectraTables = obsFactory
            .getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(DAE.endWith("SPECTRATABLES")));
    public final InitialiseOnSubscribeObservable<String> wiringTables = obsFactory
            .getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(DAE.endWith("WIRINGTABLES")));
    public final InitialiseOnSubscribeObservable<String> periodFiles = obsFactory
            .getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(DAE.endWith("PERIODFILES")));

    public final InitialiseOnSubscribeObservable<String> vetos = obsFactory
            .getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("VETOSTATUS")));
	
    public final InitialiseOnSubscribeObservable<Number> beamCurrent = obsFactory
            .getSwitchableObservable(new NumberChannel(), addPrefix(DAE.endWith("BEAMCURRENT")));
    public final InitialiseOnSubscribeObservable<Double> totalDaeCounts = obsFactory
            .getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("TOTALDAECOUNTS")));
    public final InitialiseOnSubscribeObservable<Integer> daeMemoryUsed = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("DAEMEMORYUSED")));
    public final InitialiseOnSubscribeObservable<String> timingSource = obsFactory
            .getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("DAETIMINGSOURCE")));
    public final InitialiseOnSubscribeObservable<String> rbNumber = obsFactory
            .getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("RBNUMBER")));
    public final InitialiseOnSubscribeObservable<Double> countRate = obsFactory
            .getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("COUNTRATE")));
    public final InitialiseOnSubscribeObservable<Double> eventMode = obsFactory
            .getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("EVENTMODEFRACTION")));
    public final InitialiseOnSubscribeObservable<String> startTime = obsFactory
            .getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("STARTTIME")));
    public final InitialiseOnSubscribeObservable<Integer> runDuration = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("RUNDURATION")));
    public final InitialiseOnSubscribeObservable<Integer> timeChannels = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("NUMTIMECHANNELS")));
    public final InitialiseOnSubscribeObservable<Integer> spectra = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("NUMSPECTRA")));
    public final InitialiseOnSubscribeObservable<String> isisCycle = obsFactory
            .getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("ISISCYCLE")));
	
    public final InitialiseOnSubscribeObservable<Integer> periodGoodFrames = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("GOODFRAMES_PD")));
    public final InitialiseOnSubscribeObservable<Integer> periodRawFrames = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("RAWFRAMES_PD")));
    public final InitialiseOnSubscribeObservable<Integer> periodDuration = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("RUNDURATION_PD")));
    public final InitialiseOnSubscribeObservable<String> periodType = obsFactory
            .getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("PERIODTYPE")));
    public final InitialiseOnSubscribeObservable<Integer> periodSequence = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("PERIODSEQ")));

    public final InitialiseOnSubscribeObservable<Integer> monitorSpectrum = obsFactory
            .getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("MONITORSPECTRUM")));
    public final InitialiseOnSubscribeObservable<Double> monitorFrom = obsFactory
            .getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("MONITORFROM")));
    public final InitialiseOnSubscribeObservable<Double> monitorTo = obsFactory
            .getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("MONITORTO")));
    public final InitialiseOnSubscribeObservable<Double> npRatio = obsFactory
            .getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("NPRATIO")));

	public DaeObservables(Channels channels) {
		super(channels);
	}
	
    public InitialiseOnSubscribeObservable<float[]> spectrumXData(int number, int period) {
        return obsFactory.getSwitchableObservable(new FloatArrayChannel(),
                addPrefix(spectrumData(number, period, "X")));
	}
	
    public InitialiseOnSubscribeObservable<Integer> spectrumXDataLength(int number, int period) {
        return obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(spectrumDataLength(number, period, "X")));
    }

    public InitialiseOnSubscribeObservable<float[]> spectrumYData(int number, int period) {
        return obsFactory.getSwitchableObservable(new FloatArrayChannel(),
                addPrefix(spectrumData(number, period, "Y")));
	}
	
    public InitialiseOnSubscribeObservable<Integer> spectrumYDataLength(int number, int period) {
        return obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(spectrumDataLength(number, period, "Y")));
    }

	private static String spectrumData(int spectrum, int period, String axis) {
		return String.format("DAE:SPEC:%d:%d:%s", period, spectrum, axis);	
	}

    private static String spectrumDataLength(int spectrum, int period, String axis) {
        return spectrumData(spectrum, period, axis) + ".NORD";
    }

    private String addPrefix(String address) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append(address);
        return sb.toString();
    }
}
