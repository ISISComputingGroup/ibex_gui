package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class EditSynopticDialog extends Dialog {
	
	private static final Point INITIAL_SIZE = new Point(950, 800);
	private final String title;
	
	private InstrumentDescription synoptic;
	
	private EditorPanel editor;
	private boolean isBlank;
	private Button saveAsBtn;
	
	public EditSynopticDialog(
			Shell parentShell, 
			String title, 
			InstrumentDescription synoptic,
			boolean isBlank) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.title = title;
		this.synoptic = synoptic;
		this.isBlank = isBlank;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		editor = new EditorPanel(parent, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		editor.setSynopticToEdit(synoptic);
		return editor;
	}

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (isBlank == false) { 
			createButton(parent, IDialogConstants.OK_ID, "Save", true);
		}
		saveAsBtn = createButton(parent, IDialogConstants.CLIENT_ID + 1, "Save as ...", false);
		
		saveAsBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SaveSynopticDialog dlg = new SaveSynopticDialog(null, synoptic.name(), SynopticInfo.names(Synoptic.getInstance().availableSynoptics()));
				if (dlg.open() == Window.OK) {
					synoptic.setName(dlg.getNewName());
					
					okPressed();
				}
			}
		});
		
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
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
	
	public InstrumentDescription getSynoptic() {
		return synoptic;
	}
}
