
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

// Edit which components are included in this config
package uk.ac.stfc.isis.ibex.ui.configserver.editing.components;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableComponents;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DoubleListEditor;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Panel containing controls to add/remove components to/from a configuration.
 */
public class ComponentEditorPanel extends Composite {
	private DoubleListEditor editor;
	private EditableComponents components;

    /**
     * Constructor for the panel.
     * 
     * @param parent The parent composite
     * @param style The SWT style parameter
     * @param messageDisplayer The message displayer
     */
	public ComponentEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		editor = new DoubleListEditor(this, SWT.NONE, "name", false);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

    /**
     * Sets the configuration to edit.
     * 
     * @param config The configuration
     */
    public void setConfig(final EditableConfiguration config) {

		components = config.getEditableComponents();
		IObservableList selected = BeanProperties.list("selected").observe(components);
		IObservableList unselected = BeanProperties.list("unselected").observe(components);
		editor.bind(unselected, selected);

        final DuplicateChecker duplicateChecker = new DuplicateChecker();

		editor.addSelectionListenerForSelecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
                duplicateChecker.setBase(config.asConfiguration());
                Collection<Configuration> toToggle = editor.unselectedItems();
                Map<String, Set<String>> allConflicts = duplicateChecker.checkOnAdd(toToggle);

                Iterator<Configuration> iter = toToggle.iterator();
                while (iter.hasNext()) {
                    Configuration comp = iter.next();
                    if (allConflicts.keySet().contains(comp.getName())) {
                        iter.remove();
                    }
                }

                if (allConflicts.isEmpty()) {
                    components.toggleSelection(toToggle);
                } else {
                    new MessageDialog(getShell(), "Conflicts with current configuration", null,
                            buildWarning(allConflicts),
                            MessageDialog.WARNING, new String[] {"Ok"}, 0).open();
                }
			}
		});
		
		editor.addSelectionListenerForUnselecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				components.toggleSelection(editor.selectedItems());
			}
		});
	}

    private String buildWarning(Map<String, Set<String>> conflicts) {
        boolean multi = (conflicts.size() > 1);
        StringBuilder sb = new StringBuilder();
        sb.append(
                "Cannot add the selected components, as it would result in duplicate blocks in this configuration. "
                        + "Conflicts detected for the following block" + (multi ? "s" : "") + ":\n\n");

        for (String block : conflicts.keySet()) {
            sb.append("Block \"" + block + "\" contained in:\n");
            Set<String> sources = conflicts.get(block);
            for (String source : sources) {
                sb.append("\u2022 " + source + "\n");
            }
            sb.append("\n");
        }
        sb.append("Please rename or remove the duplicate block" + (multi ? "s" : "")
                + " before adding these components.");
        return sb.toString();
    }
}
