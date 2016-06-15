
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticSelectionDialog;

public class EditSynopticHandler extends SynopticHandler<String> {

	private static final String TITLE = "Edit Synoptic";

	public EditSynopticHandler() {
		super(SYNOPTIC.editSynoptic());
	}	
	
	@Execute
	public Object execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {		
		SynopticSelectionDialog dialog = new SynopticSelectionDialog(shell, TITLE, SYNOPTIC.availableEditableSynoptics());
		if (dialog.open() == Window.OK) {
			openDialog(load(dialog.selectedSynoptic()), TITLE, false);
		}
		return null;
	}
	
	private SynopticDescription load(SynopticInfo info) {
		UpdatedValue<SynopticDescription> instrumentDescription = new UpdatedObservableAdapter<>(SYNOPTIC.synoptic(info));					
		if (Awaited.returnedValue(instrumentDescription, 1)) {
			return instrumentDescription.getValue();
		}
		return null;
	}
}
