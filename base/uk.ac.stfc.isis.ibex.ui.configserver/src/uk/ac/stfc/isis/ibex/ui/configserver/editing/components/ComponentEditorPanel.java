
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableComponents;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DoubleListEditor;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Panel containing controls to add/remove components to/from a configuration.
 */
public class ComponentEditorPanel extends Composite {
	private MessageDisplayer messageDisplayer;
	private EditableConfiguration config;
	private DoubleListEditor editor;
	private EditableComponents components;
    private Map<String, String> allCurrentBlocks;

    /**
     * Constructor for the panel.
     * 
     * @param parent The parent composite
     * @param style The SWT style parameter
     * @param messageDisplayer The message displayer
     */
	public ComponentEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		this.messageDisplayer = messageDisplayer;
		setLayout(new GridLayout(1, false));
		
		editor = new DoubleListEditor(this, SWT.NONE, "name", false);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

    /**
     * Sets the configuration to edit.
     * 
     * @param config The configuration
     */
	public void setConfig(EditableConfiguration config) {
		this.config = config;
		
		components = config.getEditableComponents();
		IObservableList selected = BeanProperties.list("selected").observe(components);
		IObservableList unselected = BeanProperties.list("unselected").observe(components);
		editor.bind(unselected, selected);
		
        allCurrentBlocks = getAllCurrentBlocks();

		editor.addSelectionListenerForSelecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
                Map<String, Map<String, String>> allConflicts = new HashMap<String, Map<String, String>>();
                Collection<Configuration> toToggle = editor.unselectedItems();
                Iterator<Configuration> iter = toToggle.iterator();

                while (iter.hasNext()) {
                    Configuration comp = iter.next();
                    Map<String, String> conflicts = getBlockConflicts(comp);
                    if (!conflicts.isEmpty()) {
                        allConflicts.put(comp.getName(), conflicts);
                        iter.remove();
                    } else {
                        addCurrentBlocks(comp);
                    }
                }

                components.toggleSelection(toToggle);

                if (!allConflicts.isEmpty()) {
                    System.out.println("conflicts");
                    // error dialog
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

    private Map<String, String> getBlockConflicts(Configuration toCheck) {
        Map<String, String> conflicts = new HashMap<String, String>();

        for (Configuration comp : components.getSelected()) {
            for (Block block : comp.getBlocks()) {
                if (!allCurrentBlocks.containsKey(block.getName())) {
                    allCurrentBlocks.put(block.getName(), comp.getName());
                }
            }
        }
        for (Block block : toCheck.getBlocks()) {
            if (allCurrentBlocks.containsKey(block.getName())) {
                conflicts.put(block.getName(), allCurrentBlocks.get(block.getName()));
            }
        }
        return conflicts;
    }

    // TODO keep updated
    private Map<String, String> getAllCurrentBlocks() {
        Map<String, String> result = new HashMap<String, String>();
        for (Block block : config.getAvailableBlocks()) {
            String name = block.getName();
            String source = block.hasComponent() ? block.getComponent() : config.getName();
            result.put(name, source);
        }
        return result;
    }

    private void addCurrentBlocks(Configuration toAdd) {
        for (Block block : toAdd.getBlocks()) {
            allCurrentBlocks.put(block.getName(), toAdd.getName());
        }
    }
}
