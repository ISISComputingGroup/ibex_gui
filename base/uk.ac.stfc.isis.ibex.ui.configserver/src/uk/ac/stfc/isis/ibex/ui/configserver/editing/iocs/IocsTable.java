package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.util.Arrays;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class IocsTable extends DataboundTable<EditableIoc> {

	private final CellDecorator<EditableIoc> rowDecorator = new IocRowCellDecorator();
	private final CellDecorator<EditableIoc> simulationDecorator = new IocSimulationCellDecorator();
	
	public IocsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, EditableIoc.class, tableStyle);
				
		initialise();		
	}
	
	@Override
	protected void addColumns() {
		name();
		description();
		simLevel();
		autostart();
		restart();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 4);
		name.setLabelProvider(new DecoratedCellLabelProvider<EditableIoc>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator, simulationDecorator)) {
			@Override
			protected String valueFromRow(EditableIoc row) {
				return row.getName();
			}
		});
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 4);
		desc.setLabelProvider(new DecoratedCellLabelProvider<EditableIoc>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableIoc row) {
				return row.getDescription();
			}
		});	
	}
	
	private void autostart() {
		TableViewerColumn enabled = createColumn("Auto-start?", 2);
		IObservableMap[] stateProperties = { observeProperty("autostart") };
		enabled.setLabelProvider(new IocCheckboxLabelProvider<Ioc>(stateProperties) {	
			@Override
			protected boolean checked(Ioc ioc) {
				return ioc.getAutostart();
			}
			
			@Override
			protected void setChecked(Ioc ioc, boolean checked) {
				ioc.setAutostart(checked);
			}
			
			@Override
			protected boolean isEditable(Ioc ioc) {
				return !ioc.hasSubConfig();
			}
		});	
	}
	
	private void restart() {
		TableViewerColumn enabled = createColumn("Auto-restart?", 2);
		IObservableMap[] stateProperties = { observeProperty("restart") };
		enabled.setLabelProvider(new IocCheckboxLabelProvider<Ioc>(stateProperties) {	
			@Override
			protected boolean checked(Ioc ioc) {
				return ioc.getRestart();
			}
			
			@Override
			protected void setChecked(Ioc ioc, boolean checked) {
				ioc.setRestart(checked);
			}
			
			@Override
			protected boolean isEditable(Ioc ioc) {
				return !ioc.hasSubConfig();
			}
		});	
	}
	
	private void simLevel() {
		TableViewerColumn simLevel = createColumn("Sim. level", 2);
		simLevel.setLabelProvider(new DecoratedCellLabelProvider<EditableIoc>(
				observeProperty("simLevel"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableIoc row) {
				return row.getSimLevel().toString();
			}
		});	
		simLevel.setEditingSupport(new SimLevelEditingSupport(viewer()));
	}
}
