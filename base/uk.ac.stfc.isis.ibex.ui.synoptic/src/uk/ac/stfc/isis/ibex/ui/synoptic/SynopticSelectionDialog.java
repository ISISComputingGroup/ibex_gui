
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


package uk.ac.stfc.isis.ibex.ui.synoptic;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.ui.dialogs.SelectionDialog;

/**
 * Dialog for asking the user to select a single synoptic.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SynopticSelectionDialog extends SelectionDialog {
	
	private final Collection<SynopticInfo> available;

	private SynopticInfo selectedSynoptic;
	
	/**
	 * @param parentShell The shell to open the dialog from.
	 * @param title The title of the dialog box.
	 * @param available The list of synoptics that the user can choose from.
	 */
	public SynopticSelectionDialog(
			Shell parentShell, 
			String title,
			Collection<SynopticInfo> available) {
		super(parentShell, title);
		this.available = available;
	}
	
	/**
	 * Get the synoptic that the user has selected.
	 * @return The synoptic that the user has selected.
	 */
	public SynopticInfo selectedSynoptic() {
		return selectedSynoptic;
	}
	
	@Override
	protected void okPressed() {
        selectedSynoptic = SynopticInfo.search(available, items.getSelection()[0].getText());
		
		super.okPressed();
	}
	
	@Override
    protected void createSelection(Composite container) {
		Label lblSelect = new Label(container, SWT.NONE);
        lblSelect.setText("Select synoptic:");
		
        items = createTable(container, SWT.BORDER | SWT.V_SCROLL);

		String[] names = SynopticInfo.names(available).toArray(new String[0]);
		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);

        setItems(names);
	}
	
}
