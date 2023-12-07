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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Spinner;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * DataAcquisitionSpinner: on input change: will change background to a colour
 * indicating a change that hasn't been applied to the instrument.
 *
 */
public class DaeExperimentSetupSpinner extends Spinner {

	private PanelViewModel panelViewModel;
	private String cachedValue;
	private String property;
	private ModelObject viewModel;
	private String name;
	private ExperimentSetupViewModel experimentSetupViewModel;
	private PropertyChangeListener propertyChangeListener;

	/**
	 * Create a new data acquisition spinner.
	 * 
	 * @param parent         a composite control which will be the parent of the new
	 *                       instance (cannot be null).
	 * @param style          the style of control to construct.
	 * @param panelViewModel the panelViewModel to help with the editing the panels.
	 * @param property       the property the listener will be observing.
	 * @param name           the name of the widget, which is used as a key in the
	 *                       cached values map.
	 */
	public DaeExperimentSetupSpinner(Composite parent, int style, PanelViewModel panelViewModel, String property,
			String name) {
		super(parent, style);
		this.panelViewModel = panelViewModel;
		this.property = property;
		experimentSetupViewModel = panelViewModel.getExperimentSetupViewModel();
		this.viewModel = experimentSetupViewModel.daeSettings();

		this.name = name;

		addSpinnerPropertyChangeListener();
	}

	@Override
	protected void checkSubclass() {
		// Allow sub-classing
	}

	/**
	 * Adds a listener to a spinner to colour it's background upon change to the
	 * observed property.
	 */
	public void addSpinnerPropertyChangeListener() {
		propertyChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (Display.getDefault().getThread() == Thread.currentThread()) {
					ifValueDifferentFromCachedValueThenChangeLabel();
				} else {
					Display.getDefault().asyncExec(() -> ifValueDifferentFromCachedValueThenChangeLabel());
				}
			}
		};
		viewModel.addPropertyChangeListener(property, propertyChangeListener);
	}

	/**
	 * Removes all property change listeners on the spinner.
	 */
	public void removeSpinnerPropertyChangeListeners() {
		viewModel.removePropertyChangeListener(property, propertyChangeListener);
	}

	/**
	 * Will set a label denoting a change that has not been applied to the
	 * instrument and notifies the dae view model.
	 */
	public void ifValueDifferentFromCachedValueThenChangeLabel() {
		if (this.getText().equals(cachedValue)) {
			setBackground(panelViewModel.getColour("white"));
			panelViewModel.setIsChanged(name, false);
		} else {
			setBackground(panelViewModel.getColour("changedColour"));
			panelViewModel.setIsChanged(name, true);
		}
	}

	/**
	 * Resets the cached value.
	 */
	public void resetCachedValue() {
		cachedValue = this.getText();
		experimentSetupViewModel.addtoCachedValues(name, cachedValue);
	}

	/**
	 * Creates a cached value for the first time, used when after the panels are
	 * created when they have been first initialised.
	 */
	public void createInitialCachedValue() {
		if (experimentSetupViewModel.getItemFromCachedValues(name).isEmpty()) {
			resetCachedValue();
		} else {
			cachedValue = experimentSetupViewModel.getItemFromCachedValues(name);
		}
	}

}
