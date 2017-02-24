
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXParseException;

import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticWriter;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.EditSynopticDialog;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * Handles opening the Synoptic Editor and saving the synoptic when updated.
 * 
 * @param <T>
 *            The type of the write destination for the synoptic data
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
	
    /**
     * Handle the sequence of opening a synoptic editor dialog and the
     * subsequent cancel/save.
     * 
     * @param synoptic
     *            The synoptic to edit
     * @param title
     *            The title of the synoptic editor dialog
     * @param isBlank
     *            Whether the requested synoptic has existing components or it
     *            is blank
     */
	protected void openDialog(SynopticDescription synoptic, String title, boolean isBlank) {
        Collection<String> opis = Opi.getDefault().descriptionsProvider().getOpiList();
        SynopticViewModel viewModel = new SynopticViewModel(synoptic);
        EditSynopticDialog editDialog =
                new EditSynopticDialog(shell(), title, isBlank, opis, viewModel);
		if (editDialog.open() == Window.OK) {
		    SynopticWriter writer = SYNOPTIC.edit().saveSynoptic();
            writer.write(viewModel.getSynoptic());
            Exception error = writer.lastError();
            if (error != null) {
                MessageBox dialog = new MessageBox(shell(), SWT.ERROR | SWT.OK);
                dialog.setText("Error saving synoptic");
                if (error.getCause().getClass() == SAXParseException.class) {
                    dialog.setMessage(
                            "Synoptic incompatible with server, please check that you are using the latest version of IBEX");
                } else {
                    dialog.setMessage("Error in saving synoptic, please contact support team");
                }
                Synoptic.LOG.error("Error saving synoptic: " + error.getMessage());
                dialog.open();
            }
		}
	}
	
    /**
     * Provides the shell to open dialogs in.
     * 
     * @return The shell to open the dialogs with.
     */
	protected Shell shell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}	
	
}
