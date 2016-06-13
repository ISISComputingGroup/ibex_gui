
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;

/**
 * Draws a border around a given label by creating a canvas drawing a rectangle
 * behind it. Label should be in its own separate composite to avoid formatting
 * issues.
 */
public class LabelBorderListener implements PaintListener {
    Label source;
    Rectangle rText;
    Canvas border;
    int borderWidth;

    /**
     * @param label the label around which the border should be drawn.
     */
    public LabelBorderListener(Label label) {
        this.borderWidth = 2;
        this.source = label;
        this.border = new Canvas(source.getParent(), SWT.NONE);
    }

    /**
     * @param returns the drawn border object
     */
    public Canvas getBorder() {
        return this.border;
    }

    /**
     * Sets the width of the border (can not exceed the label's parent's
     * dimensions)
     * 
     * @param width the new border width
     */
    public void setBorderWidth(int width) {
        this.borderWidth = width;
    }

    /**
     * Defines how the object should behave on event that requires repainting
     * the frame.
     * 
     * @param width the new border width
     */
    @Override
    public void paintControl(PaintEvent e) {
        this.rText = this.source.getBounds();
        this.border.setBounds(rText.x - borderWidth, rText.y - borderWidth, rText.width + 2 * borderWidth,
                rText.height + 2 * borderWidth);
        this.border.setVisible(true);
    }
}
