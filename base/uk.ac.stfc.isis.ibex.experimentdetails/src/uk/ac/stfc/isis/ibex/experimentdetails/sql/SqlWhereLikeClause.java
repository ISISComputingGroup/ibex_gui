package uk.ac.stfc.isis.ibex.experimentdetails.sql;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;

public class SqlWhereLikeClause extends SqlWhereClause {
	
	public SqlWhereLikeClause(ExpDataField LHS, String RHS) {
		super(LHS.toString(), RHS);
	}
	
	@Override
	public String toString() {
		return this.LHS + " LIKE " + this.RHS;
	}
}
