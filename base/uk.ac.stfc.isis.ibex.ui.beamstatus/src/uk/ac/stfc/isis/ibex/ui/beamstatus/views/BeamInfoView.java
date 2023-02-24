
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import jakarta.annotation.PostConstruct;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

/**
 * The parent composite for the beam information widgets.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BeamInfoView {

	/**
	 * Creates the Beam Info view.
	 * 
	 * @param parent The parent container obtained via dependency injection
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		ExpandBar expandBar = new ExpandBar(parent, SWT.FILL | SWT.V_SCROLL);
		expandBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

		ExpandItem xpndtmSynchrotron = new ExpandItem(expandBar, SWT.NONE);
		xpndtmSynchrotron.setExpanded(true);
		xpndtmSynchrotron.setText("Synchrotron");
		SynchrotronPanel sync = new SynchrotronPanel(expandBar, SWT.NONE);
		xpndtmSynchrotron.setControl(sync);
		xpndtmSynchrotron.setHeight(130);

		ExpandItem xpndtmTargetStation1 = new ExpandItem(expandBar, SWT.NONE);
		xpndtmTargetStation1.setExpanded(true);
		xpndtmTargetStation1.setText("Target Station 1");
		TargetStationOnePanel ts1 = new TargetStationOnePanel(expandBar, SWT.NONE);
		xpndtmTargetStation1.setControl(ts1);
		xpndtmTargetStation1.setHeight(220);

		ExpandItem xpndtmTargetStation2 = new ExpandItem(expandBar, SWT.NONE);
		xpndtmTargetStation2.setExpanded(true);
		xpndtmTargetStation2.setText("Target Station 2");
		TargetStationTwoPanel ts2 = new TargetStationTwoPanel(expandBar, SWT.NONE);
		xpndtmTargetStation2.setControl(ts2);
		xpndtmTargetStation2.setHeight(350);
		expandBar.layout();
	}

}
