
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.ui.synoptic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.synoptic.ObservingSynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;
import uk.ac.stfc.isis.ibex.targets.OpiTarget;
import uk.ac.stfc.isis.ibex.targets.Target;
import uk.ac.stfc.isis.ibex.ui.synoptic.views.SynopticOpiTargetView;
import uk.ac.stfc.isis.ibex.ui.synoptic.views.SynopticView;

/**
 * Responsible for the presentation logic of the synoptic.
 *
 */
public class SynopticPresenter extends ModelObject {

    private static final Logger LOG = IsisLog.getLogger(SynopticPresenter.class);

	private SynopticModel model;

	/**
	 * Presenter for the synoptic navigator panel, the control that allows moving around inside a synoptic.
	 */
	private NavigationPresenter navigator;

	private Map<String, TargetNode> targets = new HashMap<>();

	/**
	 * The components of the currently selected synoptic.
	 */
	private List<Component> components = new ArrayList<>();

	/**
	 * Describes the path that has been taken in navigating through a synoptic.
	 */
	private List<String> trail;

	/**
	 * Listens for changes triggered in the synoptic navigator.
	 */
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

	/**
	 * Observes for changes to the synoptic description. The description will be changed
	 * either when the server sends an updated description for the selected synoptic.
	 */
    private final Observer<SynopticDescription> descriptionObserver;

    /**
     * Manages the presentation of synoptics to the user in the synoptic perspective.
     */
	public SynopticPresenter() {
		model = Synoptic.getInstance().currentViewerModel();
		navigator = new NavigationPresenter(model.instrumentGraph().head());
		navigator.addPropertyChangeListener("currentTarget", navigationListener);

		updateModel();
		// Must be done after the navigator is initialised otherwise updateModel could
		// trigger a nullPointer exception.
		descriptionObserver = new BaseObserver<SynopticDescription>() {
	        @Override
	        public void onValue(SynopticDescription value) {
	            updateModel();
	        }

	        @Override
	        public void onError(Exception e) {
	            LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
	            clearComponents();
	        }

	        @Override
	        public void onConnectionStatus(boolean isConnected) {
	            if (!isConnected) {
	                clearComponents();
	            }
	        }
	    };

        ObservingSynopticModel observingSynopticModel = Synoptic.getInstance().currentObservingViewerModel();
        observingSynopticModel.getSynopticObservable().subscribe(descriptionObserver);
	}

    private void updateModel() {
        setTargets(model.instrumentGraph().targets());
		navigator.setCurrentTarget(model.instrumentGraph().head());
	}

    /**
     * Gets the navigator.
     *
     * @return the navigator
     */
	public NavigationPresenter navigator() {
		return navigator;
	}

    /**
     * The hierarchy of components above the current component.
     *
     * @return the current trail
     */
	public List<String> currentTrail() {
		return new ArrayList<>(trail);
	}

	private void setCurrentTrail(TargetNode currentNode) {
		firePropertyChange("currentTrail", trail,
				trail = climbInstrumentGraphAndExtractGroupNames(navigator.currentNode()));
	}

    /**
     * The names of available targets.
     *
     * @return the names of available targets
     */
	public List<String> getTargets() {
		return new ArrayList<>(targets.keySet());
	}

	private void setTargets(Map<String, TargetNode> targets) {
        List<String> oldTargets = getTargets();
        this.targets = targets;
        firePropertyChange("targets", oldTargets, getTargets());
	}

    /**
     * Navigates to a target based on a name.
     *
     * @param targetName
     *            the target to navigate to
     */
	public void navigateTo(final String targetName) {
		if (targets.containsKey(targetName)) {
            navigateTo(targets.get(targetName).item());
		}
	}

    /**
     * Navigates to a target.
     *
     * @param target
     *            the target to navigate to.
     */
    public void navigateTo(final Target target) {
        if (target instanceof GroupedComponentTarget) {
            displayGroupTarget((GroupedComponentTarget) target);
        }

        if (target instanceof OpiTarget) {
            // Opi targets don't update the navigator.
            try {
                SynopticOpiTargetView.displayOpi((OpiTarget) target);
            } catch (OPIViewCreationException e) {
                LOG.error(e);
            }
            return;
        }

        navigator.setCurrentTarget(targets.get(target.name()));
    }

    /**
     * Whether this target is valid.
     *
     * @param target
     *            the target to check
     * @return true if this target is valid.
     */
    public boolean isValidTarget(Target target) {
        if (target instanceof OpiTarget) {
            // Empty targets are saved as "NONE" with an OpiTarget type.
            // This is to keep track of if a target has been previously set,
            // for the default selection.
            String opiName = ((OpiTarget) target).opiName();
            if (opiName.equals("NONE")) {
                return false;
            }
        }

        return true;
    }

	/**
     * The components for the instrument view to render.
     *
     * @return the components
     */
	public List<? extends Component> components() {
		return new ArrayList<>(components);
	}

	private void displayTarget(Target currentTarget) {
		if (currentTarget instanceof GroupedComponentTarget) {
            displayGroupTarget((GroupedComponentTarget) currentTarget);
		}
		if (currentTarget instanceof OpiTarget) {
            try {
                SynopticOpiTargetView.displayOpi((OpiTarget) currentTarget);
            } catch (OPIViewCreationException e) {
                LOG.catching(e);
            }
		}
	}

    private void displayGroupTarget(GroupedComponentTarget currentTarget) {
		GroupedComponentTarget target = currentTarget;
		setComponents(target.components());
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

    private void clearComponents() {
        this.components = new ArrayList<>();
        firePropertyChange(SynopticView.COMPONENTS_CHANGE, null, components());
    }

	private void setComponents(List<? extends Component> components) {
		this.components = new ArrayList<>(components);
        firePropertyChange(SynopticView.COMPONENTS_CHANGE, null, components());
	}

    /**
     * Gets whether the beam is shown.
     *
     * @return true if the beam is shown
     */
	public boolean showBeam() {
		return model.instrument().showBeam();
	}

	/**
     * Closes any OPIs that have been opened on the synoptic.
     */
    public void closeAllOPIs() {
        SynopticOpiTargetView.closeAllOPIs();
    }
}
