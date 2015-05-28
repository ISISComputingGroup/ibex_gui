package uk.ac.stfc.isis.ibex.rotatingbench;

/**
 * Enum for the various states the rotating bench goes through when moving 
 * @author sjb99183
 *
 */
public enum Status {
	DONE("In position"),
	HV_GOING_DOWN("HV powering down"),
	RAISING_BENCH("Raising the bench"),
	MOVING("Bench moving"),
	LOWERING("Lowering the bench"),
	HV_COMING_UP("HV powering up");
	
	private String text;
	
	/**
	 * Return a user friendly string describing this state
	 * @return
	 */
	String text() {
		return text;
	}
	
	Status(String text) {
		this.text = text;
	}
}
