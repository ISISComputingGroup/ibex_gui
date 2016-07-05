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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * The parent view for holding the various beam and news widgets.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BeamStatusPanel extends ScrolledComposite {
	
	 /**
	  * Constructor for the panel holding the beam stats and news widgets.
	  * 
	  * @param parent The parent composite
	  * @param style The SWT style
	  */
    public BeamStatusPanel(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(1, false));
        setExpandHorizontal(true);
        setExpandVertical(true);

        Composite content = new Composite(this, SWT.NONE);
        content.setLayout(new GridLayout(1, false));

        GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd.widthHint = 400;
        content.setLayoutData(gd);

        Label newsLabel = new Label(content, SWT.NONE);
        String labelCurrentFontName = newsLabel.getFont().getFontData()[0].getName();
        Font titleFont = SWTResourceManager.getFont(labelCurrentFontName, 10, SWT.BOLD);
        newsLabel.setText("News");
        newsLabel.setFont(titleFont);

        McrNewsPanel newsPanel = new McrNewsPanel(content, SWT.NONE);

        Label statsLabel = new Label(content, SWT.NONE);
        statsLabel.setText("Beam Stats");
        statsLabel.setFont(titleFont);

        StatsPanel statsPanel = new StatsPanel(content, SWT.V_SCROLL);
        statsPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        this.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        setContent(content);
    }
}