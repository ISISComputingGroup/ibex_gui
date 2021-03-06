
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.concurrent.TimeoutException;

import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditBlockHelper;

/**
 * The handler class for editing a block on its own, without being part of a
 * config panel.
 */
public final class EditBlockHandler extends EditConfigHandler {

    /** The name of the block being edited. */
    private String blockName = "";

    /**
     * Create the handler for opening the editor on one block.
     *
     * @param blockName The block to edit
     */
    public EditBlockHandler(String blockName) {
        super();
        this.blockName = blockName;
    }

    /**
     * Open the edit block dialogue.
     *
     * @param shell shell to open
     * @throws TimeoutException
     */
	@Override
	public void safeExecute(Shell shell) throws TimeoutException {
        new EditBlockHelper(shell, SERVER).createDialog(blockName);
    }

}
