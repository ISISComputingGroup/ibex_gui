
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import java.util.Optional;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
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
import uk.ac.stfc.isis.ibex.configserver.displaying.RuncontrolState;
import uk.ac.stfc.isis.ibex.epics.pv.PvState;

/**
 * Class for grouping information on a single block into one composite.
 */
public class GroupRow extends Composite {
    
    private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    private static final int NAME_WIDTH = 100;
    private static final int VALUE_WIDTH = 75;
    private static final int VALUE_HEIGHT = 17;
    private static final int VALUE_WIDTH_MARGIN = 4;
    private static final int VALUE_HEIGHT_MARGIN = 2;
    private static final int NUM_ELEMENTS = 3;
    
    private final Label lblName;
    private final Label lblValue;
    private final Label lblStatus;
    private final Composite valueContainer;
    
    private final DisplayBlock block;
    private final int rowIndex;

    /**
     * The constructor.
     * 
     * @param parent The parent composite
     * @param style The SWT style parameter
     * @param block The observed block
     * @param rowIndex Index of the row in the group
     */
    public GroupRow(Composite parent, int style, DisplayBlock block, int rowIndex) {
        super(parent, style);
        
        this.block = block;
        this.rowIndex = rowIndex;
        
        GridLayout layout = new GridLayout(NUM_ELEMENTS, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        this.setBackground(WHITE);
        this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        lblName = boundLabelMaker(this, SWT.NONE, Optional.ofNullable(block.getName()), block, "nameTooltipText");
        GridData gdLblName = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gdLblName.widthHint = NAME_WIDTH;
        lblName.setLayoutData(gdLblName);
        

        // additional container to hold value label to draw a border around it
        valueContainer = new Composite(this, SWT.NONE);
        GridLayout valueContainerLayout = new GridLayout(1, false);
        valueContainerLayout.marginWidth = 2;
        valueContainerLayout.marginHeight = 2;
        valueContainerLayout.verticalSpacing = 0;
        valueContainerLayout.horizontalSpacing = 0;
        valueContainer.setLayout(valueContainerLayout);

        GridData gdValueContainer = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gdValueContainer.widthHint = VALUE_WIDTH + VALUE_WIDTH_MARGIN;
        gdValueContainer.heightHint = VALUE_HEIGHT + VALUE_HEIGHT_MARGIN;
        valueContainer.setLayoutData(gdValueContainer);

        lblValue = boundLabelMaker(valueContainer, SWT.RIGHT, Optional.ofNullable(block.getValue()), block, "valueTooltipText");
        GridData gdValue = new GridData(SWT.CENTER, SWT.NONE, false, false, 1, 1);
        gdValue.widthHint = VALUE_WIDTH;
        lblValue.setLayoutData(gdValue);
        valueContainer.pack();

        lblStatus = statusLabelMaker(this, SWT.NONE, "Run Control Status");
        FontDescriptor boldDescriptor = FontDescriptor.createFrom(lblStatus.getFont()).setStyle(SWT.BOLD);
        Font boldFont = boldDescriptor.createFont(lblStatus.getDisplay());
        lblStatus.setFont(boldFont);
        boldFont.dispose();
        GridData gdStatus = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gdStatus.widthHint = VALUE_HEIGHT;
        lblStatus.setLayoutData(gdStatus);

        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.text().observe(lblValue),
                BeanProperties.<DisplayBlock, String>value("value").observe(block));

        bindingContext.bindValue(WidgetProperties.visible().observe(lblStatus),
                BeanProperties.<DisplayBlock, String>value("runControlEnabled").observe(block));
        
        final UpdateValueStrategy<RuncontrolState, String> runcontrolTooltipStrategy = new UpdateValueStrategy<>();
        
        bindingContext.bindValue(WidgetProperties.tooltipText().observe(lblStatus), 
        		BeanProperties.<DisplayBlock, RuncontrolState>value("runcontrolState").observe(block), null, runcontrolTooltipStrategy);

        final UpdateValueStrategy<RuncontrolState, String> symbolStrategy = new UpdateValueStrategy<>();
        symbolStrategy.setConverter(new RunControlSymbolConverter());

        bindingContext.bindValue(WidgetProperties.text().observe(lblStatus),
                BeanProperties.<DisplayBlock, RuncontrolState>value("runcontrolState").observe(block), null, symbolStrategy);
        
        final UpdateValueStrategy<RuncontrolState, Color> fgColourStrategy = new UpdateValueStrategy<>();
        fgColourStrategy.setConverter(new RunControlForegroundColourConverter());

        bindingContext.bindValue(WidgetProperties.foreground().observe(lblStatus),
                BeanProperties.<DisplayBlock, RuncontrolState>value("runcontrolState").observe(block), null, fgColourStrategy);

        final UpdateValueStrategy<RuncontrolState, Color> bgColourStrategy = new UpdateValueStrategy<>();
        bgColourStrategy.setConverter(new RunControlBackgroundColourConverter());

        bindingContext.bindValue(WidgetProperties.background().observe(lblStatus),
                BeanProperties.<DisplayBlock, RuncontrolState>value("runcontrolState").observe(block), null, bgColourStrategy);

        final UpdateValueStrategy<PvState, Color> borderStrategy = new UpdateValueStrategy<>();
        borderStrategy.setConverter(new BlockStatusBorderColourConverter());

        bindingContext.bindValue(WidgetProperties.background().observe(valueContainer),
                BeanProperties.<DisplayBlock, PvState>value("blockState").observe(block), null, borderStrategy);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMenu(Menu menu) {
        super.setMenu(menu);
        lblName.setMenu(menu);
        lblValue.setMenu(menu);
        lblStatus.setMenu(menu);
    }

    private Label statusLabelMaker(Composite composite, int style, String description) {
        Label label = new Label(composite, style);
        label.setBackground(WHITE);
        label.setToolTipText(description);
        return label;
    }
    
    private Label boundLabelMaker(Composite composite, int style, Optional<String> text, DisplayBlock block, String propertyName) {
        Label label = new Label(composite, style);
        label.setData("org.eclipse.e4.ui.css.CssClassName", "block");

        text.ifPresent(label::setText);

        label.setBackground(WHITE);

        DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.tooltipText().observe(label), 
        		BeanProperties.<DisplayBlock, String>value(propertyName).observe(block));

        return label;
    }
    
    /**
     * Returns PvState of the block that belongs to the group row.
     * @return state of the block
     */
    public PvState getBlockStatus() {
    	return block.getBlockState();
    }
    
    /**
     * Returns index of the row in the group, as supplied when creating the row using constructor.
     * @return index of this row
     */
    public int getRowIndex() {
    	return rowIndex;
    }
    
    /**
     * Returns container that holds the row's widgets.
     * @return value container for the row
     */
    public Composite getValueContainer() {
    	return valueContainer;
    }

}
