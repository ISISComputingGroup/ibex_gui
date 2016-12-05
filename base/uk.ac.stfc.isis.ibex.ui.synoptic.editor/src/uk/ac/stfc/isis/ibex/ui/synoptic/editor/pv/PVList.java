
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;

/**
 * The class that displays the list of which PVs a synoptic component has.
 */
public class PVList extends Composite {
	private ListViewer list;
	
	private Button btnDelete;
	private Button btnAdd;
	private Button btnUp;
	private Button btnDown;
	
    private PvListViewModel viewModel;
	
    /**
     * The constructor for the class. Creates the controls to be displayed and
     * binds them to the view model.
     * 
     * @param parent
     *            The parent composite that holds this view.
     * @param viewModel
     *            The view model for the pv list.
     */
    public PVList(Composite parent, final PvListViewModel viewModel) {
		super(parent, SWT.NONE);
		
        this.viewModel = viewModel;
		
		GridLayout compositeLayout = new GridLayout(2, false);
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		
		setLayout(compositeLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
        viewModel.addPropertyChangeListener("pvListChanged", new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                list.setInput(e.getNewValue());
                list.refresh();
            }
        });

        viewModel.addPropertyChangeListener("pvSelection", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                PV selected = (PV) e.getNewValue();
                if (selected != null) {
                    list.refresh();
                    list.setSelection(new StructuredSelection(selected), true);
                }
            }
        });
		
		createControls(this);
		
        bind(viewModel);
	}
	
    /**
     * Bind the controls to the view model.
     * 
     * @param viewModel
     *            The view model to bind to.
     */
    private void bind(PvListViewModel viewModel) {
        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.enabled().observe(btnDelete),
                BeanProperties.value("deleteEnabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnUp),
                BeanProperties.value("upEnabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnDown),
                BeanProperties.value("downEnabled").observe(viewModel));
    }

    /**
     * Creates the controls to be displayed.
     * 
     * @param parent
     *            The composite on which to display the controls.
     */
    private void createControls(Composite parent) {
		list = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		list.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        list.setContentProvider(new ArrayContentProvider());
	    list.setLabelProvider(new PvLabelProvider());
	    
	    list.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
                if (!event.getSelection().isEmpty()) {
                    StructuredSelection selection = (StructuredSelection) event.getSelection();
                    viewModel.setSelectedPV((PV) selection.getFirstElement());
                }
			}
		});
	    
	    Composite moveComposite = new Composite(parent, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        btnUp = new Button(moveComposite, SWT.NONE);
        btnUp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btnUp.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"));
        btnUp.setEnabled(false);
        btnUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.promoteSelectedPV();
            }
        });

        btnDown = new Button(moveComposite, SWT.NONE);
        btnDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btnDown.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"));
        btnDown.setEnabled(false);
        btnDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.demoteSelectedPV();
            }
        });
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
	    controlComposite.setLayout(new GridLayout(1, false));
	    controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        btnAdd = new Button(controlComposite, SWT.NONE);
        btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnAdd.setText("Add New PV");
        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.addNewPV();
            }
        });

        btnDelete = new Button(controlComposite, SWT.NONE);
        btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnDelete.setText("Remove PV");
        btnDelete.setEnabled(false);
        btnDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.removeSelectedPV();
            }
        });
	}
}
