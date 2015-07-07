
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

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
