
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;

/**
 * 
 */
public class LabelBorderListener implements PaintListener {
    Label source;
    DataBindingContext bindingContext;
    UpdateValueStrategy borderStrategy;
    DisplayBlock block;
    Rectangle rText;
    Canvas border;

    public LabelBorderListener(Label label, DisplayBlock block) {
        this.source = label;
        this.block = block;
        this.bindingContext = new DataBindingContext();
        borderStrategy = new UpdateValueStrategy();
        borderStrategy.setConverter(new BlockStatusBorderColourConverter());
    }

    @Override
    public void paintControl(PaintEvent e) {
        this.rText = this.source.getBounds();
        this.border = new Canvas(source.getParent(), SWT.NONE);
        this.border.setBounds(rText.x - 2, rText.y - 2, rText.width + 4, rText.height + 4);
        this.border.setVisible(true);

        bindingContext.bindValue(WidgetProperties.background().observe(border),
                BeanProperties.value("blockState").observe(block), null, borderStrategy);
    }
}
