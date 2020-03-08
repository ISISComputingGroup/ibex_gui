package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.UIUtils;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;

/**
 * The dialog for editing the details of a script that is on the NICOS queue.
 */
public class EditScriptDialog extends ScriptDialog {

    private DataBindingContext bindingContext = new DataBindingContext();
    private static final Color ERROR_COLOR = SWTResourceManager.getColor(SWT.COLOR_RED);
    
	/**
	 *  The constructor for this class.
	 *  
	 * @param parentShell The shell that this dialog is created from.
	 * @param model The model for modifying the script queue
	 */
    public EditScriptDialog(Shell parentShell, QueueScriptViewModel model) {
		super(parentShell, model, false);
        this.script = model.getSelectedScript();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Label errorLabel = new Label(parent, SWT.NONE);
		errorLabel.setText("Script not found in queue.");
		errorLabel.setForeground(ERROR_COLOR);
		
		bindingContext.bindValue(WidgetProperties.visible().observe(errorLabel),
				BeanProperties.value("updateButtonEnabled").observe(model), null, UIUtils.NOT_CONVERTER);
		
		actionButton = createButton(parent, IDialogConstants.OK_ID, "Update", false);
		actionButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.alarm", "icons/refresh.png"));
		actionButton.addListener(SWT.Selection, e -> model.updateScript(script));

		bindingContext.bindValue(WidgetProperties.enabled().observe(actionButton),
				BeanProperties.value("updateButtonEnabled").observe(model));
		
		super.createButtonsForButtonBar(parent);

		// Account for label in parent layout
		GridLayout layout = (GridLayout) parent.getLayout();
		layout.numColumns++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
        shell.setText(script.getName());
	}
}
