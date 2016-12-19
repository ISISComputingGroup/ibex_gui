
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.SynopticPreview;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;

/**
 * This class provides the dialog to edit the synoptic. While this class is responsible for
 * saving, most of the layout for this dialogue is provided in the EditorPanel class.
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class EditSynopticDialog extends TitleAreaDialog {
	
	private static final Point INITIAL_SIZE = new Point(950, 800);
	private final String title;
	
	private EditorPanel editor;
	private boolean isBlank;
    private Button previewBtn;
	private Button saveAsBtn;
	private Button saveBtn;
	
    private SynopticViewModel synopticViewModel;
    private Collection<String> availableOPIs;

    /**
     * The constructor for the overall Synoptic editor dialog.
     * 
     * @param parentShell
     *            The shell to open the dialog in.
     * @param title
     *            The title of the dialog.
     * @param isBlank
     *            Whether the synoptic is blank or not, i.e. a new synoptic.
     * @param availableOPIs
     *            The OPIs that are available to put into the synoptic
     * @param synopticViewModel
     *            The view model describing the logic of the synoptic editor
     */
    public EditSynopticDialog(Shell parentShell, String title, boolean isBlank,
            Collection<String> availableOPIs, SynopticViewModel synopticViewModel) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.title = title;
		this.isBlank = isBlank;
        this.availableOPIs = availableOPIs;
        this.synopticViewModel = synopticViewModel;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        editor = new EditorPanel(parent, SWT.NONE, synopticViewModel, availableOPIs);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return editor;
	}

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
        // createButton(parent, IDialogConstants.OK_ID, "Save", true);
        previewBtn = createButton(parent, IDialogConstants.CLIENT_ID + 3, "Synoptic Preview", false);
        previewBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SynopticPreview previewDialog = new SynopticPreview(getShell(), synopticViewModel.getSynoptic());
                previewDialog.open();
            }

        });

		if (!isBlank) { 
			// createButton(parent, IDialogConstants.OK_ID, "Save", true);
			saveBtn = createButton(parent, IDialogConstants.CLIENT_ID + 2, "Save", false);

			saveBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Check synoptic is valid
					try {
                        XMLUtil.toXml(synopticViewModel.getSynoptic());
						okPressed();
					} catch (JAXBException | SAXException e1) {
						MessageBox dialog = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
						dialog.setText("Error saving synoptic");
						dialog.setMessage("There was a problem saving the synoptic:" + e1);
						dialog.open();
					}
				}

			});
		}
		saveAsBtn = createButton(parent, IDialogConstants.CLIENT_ID + 1, "Save as ...", false);
		
		saveAsBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                SaveSynopticDialog dlg = new SaveSynopticDialog(null, synopticViewModel.getSynoptic().name(),
                        SynopticInfo.names(Synoptic.getInstance().availableSynoptics()));
				if (dlg.open() == Window.OK) {
                    synopticViewModel.getSynoptic().setName(dlg.getNewName());
					
					// Check synoptic is valid
					try {
                        XMLUtil.toXml(synopticViewModel.getSynoptic());
						okPressed();
					} catch (JAXBException | SAXException e1) {
						MessageBox dialog = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
						dialog.setText("Error saving synoptic");
						dialog.setMessage("There was a problem saving the synoptic:" + e1);
						dialog.open();
					}
				}
			}
		});
		
        synopticViewModel.addPropertyChangeListener("error", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ErrorMessage hasError = (ErrorMessage) evt.getNewValue();
                setErrorMessage(hasError.getMessage());
                saveAsBtn.setEnabled(!hasError.isError());
                if (saveBtn != null) {
                    saveBtn.setEnabled(!hasError.isError());
                }
            }
        });
		
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}	
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}
