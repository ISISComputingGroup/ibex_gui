
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

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.NewBlockHandler;



/**
 * The parent composite for the beam information widgets.
 */
public class BeamInfoView {

    /**
     * Creates the Beam Info view.
     * 
     * @param parent The parent container obtained via dependency injection
     */
    @SuppressWarnings("checkstyle:magicnumber")
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
    
    
    public void BeamInformationMenu(Composite menu) {
    	
    	final MenuManager logSubMenu = new MenuManager("Add/Log values");
        logSubMenu.add(new Action("never shown entry") {
        	//needed if it's a submenu
        });
        // Allows the menu to be dynamic
        logSubMenu.setRemoveAllWhenShown(true);

        final IAction newPlotAction = new Action("Add/Log values") {
			@Override
			public void run() {

				//Presenter.pvHistoryPresenter().newDisplay(block.blockServerAlias(), block.getName());
			}
		};
		
//
//        logSubMenu.addMenuListener(new IMenuListener() {
//			@Override
//			public void menuAboutToShow(IMenuManager manager) {
//				logSubMenu.add(newPlotAction);
//
//				HashMap<String, ArrayList<String>> dataBrowserData = Presenter.pvHistoryPresenter().getPlotsAndAxes();
//				for (String plotName : dataBrowserData.keySet()) {
//				    MenuManager plotSubMenu = new MenuManager("Add to " + plotName + " plot...");
//
//				    plotSubMenu.add(createAddToPlotAction(plotName));
//				    dataBrowserData.get(plotName).stream().forEach(a -> plotSubMenu.add(createAddToAxisAction(plotName, a)));
//
//				    logSubMenu.add(plotSubMenu);
//				}
//			}
//        }
    }
    
}
