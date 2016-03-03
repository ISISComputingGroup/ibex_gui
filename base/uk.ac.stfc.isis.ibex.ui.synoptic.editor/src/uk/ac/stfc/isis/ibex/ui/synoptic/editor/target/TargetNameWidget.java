
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.SuggestedTargetsDialog;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetNameWidget extends Composite {

	private ComboViewer cmboOpiName;
	private boolean updateLock;
	private SynopticViewModel synopticViewModel;
	private TargetType type;
	private Collection<String> availableOPIs;

    public TargetNameWidget(Composite parent, final SynopticViewModel synopticViewModel, Collection<String> availableOPIs) {
		super(parent, SWT.NONE);
		
		this.synopticViewModel = synopticViewModel;
		
		this.availableOPIs = availableOPIs;
		
		createControls(this);

        IStructuredSelection selection = (IStructuredSelection) cmboOpiName.getSelection();
        updateModel((String) selection.getFirstElement());
    }

    private void createControls(Composite parent) {
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        setLayout(gridLayout);

        cmboOpiName = new ComboViewer(parent, SWT.READ_ONLY);
        Combo combo = cmboOpiName.getCombo();
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        cmboOpiName.setContentProvider(new ArrayContentProvider());
        cmboOpiName.setInput(availableOPIs);
        cmboOpiName.getCombo().select(-1);

        cmboOpiName.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent e) {
                IStructuredSelection selection = (IStructuredSelection) e.getSelection();
                String target = (String) selection.getFirstElement();

                updateModel(target);
            }
        });
		
        Button btnSetDefault = new Button(this, SWT.NONE);
        btnSetDefault.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        btnSetDefault.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Collection<TargetDescription> potentialTargets = DefaultTargetForComponent
                        .defaultTarget(synopticViewModel.getFirstSelectedComponent().type());

                if (potentialTargets.size() == 1) {
                    updateModel(potentialTargets.iterator().next().name());
                } else if (potentialTargets.size() > 1) {
                    SuggestedTargetsDialog dialog = new SuggestedTargetsDialog(getShell(), potentialTargets);
                    if (dialog.open() == Window.OK) {
                        updateModel(dialog.selectedTargetName());
                    }
                }
                
            }
        });
        btnSetDefault.setText("Default Target");

        Button btnClearSelection = new Button(this, SWT.NONE);
        btnClearSelection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnClearSelection.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateModel("NONE");
            }
        });
        btnClearSelection.setText("Clear Target");
	}
	
	
	private void updateModel(String targetName) 	{
        if (!updateLock && synopticViewModel.getFirstSelectedComponent() != null) {
            TargetDescription target = synopticViewModel.getFirstSelectedComponent().target();

            if (target != null) {
                target.setName(targetName);
                target.setType(type);
                target.setUserSelected(true);
                synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_TARGET);
            }
        }
	}
	
	public void setTarget(TargetDescription target) {
		updateLock = true;
		
		if (target == null) {
			cmboOpiName.getCombo().select(-1);
        } else {
            this.type = target.type();
            String name = Opi.getDefault().descriptionsProvider().guessOpiName(target.name());
            if (name == "") {
                // If no OPI found leave the selection blank
                cmboOpiName.setSelection(null);
            } else {
                ISelection selection = new StructuredSelection(name);
                cmboOpiName.setSelection(selection);
            }
		}
		
		updateLock = false;
	}
}
