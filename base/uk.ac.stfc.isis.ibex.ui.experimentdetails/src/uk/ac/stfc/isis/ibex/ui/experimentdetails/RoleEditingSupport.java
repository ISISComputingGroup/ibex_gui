package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.widgets.EnumEditingSupport;

public class RoleEditingSupport extends EnumEditingSupport<UserDetails, Role> {

	public RoleEditingSupport(ColumnViewer viewer) {
		super(viewer, UserDetails.class, Role.class);
	}
	
	@Override
	protected Role getEnumValueForRow(UserDetails row) {
		return row.getRole();
	}

	@Override
	protected void setEnumForRow(UserDetails row, Role value) {
		row.setRole(value);
	}

}
