package uk.ac.stfc.isis.ibex.synoptic.model;

import uk.ac.stfc.isis.ibex.synoptic.model.targets.PerspectiveTarget;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.ViewTarget;

public enum ComponentType {
	UNKNOWN,
	JAWS,
	CHOPPER,
	MONITOR,
	SAMPLESTACK,
	DAE (new PerspectiveTarget("DAE")) ,
	CAEN,
	KEPCO,
	BEAMSTOP, 
	MOVINGMONITOR,
	ROTATINGBENCH (new ViewTarget("Rotating bench")) ,
	SAMPLECHANGER,
	MOVINGBEAMSTOP,
	ANALYSER,
	POLARISER,
	JULABO,	
	EUROTHERM,
	GONIOMETER (new ViewTarget("Goniometer")) ,
	PINHOLESELECTOR;
	private Target target;

	private ComponentType() {
	}
	
	private ComponentType(Target target) {
		this.target = target;
	}
	
	public Target target() {
		return target;
	}
}
