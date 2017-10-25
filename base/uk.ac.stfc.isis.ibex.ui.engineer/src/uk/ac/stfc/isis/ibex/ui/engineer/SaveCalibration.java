
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

package uk.ac.stfc.isis.ibex.ui.engineer;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import uk.ac.stfc.isis.ibex.motor.Motors;

/**
 * Panel that allows users to save calibration parameters for all axes.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SaveCalibration extends ViewPart {
    private CheckboxTreeViewer checkboxTreeViewer;
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(2, false));

        checkboxTreeViewer = new CheckboxTreeViewer(parent, SWT.BORDER);
        Tree tree = checkboxTreeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        checkboxTreeViewer.setUseHashlookup(true);

        CheckboxTableViewer checkboxTableViewer =
                CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.FULL_SELECTION);
        Table table = checkboxTableViewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        new Label(parent, SWT.NONE);

        Button btnNewButton = new Button(parent, SWT.NONE);
        btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        btnNewButton.setText("Save as CSV");

        bind();
    }

    private void bind() {
        MotorTreeProvider provider = new MotorTreeProvider();
        checkboxTreeViewer.setContentProvider(provider);
        checkboxTreeViewer.setLabelProvider(provider);

        checkboxTreeViewer.setInput(Motors.getInstance().getMotorsTablesList().get(0));
	}

	@Override
	public void setFocus() {		
	}
}
