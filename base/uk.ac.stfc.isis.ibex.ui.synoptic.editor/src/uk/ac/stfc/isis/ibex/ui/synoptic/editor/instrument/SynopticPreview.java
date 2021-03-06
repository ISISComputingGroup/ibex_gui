
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.SynopticPanel;

/**
 * Provides a preview of the final synoptic.
 * 
 */
public class SynopticPreview extends Dialog {
	private final SynopticModel model;

	/**
	 * The synoptic preview creator.
	 * 
	 * @param parent
	 *             The shell in which the preview is created.
	 * @param instrumentDescription
	 *             A description of the devices present in the current synoptic on the instrument. 
	 */
	public SynopticPreview(Shell parent,
			SynopticDescription instrumentDescription) {
		super(parent);
		model = Synoptic.getInstance().getBlankModel();
		model.setSynopticFromDescription(instrumentDescription);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		SynopticPanel instrument = new SynopticPanel(container,
				SWT.NONE, true);
		instrument.setComponents(model.instrument().components(), model.instrument().showBeam());

		return instrument;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Synoptic Preview");
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	protected Point getInitialSize() {
		return new Point(1200, 500);
	}
}
