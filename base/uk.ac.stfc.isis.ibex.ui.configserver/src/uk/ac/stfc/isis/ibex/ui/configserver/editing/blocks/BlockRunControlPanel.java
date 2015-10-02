
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
import org.eclipse.swt.graphics.Color;
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
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

public class BlockRunControlPanel extends Composite {
    private Text lowLimitText;
    private Text highLimitText;
    private Button btnEnabled;
    private Label statusLabel;

    private Subscription lowLimitSubscription;
    private Subscription highLimitSubscription;
    private Subscription enabledSubscription;
    private Subscription inRangeSubscription;

    private static final Color COLOR_RED = new Color(Display.getCurrent(), 192, 0, 0);
    private static final Color COLOR_GREEN = new Color(Display.getCurrent(), 0, 192, 0);

    public BlockRunControlPanel(Composite parent, int style, String blockName, RunControlServer runControl) {
        super(parent, style);

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpRuncontrolSettings = new Group(this, SWT.NONE);
        grpRuncontrolSettings.setText("Run-Control Settings");
        grpRuncontrolSettings.setLayout(new GridLayout(6, false));

        Label lblLowLimit = new Label(grpRuncontrolSettings, SWT.NONE);
        lblLowLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblLowLimit.setText("Low Limit:");

        lowLimitText = new Text(grpRuncontrolSettings, SWT.BORDER);
        lowLimitText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        lowLimitText.setEnabled(false);

        setLowLimitRunControlValues(blockName, runControl);

        Label lblHighLimit = new Label(grpRuncontrolSettings, SWT.NONE);
        lblHighLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblHighLimit.setText("High Limit:");

        highLimitText = new Text(grpRuncontrolSettings, SWT.BORDER);
        highLimitText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        highLimitText.setEnabled(false);

        setHighLimitRunControlValues(blockName, runControl);

        btnEnabled = new Button(grpRuncontrolSettings, SWT.CHECK);
        btnEnabled.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        btnEnabled.setText("Enabled");
        btnEnabled.setEnabled(false);

        setEnabledRunControlValues(blockName, runControl);

        statusLabel = new Label(grpRuncontrolSettings, SWT.NONE);
        statusLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        statusLabel.setText("Configuration must be saved before editing run-control settings for new blocks");
        new Label(grpRuncontrolSettings, SWT.NONE);
        new Label(grpRuncontrolSettings, SWT.NONE);

        setLabelText(blockName, runControl);
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

    public void removeObservers() {
        lowLimitSubscription.removeObserver();
        highLimitSubscription.removeObserver();
        enabledSubscription.removeObserver();
        inRangeSubscription.removeObserver();

    }

    private void setLowLimitRunControlValues(String blockName, RunControlServer runControl) {
        final InitialiseOnSubscribeObservable<String> lowLimit = runControl.blockRunControlLowLimit(blockName);

        lowLimitSubscription = lowLimit.addObserver(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        lowLimitText.setText(lowLimit.getValue());
                        lowLimitText.setEnabled(true);
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

        highLimitSubscription = highLimit.addObserver(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        highLimitText.setText(highLimit.getValue());
                        highLimitText.setEnabled(true);
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

        enabledSubscription = enabled.addObserver(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        btnEnabled.setSelection(enabled.getValue().equals("YES"));
                        btnEnabled.setEnabled(true);
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

    private void setLabelText(String blockName, RunControlServer runControl) {
        final InitialiseOnSubscribeObservable<String> inRange = runControl.blockRunControlInRange(blockName);

        inRangeSubscription = inRange.addObserver(new BaseObserver<String>() {
            @Override
            public void onValue(String value) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (inRange.getValue().equals("YES")) {
                            statusLabel.setText("Block not preventing run continuing");
                            statusLabel.setForeground(COLOR_GREEN);
                        } else {
                            statusLabel.setText("Block preventing run from continuing");
                            statusLabel.setForeground(COLOR_RED);
                        }
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
