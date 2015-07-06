
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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticSelectionDialog;

public class LoadSynopticHandler extends SynopticHandler {

	private static final String TITLE = "Load Synoptic";
	
	public LoadSynopticHandler() {
		super();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {	
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Synoptic synoptic = Synoptic.getInstance();
		SynopticSelectionDialog dialog = new SynopticSelectionDialog(shell, TITLE, synoptic.availableSynoptics());
		if (dialog.open() == Window.OK) {
			synoptic.setViewerSynoptic(dialog.selectedSynoptic());
		}
		return null;
	}
}
