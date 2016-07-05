
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.perspectives;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.stfc.isis.ibex.preferences.Preferences;

/**
 * A preference page for setting which of the ISIS perspectives 
 */
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
		for (IsisPerspective perspective : Activator.getDefault().perspectives().get()) {
			addField(new BooleanFieldEditor(perspective.id(), perspective.name(), getFieldEditorParent()));
		}
	}

	/** {@inheritDoc} */
	@Override
	public final void propertyChange(final PropertyChangeEvent event) {
		setMessage("Please restart the application to apply the changed settings", IMessageProvider.INFORMATION);
		super.propertyChange(event);
	}
}
