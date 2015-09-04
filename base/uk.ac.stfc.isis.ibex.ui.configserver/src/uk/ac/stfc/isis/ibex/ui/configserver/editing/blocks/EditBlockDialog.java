package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;

public class EditBlockDialog extends Dialog {
	
	EditableBlock block;
	BlockDetailsPanel blockDetailsPanel;

	protected EditBlockDialog(Shell parentShell, EditableBlock block) {
		super(parentShell);
		this.block = block;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		blockDetailsPanel = new BlockDetailsPanel(parent, SWT.NONE, block);
		return blockDetailsPanel;
	}
	
	@Override
	protected void okPressed() {
		block.setName(blockDetailsPanel.getBlockName());
		block.setPV(blockDetailsPanel.getPV());
		block.setIsLocal(blockDetailsPanel.getIsLocal());
		block.setIsVisible(blockDetailsPanel.getIsVisible());
		
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Block Configuration");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Save", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}
}
