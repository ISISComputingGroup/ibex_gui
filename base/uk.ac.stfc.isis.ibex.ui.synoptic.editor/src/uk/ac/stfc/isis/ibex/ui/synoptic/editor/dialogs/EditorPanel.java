
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


import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeControls;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvDetailView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvDetailViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvListViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.properties.TargetPropertiesView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.targetselector.TargetSelectorPanel;

@SuppressWarnings("checkstyle:magicnumber")
public class EditorPanel extends Composite {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.EditorView";
	
	private static Font titleFont = SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD);
	
    private SynopticViewModel synopticViewModel;

	private Composite treeComposite;
	private Composite detailBarComposite;
	private Composite targetDetailsComposite;
	private Composite componentComposite;
	private Composite pvComposite;	
	private Composite macrosComposite;
    private Composite targetSelectorComposite;
	
    public EditorPanel(Composite parent, int style, SynopticViewModel synopticViewModel,
            Map<String, List<String>> opis) {
		super(parent, style);

        this.synopticViewModel = synopticViewModel;

        setLayout(new GridLayout(3, false));
		
		GridData treeGridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData detailBarData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData targetDetailsLayoutData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData componentGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData targetGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData pvGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        GridData targetSelectorGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		treeGridData.widthHint = 300;
		detailBarData.widthHint = 300;
		targetDetailsLayoutData.widthHint = 300;
        targetSelectorGridData.widthHint = 300;
		
		GridLayout detailBarLayout = new GridLayout(1, false);
		detailBarLayout.marginHeight = 0;
		detailBarLayout.marginWidth = 0;
		
		GridLayout targetDetailsLayout = new GridLayout(1, false);
		targetDetailsLayout.marginHeight = 0;
		targetDetailsLayout.marginWidth = 0;
		

		treeComposite = new Composite(this, SWT.BORDER);
		treeComposite.setLayout(new GridLayout(1, false));
        treeComposite.setLayoutData(treeGridData);

        Label lblInstrumentTree = new Label(treeComposite, SWT.NONE);
        lblInstrumentTree.setFont(titleFont);
        lblInstrumentTree.setText("Instrument Tree");

        new InstrumentTreeView(treeComposite, this.synopticViewModel);
        new InstrumentTreeControls(treeComposite, this.synopticViewModel);

        targetSelectorComposite = new Composite(this, SWT.BORDER);
        targetSelectorComposite.setLayout(new GridLayout(1, false));
        targetSelectorComposite.setLayoutData(targetSelectorGridData);

        Label lblTargetSelectorTree = new Label(targetSelectorComposite, SWT.NONE);
        lblTargetSelectorTree.setFont(titleFont);
        lblTargetSelectorTree.setText("Target selection");

        Composite targetSelectorPanel =
                new TargetSelectorPanel(targetSelectorComposite, SWT.NONE, synopticViewModel, opis);

        detailBarComposite = new Composite(this, SWT.NONE);
        detailBarComposite.setLayout(detailBarLayout);
        detailBarComposite.setLayoutData(detailBarData);

        PvListViewModel pvListViewModel = new PvListViewModel(synopticViewModel);

        pvComposite = new Composite(detailBarComposite, SWT.BORDER);
        pvComposite.setLayout(new GridLayout(1, false));
        pvComposite.setLayoutData(pvGridData);

        Label lblPvTitle = new Label(pvComposite, SWT.NONE);
        lblPvTitle.setFont(titleFont);
        lblPvTitle.setText("PV Details");

        PvDetailViewModel pvDetailViewModel = new PvDetailViewModel(pvListViewModel);
        new PvDetailView(pvComposite, pvDetailViewModel);

        targetDetailsComposite = new Composite(detailBarComposite, SWT.NONE);
        targetDetailsComposite.setLayout(targetDetailsLayout);
        targetDetailsComposite.setLayoutData(targetDetailsLayoutData);

        macrosComposite = new Composite(targetDetailsComposite, SWT.BORDER);
        macrosComposite.setLayout(new GridLayout(1, false));
        macrosComposite.setLayoutData(targetGridData);

        Label lblTargetTitle = new Label(macrosComposite, SWT.NONE);
        lblTargetTitle.setFont(titleFont);
        lblTargetTitle.setText("Component macros");

        new TargetPropertiesView(macrosComposite, this.synopticViewModel);
    }
}
