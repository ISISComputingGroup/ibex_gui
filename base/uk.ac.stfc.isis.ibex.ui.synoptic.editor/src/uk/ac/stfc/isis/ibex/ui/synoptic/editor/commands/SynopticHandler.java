
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

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.EditSynopticDialog;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * Handles opening the Synoptic Editor and saving the synoptic when updated. 
 * 
 */
public abstract class SynopticHandler<T> extends AbstractHandler {

	protected static final Synoptic SYNOPTIC = Synoptic.getInstance();
	
	/**
	 * This is an inner anonymous class inherited from SameTypeWriter with added functionality
	 * for disabling the command if the underlying PV cannot be written to.
	 */
	protected final SameTypeWriter<T> synopticService = new SameTypeWriter<T>() {	
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			setBaseEnabled(canWrite);
		};	
	};
	
	
	/**
	 * Constructor.
	 * 
	 * @param destination where to write the data to
	 */
	public SynopticHandler(Writable<T> destination) {
		synopticService.writeTo(destination);
		destination.subscribe(synopticService);
	}		
	
	protected void openDialog(SynopticDescription synoptic, String title, boolean isBlank) {
        Collection<String> opis = Opi.getDefault().descriptionsProvider().getOpiList();
        EditSynopticDialog editDialog = new EditSynopticDialog(shell(), title, synoptic, isBlank, opis,
                new SynopticViewModel());
		if (editDialog.open() == Window.OK) {
			SYNOPTIC.edit().saveSynoptic().write(editDialog.getSynoptic());
			
			// Refresh the synoptic
			if (editDialog.getSynoptic().name().equals(SYNOPTIC.getSynopticInfo().name())) {
				SYNOPTIC.setViewerSynoptic(editDialog.getSynoptic().name());
			}
		}
	}
	
	protected Shell shell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}	
	
}
