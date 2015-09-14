package uk.ac.stfc.isis.ibex.experimentdetails.sql;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataField;

public class SqlWhereLikeClause extends SqlWhereClause {
	
	public SqlWhereLikeClause(ExperimentDataField LHS, String RHS) {
		super(LHS.toString(), RHS);
	}
	
	@Override
	public String toString() {
		return this.LHS + " LIKE " + this.RHS;
	}
}
