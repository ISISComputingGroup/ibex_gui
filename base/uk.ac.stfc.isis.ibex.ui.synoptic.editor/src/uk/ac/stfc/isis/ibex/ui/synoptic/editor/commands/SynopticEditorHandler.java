
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

import java.io.IOException;
import java.util.Collection;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXParseException;

import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticWriter;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.EditSynopticDialog;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * Handles opening the Synoptic Editor and saving the synoptic when updated.
 * 
 */
public abstract class SynopticEditorHandler {

    /** Synoptic model. */
    protected static final Synoptic SYNOPTIC = Synoptic.getInstance();
    
    /** Synoptic writer, to write the model to the blockserver. */
    protected final SynopticWriter writer = SYNOPTIC.edit().saveSynoptic();
    
    /** can execute the handler */
    private boolean canExecute;

    /**
     * Constructor.
     */
    public SynopticEditorHandler() {
	synopticService.subscribe(SYNOPTIC.delete());
	SYNOPTIC.delete().subscribe(synopticService);

	canExecute = synopticService.canWrite();
    }

    /**
     * This is an inner anonymous class inherited from SameTypeWriter with added functionality
     * for modifying the command if the underlying PV cannot be written to.
     */
    protected final SameTypeWriter<Collection<String>> synopticService = new SameTypeWriter<Collection<String>>() {
	@Override
	public synchronized void onCanWriteChanged(boolean canWrite) {
	    canExecute = canWrite;
	}
    };

    /**
     * 
     * @return whether the handler can be executed
     */
    @CanExecute
    public boolean canExecute() {
	return canExecute;
    }

    /**
     * Handle the sequence of opening a synoptic editor dialog and the
     * subsequent cancel/save.
     * 
     * @param shell
     * 			  The shell that contains the synoptic editor
     * @param synoptic
     *            The synoptic to edit
     * @param title
     *            The title of the synoptic editor dialog
     * @param isBlank
     *            Whether the requested synoptic has existing components or it
     *            is blank
     */
    protected void openDialog(Shell shell, SynopticDescription synoptic, String title, boolean isBlank) {
	SynopticViewModel viewModel = new SynopticViewModel(synoptic);
	String SynopticName = (viewModel.getSynoptic().name() == null) ? "a new" : viewModel.getSynoptic().name();
	String subtitle = "Editing " + SynopticName + " synoptic";
	EditSynopticDialog editDialog =
		new EditSynopticDialog(shell, title, subtitle, isBlank, viewModel);
	if (editDialog.open() == Window.OK) {
	    try {
		writer.write(viewModel.getSynoptic());
	    } catch (IOException e) {
		handleError(shell, e);
	    }

	    Exception error = writer.lastError();
	    if (error != null) {
		handleError(shell, error);
	    }
	}
    }

    private void handleError(Shell shell, Exception error) {
	MessageBox dialog = new MessageBox(shell, SWT.ERROR | SWT.OK);
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
