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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 *
 */
public class TargetSelectorPanel extends Composite {

    private Map<String, List<String>> availableOpis;

    /**
     * @param parent
     *            the parent composite
     * @param style
     *            the style of this composite
     */
    public TargetSelectorPanel(Composite parent, int style, Map<String, List<String>> availableOpis) {
        super(parent, style);
        this.availableOpis = availableOpis;
        setLayout(new GridLayout(1, false));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        draw();
    }

    private void draw() {

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

            for (String target : targets) {
                TreeItem targetTreeItem = new TreeItem(category, 0);
                targetTreeItem.setText(target);
            }
        }
    }

}
