package uk.ac.stfc.isis.ibex.e4.ui.configeditor;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.IShellProvider;

public class EditCurrentConfigHandler {
	
    @Execute
    public void execute(IShellProvider shell) {
    	new ConfigEditorDialog(shell).open();
    }
    
    @CanExecute
    public boolean canExecute() {
    	return true;
    }
}