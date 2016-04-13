
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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;


@SuppressWarnings("checkstyle:magicnumber")
public class BeamStatusStatsAndNewsView extends ViewPart {

    public static final String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.views.BeamStatusView"; //$NON-NLS-1$

    public BeamStatusStatsAndNewsView() {
        setPartName("BeamStatusView");
    }

    /**
     * Create contents of the view part.
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        GridLayout glParent = new GridLayout(2, false);
        glParent.verticalSpacing = 0;
        glParent.marginHeight = 0;
        parent.setLayout(glParent);

        StatusPanel status = new StatusPanel(parent, SWT.NONE);
        GridData gdStatus = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
        gdStatus.minimumHeight = 600;
        gdStatus.widthHint = 420;
        gdStatus.minimumWidth = 420;
        status.setLayoutData(gdStatus);

        NewsPanel news = new NewsPanel(parent, SWT.NONE);
        news.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        createActions();
        initializeToolBar();
        initializeMenu();
    }

    /**
     * Create the actions.
     */
    private void createActions() {
        // Create the actions
    }

    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {
        @SuppressWarnings("unused")
        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
    }

    /**
     * Initialize the menu.
     */
    private void initializeMenu() {
        @SuppressWarnings("unused")
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
    }

    @Override
    public void setFocus() {
        // Set the focus
    }
}
