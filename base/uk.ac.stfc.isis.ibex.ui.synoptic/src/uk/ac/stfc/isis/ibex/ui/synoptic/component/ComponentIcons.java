package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;

public final class ComponentIcons {
	
	private ComponentIcons() { }
	
	public static Image iconForComponent(Component component) {
		switch (component.type()) {
			case JAWS:
				return icon("jaws.png");
			case CHOPPER:
				return icon("chopper.png");
			case MONITOR:
				return icon("monitor.png");
			case SAMPLESTACK:
				return icon("sample_stack.png");
			case DAE:
				return icon("dae.png");
			case KEPCO:
			case CAEN:
				return icon("caen.png");
			case BEAMSTOP:
				return icon("beamstop.png");
			case MOVINGMONITOR:
			case MOVINGBEAMSTOP:
				return icon("moving_monitor.png");
			case SAMPLECHANGER:
				return icon("sample_changer.png");
			case ANALYSER:
				return icon("analyser.png");
			case POLARISER:
				return icon("polariser.png");
			case ROTATINGBENCH:
				return icon("rotating_bench.png");
            case JULABO:
				return icon("julabo.png");
			case EUROTHERM:
				return icon("euro.png");
			case GONIOMETER:
				return icon("ALF Gonio.png");
			case PINHOLESELECTOR:
				return icon("pinhole_selector.png");
			default:
				return icon("cog.png");
		}
	}
	
	private static Image icon(String fileName) {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/" + fileName);
	}
}
