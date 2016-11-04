
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

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
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

    public final ForwardingObservable<String> instrumentName;
    public final ForwardingObservable<DaeRunState> runState;
    public final ForwardingObservable<String> runNumber;
    public final ForwardingObservable<String> title;
    public final ForwardingObservable<Boolean> displayTitle;
    public final ForwardingObservable<String> users;
    public final ForwardingObservable<Integer> goodFrames;
    public final ForwardingObservable<Integer> rawFrames;
    public final ForwardingObservable<Integer> monitorCounts;
    public final ForwardingObservable<Number> goodCurrent;
    public final ForwardingObservable<String> runTime;
    public final ForwardingObservable<Integer> currentPeriod;
    public final ForwardingObservable<Integer> totalPeriods;
    public final ForwardingObservable<Boolean> inStateTransition;
    public final ForwardingObservable<String> daeSettings;
    public final ForwardingObservable<String> hardwarePeriods;
    public final ForwardingObservable<String> updateSettings;
    public final ForwardingObservable<String> timeChannelSettings;
    public final ForwardingObservable<String> detectorTables;
    public final ForwardingObservable<String> spectraTables;
    public final ForwardingObservable<String> wiringTables;
    public final ForwardingObservable<String> periodFiles;
    public final ForwardingObservable<String> timeChannelFiles;
    public final ForwardingObservable<String> vetos;
    public final ForwardingObservable<Number> beamCurrent;
    public final ForwardingObservable<Double> totalDaeCounts;
    public final ForwardingObservable<Integer> daeMemoryUsed;
    public final ForwardingObservable<String> timingSource;
    public final ForwardingObservable<String> rbNumber;
    public final ForwardingObservable<Double> countRate;
    public final ForwardingObservable<Double> eventMode;
    public final ForwardingObservable<String> startTime;
    public final ForwardingObservable<Integer> runDuration;
    public final ForwardingObservable<Integer> timeChannels;
    public final ForwardingObservable<Integer> spectra;
    public final ForwardingObservable<String> isisCycle;
    public final ForwardingObservable<Integer> periodGoodFrames;
    public final ForwardingObservable<Integer> periodRawFrames;
    public final ForwardingObservable<Integer> periodDuration;
    public final ForwardingObservable<String> periodType;
    public final ForwardingObservable<Integer> periodSequence;
    public final ForwardingObservable<Integer> monitorSpectrum;
    public final ForwardingObservable<Double> monitorFrom;
    public final ForwardingObservable<Double> monitorTo;
    public final ForwardingObservable<Double> npRatio;

    public DaeObservables() {
        instrumentName = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("INSTNAME")));
        runState = obsFactory.getSwitchableObservable(new EnumChannel<>(DaeRunState.class),
                addPrefix(DAE.endWith("RUNSTATE_STR")));
        runNumber = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("RUNNUMBER")));
        title = obsFactory.getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("TITLE")));
        displayTitle =
                obsFactory.getSwitchableObservable(new BooleanChannel(), addPrefix(DAE.endWith("TITLE:DISPLAY")));
        users = obsFactory.getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("_USERNAME")));
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
        timeChannelFiles = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix(DAE.endWith("TCBFILES")));
        vetos = obsFactory.getSwitchableObservable(new CharWaveformChannel(), addPrefix(DAE.endWith("VETOSTATUS")));
        beamCurrent = obsFactory.getSwitchableObservable(new NumberChannel(), addPrefix(DAE.endWith("BEAMCURRENT")));
        totalDaeCounts = obsFactory.getSwitchableObservable(new DoubleChannel(),
                addPrefix(DAE.endWith("TOTALDAECOUNTS")));
        daeMemoryUsed = obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(DAE.endWith("DAEMEMORYUSED")));
        timingSource = obsFactory.getSwitchableObservable(new StringChannel(),
                addPrefix(DAE.endWith("DAETIMINGSOURCE")));
        rbNumber = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(DAE.endWith("_RBNUMBER")));
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

    public ForwardingObservable<float[]> spectrumXData(int number, int period) {
        return obsFactory.getSwitchableObservable(new FloatArrayChannel(),
                addPrefix(spectrumData(number, period, "X")));
    }

    public ForwardingObservable<Integer> spectrumXDataLength(int number, int period) {
        return obsFactory.getSwitchableObservable(new IntegerChannel(),
                addPrefix(spectrumDataLength(number, period, "X")));
    }

    public ForwardingObservable<float[]> spectrumYData(int number, int period) {
        return obsFactory.getSwitchableObservable(new FloatArrayChannel(),
                addPrefix(spectrumData(number, period, "Y")));
    }

    public ForwardingObservable<Integer> spectrumYDataLength(int number, int period) {
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
        StringBuilder sb = new StringBuilder();
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append(address);
        return sb.toString();
    }
}
