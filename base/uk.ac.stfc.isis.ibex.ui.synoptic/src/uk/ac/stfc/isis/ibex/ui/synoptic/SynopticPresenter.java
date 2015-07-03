package uk.ac.stfc.isis.ibex.ui.synoptic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.OpiTarget;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.PerspectiveTarget;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.ViewTarget;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;
import uk.ac.stfc.isis.ibex.ui.UI;
import uk.ac.stfc.isis.ibex.ui.synoptic.views.LinkedViews;
import uk.ac.stfc.isis.ibex.ui.synoptic.views.OpiTargetView;

public class SynopticPresenter extends ModelObject {

	private static final Logger LOG = IsisLog.getLogger(SynopticPresenter.class);

	private SynopticModel model;
	private NavigationPresenter navigator;

	private Map<String, TargetNode> targets = new HashMap<>();
	private List<Component> components = new ArrayList<>();
	private List<String> trail;
	private List<IViewPart> openOPIs = new ArrayList<>();

	private final PropertyChangeListener navigationListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Target next = navigator.currentTarget();
			if (next instanceof GroupedComponentTarget) {
				displayTarget(navigator.currentTarget());
				setCurrentTrail(navigator.currentNode());
			}
		}
	};

	private final PropertyChangeListener modelListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			updateModel();
		}
	};

	public SynopticPresenter() {
		model = Synoptic.getInstance().currentViewerModel();
		model.addPropertyChangeListener("instrument", modelListener);

		navigator = new NavigationPresenter(model.instrumentGraph().head());
		navigator.addPropertyChangeListener("currentTarget", navigationListener);

		updateModel();
		navigateTo(navigator.currentTarget().name());
	}

	private void updateModel() {
		setTargets(model.instrumentGraph().targets());
		navigator.setCurrentTarget(model.instrumentGraph().head());
	}

	public NavigationPresenter navigator() {
		return navigator;
	}

	/*
	 * The hierarchy of components above the current component
	 */
	public List<String> currentTrail() {
		return new ArrayList<>(trail);
	}

	private void setCurrentTrail(TargetNode currentNode) {
		firePropertyChange("currentTrail", trail,
				trail = climbInstrumentGraphAndExtractGroupNames(navigator.currentNode()));
	}

	/*
	 * The names of available targets
	 */
	public List<String> getTargets() {
		return new ArrayList<>(targets.keySet());
	}

	private void setTargets(Map<String, TargetNode> targets) {
		List<String> oldTargets = getTargets();
		this.targets = targets;
		firePropertyChange("targets", oldTargets, getTargets());
	}

	public void navigateTo(final String targetName) {
		LOG.info(targetName + " requested");
		if (targets.containsKey(targetName)) {
			Target target = targets.get(targetName).item();

			if (target instanceof OpiTarget) {
				// Opi targets don't update the navigator.
				displayOpi(target);
				return;
			}

			if (target instanceof PerspectiveTarget) {
				// Perspective targets don't update the navigator.
				switchPerspective(target);
				return;
			}

			if (target instanceof ViewTarget) {
				displayView(target);
				return;
			}

			navigator.setCurrentTarget(targets.get(targetName));
		}
	}

	/*
	 * The components for the instrument view to render
	 */
	public List<? extends Component> components() {
		return new ArrayList<>(components);
	}

	private void displayView(Target target) {
		LinkedViews.openView(target.name());
	}

	private void displayOpi(Target currentTarget) {
		try {
			OpiTarget opiTarget = (OpiTarget) currentTarget;
			IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart view = workbenchPage.showView(OpiTargetView.ID, opiTarget.name(), IWorkbenchPage.VIEW_ACTIVATE);
			openOPIs.add(view);
			OpiTargetView targetView = (OpiTargetView) view;
			targetView.setOpi(opiTarget.name(), opiTarget.opiName(), opiTarget.properties());
		} catch (PartInitException e) {
			LOG.catching(e);
		}
	}

	private void displayTarget(Target currentTarget) {
		LOG.info(currentTarget);

		if (currentTarget instanceof GroupedComponentTarget) {
			displayGroupTarget(currentTarget);
		}

		if (currentTarget instanceof OpiTarget) {
			displayOpi(currentTarget);
		}
	}

	private void displayGroupTarget(Target currentTarget) {
		GroupedComponentTarget target = (GroupedComponentTarget) currentTarget;
		setComponents(target.components());
	}

	private void switchPerspective(Target target) {
		String id = uk.ac.stfc.isis.ibex.ui.perspectives.Activator.getDefault().perspectives().getID(target.name());
		UI.getDefault().switchPerspective(id);
	}

	/**
	 * Walk up the instrument graph from the target node to the top level,
	 * extracting the names of the group nodes.
	 */
	private List<String> climbInstrumentGraphAndExtractGroupNames(TargetNode targetNode) {
		List<String> trail = new ArrayList<>();

		TargetNode node = targetNode;
		if (node.item() instanceof GroupedComponentTarget) {
			trail.add(node.item().name());
		}

		while (node.up() != null) {
			node = node.up();
			if (node.item() instanceof GroupedComponentTarget) {
				trail.add(node.item().name());
			}
		}

		Collections.reverse(trail);
		return trail;
	}

	private void setComponents(List<? extends Component> components) {
		this.components = new ArrayList<>(components);
		firePropertyChange("components", null, components());
	}

	/**
	 * Closes any OPIs that have been opened on the synoptic.
	 */
	public void closeAllOPIs() {
		for (ListIterator<IViewPart> iter = openOPIs.listIterator(); iter.hasNext();) {
			final IViewPart vp = iter.next();
			// Must run on the GUI thread
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getPerspectiveRegistry()
							.findPerspectiveWithId(InstrumentPerspective.ID);
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
					IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					wp.hideView(vp);
				}
			});
		}
		openOPIs.clear();
	}
}
