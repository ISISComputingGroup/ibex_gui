package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.UserDetailsTable;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;

@SuppressWarnings("checkstyle:magicnumber")
public class UserDetailsWithExperimentsTable extends UserDetailsTable {	
	public UserDetailsWithExperimentsTable(Composite parent, int style,
			int tableStyle) {
		super(parent, style, tableStyle);
	}
	
	@Override
	protected void addColumns() {
		experimentID();
		super.addColumns();
		startDate();
	}
	
	private void experimentID() {
		createColumn("Experiment ID", 3, new DataboundCellLabelProvider<UserDetails>(observeProperty("associatedExperimentID")) {

			@Override
			public String stringFromRow(UserDetails row) {
				return row.getAssociatedExperimentID();
			}
		});	
	}
	
	private void startDate() {
		createColumn("Start Date", 3, new DataboundCellLabelProvider<UserDetails>(observeProperty("associatedExperimentStartDate")) {
			@Override
			public String stringFromRow(UserDetails row) {
				return row.getAssociatedExperimentStartDateString();
			}
			
			@Override
			public Comparable<UserDetails> comparableForRow(final UserDetails row) {
				return new Comparable<UserDetails>() {
					@Override
					public int compareTo(UserDetails arg0) {
						return row.getAssociatedExperimentStartDate().compareTo(arg0.getAssociatedExperimentStartDate());
					}
				};
			}
		});	
	}
}
