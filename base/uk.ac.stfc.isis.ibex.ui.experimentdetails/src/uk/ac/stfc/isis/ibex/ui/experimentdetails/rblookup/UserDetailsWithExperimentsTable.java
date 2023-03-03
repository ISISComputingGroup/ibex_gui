package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.UserDetailsTable;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;

/**
 * A table displaying details of users for given experiments (for RB-lookup search).
 */
@SuppressWarnings("checkstyle:magicnumber")
public class UserDetailsWithExperimentsTable extends UserDetailsTable {	
	
	/**
	 * Create the table.
	 * @param parent the parent composite
	 * @param style the SWT style
	 * @param tableStyle the SWT table style
	 */
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
			protected String stringFromRow(UserDetails row) {
				return row.getAssociatedExperimentID();
			}
		});	
	}
	
	private void startDate() {
		createColumn("Start Date", 3, new DataboundCellLabelProvider<UserDetails>(observeProperty("associatedExperimentStartDate")) {
			@Override
			protected String stringFromRow(UserDetails row) {
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
