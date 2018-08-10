package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.models.LoadScriptAction;
import uk.ac.stfc.isis.ibex.ui.nicos.models.SaveScriptAction;
/**
 * A toolbar that allows the user to interact with a queued script.
 */
public class QueuedScriptToolbar {    
	/**
	 * A toolbar that allows the user to interact with a queued script.
	 * @param parent The composite that the toolbar belongs to.
	 * @param script The script to interact with.
	 * @param writePermission Whether the user has permission to write to the script.
	 */
	public QueuedScriptToolbar(Composite parent, QueuedScript script, boolean writePermission) {		
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.LEFT);
		ToolItem loadScript = new ToolItem(toolbar, SWT.PUSH);
	    loadScript.setToolTipText("Load Script");
	    loadScript.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.nicos", "icons/open_folder.png"));
	    loadScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				(new LoadScriptAction(parent.getShell(), script)).execute();
			}
		});
	    loadScript.setEnabled(writePermission);
	    
		ToolItem saveScript = new ToolItem(toolbar, SWT.PUSH);
		saveScript.setToolTipText("Save Script");
		saveScript.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.nicos", "icons/save.png"));
		saveScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				(new SaveScriptAction(parent.getShell(), script)).execute();
			}
		});
	}

}
