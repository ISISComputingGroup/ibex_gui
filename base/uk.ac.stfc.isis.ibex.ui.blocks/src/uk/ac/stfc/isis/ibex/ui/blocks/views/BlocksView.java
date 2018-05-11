
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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;

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
public class BlocksView extends ViewPart implements ISizeProvider {

    /**
     * The default constructor.
     */
	public BlocksView() {
	}

    /**
     * The view ID.
     */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.blocks.views.BlocksView"; //$NON-NLS-1$

    private static final int FIXED_HEIGHT = 250;

	private static final ForwardingObservable<DisplayConfiguration> CONFIG = 
			Configurations.getInstance().display().displayCurrentConfig();

	private GroupsPanel groups;
	private Subscription configSubscription;
	
	private final BaseObserver<DisplayConfiguration> configObserver = new BaseObserver<DisplayConfiguration>() {
		@Override
		public void onValue(DisplayConfiguration value) {
			setGroups(value.groups());
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

		private void setGroups(Collection<DisplayGroup> newGroups) {
			if (groups != null) {
				groups.updateGroups(newGroups);
			}
		}
		
		private void setUnknownConfiguration() {
			setGroups(Collections.<DisplayGroup>emptyList());
		}		
	};	

	@Override
	public void createPartControl(final Composite parent) {
		
		GridLayout glParent = new GridLayout(1, false);
		glParent.verticalSpacing = 2;
		glParent.marginWidth = 0;
		glParent.marginHeight = 0;
		glParent.horizontalSpacing = 0;
		parent.setLayout(glParent);
		
		if (configSubscription != null) {
			configSubscription.removeObserver();
		}
		
		groups = new GroupsPanel(parent, SWT.NONE);
		groups.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		configSubscription = CONFIG.addObserver(configObserver);
	}

	@Override
	public void dispose() {
		if (configSubscription != null) {
			configSubscription.removeObserver();
		}
	}
	
	@Override
	public int getSizeFlags(boolean width) {
		return width ? SWT.NONE : SWT.MIN | SWT.MAX;
	}

	@Override
	public int computePreferredSize(boolean width, int availableParallel,
			int availablePerpendicular, int preferredResult) {
		return  width ? 0 : FIXED_HEIGHT;
	}

	@Override
	public void setFocus() {
	}

}
