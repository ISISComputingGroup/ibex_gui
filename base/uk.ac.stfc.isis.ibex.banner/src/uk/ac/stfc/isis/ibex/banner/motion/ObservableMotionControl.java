package uk.ac.stfc.isis.ibex.banner.motion;

import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public class ObservableMotionControl extends ModelAdapter {

	private static final Long STOP_VALUE = 1L;
	
	private final SameTypeWriter<Long> stop = new SameTypeWriter<>();
	
	public ObservableMotionControl( 
			Writable<Long> stop) {
		this.stop.writeTo(stop);
	}

	public void stop() {
		stop.write(STOP_VALUE);
	}
}
