
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.EditSynopticDialog;

public abstract class SynopticHandler extends AbstractHandler {

	protected final Synoptic SYNOPTIC = Synoptic.getInstance();
	
	public SynopticHandler() {
		SYNOPTIC.edit().saveSynoptic().canSave().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setBaseEnabled(SYNOPTIC.edit().saveSynoptic().canSave().getValue());
			}
		});
	}		
	
	protected void openDialog(SynopticDescription synoptic, String title, boolean isBlank) {
		EditSynopticDialog editDialog = new EditSynopticDialog(shell(), title, synoptic, isBlank);	
		if (editDialog.open() == Window.OK) {
			SYNOPTIC.edit().saveSynoptic().write(editDialog.getSynoptic());
		}
	}
	
	protected Shell shell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}	
	
}
