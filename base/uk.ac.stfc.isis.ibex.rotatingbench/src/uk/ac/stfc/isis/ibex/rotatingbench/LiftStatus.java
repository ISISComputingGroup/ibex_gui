package uk.ac.stfc.isis.ibex.rotatingbench;

public enum LiftStatus {
	RAISED("Bench raised"),
	LOWERED("Bench lowered");
	
	private String text;
	
	/**
	 * Return a user friendly string describing this state
	 * @return
	 */
	public String text() {
		return text;
	}
	
	private LiftStatus(String text) {
		this.text = text;
	}
}
