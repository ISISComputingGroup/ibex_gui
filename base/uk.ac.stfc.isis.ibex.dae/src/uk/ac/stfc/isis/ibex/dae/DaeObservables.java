
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
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
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

    /**
     * An observable on the name of the instrument.
     */
    public final ForwardingObservable<String> instrumentName;

    /**
     * An observable on the name of the instrument.
     */
    public final ForwardingObservable<String> periodFilesDir;

    /**
     * An observable on the name of the instrument.
     */
    public final ForwardingObservable<String> timeChannelsDir;

    /**
     * An observable on the name of the instrument.
     */
    public final ForwardingObservable<String> wiringTablesDir;

    /**
     * An observable on the name of the instrument.
     */
    public final ForwardingObservable<String> detectorTablesDir;

    /**
     * An observable on the name of the instrument.
     */
    public final ForwardingObservable<String> spectraTablesDir;

    /**
     * An observable on the status of the run.
     */
    public final ForwardingObservable<DaeRunState> runState;

    /**
     * An observable on the run number.
     */
    public final ForwardingObservable<String> runNumber;

    /**
     * An observable on the title for the current run.
     */
    public final ForwardingObservable<String> title;

    /**
     * An observable giving whether or not to display the title on the webpage.
     */
    public final ForwardingObservable<Boolean> displayTitle;

    /**
     * An observable on the users of the instrument for the current run.
     */
    public final ForwardingObservable<String> users;

    /**
     * An observable on the number of good frames for this run.
     */
    public final ForwardingObservable<Integer> goodFrames;

    /**
     * An observable on the number of raw frames for this run.
     */
    public final ForwardingObservable<Integer> rawFrames;

    /**
     * An observable on the monitor counts for the run.
     */
    public final ForwardingObservable<Integer> monitorCounts;

    /**
     * An observable on the good current for this run.
     */
    public final ForwardingObservable<Number> goodCurrent;

    /**
     * An observable on the length of time that the current run has been going.
     */
    public final ForwardingObservable<String> runTime;

    /**
     * An observable on the current period of the run.
     */
    public final ForwardingObservable<Integer> currentPeriod;

    /**
     * An observable on the total periods in the run.
     */
    public final ForwardingObservable<Integer> totalPeriods;

    /**
     * An observable on whether the DAE is currently transitioning between
     * states.
     */
    public final ForwardingObservable<Boolean> inStateTransition;

    /**
     * An observable for the DAE settings data.
     */
    public final ForwardingObservable<String> daeSettings;

    /**
     * An observable for the hardware period settings data.
     */
    public final ForwardingObservable<String> hardwarePeriods;

    /**
     * An observable for the PV for updating setting on the DAE.
     */
    public final ForwardingObservable<String> updateSettings;

    /**
     * An observable for the time channel settings data.
     */
    public final ForwardingObservable<String> timeChannelSettings;

    /**
     * An observable for the detector table data.
     */
    public final ForwardingObservable<String> detectorTables;

    /**
     * An observable for the spectra tables data.
     */
    public final ForwardingObservable<String> spectraTables;

    /**
     * An observable for the wiring tables data.
     */
    public final ForwardingObservable<String> wiringTables;

    /**
     * An observable for the list of period files available.
     */
    public final ForwardingObservable<String> periodFiles;

    /**
     * An observable for the list of time channel files available.
     */
    public final ForwardingObservable<String> timeChannelFiles;

    /**
     * An observable for the veto information.
     */
    public final ForwardingObservable<String> vetos;

    /**
     * An observable on a number giving the current beam current.
     */
    public final ForwardingObservable<Number> beamCurrent;

    /**
     * An observable on the number of counts for this run.
     */
    public final ForwardingObservable<Double> totalDaeCounts;

    /**
     * An observable on the memory used on the DAE.
     */
    public final ForwardingObservable<Integer> daeMemoryUsed;

    /**
     * An observable on the timing source being used by the DAE.
     */
    public final ForwardingObservable<String> timingSource;

    /**
     * An observable on the current RB number.
     */
    public final ForwardingObservable<String> rbNumber;

    /**
     * An observable giving the count rate of the DAE.
     */
    public final ForwardingObservable<Double> countRate;

    /**
     * An observable giving the event mode of the DAE.
     */
    public final ForwardingObservable<Double> eventMode;

    /**
     * An observable on the start time of the current run.
     */
    public final ForwardingObservable<String> startTime;

    /**
     * An observable giving the duration of the current run in seconds.
     */
    public final ForwardingObservable<Integer> runDuration;

    /**
     * An observable giving the number of time channels.
     */
    public final ForwardingObservable<Integer> timeChannels;

    /**
     * An observable giving the number of spectra.
     */
    public final ForwardingObservable<Integer> spectra;

    /**
     * An observable on the cycle number.
     */
    public final ForwardingObservable<String> isisCycle;

    /**
     * An observable giving the number of good frames in the current period.
     */
    public final ForwardingObservable<Integer> periodGoodFrames;

    /**
     * An observable giving the number of raw frames in the current period.
     */
    public final ForwardingObservable<Integer> periodRawFrames;

    /**
     * An observable on the duration of the current period in seconds.
     */
    public final ForwardingObservable<Integer> periodDuration;

    /**
     * An observable on the period type (e.g. soft periods).
     */
    public final ForwardingObservable<String> periodType;

    /**
     * An observable on the period sequence.
     */
    public final ForwardingObservable<Integer> periodSequence;

    /**
     * An observable on the selected monitor spectrum.
     */
    public final ForwardingObservable<Integer> monitorSpectrum;

    /**
     * An observable on the lower time limit on the monitor.
     */
    public final ForwardingObservable<Double> monitorFrom;

    /**
     * An observable on the upper time limit on the monitor.
     */
    public final ForwardingObservable<Double> monitorTo;

    /**
     * An observable on the neutron proton ratio.
     */
    public final ForwardingObservable<Double> npRatio;

    /**
     * The default constructor for the class. Binds the observables to PVs.
     */
    public DaeObservables() {
        instrumentName = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("INSTNAME")));
        runState = obsFactory.getSwitchableObservable(new EnumChannel<>(DaeRunState.class),
                InstrumentUtils.addPrefix(DAE.endWith("RUNSTATE")));
        runNumber = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("RUNNUMBER")));
        title = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("TITLE")));
        displayTitle =
                obsFactory.getSwitchableObservable(new BooleanChannel(),
                        InstrumentUtils.addPrefix(DAE.endWith("TITLE:DISPLAY")));
        users = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("_USERNAME")));
        goodFrames = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("GOODFRAMES")));
        rawFrames = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("RAWFRAMES")));
        monitorCounts = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("MONITORCOUNTS")));
        goodCurrent = obsFactory.getSwitchableObservable(new NumberChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("GOODUAH")));
        runTime = obsFactory.getSwitchableObservable(new ElapsedTimeChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("RUNDURATION")));
        currentPeriod = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("PERIOD")));
        totalPeriods = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("NUMPERIODS")));
        inStateTransition = obsFactory.getSwitchableObservable(new BooleanChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("STATETRANS")));
        daeSettings = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("DAESETTINGS")));
        hardwarePeriods = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("HARDWAREPERIODS")));
        updateSettings = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("UPDATESETTINGS")));
        timeChannelSettings = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("TCBSETTINGS")));
        detectorTables = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("DETECTORTABLES")));
        spectraTables = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("SPECTRATABLES")));
        wiringTables = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("WIRINGTABLES")));
        periodFiles = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("PERIODFILES")));
        timeChannelFiles = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("TCBFILES")));
        vetos = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("VETOSTATUS")));
        beamCurrent = obsFactory.getSwitchableObservable(new NumberChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("BEAMCURRENT")));
        totalDaeCounts = obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("TOTALDAECOUNTS")));
        daeMemoryUsed = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("DAEMEMORYUSED")));
        timingSource = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("DAETIMINGSOURCE")));
        rbNumber = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("_RBNUMBER")));
        countRate = obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("COUNTRATE")));
        eventMode = obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("EVENTMODEFRACTION")));
        startTime = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("STARTTIME")));
        runDuration = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("RUNDURATION")));
        timeChannels = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("NUMTIMECHANNELS")));
        spectra = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("NUMSPECTRA")));
        isisCycle = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("ISISCYCLE")));
        periodGoodFrames = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("GOODFRAMES_PD")));
        periodRawFrames = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("RAWFRAMES_PD")));
        periodDuration = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("RUNDURATION_PD")));
        periodType = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("PERIODTYPE")));
        periodSequence = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("PERIODSEQ")));
        monitorSpectrum = obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("MONITORSPECTRUM")));
        monitorFrom = obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("MONITORFROM")));
        monitorTo = obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("MONITORTO")));
        npRatio = obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("NPRATIO")));
        periodFilesDir = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("PERIOD_DIR")));
        timeChannelsDir = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("TCB_DIR")));
        wiringTablesDir = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("WIRING_DIR")));
        detectorTablesDir = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("DETECTOR_DIR")));
        spectraTablesDir = obsFactory.getSwitchableObservable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix(DAE.endWith("SPECTRA_DIR")));
    }

    /**
     * Gets the X data for a given DAE spectrum.
     * 
     * @param number
     *            The spectrum number
     * @param period
     *            The period number
     * @return The X data points related to the specified spectrum
     */
    public ForwardingObservable<float[]> spectrumXData(int number, int period) {
        return obsFactory.getSwitchableObservable(new FloatArrayChannel(),
                InstrumentUtils.addPrefix(spectrumData(number, period, "X")));
    }

    /**
     * Gets the length of the available X data for a given DAE spectrum.
     * 
     * @param number
     *            The spectrum number
     * @param period
     *            The period number
     * @return The number of X data points related to the specified spectrum
     */
    public ForwardingObservable<Integer> spectrumXDataLength(int number, int period) {
        return obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(spectrumDataLength(number, period, "X")));
    }

    /**
     * Gets the Y data for a given DAE spectrum.
     * 
     * @param number
     *            The spectrum number
     * @param period
     *            The period number
     * @return The Y data points related to the specified spectrum
     */
    public ForwardingObservable<float[]> spectrumYData(int number, int period) {
        return obsFactory.getSwitchableObservable(new FloatArrayChannel(),
                InstrumentUtils.addPrefix(spectrumData(number, period, "Y")));
    }

    /**
     * Gets the length of the available Y data for a given DAE spectrum.
     * 
     * @param number
     *            The spectrum number
     * @param period
     *            The period number
     * @return The number of Y data points related to the specified spectrum
     */
    public ForwardingObservable<Integer> spectrumYDataLength(int number, int period) {
        return obsFactory.getSwitchableObservable(new IntegerChannel(),
                InstrumentUtils.addPrefix(spectrumDataLength(number, period, "Y")));
    }

    private static String spectrumData(int spectrum, int period, String axis) {
        return String.format("DAE:SPEC:%d:%d:%s", period, spectrum, axis);
    }

    private static String spectrumDataLength(int spectrum, int period, String axis) {
        return spectrumData(spectrum, period, axis) + ".NORD";
    }
}
