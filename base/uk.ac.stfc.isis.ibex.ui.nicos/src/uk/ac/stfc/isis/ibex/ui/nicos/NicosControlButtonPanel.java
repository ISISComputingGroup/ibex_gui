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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;

/**
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosControlButtonPanel extends Composite {

    DataBindingContext bindingContext = new DataBindingContext();

    private Label lblStatusReadback;
    private Button btnStop;
    private Button btnTogglePause;
    private ScriptStatusViewModel statusModel;

    /**
     * The constructor.
     * 
     * @param parent
     *            The parent composite
     * @param style
     *            The SWT style
     * @param statusModel
     *            The viewmodel for styling the view based on the script
     *            execution status
     */
    public NicosControlButtonPanel(Composite parent, int style, ScriptStatusViewModel statusModel) {
        super(parent, style);

        this.statusModel = statusModel;

        GridLayout gridLayout = new GridLayout(3, true);
        gridLayout.marginHeight = 10;
        gridLayout.marginWidth = 10;
        setLayout(gridLayout);

        lblStatusReadback = new Label(this, SWT.NONE);
        lblStatusReadback.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        btnTogglePause = new Button(this, SWT.CENTER);
        btnTogglePause.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnTogglePause.setText("Pause");
        btnTogglePause.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/pause.png"));

        btnStop = new Button(this, SWT.CENTER);
        btnStop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnStop.setText("Stop Script Execution");
        btnStop.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/stop.png"));

        bind();
    }

    private void bind() {
        bindingContext.bindValue(WidgetProperties.text().observe(btnTogglePause),
                BeanProperties.value("toggleButtonText").observe(statusModel));
        bindingContext.bindValue(WidgetProperties.image().observe(btnTogglePause),
                BeanProperties.value("toggleButtonIcon").observe(statusModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnTogglePause),
                BeanProperties.value("enableButtons").observe(statusModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnStop),
                BeanProperties.value("enableButtons").observe(statusModel));
        bindingContext.bindValue(WidgetProperties.text().observe(lblStatusReadback),
                BeanProperties.value("statusReadback").observe(statusModel));
        
        btnTogglePause.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                statusModel.toggleExecution();
            }
        });

        btnStop.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                statusModel.stopExecution();
            }
        });
    }
}
