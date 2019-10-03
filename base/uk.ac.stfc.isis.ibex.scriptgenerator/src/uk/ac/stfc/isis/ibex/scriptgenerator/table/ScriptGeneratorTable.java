package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ColumnDescription;

public class ScriptGeneratorTable extends ModelObject {

	private List<ColumnDescription> columnDescriptions;
	private List<ScriptGeneratorRow> rows = Collections.emptyList();

	public List<ScriptGeneratorRow> getRows() {
		return rows;
	}

	public ScriptGeneratorTable(List<ColumnDescription> columnDescriptions) {
		this.columnDescriptions = columnDescriptions;
		
	}

	public List<ColumnDescription> getColumns() {
		return this.columnDescriptions;
	}
	
	public void set_rows() {
		var rows = new ArrayList<ScriptGeneratorRow>(); 
		rows.add(new ScriptGeneratorRow());
		rows.add(new ScriptGeneratorRow());
		
		firePropertyChange("rows", this.rows, this.rows=rows);
		
	}

}
