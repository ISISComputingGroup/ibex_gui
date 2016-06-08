
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

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;

/**
 * Provides the display of the groups, which contain the selected blocks. Allows
 * showing and hiding of selected blocks.
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class Group extends Composite {
    private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    private static final int NUMBER_OF_ROWS = 9;
    private static final int NUMBER_OF_FIELDS = 3;

    /**
     * Provides the display of groups.
     * 
     * @param parent a widget which will be the parent of the new instance
     * @param style the style of widget to construct
     * @param group the group to be displayed
     * @param showHiddenBlocks whether hidden blocks should be shown
     */
    public Group(Composite parent, int style, DisplayGroup group, boolean showHiddenBlocks) {
        super(parent, style | SWT.BORDER);

        // Add the blocks to the list if they are visible, or if
        // showHiddenBlocks is true
        List<DisplayBlock> blocksList = new ArrayList<>();
        for (DisplayBlock block : group.blocks()) {
            if (block.getIsVisible() || showHiddenBlocks) {
                blocksList.add(block);
            }
        }

        // Calculate number of columns we need, each column holding one block
        // with a name and value
        int numberOfColumns = (blocksList.size() - 1) / NUMBER_OF_ROWS + 1;

        // For each block we need three columns in the grid layout, one for
        // name, one for value, one for
        // run control status, and for every column but the last we need a
        // divider label column
        GridLayout layout = new GridLayout(3 * numberOfColumns + (numberOfColumns - 1), false);
        layout.verticalSpacing = 7;
        this.setLayout(layout);
        this.setBackground(WHITE);

        // In the first column put the title in
        Label title = labelMaker(this, SWT.NONE, group.name(), "", null);
        Font titleFont = getEditedLabelFont(title, 10, SWT.BOLD);
        title.setFont(titleFont);

        // For the title row, fill with blanks
        for (int i = 0; i < (NUMBER_OF_FIELDS + 1) * numberOfColumns - 2; i++) {
            labelMaker(this, SWT.NONE, "", "", titleFont);
        }

        DataBindingContext bindingContext = new DataBindingContext();

        // Loop over the rows and columns. The GridLayout is filled with labels
        // across rows first, then moving on to
        // the next column. So blank labels need to be inserted so that columns
        // are always filled.
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                int position = i + j * NUMBER_OF_ROWS;

                if (position >= blocksList.size()) {
                    // put blank labels in these name and value columns
                    for (int k = 0; k < NUMBER_OF_FIELDS; k++) {
                        labelMaker(this, SWT.NONE, "", "", null);
                    }
                    break;
                }

                DisplayBlock currentBlock = blocksList.get(position);

                Label blockName =
                        labelMaker(this, SWT.NONE, currentBlock.getName() + ": ", currentBlock.getDescription(), null);
                blockName.setMenu(new BlocksMenu(currentBlock).createContextMenu(blockName));
                blockName.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));

                Label blockValue =
                        labelMaker(this, SWT.RIGHT, currentBlock.getValue(), currentBlock.getDescription(), null);
                blockValue.setMenu(new BlocksMenu(currentBlock).createContextMenu(blockName));
                GridData gdValue = new GridData(SWT.CENTER, SWT.NONE, false, false, 1, 1);
                gdValue.widthHint = 70;
                blockValue.setLayoutData(gdValue);

                Label blockStatus = labelMaker(this, SWT.CENTER, "", "Run Control Status", null);
                FontDescriptor boldDescriptor = FontDescriptor.createFrom(blockStatus.getFont()).setStyle(SWT.BOLD);
                Font boldFont = boldDescriptor.createFont(blockStatus.getDisplay());
                blockStatus.setFont(boldFont);
                GridData gdStatus = new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1);
                gdStatus.widthHint = 17;
                blockStatus.setLayoutData(gdStatus);

                addPaintListener(new LabelBorderListener(blockValue, currentBlock));

//				Graphics2D borderProperties

                if (j < numberOfColumns - 1) {
                    // insert divider label
                    labelMaker(this, SWT.NONE, "   |   ", "", null);
                }

                bindingContext.bindValue(WidgetProperties.text().observe(blockValue),
                        BeanProperties.value("value").observe(currentBlock));

                bindingContext.bindValue(WidgetProperties.visible().observe(blockStatus),
                        BeanProperties.value("enabled").observe(currentBlock));

                UpdateValueStrategy symbolStrategy = new UpdateValueStrategy();
                symbolStrategy.setConverter(new RunControlSymbolConverter());

                bindingContext.bindValue(WidgetProperties.text().observe(blockStatus),
                        BeanProperties.value("runcontrolState").observe(currentBlock), null, symbolStrategy);

                UpdateValueStrategy fgColourStrategy = new UpdateValueStrategy();
                fgColourStrategy.setConverter(new RunControlForegroundColourConverter());

                bindingContext.bindValue(WidgetProperties.foreground().observe(blockStatus),
                        BeanProperties.value("runcontrolState").observe(currentBlock), null, fgColourStrategy);

                UpdateValueStrategy bgColourStrategy = new UpdateValueStrategy();
                bgColourStrategy.setConverter(new RunControlBackgroundColourConverter());

                bindingContext.bindValue(WidgetProperties.background().observe(blockStatus),
                        BeanProperties.value("runcontrolState").observe(currentBlock), null, bgColourStrategy);

//                UpdateValueStrategy textColourStrategy = new UpdateValueStrategy();
//                textColourStrategy.setConverter(new DisconnectedForegroundColourConverter());
//
//                bindingContext.bindValue(WidgetProperties.foreground().observe(blockName),
//                        BeanProperties.value("blockState").observe(currentBlock), null, textColourStrategy);
//
//                bindingContext.bindValue(WidgetProperties.foreground().observe(blockValue),
//                        BeanProperties.value("blockState").observe(currentBlock), null, textColourStrategy);
            }
        }
    }

    private Font getEditedLabelFont(Label label, int size, int style) {
        final String currentFontName = label.getFont().getFontData()[0].getName();
        return SWTResourceManager.getFont(currentFontName, size, style);
    }

    private Label labelMaker(Composite composite, int style, String text, String toolTip, Font font) {
        Label label = new Label(composite, style);
        if (text != null) {
            label.setText(text);
        }

        label.setBackground(WHITE);

        if (toolTip != null) {
            label.setToolTipText(toolTip);
        }

        if (font != null) {
            label.setFont(font);
        }

        return label;
    }

    @Override
    public void setMenu(Menu menu) {
        super.setMenu(menu);
    }
}
