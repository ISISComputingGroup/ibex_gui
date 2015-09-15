package uk.ac.stfc.isis.ibex.experimentdetails.database;

public class ExpDataField {
	private ExpDataTablesEnum table;
	private ExpDataFieldsEnum field;
	
	public ExpDataField (ExpDataTablesEnum table, ExpDataFieldsEnum field) {
		this.table = table;
		this.field = field;
	}
	
	@Override
	public String toString() {
		return table + "." + field;
	}
}
