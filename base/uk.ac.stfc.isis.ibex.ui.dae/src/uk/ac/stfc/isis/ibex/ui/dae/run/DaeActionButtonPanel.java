
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2024 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.dae.run;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.dae.actions.DaeActions;
import uk.ac.stfc.isis.ibex.model.Action;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.HelpButton;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonFactory;

/**
 * Pane which contains the action buttons in the DAE perspective in the "Run
 * Summary" tab. For example "BEGIN RUN" or "END RUN".
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DaeActionButtonPanel extends Composite {

	private static final String HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Manage-the-DAE";
	private static final String DESCRIPTION = "DAE View";

	/**
	 * Create the panel with all the buttons inside.
	 * 
	 * @param parent  a composite control which will be the parent of the new
	 *                instance (cannot be null)
	 * @param style   the style of control to construct
	 * @param actions all state transitioning actions on the DAE
	 */
	public DaeActionButtonPanel(Composite parent, int style, DaeActions actions) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 10;
		setLayout(gridLayout);

		addActionButton("BEGIN RUN", "play.png", actions.begin);
		addActionButton("END RUN", "stop.png", actions.end);
		addActionButton("PAUSE RUN", "pause.png", actions.pause);
		addActionButton("RESUME RUN", "resume.png", actions.resume);
		addActionButton("ABORT RUN", "abort.png", actions.abort);
		addActionButton("CANCEL ABORT", "undo.png", actions.cancelAbort);

		Label middleSpacer = new Label(this, SWT.NONE);
		middleSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		middleSpacer = new Label(this, SWT.NONE);
		middleSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		middleSpacer = new Label(this, SWT.NONE);
		middleSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

		addActionButton("SAVE RUN", "save.png", actions.save);

		Label bottomSpacer = new Label(this, SWT.NONE);
		bottomSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

		IBEXButtonFactory.helpButton(parent, HELP_LINK, DESCRIPTION);
	}

	private void addActionButton(String text, String imageFileName, final Action action) {
		Image image = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/" + imageFileName);
		IBEXButtonFactory.actionButton(this, action, text, image);
	}
}
