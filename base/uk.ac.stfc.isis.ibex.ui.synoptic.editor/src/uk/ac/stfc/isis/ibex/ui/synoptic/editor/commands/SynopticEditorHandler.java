
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
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXParseException;

import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticWriter;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.EditSynopticDialog;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * Handles opening the Synoptic Editor and saving the synoptic when updated.
 * 
 */
public abstract class SynopticEditorHandler extends AbstractHandler {

	protected static final Synoptic SYNOPTIC = Synoptic.getInstance();
    protected static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    private final SynopticWriter writer = SYNOPTIC.edit().saveSynoptic();
	
    /**
     * Constructor for the handler that adds a listener for when the destination
     * can not written to.
     */
    public SynopticEditorHandler() {
        writer.canSave().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Boolean canSave = (Boolean) evt.getNewValue();
                setBaseEnabled(canSave);
            }
        });
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
                new EditSynopticDialog(SHELL, title, isBlank, opis, viewModel);
		if (editDialog.open() == Window.OK) {
            writer.write(viewModel.getSynoptic());
            Exception error = writer.lastError();
            if (error != null) {
                MessageBox dialog = new MessageBox(SHELL, SWT.ERROR | SWT.OK);
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
	
}
