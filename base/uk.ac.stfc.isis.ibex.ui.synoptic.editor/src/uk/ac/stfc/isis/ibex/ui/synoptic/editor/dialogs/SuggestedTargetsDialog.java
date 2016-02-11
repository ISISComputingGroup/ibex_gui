
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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

/**
 * Opens a dialog showing a list of suggested targets when multiple suggested
 * targets exist.
 */
public class SuggestedTargetsDialog extends Dialog {
    
    private static final int DIALOG_WIDTH = 300;
    private static final int DIALOG_HEIGHT = 200;

    private List items;
    private Collection<TargetDescription> targetDescriptions;

    private String selectedTargetName;

    /**
     * @param parentShell
     */
    public SuggestedTargetsDialog(Shell parentShell, Collection<TargetDescription> targetDescriptions) {
        super(parentShell);
        this.targetDescriptions = targetDescriptions;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(DIALOG_WIDTH, DIALOG_HEIGHT);
    }

    @Override
    public Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);

        Label lblSelect = new Label(container, SWT.NONE);
        lblSelect.setText("Select Target:");

        items = new List(container, SWT.BORDER | SWT.V_SCROLL);
        items.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        items.setItems(targetDescriptionsToStringArray());

        items.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectedTargetName = items.getSelection()[0];
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                selectedTargetName = items.getSelection()[0];
            }
        });

        return container;
    }

    private String[] targetDescriptionsToStringArray() {
        Collection<String> names = new ArrayList<>();
        
        for (TargetDescription td : targetDescriptions) {
            names.add(td.name());
        }
        
        return names.toArray(new String[targetDescriptions.size()]);
    }

    public String selectedTargetName() {
        return selectedTargetName;
    }

    public TargetDescription selectedTarget() {
        TargetDescription targetDescription = new TargetDescription("NONE", TargetType.OPI);
        
        for (TargetDescription td : targetDescriptions) {
            if (td.name().equals(selectedTargetName)) {
                targetDescription = td;
            }
        }
        
        return targetDescription;
    }

}
