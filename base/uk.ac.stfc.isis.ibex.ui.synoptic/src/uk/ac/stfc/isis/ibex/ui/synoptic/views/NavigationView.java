
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

package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.Navigator;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.SynopticSelection;

public class NavigationView extends ViewPart implements ISizeProvider {
		
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.NavigationView"; //$NON-NLS-1$
	
	private static final Color BACKGROUND = SWTResourceManager.getColor(240, 240, 240);
	public static final int FIXED_HEIGHT = 62;
	
	public NavigationView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(BACKGROUND);
		parent.setLayout(new GridLayout(2, false));
		
		Group synopticGroup = new Group(parent, SWT.NONE);		
	    synopticGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1));
	    synopticGroup.setLayout(new GridLayout(1, false));
	    synopticGroup.setText("Synoptic Selection");

		SynopticSelection synopticSelection = new SynopticSelection(synopticGroup, SWT.NONE);
		synopticSelection.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		synopticSelection.setBackground(BACKGROUND);
		
		Group navigationGroup = new Group(parent, SWT.NONE);		
		navigationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		navigationGroup.setLayout(new GridLayout(1, false));
		navigationGroup.setText("Synoptic Navigation");
				
		Navigator navigator = new Navigator(navigationGroup, SWT.NONE);
		navigator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		navigator.setBackground(BACKGROUND);
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSizeFlags(boolean width) {
		return SWT.MIN | SWT.MAX;
	}

	@Override
	public int computePreferredSize(boolean width, int availableParallel,
			int availablePerpendicular, int preferredResult) {
		return  width ? -1 : FIXED_HEIGHT;
	}
}
