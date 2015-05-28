package uk.ac.stfc.isis.ibex.ui.synoptic.beamline;

public abstract class Line {

	private final int width;
	
	public Line(int width) {
		this.width = width;
	}
	
	public abstract int swtLineStyle();
	
	public int width() {
		return width;
	}
}
