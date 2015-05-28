package uk.ac.stfc.isis.ibex.ui.synoptic.beamline;

import org.eclipse.swt.SWT;

public class DotDashedLine extends Line {

	public DotDashedLine(int width) {
		super(width);
	}

	@Override
	public int swtLineStyle() {
		return SWT.LINE_DASHDOT;
	}
}
