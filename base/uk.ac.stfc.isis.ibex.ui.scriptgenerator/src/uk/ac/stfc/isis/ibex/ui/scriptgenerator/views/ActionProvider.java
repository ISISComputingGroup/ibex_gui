package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ActionProvider implements IStructuredContentProvider {
	
	private TableViewer viewer;
	private List<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorViewModel.class);

	public ActionProvider(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		LOG.warn("Getting elements: " + actions);
		return actions.toArray();
	}
	
	public boolean valuesDiffer(TableItem item, TableColumn[] columns, ScriptGeneratorAction action) {
		for (TableColumn column : columns) {
			var columnHeader = column.getText();
			var actionParameterValues = action.getActionParameterValueMapAsStrings();
			Optional<String> parameterValue = Optional.ofNullable(actionParameterValues.get(columnHeader));
			if (parameterValue.isPresent() && !parameterValue.get().equals(item.getText())) {
				return true;
			}
		}
		return false;
	}
	
	public void removeDeletedRows(List<ScriptGeneratorAction> newActions) {
		for (int i = newActions.size(); i < this.actions.size(); i++) {
			viewer.remove(this.actions.get(i));
		}
	}
	
	
	public void updateActions(List<ScriptGeneratorAction> newActions) {
		removeDeletedRows(newActions);
		var columns = viewer.getTable().getColumns();
		for (int i = 0; i < newActions.size(); i++) {
			var action = newActions.get(i);
			try {
				var item = viewer.getTable().getItem(i);
				if (valuesDiffer(item, columns, action)) {
					viewer.replace(action, i);
				}
			} catch (IllegalArgumentException e) {
				viewer.add(action);
			}
		}
		this.actions = newActions;
	}

}
