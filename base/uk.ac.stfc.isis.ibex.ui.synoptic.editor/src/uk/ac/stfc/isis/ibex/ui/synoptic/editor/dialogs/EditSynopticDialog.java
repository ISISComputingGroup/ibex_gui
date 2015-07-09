
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

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

public class EditSynopticDialog extends Dialog {
	
	private static final Point INITIAL_SIZE = new Point(950, 800);
	private final String title;
	
	private InstrumentDescription synoptic;
	
	private EditorPanel editor;
	private boolean isBlank;
	private Button saveAsBtn;
	private Button saveBtn;
	
	public EditSynopticDialog(
			Shell parentShell, 
			String title, 
			InstrumentDescription synoptic,
			boolean isBlank) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.title = title;
		this.synoptic = synoptic;
		this.isBlank = isBlank;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		editor = new EditorPanel(parent, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		editor.setSynopticToEdit(synoptic);
		return editor;
	}

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (!isBlank) { 
			// createButton(parent, IDialogConstants.OK_ID, "Save", true);
			saveBtn = createButton(parent, IDialogConstants.CLIENT_ID + 2, "Save", false);

			saveBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Check synoptic is valid
					try {
						XMLUtil.toXml(synoptic);
						okPressed();
					} catch (JAXBException | SAXException e1) {
						MessageBox dialog = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
						dialog.setText("Error saving synoptic");
						dialog.setMessage("There was a problem saving the synoptic - is it is likely that there is an invalid component");
						dialog.open();
					}
				}

			});
		}
		saveAsBtn = createButton(parent, IDialogConstants.CLIENT_ID + 1, "Save as ...", false);
		
		saveAsBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SaveSynopticDialog dlg = new SaveSynopticDialog(null, synoptic.name(), SynopticInfo.names(Synoptic.getInstance().availableSynoptics()));
				if (dlg.open() == Window.OK) {
					synoptic.setName(dlg.getNewName());
					
					// Check synoptic is valid
					try {
						XMLUtil.toXml(synoptic);
						okPressed();
					} catch (JAXBException | SAXException e1) {
						MessageBox dialog = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
						dialog.setText("Error saving synoptic");
						dialog.setMessage("There was a problem saving the synoptic - is it is likely that there is an invalid component");
						dialog.open();
					}
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
	
	public InstrumentDescription getSynoptic() {
		return synoptic;
	}
}
