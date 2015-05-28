package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

public final class ComponentIcons {
	
	private ComponentIcons() { }
	
	public static Image thumbnailForComponent(ComponentDescription component) {
		return thumbnailForType(component.type());
	}

	public static Image thumbnailForType(ComponentType type) {
		switch (type) {
		case JAWS:
			return icon("thumb/jaws_tb.png");
		case CHOPPER:
			return icon("thumb/chopper_tb.png");
		case MONITOR:
			return icon("thumb/monitor_tb.png");
		case SAMPLESTACK:
			return icon("thumb/sample_stack_tb.png");
		case DAE:
			return icon("thumb/dae_tb.png");
		case KEPCO:
		case CAEN:
			return icon("thumb/caen_tb.png");
		case BEAMSTOP:
			return icon("thumb/beamstop_tb.png");
		case MOVINGMONITOR:
		case MOVINGBEAMSTOP:
			return icon("thumb/moving_monitor_tb.png");
		case SAMPLECHANGER:
			return icon("thumb/sample_changer_tb.png");
		case ANALYSER:
			return icon("thumb/analyser_tb.png");
		case POLARISER:
			return icon("thumb/polariser_tb.png");
		case ROTATINGBENCH:
			return icon("thumb/rotating_b_tb.png");
		case JULABO:
			return icon("thumb/julabo_tb.png");
		case EUROTHERM:
			return icon("thumb/euro_tb.png");
		default:
			return icon("thumb/cog_tb.png");
		}
	}

	private static Image icon(String fileName) {
		return ResourceManager.getPluginImage(
				"uk.ac.stfc.isis.ibex.ui.synoptic.editor", "icons/" + fileName);
	}
}
