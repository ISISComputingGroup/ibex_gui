package uk.ac.stfc.isis.ibex.experimentdetails.sql;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataField;

public class SqlWhereEqualClause extends SqlWhereClause {
	
	public SqlWhereEqualClause(ExperimentDataField LHS, ExperimentDataField RHS) {
		super(LHS.toString(), RHS.toString());
	}
	
	@Override
	public String toString() {
		return this.LHS + " = " + this.RHS;
	}
}
