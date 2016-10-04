
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;

public class TargetNameWidget extends Composite {

	private ComboViewer cmboOpiName;
	private boolean updateLock;
    private DeviceScreensDescriptionViewModel viewModel;
	private TargetType type;
    private List<String> availableOPIs;


    public TargetNameWidget(Composite parent, Collection<String> availableOPIs,
            DeviceScreensDescriptionViewModel viewModel) {
		super(parent, SWT.NONE);
		
        this.viewModel = viewModel;
		
        this.availableOPIs = new ArrayList<>(availableOPIs);
        // Insert a blank option for the OPIs
        this.availableOPIs.add(0, "");
		
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
//                Collection<TargetDescription> potentialTargets =
//                        DefaultTargetForComponent.defaultTarget(synopticViewModel.getFirstSelectedComponent().type());
//
//                if (potentialTargets.size() == 1) {
//                    updateModel(potentialTargets.iterator().next().name());
//                } else if (potentialTargets.size() > 1) {
//                    SuggestedTargetsDialog dialog = new SuggestedTargetsDialog(getShell(), potentialTargets);
//                    if (dialog.open() == Window.OK) {
//                        updateModel(dialog.selectedTargetName());
//                    }
//                }
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

        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.selection().observe(cmboOpiName.getCombo()),
                BeanProperties.value("currentKey").observe(viewModel), null, null);
	}
	
	
	private void updateModel(String targetName) 	{
//        if (!updateLock && synopticViewModel.getFirstSelectedComponent() != null) {
//            TargetDescription target = synopticViewModel.getFirstSelectedComponent().target();
//
//            if (target != null) {
//                target.setName(targetName);
//                target.setType(type);
//                target.setUserSelected(true);
//                target.clearProperties();
//                
//                List<String> keys = synopticViewModel.getPropertyKeys(target.name());
//                target.addProperties(keys);
//
//                synopticViewModel.setSelectedProperty(null);
//                synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_TARGET);
//            }
//        }
	}

}
