package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class TimeRegime extends ModelObject {
	private final ArrayList<TimeRegimeRow> rows = new ArrayList<>();
	
	public TimeRegime(List<TimeRegimeRow> rows) {
		this.rows.addAll(rows);
	}
	
	public List<TimeRegimeRow> rows() {
		return Collections.unmodifiableList(rows);
	}
}
