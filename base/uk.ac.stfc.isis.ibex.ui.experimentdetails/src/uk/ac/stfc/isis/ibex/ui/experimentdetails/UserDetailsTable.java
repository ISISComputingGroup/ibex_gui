package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

public class UserDetailsTable extends DataboundTable<UserDetails> {

	public UserDetailsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, UserDetails.class, tableStyle);
		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		institute();
		role();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 4);
		name.setEditingSupport(new StringEditingSupport<UserDetails>(viewer(), UserDetails.class) {

			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getName();
			}

			@Override
			protected void setValueForRow(UserDetails row, String value) {
				row.setName(value);
			}
		});
		name.setLabelProvider(new DataboundCellLabelProvider<UserDetails>(observeProperty("name")) {
			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getName();
			}
		});		
	}
	
	private void institute() {
		TableViewerColumn institute = createColumn("Institute", 4);
		institute.setEditingSupport(new StringEditingSupport<UserDetails>(viewer(), UserDetails.class) {

			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getInstitute();
			}

			@Override
			protected void setValueForRow(UserDetails row, String value) {
				row.setInstitute(value);
			}
		});
		institute.setLabelProvider(new DataboundCellLabelProvider<UserDetails>(observeProperty("institute")) {
			@Override
			protected String valueFromRow(UserDetails row) {
				return row.getInstitute();
			}
		});		
	}
	
	private void role() {
		TableViewerColumn role = createColumn("Role", 2);
		role.setEditingSupport(new RoleEditingSupport(viewer()));	
		role.setLabelProvider(new DataboundCellLabelProvider<UserDetails>(observeProperty("role")) {
			@Override
			protected String valueFromRow(UserDetails row) {
				Role role = row.getRole();
				return role == null ? "" : role.toString();
			}
		});	
	}
}
