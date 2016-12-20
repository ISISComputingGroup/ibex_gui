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

package uk.ac.stfc.isis.ibex.log.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.stfc.isis.ibex.log.Log;

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
	setPreferenceStore(Log.getDefault().getPreferenceStore());
	setDescription("Settings for connection to JMS server and SQL Database.");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
	addField(new IntegerFieldEditor(PreferenceConstants.P_JMS_PORT,
		"JMS Server Port:", getFieldEditorParent()));
	addField(new StringFieldEditor(PreferenceConstants.P_JMS_TOPIC,
		"JMS Message Topic:", getFieldEditorParent()));

	addField(new StringFieldEditor(PreferenceConstants.P_MESSAGE_SQL_SCHEMA,
		"SQL Server Schema:", getFieldEditorParent()));
	addField(new StringFieldEditor(PreferenceConstants.P_MESSAGE_SQL_USERNAME,
		"SQL Server Username:", getFieldEditorParent()));

	// Star out password field
	StringFieldEditor password = new StringFieldEditor(
		PreferenceConstants.P_MESSAGE_SQL_PASSWORD, "SQL Server Password:",
		getFieldEditorParent()) {
	    @Override
	    protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.doFillIntoGrid(parent, numColumns);

		getTextControl().setEchoChar('*');
	    }
	};

	addField(password);

	addField(new BooleanFieldEditor(PreferenceConstants.P_MINOR_MESSAGE,
		"Display minor errors on IOC Log button",
		getFieldEditorParent()));

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
    }

}