
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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * Handler for creating a new synoptic.
 */
public class NewSynopticHandler extends SynopticEditorHandler {

	private static final String TITLE = "New Synoptic";
	
	/**
	 * Run new synoptic from the menu. 
	 * @param shell shell to open the dialogue in.
	 * @return nothing
	 * @throws ExecutionException if there is a error
	 */
	@Execute
	public Object execute(Shell shell) throws ExecutionException {
		openDialog(shell, new SynopticDescription(), TITLE, true);
		return null;
	}
}
