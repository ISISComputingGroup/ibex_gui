
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

package uk.ac.stfc.isis.ibex.ui.blocks.views;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.handlers.IHandlerService;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.ui.blocks.groups.GroupsPanel;

/**
 * The overall view that holds the information on the blocks in the main ISIS
 * perspective.
 */
public class BlocksView {

    

	/**
     * The default constructor.
     */
	public BlocksView() {
	}

    /**
     * The view ID.
     */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.blocks.views.BlocksView"; //$NON-NLS-1$
	
	/**
	 * The part service currently in use.
	 */
	@Inject
	public static EPartService partService;
	
	private static final ForwardingObservable<DisplayConfiguration> CONFIG = 
			Configurations.getInstance().display().displayCurrentConfig();

	private GroupsPanel groups;
	private Subscription configSubscription;
	
	private final BaseObserver<DisplayConfiguration> configObserver = new BaseObserver<DisplayConfiguration>() {
		@Override
		public void onValue(DisplayConfiguration value) {
			setGroups(Optional.of(value.groups()));
		}

		@Override
		public void onError(Exception e) {
			setUnknownConfiguration();
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setUnknownConfiguration();
			}
		}

		private void setGroups(Optional<List<DisplayGroup>> newGroups) {
			if (groups != null) {
				groups.updateGroups(newGroups);
			}
		}
		
		private void setUnknownConfiguration() {
			setGroups(Optional.empty());
		}		
	};
	
	/**
	 * Create the controls within the blocks view.
	 * 
	 * @param parent parent of the blocks view
	 * @param app The E4 application model
	 * @param modelService The E4 service responsible for handling model elements
	 * @param handlerService The handler service to be passed down to BlocksMenu
	 */
    @PostConstruct
	public void createPartControl(final Composite parent, MApplication app, EModelService modelService, IHandlerService handlerService) {
		GridLayout glParent = new GridLayout(1, false);
		glParent.verticalSpacing = 2;
		glParent.marginWidth = 0;
		glParent.marginHeight = 0;
		glParent.horizontalSpacing = 0;
		parent.setLayout(glParent);
		
		if (configSubscription != null) {
			configSubscription.cancelSubscription();
		}
		
		groups = new GroupsPanel(parent, SWT.NONE, handlerService);
		groups.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		configSubscription = CONFIG.subscribe(configObserver);
	}

    /**
     * Dispose of observers when blocks view is destroyed.
     */
    @PreDestroy
	public void dispose() {
    	configSubscription.cancelSubscription();
	}
}
