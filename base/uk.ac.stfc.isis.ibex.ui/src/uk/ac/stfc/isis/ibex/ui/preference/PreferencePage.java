package uk.ac.stfc.isis.ibex.ui.preference;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.preferences.Preferences;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {	
	public PreferencePage() {
		super(GRID);
	}

    /** {@inheritDoc} */
	@Override
	public void init(IWorkbench workbench) {
        setPreferenceStore(Preferences.getDefault().getPreferenceStore());
        setDescription("");
	}

    /** {@inheritDoc} */
	@Override
	protected void createFieldEditors() {
        addField(new FileFieldEditor(PreferenceSupplier.PYTHON_INTERPRETER_PATH, "Python interpreter path", getFieldEditorParent()));
        addField(new DirectoryFieldEditor(PreferenceSupplier.EPICS_BASE_DIRECTORY, "Epics base directory :", getFieldEditorParent()));
        addField(new DirectoryFieldEditor(PreferenceSupplier.EPICS_UTILS_DIRECTORY, "Epics utils directory :", getFieldEditorParent()));
        addField(new DirectoryFieldEditor(PreferenceSupplier.GENIE_PYTHON_DIRECTORY, "Genie Python directory :", getFieldEditorParent()));
        addField(new DirectoryFieldEditor(PreferenceSupplier.PYEPICS_DIRECTORY, "PyEpics directory :", getFieldEditorParent()));
	}

	/** {@inheritDoc} */
	@Override
	public final void propertyChange(final PropertyChangeEvent event) {
		setMessage("Please restart the application to apply the changed settings", IMessageProvider.INFORMATION);
		super.propertyChange(event);
	}
}
