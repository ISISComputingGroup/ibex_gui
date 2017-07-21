package uk.ac.stfc.isis.ibex.e4.ui.configeditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

public class ConfigEditorDialog extends Dialog {

	public ConfigEditorDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	public int open() {
		return super.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		GridLayout glParent = new GridLayout(1, false);
		glParent.marginHeight = 0;
		glParent.marginWidth = 0;
		glParent.horizontalSpacing = 1;
		glParent.verticalSpacing = 0;
		parent.setLayout(glParent);

		Label prototype = new Label(parent, SWT.NONE);
		GridData gd_prototype = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		Image img = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.e4.ui", "screenshots/ConfigEditor.png");
		prototype.setImage(img);
		prototype.setLayoutData(gd_prototype);

		return container;
	}
}