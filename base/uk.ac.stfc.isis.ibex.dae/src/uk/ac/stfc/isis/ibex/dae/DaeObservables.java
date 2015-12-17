
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
import uk.ac.stfc.isis.ibex.instrument.Instrument;
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

/**
 * Holds the Observables for the DAE.
 */
public class DaeObservables {

    private static final PVAddress DAE = PVAddress.startWith("DAE");
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

    public final InitialiseOnSubscribeObservable<String> instrumentName;
    public final InitialiseOnSubscribeObservable<DaeRunState> runState;
    public final InitialiseOnSubscribeObservable<String> runNumber;
    public final InitialiseOnSubscribeObservable<String> title;
    public final InitialiseOnSubscribeObservable<String> users;
    public final InitialiseOnSubscribeObservable<Integer> goodFrames;
    public final InitialiseOnSubscribeObservable<Integer> rawFrames;
    public final InitialiseOnSubscribeObservable<Integer> monitorCounts;
    public final InitialiseOnSubscribeObservable<Number> goodCurrent;
    public final InitialiseOnSubscribeObservable<String> runTime;
    public final InitialiseOnSubscribeObservable<Integer> currentPeriod;
    public final InitialiseOnSubscribeObservable<Integer> totalPeriods;
    public final InitialiseOnSubscribeObservable<Boolean> inStateTransition;
    public final InitialiseOnSubscribeObservable<String> daeSettings;
    public final InitialiseOnSubscribeObservable<String> hardwarePeriods;
    public final InitialiseOnSubscribeObservable<String> updateSettings;
    public final InitialiseOnSubscribeObservable<String> timeChannelSettings;
    public final InitialiseOnSubscribeObservable<String> detectorTables;
    public final InitialiseOnSubscribeObservable<String> spectraTables;
    public final InitialiseOnSubscribeObservable<String> wiringTables;
    public final InitialiseOnSubscribeObservable<String> periodFiles;
    public final InitialiseOnSubscribeObservable<String> vetos;
    public final InitialiseOnSubscribeObservable<Number> beamCurrent;
    public final InitialiseOnSubscribeObservable<Double> totalDaeCounts;
    public final InitialiseOnSubscribeObservable<Integer> daeMemoryUsed;
    public final InitialiseOnSubscribeObservable<String> timingSource;
    public final InitialiseOnSubscribeObservable<String> rbNumber;
    public final InitialiseOnSubscribeObservable<Double> countRate;
    public final InitialiseOnSubscribeObservable<Double> eventMode;
    public final InitialiseOnSubscribeObservable<String> startTime;
    public final InitialiseOnSubscribeObservable<Integer> runDuration;
    public final InitialiseOnSubscribeObservable<Integer> timeChannels;
    public final InitialiseOnSubscribeObservable<Integer> spectra;
    public final InitialiseOnSubscribeObservable<String> isisCycle;
    public final InitialiseOnSubscribeObservable<Integer> periodGoodFrames;
    public final InitialiseOnSubscribeObservable<Integer> periodRawFrames;
    public final InitialiseOnSubscribeObservable<Integer> periodDuration;
    public final InitialiseOnSubscribeObservable<String> periodType;
    public final InitialiseOnSubscribeObservable<Integer> periodSequence;
    public final InitialiseOnSubscribeObservable<Integer> monitorSpectrum;
    public final InitialiseOnSubscribeObservable<Double> monitorFrom;
    public final InitialiseOnSubscribeObservable<Double> monitorTo;
    public final InitialiseOnSubscribeObservable<Double> npRatio;

    public DaeObservables() {
        instrumentName = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("INSTNAME")));
        runState = obsFactory.getSwitchableObservable(new EnumChannel<>(DaeRunState.class),
                addPrefix(DAE.endWith("RUNSTATE_STR")));
        runNumber = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("RUNNUMBER")));
        title = obsFactory.getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("TITLE")));
        users = obsFactory.getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("USERNAME")));
        goodFrames = obsFactory.getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("GOODFRAMES")));
        rawFrames = obsFactory.getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("RAWFRAMES")));
        monitorCounts = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("MONITORCOUNTS")));
        goodCurrent = obsFactory.getSwitchableObservable(new NumberChannel(), addPrefix(DAE.endWith("GOODUAH")));
        runTime = obsFactory.getSwitchableObservable(new ElapsedTimeChannel(), addPrefix(DAE.endWith("RUNDURATION")));
        currentPeriod = obsFactory.getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("PERIOD")));
        totalPeriods = obsFactory.getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("NUMPERIODS")));
        inStateTransition = obsFactory.getSwitchableObservable(new BooleanChannel(),
                addPrefix(DAE.endWith("STATETRANS")));
        daeSettings = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                addPrefix(DAE.endWith("DAESETTINGS")));
        hardwarePeriods = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                addPrefix(DAE.endWith("HARDWAREPERIODS")));
        updateSettings = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                addPrefix(DAE.endWith("UPDATESETTINGS")));
        timeChannelSettings = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix(DAE.endWith("TCBSETTINGS")));
        detectorTables = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix(DAE.endWith("DETECTORTABLES")));
        spectraTables = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix(DAE.endWith("SPECTRATABLES")));
        wiringTables = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix(DAE.endWith("WIRINGTABLES")));
        periodFiles = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix(DAE.endWith("PERIODFILES")));
        vetos = obsFactory.getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("VETOSTATUS")));
        beamCurrent = obsFactory.getSwitchableObservable(new NumberChannel(), addPrefix(DAE.endWith("BEAMCURRENT")));
        totalDaeCounts = obsFactory.getSwitchableObservable(new DoubleChannel(),
                addPrefix(DAE.endWith("TOTALDAECOUNTS")));
        daeMemoryUsed = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("DAEMEMORYUSED")));
        timingSource = obsFactory.getSwitchableObservable(new StringChannel(),
                addPrefix(DAE.endWith("DAETIMINGSOURCE")));
        rbNumber = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("RBNUMBER")));
        countRate = obsFactory.getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("COUNTRATE")));
        eventMode = obsFactory.getSwitchableObservable(new DoubleChannel(),
                addPrefix(DAE.endWith("EVENTMODEFRACTION")));
        startTime = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("STARTTIME")));
        runDuration = obsFactory.getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("RUNDURATION")));
        timeChannels = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("NUMTIMECHANNELS")));
        spectra = obsFactory.getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("NUMSPECTRA")));
        isisCycle = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("ISISCYCLE")));
        periodGoodFrames = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("GOODFRAMES_PD")));
        periodRawFrames = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("RAWFRAMES_PD")));
        periodDuration = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("RUNDURATION_PD")));
        periodType = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("PERIODTYPE")));
        periodSequence = obsFactory.getSwitchableObservable(new IntegerChannel(), addPrefix(DAE.endWith("PERIODSEQ")));
        monitorSpectrum = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("MONITORSPECTRUM")));
        monitorFrom = obsFactory.getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("MONITORFROM")));
        monitorTo = obsFactory.getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("MONITORTO")));
        npRatio = obsFactory.getSwitchableObservable(new DoubleChannel(), addPrefix(DAE.endWith("NPRATIO")));
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
