package uk.ac.stfc.isis.ibex.ui.mainmenu.managermode;

import javax.security.auth.login.FailedLoginException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.managermode.IManagerModeModel;

/**
 * Base class for dialogs which prompt the user for the manager password.
 */
public abstract class ManagerModeDialog extends TitleAreaDialog {

    private Composite upperDialogArea;

    private final IManagerModeModel model;

    private Text passwordEntryField;

    /**
     * Constructor.
     * 
     * @param parentShell
     *            the parent shell
     * @param model
     *            the view model
     */
    protected ManagerModeDialog(Shell parentShell, IManagerModeModel model) {
        super(parentShell);
        upperDialogArea = (Composite) super.createDialogArea(parentShell);
        upperDialogArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        this.model = model;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(getWindowTitle());

        shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Point getInitialSize() {
        return new Point(400, 200);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void okPressed() {

        try {
            model.authenticate(passwordEntryField.getText());
            super.okPressed();
        } catch (FailedLoginException ex) {
            displayError(this.getShell(), ex.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(getAreaTitle());

        Composite container = (Composite) super.createDialogArea(parent);
        Group group = new Group(container, SWT.NONE);
        group.setLayout(new GridLayout(2, true));

        Label passwordEntryLabel = new Label(group, SWT.LEFT);
        passwordEntryLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        passwordEntryLabel.setText(getQuestion());

        passwordEntryField = new Text(group, SWT.PASSWORD | SWT.BORDER);
        passwordEntryField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        return container;
    }

    private static void displayError(Shell shell, String message) {
        MessageDialog error = new MessageDialog(shell, "Error", null,
                message, MessageDialog.ERROR, new String[] {"OK"}, 0);
        error.open();
    }
    
    /**
     * The title to use for the area within the dialog.
     * @return The title to use for the area within the dialog.
     */
    public abstract String getAreaTitle();
    
    /**
     * The dialog title.
     * @return the dialog title
     */
    public abstract String getWindowTitle();
    
    /**
     * The question to ask the user.
     * @return the question to ask the user.
     */
    public abstract String getQuestion();
}
