/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * The parent view for holding the various beam and news widgets.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BeamStatusStatsAndNewsPanel extends ScrolledComposite {

    public BeamStatusStatsAndNewsPanel(Composite parent, int style) {
        super(parent, SWT.BORDER | SWT.V_SCROLL);
        setAlwaysShowScrollBars(true);
        setExpandHorizontal(true);
        setExpandVertical(true);
        setLayout(new GridLayout(1, false));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

        Composite statusNewsPanel = new Composite(this, SWT.NONE);
        statusNewsPanel.setLayout(new GridLayout(1, false));
        GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
        statusNewsPanel.setLayoutData(gd);

        McrNewsPanel news = new McrNewsPanel(statusNewsPanel, SWT.NONE);
        news.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        StatusPanel status = new StatusPanel(statusNewsPanel, SWT.V_SCROLL);
        status.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1));

        this.setMinSize(statusNewsPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        setContent(statusNewsPanel);
    }
}