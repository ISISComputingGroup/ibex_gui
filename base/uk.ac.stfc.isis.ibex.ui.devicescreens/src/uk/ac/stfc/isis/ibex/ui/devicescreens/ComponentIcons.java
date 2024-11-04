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

package uk.ac.stfc.isis.ibex.ui.devicescreens;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;

/**
 * Given a component type, returns an icon appropriate for that type.
 */
public final class ComponentIcons {
	
    /**
     * An empty constructor required for check style.
     */
	private ComponentIcons() { }
	
    /**
     * Gets the appropriate icon for a given component type.
     * 
     * @param componentType the component type
     * @return the icon corresponding to the component type
     */
    public static Image iconForType(ComponentType componentType) {
        return icon(getIconNameForType(componentType) + ".png");
    }
    
    /**
     * Gets the appropriate icon name for a given component type.
     * 
     * @param componentType the component type
     * @return the icon name corresponding to the component type
     */
    public static String iconNameForType(ComponentType componentType) {
        return getIconNameForType(componentType) + ".png";
    }

    /**
     * Gets the appropriate thumbnail icon for a given component type.
     * 
     * @param componentType the input component type
     * @return the thumbnail icon corresponding to the component type
     */
    public static Image thumbnailForType(ComponentType componentType) {
        String iconName = getIconNameForType(componentType);
        return icon("thumbs/" + iconName + "_tb.png");
    }
    
    /**
     * Gets the appropriate thumbnail name for a given component type.
     * 
     * @param componentType the input component type
     * @return the thumbnail name corresponding to the component type
     */
    public static String thumbnailNameForType(ComponentType componentType) {
    	String thumbName = getIconNameForType(componentType) + "_tb.png";
    	return thumbName;
    }

    private static Image icon(String fileName) {
    	return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.devicescreens", "icons/components/" + fileName);
    }

    private static String getIconNameForType(ComponentType componentType) {
        switch (componentType) {
            case MOTION_SET_POINTS_FEW:
                return "in_out_motion_set_points";
            case JAWS:
                return "jaws";
            case CHOPPER:
                return "chopper";
            case FERMI_CHOPPER:
                return "fermi_chopper";
            case T0_CHOPPER:
                return "t0_chopper";
            case MONITOR:
                return "monitor";
            case SAMPLESTACK:
            case ZOOM_SAMPLE_STACK:
                return "sample_stack";
            case GONIOMETER:
                return "goniometer";
            case DAE:
                return "dae";
            case DANFYSIK:
            case KEPCO:
            case TDK_LAMBDA_GENESYS:
            case CAEN:
            case IPS:
            case POWER_SUPPLY:
            case PSU_STATUS_SUMMARY:
                return "powersupply";
            case HTS_MAGNET:
            case B17TMAG:
            case MAGNET:
            	return "magnet";
            case TESLAMETER:
            	return "magnet";
            case BEAMSTOP:
                return "beamstop";
            case MOVINGMONITOR:
            case MOVINGBEAMSTOP:
                return "moving_monitor";
            case LINEARSAMPLECHANGER:
            case SAMPLECHANGER:
                return "sample_changer";
            case ANALYSER:
                return "analyser";
            case POLARISER:
                return "polariser";
            case ROTATINGBENCH:
                return "rotating_bench";
            case JULABO:
                return "julabo";
            case EUROTHERM:
                return "euro";
            case PINHOLESELECTOR:
                return "pinhole_selector";
            case SINGLE_APERTURE: 
            	return "single_aperture";
            case VERTICAL_APERTURE_STRIP:
            	return "vertical_aperture";
            case SINGLESTAGE:
                return "single_stage";
            case LINKAM95:
                return "linkam95";
            case ATTENUATOR:
                return "attenuator";
            case MERCURY:
                return "mercury";
            case PDR2000:
                return "pdr2000";
            case CRYVALVE:
                return "iriscryovalve";
            case MUON_FRONT_END:
                return "muon_front_end";
            case LAKESHORE:
                return "lakeshore";
			case LKSH460:
                return "magnet_gaussmeter";
            case CCD100:
            case PRESSURE_GAUGE:
            case PEARLPC:
            case RIKEN:
                return "pressure_gauge";
            case SCIMAG3D:
                return "scimag3D";
            case PGC:
                return "pgc";
            case DETECTOR_MOTION_SYSTEM:
                return "detector_motion_system";
            case PIXELMAN:
                return "pixelman";
            case NEOCERA:
                return "neocera";
            case KEITHLEY:
            case KHLY2700:
            case KHLY2400:
            case KHLY2290:
                return "keithley2400";
            case KSE4980:
            	return "lcr_circuit"; 
            case DSC:
            	return "hrpd_in_situ_dsc";
            case COUNTER:
            case MULTIMETER:
                return "multimeter";
            case ROT_SAMPLE_CHANGER:
                return "rotating_sample_changer";
            case STRESS_RIG:
                return "stress_rig";
            case GAS_EXCHANGE:
                return "gas_exchange";
            case CRYO_LEVEL_GAUGE:
            case CP2800:
                return "cryo_level_gauge";
            case OSCILLATING_COLLIMATOR:
                return "oscillating_collimator";
            case SM300_SAMPLE_CHANGER:
            	return "sm300_sample_changer";
            case MOTION_SET_POINTS:
            	return "motion_set_points";
            case DILUTION_FRIDGE:
            	return "dilution_fridge";
            case COUETTE:
            	return "couette";
            case SYRINGE_PUMP:
            	return "syringe_pump";
            case KEYENCE:
            	return "keyence";
            case OSCILLOSCOPE:
            	return "oscilloscope";
            case MOXA_12XX:
                return "moxa_12XX";
            case FURNACE:
                return "furnace";
            case DH2000:
                return "DH2000";
            case PUMP:
                return "pump";
            case WATER_BATH_VALVE:
            	return "water_bath_valve";
            case REFLECTOMETRY:
            	return "reflectometry";
            case SENSOR:
            	return "sensor";
            case FILM_FABRICATION:
            	return "film_fabrication";
            case OBIS_LASER_REMOTE:
            	return "coherent_obis_laser_remote";
            case ZERO_FIELD:
            	return "zerofield";
            case LSICORR:
            	return "lsicorr";
            case DENSITY_METER:
                return "density_meter";
            case GUIDE_APERTURE:
            	return "guide_aperture";
            case SPIN_FLIPPER:
            	return "flipper";
            case PLC:
            	return "plc";
            case DEBUG:
            	return "debug";
            case HE_LEVEL_GAUGE:
            	return "he_level_gauge";
            case VACUUM_TANK:
            	return "sans2d_vacuum_tank";
            case MUSR_STEERING:
            	return "musr_steering";
            case DELAY_GENERATORS:
            	return "delay_generators";
            case FUNCTION_GENERATOR:
            	return "function_generator";
            case PHOTON_COUNTER:
            	return "photon_counter";
            default:
                return "cog";
        }
	}
}
