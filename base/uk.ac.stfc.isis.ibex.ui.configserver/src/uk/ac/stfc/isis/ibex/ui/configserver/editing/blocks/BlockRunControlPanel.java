
/**
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

public class BlockRunControlPanel extends Composite {
    private Text lowLimitText;
    private Text highLimitText;
    private Button btnEnabled;

    private String lowLimit;
    private String highLimit;
    private String enabled;

    public BlockRunControlPanel(Composite parent, int style, String blockName, RunControlServer runControl) {
        super(parent, style);

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpRuncontrolSettings = new Group(this, SWT.NONE);
        grpRuncontrolSettings.setText("Run-Control Settings");
        grpRuncontrolSettings.setLayout(new GridLayout(5, false));

        Label lblLowLimit = new Label(grpRuncontrolSettings, SWT.NONE);
        lblLowLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblLowLimit.setText("Low Limit:");

        lowLimitText = new Text(grpRuncontrolSettings, SWT.BORDER);
        lowLimitText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        setLowLimitRunControlValues(blockName, runControl);

        Label lblHighLimit = new Label(grpRuncontrolSettings, SWT.NONE);
        lblHighLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblHighLimit.setText("High Limit:");

        highLimitText = new Text(grpRuncontrolSettings, SWT.BORDER);
        highLimitText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        setHighLimitRunControlValues(blockName, runControl);

        btnEnabled = new Button(grpRuncontrolSettings, SWT.CHECK);
        btnEnabled.setText("Enabled");

        setEnabledRunControlValues(blockName, runControl);
    }

    protected String getLowLimit() {
        return lowLimitText.getText();
    }

    protected String getHighLimit() {
        return highLimitText.getText();
    }

    protected String getEnabledSetting() {
        if (btnEnabled.getSelection()) {
            return "YES";
        } else {
            return "NO";
        }
    }

    private void setLowLimitRunControlValues(String blockName, RunControlServer runControl) {
        final InitialiseOnSubscribeObservable<String> lowLimit = runControl.blockRunControlLowLimit(blockName);

        lowLimit.addObserver(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        lowLimitText.setText(lowLimit.getValue());
                    }
                });
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
            }
        });
    }

    private void setHighLimitRunControlValues(String blockName, RunControlServer runControl) {
        final InitialiseOnSubscribeObservable<String> highLimit = runControl.blockRunControlHighLimit(blockName);

        highLimit.addObserver(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        highLimitText.setText(highLimit.getValue());
                    }
                });
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
            }
        });
    }

    private void setEnabledRunControlValues(String blockName, RunControlServer runControl) {
        final InitialiseOnSubscribeObservable<String> enabled = runControl.blockRunControlEnabled(blockName);

        enabled.addObserver(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        btnEnabled.setSelection(enabled.getValue().equals("YES"));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
            }
        });
    }
}
