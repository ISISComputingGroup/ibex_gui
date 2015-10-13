package uk.ac.stfc.isis.ibex.experimentdetails.sql;

public abstract class SqlWhereClause {
	protected final String LHS;
	protected final String RHS;
	
	public SqlWhereClause(String LHS, String RHS) {
		this.LHS = LHS;
		this.RHS = RHS;
	}
	
	public abstract String toString();
}
