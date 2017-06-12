 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

/**
 * Listener for changes to a part and tab folder to react when a tab becomes
 * visible. When it is visible then the tab shows action is called.
 */
public class TabIsShownListener implements IPartListener2 {
    /**
     * Listener to determine which tab is currently selected
     */
    private final class TabSelectionListener implements SelectionListener {
        /**
         * The tab item containing detector diagnostics
         */
        private final TabIsShownAction tabSelection;

        /**
         * Constructor.
         * 
         * @param tbtmDiagnostics
         *            The tab item containing detector diagnostics
         */
        private TabSelectionListener(TabIsShownAction tabSelection) {
            this.tabSelection = tabSelection;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            Widget item = e.item;
            onSelectionChanged((CTabItem) item);
        }

        /**
         * Update the model with the tab that was just selected
         * 
         * @param item
         *            the tab that was just selected; null means no tab is
         *            selected or no tab is visible i.e. when a new
         *            perspective is chosen
         */
        private void onSelectionChanged(CTabItem item) {
            tabSelection.visibleTabChanged(item);

        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // do nothing
        }
    }


    private ViewPart viewPart;
    private CTabFolder tabFolder;
    private TabSelectionListener tabSelectionListener;

    /**
     * Create and register the listener on the tab folder in a part to call the
     * action when a tab becomes visible or is hidden (as in shown to the user).
     * 
     * @param viewPart
     *            the view part the tab is on
     * @param tabFolder
     *            the tab folder which is changed
     * @param tabIsShownAction
     *            the action to perform when a new tab is shown
     */
    public static void createAndRegister(ViewPart viewPart, CTabFolder tabFolder, TabIsShownAction tabIsShownAction) {
        TabIsShownListener innerObject = new TabIsShownListener(viewPart, tabFolder, tabIsShownAction);
        viewPart.getSite().getPage().addPartListener(innerObject);
    }

    /**
     * Instantiates a new part listener.
     *
     * @param daeViewPart
     *            the view part the tab is on
     * @param tabFolder
     *            the tab folder which is changed
     * @param tabIsShownAction
     *            the action to perform when a tab is shown
     */
    TabIsShownListener(ViewPart daeViewPart, CTabFolder tabFolder, TabIsShownAction tabIsShownAction) {
        this.viewPart = daeViewPart;
        this.tabFolder = tabFolder;
        this.tabSelectionListener = new TabSelectionListener(tabIsShownAction);
    }

    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        IWorkbenchPart part = partRef.getPart(false);
        if (part == viewPart && tabSelectionListener != null) {
            tabFolder.addSelectionListener(tabSelectionListener);
            tabSelectionListener.onSelectionChanged(tabFolder.getSelection());
        }
    }

    @Override
    public void partHidden(IWorkbenchPartReference partRef) {
        IWorkbenchPart part = partRef.getPart(false);
        if (part == viewPart && tabSelectionListener != null) {
            tabFolder.removeSelectionListener(tabSelectionListener);
            tabSelectionListener.onSelectionChanged(null);
        }

    }

    @Override
    public void partActivated(IWorkbenchPartReference partRef) {
        //
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
        //
    }

    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
        //
    }

    @Override
    public void partDeactivated(IWorkbenchPartReference partRef) {
        //
    }

    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
        //
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {
        //
    }

}