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
package uk.ac.stfc.isis.ibex.ui.nicos;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

/**
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosControlButtonPanel extends Composite {

    private Button btnStop;
    private Button btnToggleRun;

    public NicosControlButtonPanel(Composite parent, int style) {
        super(parent, style);

        GridLayout gridLayout = new GridLayout(3, false);
        setLayout(gridLayout);

        btnToggleRun = new Button(this, SWT.CENTER);
        btnToggleRun.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        btnToggleRun.setText("Start");
        btnToggleRun.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/play.png"));

        btnStop = new Button(this, SWT.CENTER);
        btnStop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        btnStop.setText("Stop");
        btnStop.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/stop.png"));

        Label spacer = new Label(this, SWT.NONE);
        spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    }
}
