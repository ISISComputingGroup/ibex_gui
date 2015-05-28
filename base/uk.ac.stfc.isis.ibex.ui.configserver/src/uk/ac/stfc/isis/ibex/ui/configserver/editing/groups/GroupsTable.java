package uk.ac.stfc.isis.ibex.ui.configserver.editing.groups;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class GroupsTable extends DataboundTable<Group> {

	public GroupsTable(Composite parent, int style,	int tableStyle) {
		super(parent, style, Group.class, tableStyle);
		
		initialise();
	}

	@Override
	protected void addColumns() {
		name();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 3);
		name.setLabelProvider(new DataboundCellLabelProvider<Group>(observeProperty("name")) {
			@Override
			protected String valueFromRow(Group row) {
				return row.getName();
			}
		});			
	}

}
