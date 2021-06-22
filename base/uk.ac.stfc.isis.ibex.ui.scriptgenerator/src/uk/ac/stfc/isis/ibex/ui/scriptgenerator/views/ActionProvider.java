package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;

public class ActionProvider implements IStructuredContentProvider {
	
	private TableViewer viewer;
	private List<ScriptGeneratorAction> actions;
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorViewModel.class);

	public ActionProvider(TableViewer viewer) {
		this.viewer = viewer;
		viewer.setComparator(new NullComparator<ScriptGeneratorAction>());
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			this.actions = (List<ScriptGeneratorAction>) newInput;
			LOG.warn("Actions updated: " + this.actions.toString());
		}
	}

	public void updateElement(int index) {
		viewer.replace(actions.get(index), index);
		LOG.warn("Updated: " + index);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		LOG.warn("Getting elements: " + actions);
		return actions.toArray();
	}
	
	public void updateActions(List<ScriptGeneratorAction> newActions) {
		LOG.warn("New actions: " + newActions);
		LOG.warn("Old actions: " + actions);
		int i = 0;
		for (; i < this.actions.size(); i++) {
			if (i < newActions.size()) {
				JavaActionParameter[] differingParameters = this.actions.get(i).getDifferingParameters(newActions.get(i));
				if (differingParameters.length > 0) {
					LOG.warn("Replacing");
					viewer.replace(newActions.get(i), i);
				} else {
					LOG.warn("Same");
				}
			} else {
				LOG.warn("Removing");
				viewer.remove(this.actions.get(i));
			}
		}
		for (; i < newActions.size(); i++) {
			LOG.warn("Adding");
			viewer.add(newActions.get(i));
		}
		this.actions = newActions;
	}

}
