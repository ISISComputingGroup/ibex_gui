package uk.ac.stfc.isis.ibex.experimentdetails.sql;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsCreator;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsEnum;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataTablesEnum;

public class SqlWhereDateBetweenClause extends SqlWhereClause{

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 08:00:00");
	private static final ExpDataField experimentStart = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.STARTDATE);
	private static final ExpDataField experimentDuration = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.DURATION);
	private static final String betweenCase = experimentStart + " AND DATE_ADD(" + experimentStart + ", INTERVAL " + experimentDuration + " DAY)";
	
	
	public SqlWhereDateBetweenClause(GregorianCalendar LHS) {
		super("'" + formatter.format(LHS.getTime()) + "'", betweenCase);
	}

	@Override
	public String toString() {
		return LHS + " BETWEEN " + RHS;
	}

}
