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

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

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

	public void init(IWorkbench workbench) {
	}

}