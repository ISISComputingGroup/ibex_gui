
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockDuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableComponents;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.IocDuplicateChecker;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DoubleListEditor;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Panel containing controls to add/remove components to/from a configuration.
 */
public class ComponentEditorPanel extends Composite {
	private DoubleListEditor<Configuration> editor;
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
		
		editor = new DoubleListEditor<Configuration>(this, SWT.NONE, "name", false);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

    /**
     * Sets the configuration to edit.
     * 
     * @param config The configuration
     */
    public void setConfig(final EditableConfiguration config) {

		components = config.getEditableComponents();
		IObservableList<Configuration> selected = BeanProperties.list("selected").observe(components);
		IObservableList<Configuration> unselected = BeanProperties.list("unselected").observe(components);
		editor.bind(unselected, selected);

        DuplicateChecker<Block> blockDuplicateChecker = new BlockDuplicateChecker();
        DuplicateChecker<Ioc> iocDuplicateChecker = new IocDuplicateChecker();

		editor.addSelectionListenerForSelecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
			    blockDuplicateChecker.setBase(config.asConfiguration());
			    iocDuplicateChecker.setBase(config.asConfiguration());
                Collection<Configuration> toToggle = editor.unselectedItems();
                
                Map<String, Set<String>> blockConflicts = blockDuplicateChecker.checkItemsOnAdd(toToggle);
                Map<String, Set<String>> iocConflicts = iocDuplicateChecker.checkItemsOnAdd(toToggle);
                
                if (blockConflicts.isEmpty() && iocConflicts.isEmpty()) {
                    components.toggleSelection(toToggle);
                } else {
                    new MessageDialog(getShell(), "Conflicts in selected configuration", null,
                            DisplayConfiguration.buildWarning(blockConflicts, iocConflicts, "add", "component"),
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
}
