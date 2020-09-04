
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.ui.dialogs.SelectionDialog;

/**
 * A dialog for selecting multiple synoptics.
 */
public class MultipleSynopticsSelectionDialog extends SelectionDialog {
	
	private final Collection<SynopticInfo> available;

	private Collection<String> selected = new ArrayList<>();
	

  /**
   * Default constructor.
   * 
   * @param parentShell
   *            The parent shell to open this dialog within.
   * @param title
   *            The title of the dialog.
   * @param available
   *            The synoptics that the user can choose from.
   */
	public MultipleSynopticsSelectionDialog(
			Shell parentShell, 
			String title,
			Collection<SynopticInfo> available) {
		super(parentShell, title);
		this.available = available;
	}
	
  /**
   * Get the synoptics that the user has chosen.
   * 
   * @return The list of the chosen synoptics.
   */
	public Collection<String> selectedSynoptics() {
		return selected;
	}
	
	@Override
	protected void okPressed() {
        selected = asString(items.getSelection());
		super.okPressed();
	}
	
	@Override
    protected void createSelection(Composite container) {
		Label lblSelect = new Label(container, SWT.NONE);
        lblSelect.setText("Select synoptics:");

        items = createTable(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        List<String> names = (List<String>) SynopticInfo.names(available);
        setItems(names);
	}
	
}
