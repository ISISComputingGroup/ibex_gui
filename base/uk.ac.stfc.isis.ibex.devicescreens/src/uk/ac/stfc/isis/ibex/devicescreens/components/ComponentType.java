/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.devicescreens.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * The type of components available to the synoptic.
 * Basically, the things that appear on a beam-line.
 */
public enum ComponentType {

    /** Unknown component type. */
    UNKNOWN,
    /** Debug component type. */
    DEBUG,
    /** Jaws component type. */
    JAWS,
    /** CP2800 Helum compressor component type. */
    CP2800,
    /** Chopper component type. */
    CHOPPER,
    /** Fermi Chopper component type. */
    FERMI_CHOPPER,
    /** T0 Chopper component type. */
    T0_CHOPPER,
    /** Monitor component type. */
    MONITOR,
    /** Sample stack component type. */
    SAMPLESTACK,
    /** DAE component type. */
    DAE,
    /** Danfysik component type. */
    DANFYSIK,
    /** CAEN component type. */
    CAEN,
    /** IPS component type. */
    IPS,
    /** CCD100 component type. */
    CCD100,
    /** Kepco component type. */
    KEPCO,
    /** Lambda Genesys component type. */
    TDK_LAMBDA_GENESYS,
    /** Beamstop component type. */
    BEAMSTOP, 
    /** Motion set points for one to three set points. */
    MOTION_SET_POINTS_FEW,
    /** Moving monitor component type. */
    MOVINGMONITOR,
    /** Rotating bench component type. */
    ROTATINGBENCH,
    /** Sample changer component type. */
    SAMPLECHANGER,
    /** Linear sample changer component type. */
    LINEARSAMPLECHANGER,
    /** Moving beam stop component type. */
    MOVINGBEAMSTOP,
    /** Analyser component type. */
    ANALYSER,
    /** Polariser component type. */
    POLARISER,
    /** Julabo component type. */
    JULABO,	
    /** Eurotherm component type. */
    EUROTHERM,
    /** Pinhole selector component type. */
    PINHOLESELECTOR,
    /** Single aperture component type. */
    SINGLE_APERTURE,
    /** Vertical aperture component type. */
    VERTICAL_APERTURE_STRIP,
    /** Goniometer component type. */
    GONIOMETER,
    /** Single stage component type. */
    SINGLESTAGE,
    /** Linkam95 component type. */
    LINKAM95,
    /** Lakeshore component type. */
    LAKESHORE,
    /** Lakeshore 460 component type. */
    LKSH460,
    /** Attenuator component type. */
    ATTENUATOR,
    /** Mercury component type. */
    MERCURY,
    /** Pressure transducer PDR2000 component type. */
    PDR2000,
    /** Iris cryo valve component type. */
    CRYVALVE,
    /** Muon Front End component type. */
    MUON_FRONT_END,
    /** Pressure gauge component type. */
    PRESSURE_GAUGE,
    /** 3D magnet component type. */
    SCIMAG3D,
    /** Stress rig component type. */
    STRESS_RIG,
    /** Gas exchange system. */
    GAS_EXCHANGE,
    /** Polarises, Guide and Collimation for MUONFE. */
    PGC,
    /** Detector motion system component type. */
    DETECTOR_MOTION_SYSTEM,
    /** Pixelman camera. */
    PIXELMAN,
    /** Neocera temperature controller. */
    NEOCERA,
    /** Keithley 2400 Source Meter. */
    KHLY2400,
    /** Keithley 2700 source meter. */
    KHLY2700,
    /** Keithley 2290. **/
    KHLY2290,
    /** Keysight E4980AL. **/
    KSE4980,
    /** RIKEN Vacuum. **/
    RIKEN,
    /** RIKEN PSU Status Summary. **/
    PSU_STATUS_SUMMARY,
    /** HTS Magnet. **/
    HTS_MAGNET,
    /** B17TMAG. **/
    B17TMAG,
    /** HRPD In Situ DSC. **/
    DSC,
    /** PEARLPC. **/
    PEARLPC,
    /** NMR Teslameter PT2025. **/
    TESLAMETER,
    /** Omron PLC. */
    PLC,
    /** Zoom sample stack. */
    ZOOM_SAMPLE_STACK,
    /** Sample changer for HRPD/POLARIS/GEM. */
    ROT_SAMPLE_CHANGER,
    /** Cryogenics Level Gauge.*/
    CRYO_LEVEL_GAUGE,
    /** LET oscillating collimator. */
    OSCILLATING_COLLIMATOR,
    /** SM300 sample changer.*/
    SM300_SAMPLE_CHANGER,
    /** Dilution fridge.*/
    DILUTION_FRIDGE,
    /** Couette cell.*/
    COUETTE,
    /** Motion setpoints.*/
    MOTION_SET_POINTS,
    /**Syringe pump.*/
    SYRINGE_PUMP,
    /** Keyence micrometer. */
    KEYENCE,
    /** Generic Power Supply. */
    POWER_SUPPLY,
    /** Oscilloscope. */
    OSCILLOSCOPE,
    /** Moxa ioLogik e12XX Remote I/O. */
    MOXA_12XX,
    /** Furnace temperature controller. */
    FURNACE,
    /** Generic Multimeter. */
    MULTIMETER,
    /** Generic Keithley Multimeter. **/
    KEITHLEY,
    /** Ocean Optics DH2000 shutter control. */
    DH2000,
    /** Generic Pump. */
    PUMP,
    /** Water bath position valve. */
    WATER_BATH_VALVE,
    /** Reflectometry Front Panel. */
    REFLECTOMETRY,
    /** Generic sensor. **/
    SENSOR,
    /** Thin film fabrication device. **/
    FILM_FABRICATION,
    /** Coherent OBIS Laser Remote. */
    OBIS_LASER_REMOTE,
    /** Zero field. */
    ZERO_FIELD,
    /** LSI correlator. */
    LSICORR,
    /** Density meter (e.g. DMA4500M) */
    DENSITY_METER,
    /** guide and aperture. **/
    GUIDE_APERTURE,
    /**Spin flipper.*/
    SPIN_FLIPPER,
    /** SANS2D Vacuum Tank. */
    VACUUM_TANK,
    /** Helium Level Gauge. */
    HE_LEVEL_GAUGE,
    /** Musr Steering Magnets. */
    MUSR_STEERING,
    /** Delay generators. */
    DELAY_GENERATORS,
    /** Function generators. */
    FUNCTION_GENERATOR,
    /** STANDARD MAGNET. */
    MAGNET,
    /** Counters. */
    COUNTER,
	/** PHOTON COUNTER. */
	PHOTON_COUNTER;
	

    private Target target;

    /**
     * Instantiates a new component type.
     */
    ComponentType() {
    }

    /**
     * Instantiates a new component type based on a target.
     *
     * @param target the target
     */
    ComponentType(Target target) {
	this.target = target;
    }

    /**
     * Gets the target of this component type .
     *
     * @return the target
     */
    public Target target() {
	return target;
    }

    /**
     * @return a string list of the enum entries
     */
    public static List<String> componentTypeList() {
	ComponentType[] types = values();
	List<String> list = new ArrayList<>();

	for (int i = 0; i < types.length; i++) {
	    list.add(types[i].name());
	}

	return list;
    }

    /**
     * @return an alphabetised list of the enum entries
     */
    public static List<String> componentTypeAlphaList() {
	List<String> list = componentTypeList();

	// remove unknown
	list.remove(ComponentType.UNKNOWN.toString());
	Collections.sort(list);

	// add unknown back at the beginning
	list.add(0, ComponentType.UNKNOWN.toString());
	return list;
    }
}
