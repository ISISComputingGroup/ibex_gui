package uk.ac.stfc.isis.ibex.experimentdetails.database;

public class ExperimentDataField {
	private ExperimentDataTablesTags table;
	private ExperimentDataFieldsTags field;
	
	public ExperimentDataField (ExperimentDataTablesTags table, ExperimentDataFieldsTags field) {
		this.table = table;
		this.field = field;
	}
	
	@Override
	public String toString() {
		return table + "." + field;
	}
}
