package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
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
	private ToolBar toolbar;
	
	private ToolItem createToolItem(String tooltip, String icon, Consumer<Event> action) {
	    ToolItem toolItem = new ToolItem(toolbar, SWT.PUSH);
	    toolItem.setToolTipText(tooltip);
	    toolItem.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.nicos", "icons/" + icon));
	    toolItem.addListener(SWT.Selection, e -> action.accept(e));
	    return toolItem;
	}
	
	/**
	 * A toolbar that allows the user to interact with a queued script.
	 * @param parent The composite that the toolbar belongs to.
	 * @param script The script to interact with.
	 * @param writePermission Whether the user has permission to write to the script.
	 */
	public QueuedScriptToolbar(Composite parent, QueuedScript script, boolean writePermission) {		
		toolbar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.LEFT);
		ToolItem loadScript = createToolItem("Load Script", "open_folder.png", e ->
				(new LoadScriptAction(parent.getShell(), script)).execute());
	    loadScript.setEnabled(writePermission);
	    
	    createToolItem("Save Script", "save.png", e ->
				(new SaveScriptAction(parent.getShell(), script)).execute());
	}

}
