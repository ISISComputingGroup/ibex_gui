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
package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A StyledText widget decorated with line numbers.
 */
public class NumberedStyledText extends StyledText {

    static final int WIDTH_PX = 12;

    /**
     * The constructor.
     * 
     * @param parent The parent container.
     * @param style The SWT style.
     */
    public NumberedStyledText(Composite parent, int style) {
        super(parent, style);
        addLineStyleListener(this);
        addModifyListener(this);
    }

    private void addLineStyleListener(final StyledText text) {
        text.addLineStyleListener(new LineStyleListener() {
            @Override
            public void lineGetStyle(LineStyleEvent e) {
                e.bulletIndex = text.getLineAtOffset(e.lineOffset);

                StyleRange style = new StyleRange();
                style.foreground = SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY);

                int lineDigits = Integer.toString(text.getLineCount() + 1).length();
                style.metrics =
                        new GlyphMetrics(0, 0, lineDigits * WIDTH_PX);

                // Create and set the bullet
                e.bullet = new Bullet(ST.BULLET_NUMBER, style);
            }
        });
    }

    private void addModifyListener(final StyledText text) {
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                // Redraw the text when max line 
                text.redraw();
            }
        });
    }
}
