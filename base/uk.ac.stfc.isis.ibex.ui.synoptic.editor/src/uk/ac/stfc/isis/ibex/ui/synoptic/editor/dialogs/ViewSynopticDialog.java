package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.SynopticPreview;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.validators.SynopticValidator;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;

/**
 * Dialog for viewing a synoptic.
 */
@SuppressWarnings("magicnumber")
public class ViewSynopticDialog extends TitleAreaDialog {
	private static final Point INITIAL_SIZE = new Point(950, 800);
	    private final String title;
	    private final String subtitle;
	    private EditorPanel editor;
	    private Button previewBtn;

	    private SynopticViewModel synopticViewModel;

	    private SynopticValidator synopticValidator;

	    /**
	     * The constructor for the overall Synoptic editor dialog.
	     * 
	     * @param parentShell
	     *            The shell to open the dialog in.
	     * @param title
	     *            The title of the dialog.
	     * @param subtitle
	     * 		  Title within the dialog.
	     * @param isBlank
	     *            Whether the synoptic is blank or not, i.e. a new synoptic.
	     * @param synopticViewModel
	     *            The view model describing the logic of the synoptic editor
	     */
	    public ViewSynopticDialog(Shell parentShell, String title, String subtitle, boolean isBlank,
		    SynopticViewModel synopticViewModel) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.title = title;
		this.subtitle = subtitle;
		this.synopticViewModel = synopticViewModel;
		this.synopticValidator = new SynopticValidator(synopticViewModel.getSynoptic());
	    }

	    @Override
	    protected Control createDialogArea(Composite parent) {
		editor = new EditorPanel(parent, SWT.NONE, synopticViewModel);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setTitle(subtitle);
		return editor;
	    }


	    @Override
	    protected void createButtonsForButtonBar(Composite parent) {
		previewBtn = createButton(parent, IDialogConstants.CLIENT_ID + 3, "Synoptic Preview", false);
		previewBtn.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
			SynopticPreview previewDialog = new SynopticPreview(getShell(), synopticViewModel.getSynoptic());
			previewDialog.open();
		    }

		});

		synopticValidator.addPropertyChangeListener("error", new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			updateErrors((ErrorMessage) evt.getNewValue());
		    }
		});

		updateErrors(synopticValidator.getError());

		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	    }	

	    private void updateErrors(ErrorMessage error) {
		setErrorMessage(error.getMessage());
	    }

	    @Override
	    protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	    }

	    @Override
	    protected Point getInitialSize() {
		return INITIAL_SIZE;
	    }
}
