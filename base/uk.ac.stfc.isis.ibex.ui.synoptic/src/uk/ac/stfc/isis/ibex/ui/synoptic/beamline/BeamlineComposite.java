package uk.ac.stfc.isis.ibex.ui.synoptic.beamline;

import org.eclipse.swt.widgets.Composite;

public abstract class BeamlineComposite extends Composite  {

	public BeamlineComposite(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * The desired height of the composite relative to its parent
	 */
	public abstract int beamLineHeight();
}
