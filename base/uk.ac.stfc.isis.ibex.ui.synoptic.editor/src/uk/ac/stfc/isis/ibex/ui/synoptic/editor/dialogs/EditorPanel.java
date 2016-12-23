
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;


import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentDetailView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeControls;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvDetailView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvDetailViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvListViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.TargetDetailView;

@SuppressWarnings("checkstyle:magicnumber")
public class EditorPanel extends Composite {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.EditorView";
	
	private static Font titleFont = SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD);
	
    private SynopticViewModel synopticViewModel;
	
	private Composite treeComposite;
	private Composite detailBarComposite;
	private Composite targetBarComposite;
	private Composite componentComposite;
	private Composite pvComposite;	
	private Composite targetComposite;
	
    public EditorPanel(Composite parent, int style, SynopticViewModel synopticViewModel,
            Collection<String> availableOPIs) {
		super(parent, style);

        this.synopticViewModel = synopticViewModel;

		setLayout(new GridLayout(3, false));
		
		GridData treeGridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData detailBarData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData targetBarData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData componentGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData targetGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData pvGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		treeGridData.widthHint = 300;
		detailBarData.widthHint = 300;
		targetBarData.widthHint = 300;
		
		GridLayout detailBarLayout = new GridLayout(1, false);
		detailBarLayout.marginHeight = 0;
		detailBarLayout.marginWidth = 0;
		
		GridLayout targetBarLayout = new GridLayout(1, false);
		targetBarLayout.marginHeight = 0;
		targetBarLayout.marginWidth = 0;
		

		treeComposite = new Composite(this, SWT.BORDER);
		treeComposite.setLayout(new GridLayout(1, false));
        treeComposite.setLayoutData(treeGridData);

        Label lblInstrumentTree = new Label(treeComposite, SWT.NONE);
        lblInstrumentTree.setFont(titleFont);
        lblInstrumentTree.setText("Instrument Tree");

        new InstrumentTreeView(treeComposite, this.synopticViewModel);
        new InstrumentTreeControls(treeComposite, this.synopticViewModel);
		
		detailBarComposite = new Composite(this, SWT.NONE);
		detailBarComposite.setLayout(detailBarLayout);
		detailBarComposite.setLayoutData(detailBarData);
			
        componentComposite = new Composite(detailBarComposite, SWT.BORDER);
        componentComposite.setLayout(new GridLayout(1, false));
        componentComposite.setLayoutData(componentGridData);

        Label lblComponentTitle = new Label(componentComposite, SWT.NONE);
        lblComponentTitle.setFont(titleFont);
        lblComponentTitle.setText("Component Details");

        PvListViewModel pvListViewModel = new PvListViewModel(synopticViewModel);

        new ComponentDetailView(componentComposite, this.synopticViewModel, pvListViewModel);

        pvComposite = new Composite(detailBarComposite, SWT.BORDER);
        pvComposite.setLayout(new GridLayout(1, false));
        pvComposite.setLayoutData(pvGridData);

        Label lblPvTitle = new Label(pvComposite, SWT.NONE);
        lblPvTitle.setFont(titleFont);
        lblPvTitle.setText("PV Details");

        new PvDetailView(pvComposite, new PvDetailViewModel(pvListViewModel));
		
		targetBarComposite = new Composite(this, SWT.NONE);
		targetBarComposite.setLayout(targetBarLayout);
		targetBarComposite.setLayoutData(targetBarData);

        targetComposite = new Composite(targetBarComposite, SWT.BORDER);
        targetComposite.setLayout(new GridLayout(1, false));
        targetComposite.setLayoutData(targetGridData);

        Label lblTargetTitle = new Label(targetComposite, SWT.NONE);
        lblTargetTitle.setFont(titleFont);
        lblTargetTitle.setText("Component Target Details");

        new TargetDetailView(targetComposite, this.synopticViewModel, availableOPIs);
    }
}
