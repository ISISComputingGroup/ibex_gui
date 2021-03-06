
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.Navigator;

/**
 * View to navigate through nested synoptics.
 *
 */
public final class NavigationView {

    /** ID for synoptic navigation view. */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.NavigationView"; //$NON-NLS-1$


    /**
     * Create controls in view.
     * @param parent composite in which the view will sit.
     */
    @PostConstruct
    public void createPartControl(Composite parent) {
	parent.setLayout(new GridLayout(2, false));
	Navigator navigator = new Navigator(parent, SWT.NONE);
	navigator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    }
}
