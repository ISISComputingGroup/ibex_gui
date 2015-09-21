package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.UserDetailsTable;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;

public class UserDetailsWithExperimentsTable extends UserDetailsTable {

	public UserDetailsWithExperimentsTable(Composite parent, int style,
			int tableStyle) {
		super(parent, style, tableStyle);
	}
	
	@Override
	protected void addColumns() {
		experimentID();
		super.addColumns();
	}
	
	private void experimentID() {
		TableViewerColumn experimentID = createColumn("Experiment ID", 4);
		experimentID.setLabelProvider(new DataboundCellLabelProvider<UserDetails>(observeProperty("associatedExperiment")) {

			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getAssociatedExperiment();
			}
		});	
	}
}
