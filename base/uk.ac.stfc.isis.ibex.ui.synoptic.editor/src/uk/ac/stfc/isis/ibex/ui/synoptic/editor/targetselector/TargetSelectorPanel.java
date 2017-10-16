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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.targetselector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.Utils;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 *
 */
public class TargetSelectorPanel extends Composite {

    private Map<String, List<String>> availableOpis;
    private final SynopticViewModel viewModel;

    /**
     * @param parent
     *            the parent composite
     * @param style
     *            the style of this composite
     * @param viewModel
     *            the synoptic view model
     * @param availableOpis
     *            the available opi groups
     */
    public TargetSelectorPanel(Composite parent, int style, final SynopticViewModel viewModel,
            Map<String, List<String>> availableOpis) {
        super(parent, style);

        this.availableOpis = availableOpis;
        this.viewModel = viewModel;

        setLayout(new GridLayout(1, false));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // Draw the components
        drawNameBox();
        drawTargetTree();

        Utils.recursiveSetEnabled(this, false);

        viewModel.addPropertyChangeListener("selectedComponents", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (viewModel.getSingleSelectedComp() != null) {
                    Utils.recursiveSetEnabled(TargetSelectorPanel.this, true);
                } else {
                    Utils.recursiveSetEnabled(TargetSelectorPanel.this, false);
                }
            }
        });
    }

    private void drawTargetTree() {

        final List<String> keyset = new ArrayList<>(new TreeSet<>(availableOpis.keySet()));
        Collections.sort(keyset);

        final Tree tree = new Tree(this, SWT.BORDER);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        for (String key : keyset) {

            if (availableOpis.get(key) == null || availableOpis.get(key).isEmpty()) {
                continue;
            }

            List<String> targets = new ArrayList<>(availableOpis.get(key));
            Collections.sort(targets);

            TreeItem category = new TreeItem(tree, 0);
            category.setText(key);
            category.setExpanded(false);
            category.setData(null);

            for (String target : targets) {
                TreeItem targetTreeItem = new TreeItem(category, 0);
                targetTreeItem.setText(target);
                targetTreeItem.setData(viewModel.getOpi(target));
            }
        }
        
        tree.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Correct things aren't selected or too many things are
                // selected.
                if (tree.getSelectionCount() != 1 || viewModel.getSingleSelectedComp() == null) {
                    return;
                }

                TreeItem item = tree.getSelection()[0];

                // Was a category not an item.
                if (item.getData() == null) {
                    return;
                }

                viewModel.getSingleSelectedComp().setTarget(new TargetDescription(item.getText(0), TargetType.OPI));
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
    }

    private void drawNameBox() {

    }

}
