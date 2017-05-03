
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.AddIocDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.IocDialog;
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
    private boolean readOnly;

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
		
		
        // Add IOC button
        btnAddIoc = new Button(this, SWT.NONE);
        btnAddIoc.setText("Add IOC");

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
        btnEditIoc = new Button(this, SWT.NONE);
        btnEditIoc.setText("Edit IOC");
        btnEditIoc.setEnabled(false);

        // Delete IOC Button
        btnDeleteIoc = new Button(this, SWT.NONE);
        btnDeleteIoc.setText("Delete IOC");
        btnDeleteIoc.setEnabled(false);

        GridData gdButton = new GridData();
        gdButton.widthHint = BUTTON_WIDTH;

        btnAddIoc.setLayoutData(gdButton);
        btnEditIoc.setLayoutData(gdButton);
        btnDeleteIoc.setLayoutData(gdButton);

        // Add listeners
        btnAddIoc.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                EditableIoc added = new EditableIoc("");
                openIocDialog(added, true);
            }
        });

        btnEditIoc.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                openIocDialog(table.firstSelectedRow(), false);
            }
        });
        
        btnDeleteIoc.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteSelected();
            }
        });

        table.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent arg0) {
                List<EditableIoc> selected = table.selectedRows();
                setSelectedIocs(selected);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                EditableIoc toEdit = table.getItemAtPoint(new Point(e.x, e.y));
                if (toEdit != null) {
                    openIocDialog(table.firstSelectedRow(), false);
                }
            }
        });
	}

    private void setSelectedIocs(List<EditableIoc> selected) {
        if (selected.size() == 0) {
            // No IOC selected
            return;
        } else if (selected.size() == 1) {
            // Exactly one IOC selected
            btnEditIoc.setEnabled(true);
            if (editEnabled(selected)) {
                readOnly = false;
                btnEditIoc.setText("Edit IOC");
            } else {
                readOnly = true;
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
        String dialogTitle = "Delete IOC";
        String dialogText = "Do you really want to delete the IOC";
        
        if (toRemove.size() == 1) {
            dialogText += " " + toRemove.get(0).getName() + "?";
        } else {
            dialogTitle = "Delete IOCs";
            dialogText += "s " + iocNamesToString(toRemove) + "?";
        }
                
        MessageBox dialog = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        dialog.setText(dialogTitle);
        dialog.setMessage(dialogText);
        int returnCode = dialog.open();
        
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

    private String iocNamesToString(List<EditableIoc> iocs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iocs.size(); i++) {
            EditableIoc ioc = iocs.get(i);
            sb.append(ioc.getName());
            if (i == iocs.size() - 2) {
                sb.append(" and ");
            } else if (i != iocs.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
