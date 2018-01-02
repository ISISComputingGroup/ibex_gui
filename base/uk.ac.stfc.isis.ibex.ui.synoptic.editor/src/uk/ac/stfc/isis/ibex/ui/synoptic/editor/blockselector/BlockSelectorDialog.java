
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.blockselector;

import java.util.Collection;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;


/**
 * A dialog for selecting a block, providing the block name and block PV address.
 */
public class BlockSelectorDialog extends TitleAreaDialog {

	private BlockSelectorPanel selector;
    private Collection<EditableBlock> availableBlocks;
	private Block block;
	
    /**
     * The constructor.
     * 
     * @param parentShell
     *            The parent shell to create the dialog within.
     * @param blocks
     *            The list of available blocks to select from.
     */
    public BlockSelectorDialog(Shell parentShell, Collection<EditableBlock> blocks) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
        this.availableBlocks = blocks;
        block = new Block("", "", true, true);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        selector = new BlockSelectorPanel(parent, SWT.NONE, availableBlocks);
        selector.setBlock(block);
		selector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));		
		setTitle("Block Selector");
		
		return selector;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, 
				IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
    /**
     * @return The name of the block that has been selected.
     */
	public String getBlockName() {
		return block.getName();
	}
	
    /**
     * @return The PV address of the block that has been selected.
     */
	public String getPVAddress() {
		return block.getPV();
	}

}
