package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class EditBlockDialog extends Dialog {
	
	EditableBlock block;
	BlockDetailsPanel container;
	MessageDisplayer messageDisplayer;

	protected EditBlockDialog(Shell parentShell, MessageDisplayer messageDisplayer, EditableBlock block) {
		super(parentShell);
		this.block = block;
		this.messageDisplayer = messageDisplayer;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		container = new BlockDetailsPanel(parent, SWT.NONE, messageDisplayer); //(Composite) super.createDialogArea(parent);
		container.setBlock(block);
		return container;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Block Configuration");
	}
}
