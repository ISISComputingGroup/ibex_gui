
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
package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */
public class McrNewsPanel extends Composite {
    private static final int FONT_SIZE = 12;
    
    private Text txtTheMcrNews;
    
    /**
     * @param parent
     * @param style
     */
    public McrNewsPanel(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(1, false));

        txtTheMcrNews = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        txtTheMcrNews.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        txtTheMcrNews.setText("The MCR news should be shown here.");

        Font font = modifyDefaultFont(txtTheMcrNews.getFont());
        txtTheMcrNews.setFont(font);
    }

    public void setText(String text) {
        int topIndex = txtTheMcrNews.getTopIndex();
        Point selection = txtTheMcrNews.getSelection();

        txtTheMcrNews.setText(text);

        txtTheMcrNews.setTopIndex(topIndex);
        txtTheMcrNews.setSelection(selection);
    }

    private Font modifyDefaultFont(Font font) {
        FontData fontData = font.getFontData()[0];
        fontData.setHeight(FONT_SIZE);
        return new Font(Display.getCurrent(), fontData);
    }
}
