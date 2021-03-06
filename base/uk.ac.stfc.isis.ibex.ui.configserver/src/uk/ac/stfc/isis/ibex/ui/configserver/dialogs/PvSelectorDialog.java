
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

package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs.PVSelectorPanel;


/**
 * A dialog for selecting a PV.
 */
public class PvSelectorDialog extends TitleAreaDialog {

	private PVSelectorPanel selector;
	private EditableConfiguration config;
	private PV pv;
    private static final String SHELL_TITLE = "Select PV";
	
    /**
     * Constructor for the dialog.
     * 
     * @param parentShell
     *            The parent shell to display this dialog in.
     * @param config
     *            The config that this PV will belong to.
     * @param address
     *            The starting address for the PV.
     */
	public PvSelectorDialog(Shell parentShell, EditableConfiguration config, String address) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
		this.config = config;
		pv = new PV(address, "", "", "");
	}
	
	@Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(PvSelectorDialog.SHELL_TITLE);
    }

    @Override
	protected Control createDialogArea(Composite parent) {
		selector = new PVSelectorPanel(parent, SWT.NONE);
		selector.setConfig(config, pv);
		selector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));		
		setTitle("PV Selector");
		
		return selector;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent The parent composite this dialog belongs to.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, 
				IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
    /**
     * Get the address for this PV.
     * 
     * @return The PV address.
     */
	public String getPVAddress() {
		return pv.getAddress();
	}

}
