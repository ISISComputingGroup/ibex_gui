
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.SynopticPanel;

/**
 * Provides the containing view for they synoptic.
 * 
 */
public class SynopticView {
	
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.SynopticView"; //$NON-NLS-1$
    public static final String COMPONENTS_CHANGE = "components";

    private SynopticPanel instrument;
	private final SynopticPresenter presenter = Activator.getDefault().presenter();
	
	private final Display display = Display.getCurrent();
	

	//TODO: Why couldn't we do this with postConstruct?
	@Inject
	public SynopticView(Composite parent) {
		instrument = new SynopticPanel(parent, SWT.NONE);
        instrument.setComponents(presenter.components(), presenter.showBeam());

        presenter.addPropertyChangeListener(COMPONENTS_CHANGE, new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
				display.asyncExec(new Runnable() {		
					@Override
					public void run() {
                        instrument.setComponents(presenter.components(), presenter.showBeam());
					}
				});
			}
		});
	}
}
