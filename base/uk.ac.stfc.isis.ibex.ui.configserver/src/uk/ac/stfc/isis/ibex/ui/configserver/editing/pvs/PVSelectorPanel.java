
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Timer;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlockPVTable;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.InterestFilters;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.PVFilter;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.PVFilterFactory;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.SourceFilters;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;


/**
 * A composite for selecting a PV.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class PVSelectorPanel extends Composite {

	private final Text pvAddress;
	private ComboViewer interestLevel;
	private ComboViewer pvSource;
	private final BlockPVTable blockPVTable;
	private PVFilterFactory filterFactory;
	private PVFilter sourceFilter;
	private PVFilter interestFilter;
	private DataBindingContext bindingContext;
	
    /**
     * Builds the selector panel for display.
     * 
     * @param parent - the composite the panel is being added to
     * @param style - the style to use specified by the caller
     */
	public PVSelectorPanel(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpPV = new Group(this, SWT.NONE);
		grpPV.setText("PV Selector");
		
		grpPV.setLayout(new GridLayout(3, false));
		
		Label lblViewPVs = new Label(grpPV, SWT.NONE);
		lblViewPVs.setAlignment(SWT.RIGHT);
		lblViewPVs.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblViewPVs.setText("PVs From:");		
		
		pvSource = new ComboViewer(grpPV, SWT.READ_ONLY);
		GridData gdPvSource = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gdPvSource.widthHint = 100;
		pvSource.getCombo().setLayoutData(gdPvSource);
		pvSource.setContentProvider(new ArrayContentProvider());
		pvSource.setInput(SourceFilters.values());	

		Label lblInterestLevel = new Label(grpPV, SWT.NONE);
		lblInterestLevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInterestLevel.setText("Interest Level:");
		
		interestLevel = new ComboViewer(grpPV, SWT.READ_ONLY);
		GridData gdInterestLevel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gdInterestLevel.widthHint = 100;
		interestLevel.getCombo().setLayoutData(gdInterestLevel);
		interestLevel.setContentProvider(new ArrayContentProvider());
		interestLevel.setInput(InterestFilters.values());	
		
		Label lblPvAddress = new Label(grpPV, SWT.NONE);
		lblPvAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPvAddress.setText("PV address:");
		
		pvAddress = new Text(grpPV, SWT.BORDER);
		GridData gdPvAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdPvAddress.widthHint = 250;
		pvAddress.setLayoutData(gdPvAddress);
		
		final Button btnClear = new IBEXButtonBuilder(grpPV, SWT.NONE)
				.text("Clear")
				.build();

		Listener clearListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.widget == btnClear) {
					pvAddress.setText("");
				}
			}			
		};
		btnClear.addListener(SWT.Selection, clearListener);
		
		blockPVTable = new BlockPVTable(grpPV, SWT.NONE, SWT.FULL_SELECTION);
		GridData gdPvTable = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gdPvTable.heightHint = 300;
		blockPVTable.setLayoutData(gdPvTable);
		new Label(grpPV, SWT.NONE);
		new Label(grpPV, SWT.NONE);
		new Label(grpPV, SWT.NONE);
		
		blockPVTable.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size() > 0) {
					PV pv = (PV) selection.getFirstElement();
					pvAddress.setText(pv.getAddress());
				}
			}
		});
		
		int pvSearchDelay = 1000; //milliseconds
		ActionListener pvSearchTaskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Display.getDefault().asyncExec(() -> blockPVTable.setSearch(pvAddress.getText()));
			}
		};
		
		Timer pvSearch = new Timer(pvSearchDelay, pvSearchTaskPerformer);
		pvSearch.setRepeats(false); // Set timer to go off only once
		
		pvAddress.addModifyListener(e -> pvSearch.restart());
	}
	
    /**
     * Make sure that the appropriate config is associated with any changes.
     * 
     * @param config - the configuration being edited, or which is loaded
     * @param pv - a pv associated with the config
     */
	public void setConfig(EditableConfiguration config, PV pv) {
		blockPVTable.setRows(config.pvs());
		
		filterFactory = new PVFilterFactory(config.getAddedIocs());
		
        // Get the filter values to use on loading the dialog
        pvSource.setSelection(new StructuredSelection(SourceFilters.lastValue()));
        interestLevel.setSelection(new StructuredSelection(InterestFilters.lastValue()));
		
		//respond to changes in combo box
		pvSource.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				StructuredSelection selection = (StructuredSelection) arg0.getSelection();
				SourceFilters pvFilter = (SourceFilters) selection.getFirstElement();
                SourceFilters.setSelectedValue(pvFilter);
				changeSourceFilter(pvFilter);
			}
		});
		
		interestLevel.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				StructuredSelection selection = (StructuredSelection) arg0.getSelection();
				InterestFilters interestFilter = (InterestFilters) selection.getFirstElement();
                InterestFilters.setSelectedValue(interestFilter);
				changeInterestFilter(interestFilter);
			}
		});
		
		//Set up the binding here
		bindingContext = new DataBindingContext();		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(pvAddress), BeanProperties.value("address").observe(pv), null, null);
	}
	
    /**
     * Update the pv list based on any changes to the selected source filter
     * value.
     * 
     * @param pvFilter - the filter to apply to the pvs
     */
	private void changeSourceFilter(SourceFilters pvFilter) {
		sourceFilter = filterFactory.getFilter(pvFilter);
		blockPVTable.setSourceFilter(sourceFilter.getFilter());
		addFilterListener(sourceFilter);
	}
	
    /**
     * Update the pv list based on any changes to the selected interest filter
     * value.
     * 
     * @param pvFilter - the filter to apply to the pvs
     */
	private void changeInterestFilter(InterestFilters pvFilter) {
		interestFilter = filterFactory.getFilter(pvFilter);
		blockPVTable.setInterestFilter(interestFilter.getFilter());
		addFilterListener(interestFilter);
	}	
	
    /**
     * Update the contents of the block there are changes to the filter.
     * 
     * @param filter - the filter to apply
     */
	private void addFilterListener(PVFilter filter) {
		filter.addPropertyChangeListener("refresh", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				blockPVTable.refresh();
			}
		});		
	}
}
