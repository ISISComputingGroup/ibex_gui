
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DeleteTableItemHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.AddIocDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.IocDialog;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Panel showing an overview of all IOCs that are part of this configuration.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class IocOverviewPanel extends Composite {

    private EditableConfiguration config;

	private EditableIocsTable table;
    private Button btnAddIoc;
    private Button btnEditIoc;
    private Button btnDeleteIoc;
    private Text selectedIocRb;
	private final Display display = Display.getCurrent();

    private static final int BUTTON_WIDTH = 100;

	private final PropertyChangeListener updateIocs = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (config != null) {
				updateIocs(config.getAddedIocs());
			}
		}
	};
	
    /**
     * Constructor for the IOC overview panel.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     * @param msgDisp
     *            The dialog used for displaying error messages.
     */
	public IocOverviewPanel(Composite parent, int style, MessageDisplayer msgDisp) {
		super(parent, style);
        setLayout(new GridLayout(4, false));
		
        // IOC selection table
        table = new EditableIocsTable(this, SWT.NONE, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gdTable.heightHint = 200;
		table.setLayoutData(gdTable);
		
		GridData gdButton = new GridData();
        gdButton.widthHint = BUTTON_WIDTH;
		
        // Add IOC button
        btnAddIoc = new IBEXButton(this, SWT.NONE, evt -> {
        	EditableIoc added = new EditableIoc("");
            openIocDialog(added, true);
        })
				.text("Add IOC")
				.layoutData(gdButton)
				.get();

        // Selected IOC readback
        Composite cmpSelectedIoc = new Composite(this, SWT.FILL);
        cmpSelectedIoc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        cmpSelectedIoc.setLayout(new GridLayout(2, true));

        Label lblSelectedIoc = new Label(cmpSelectedIoc, SWT.NONE);
        lblSelectedIoc.setText("Selected:");
        lblSelectedIoc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        selectedIocRb = new Text(cmpSelectedIoc, SWT.BORDER);
        selectedIocRb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        selectedIocRb.setEditable(false);
        
        // Edit IOC button
        btnEditIoc = new IBEXButton(this, SWT.NONE, evt -> {
        	openIocDialog(table.firstSelectedRow(), false);
        })
				.text("Edit IOC")
				.layoutData(gdButton)
				.enabled(false)
				.get();

        // Delete IOC Button
        btnDeleteIoc = new IBEXButton(this, SWT.NONE, evt -> {
        	deleteSelected();
        })
				.text("Delete IOC")
				.layoutData(gdButton)
				.enabled(false)
				.get();

        table.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent arg0) {
                List<EditableIoc> selected = table.selectedRows();
                setSelectedIocs(selected);
            }
        });

        table.viewer().addDoubleClickListener(e -> openIocDialog(table.firstSelectedRow(), false));
	}

    private void setSelectedIocs(List<EditableIoc> selected) {
        if (selected.size() == 0) {
            // No IOCs selected
            selectedIocRb.setText("");
            btnEditIoc.setEnabled(false);
            btnDeleteIoc.setEnabled(false);
            return;
        } else if (selected.size() == 1) {
            // Exactly one IOC selected
            btnEditIoc.setEnabled(true);
            if (editEnabled(selected)) {
                btnEditIoc.setText("Edit IOC");
            } else {
                btnEditIoc.setText("View IOC");
            }
            selectedIocRb.setText(table.firstSelectedRow().getName());
        } else {
            // Multiple IOCs selected
            btnEditIoc.setEnabled(false);
            selectedIocRb.setText("(Multiple)");
        }
        btnDeleteIoc.setEnabled(editEnabled(selected));
    }

    private boolean editEnabled(List<EditableIoc> iocs) {
        boolean output = true;
        for (EditableIoc ioc : iocs) {
            output &= ioc != null && ioc.isEditable();
        }
        return output;
    }

    /**
     * Sets the configuration being edited.
     * 
     * @param config
     *            The configuration.
     */
	public void setConfig(EditableConfiguration config) {
		this.config = config;
		updateIocs(config.getAddedIocs());	
		config.addPropertyChangeListener(updateIocs);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		table.setEnabled(enabled);
	}
	
	private void updateIocs(final Collection<EditableIoc> iocs) {
		display.asyncExec(new Runnable() {	
			@Override
			public void run() {
				if (!table.isDisposed()) {
					table.setRows(iocs);
					table.refresh();	
				}
			}
		});
	}

    private void openIocDialog(EditableIoc toEdit, boolean isBlank) {
        if (isBlank) {
            IocDialog dialog = new AddIocDialog(getShell(), config, toEdit);
            dialog.open();
        } else {
            IocDialog dialog = new IocDialog(getShell(), config, toEdit);
            dialog.open();
        }
    }
    
    private void deleteSelected() {
    	List<EditableIoc> toRemove = table.selectedRows();
    	
    	DeleteTableItemHelper<EditableIoc> helper = new DeleteTableItemHelper<>();
        int returnCode = helper.createDeleteDialog("IOC", toRemove);
        
        if (returnCode == SWT.OK) {
            int index = table.getSelectionIndex();
            config.removeIocs(toRemove);
            table.setRows(config.getAddedIocs());
            table.refresh();
        
            // Update new selection
            int newIndex = index > 0 ? index - 1 : index;
            table.setSelectionIndex(newIndex);
            setSelectedIocs(table.selectedRows());
        }
    }
}
