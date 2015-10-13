package uk.ac.stfc.isis.ibex.experimentdetails.sql;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;

public class SqlWhereEqualClause extends SqlWhereClause {
	
	public SqlWhereEqualClause(ExpDataField LHS, ExpDataField RHS) {
		super(LHS.toString(), RHS.toString());
	}

	public SqlWhereEqualClause(ExpDataField LHS, Role RHS) {
		super(LHS.toString(), "'" + RHS.name() + "'");
	}	
	
	@Override
	public String toString() {
		return this.LHS + " = " + this.RHS;
	}
}
