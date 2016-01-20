
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.logger.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	public PreferencePage() {
		super(GRID);
		setPreferenceStore(IsisLog.getDefault().getPreferenceStore());
		setDescription("Settings for program logging. Changes require restart before taking effect.");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_LOG_DIR,
				PreferenceConstants.LABEL_LOG_DIR, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_LOG_FILE,
				PreferenceConstants.LABEL_LOG_FILE, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_MESSAGE_PATTERN,
				PreferenceConstants.LABEL_MESSAGE_PATTERN,
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_ARCHIVE_PATTERN,
				PreferenceConstants.LABEL_ARCHIVE_PATTERN,
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_MAX_FILE_SIZE,
				PreferenceConstants.LABEL_MAX_FILE_SIZE, getFieldEditorParent()));
		addField(new IntegerFieldEditor(
				PreferenceConstants.P_MAX_ARCHIVE_PER_DAY,
				PreferenceConstants.LABEL_MAX_ARCHIVE_PER_DAY,
				getFieldEditorParent()));

		addField(new ComboFieldEditor(PreferenceConstants.P_LOGGING_LEVEL,
				PreferenceConstants.LABEL_LOGGING_LEVEL,
				PreferenceConstants.LOGGING_LEVELS, getFieldEditorParent()));
	}

	@Override
    public void init(IWorkbench workbench) {
	}

}