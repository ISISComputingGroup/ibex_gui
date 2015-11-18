
/*
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

package uk.ac.stfc.isis.ibex.ui.runcontrol.dialogs;

import java.util.Arrays;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

@SuppressWarnings({ "checkstyle:magicnumber" })
public class RunControlSettingsTable extends DataboundTable<DisplayBlock> {

    private final CellDecorator<DisplayBlock> rowDecorator = new RunControlSettingCellDecorator();

    public RunControlSettingsTable(Composite parent, int style, int tableStyle) {
        super(parent, style, DisplayBlock.class, tableStyle);

        initialise();
    }

    @Override
    protected void addColumns() {
        addName();
        addValue();
        addInRange();
        addEnabled();
        addLowLimit();
        addHighLimit();
    }

    private void addName() {
        TableViewerColumn name = createColumn("Name", 4);
        name.setLabelProvider(
                new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("name"), Arrays.asList(rowDecorator)) {
                    @Override
                    protected String valueFromRow(DisplayBlock setting) {
                        if (setting != null) {
                            return setting.getName();
                        }
                        return "";
                    }
                });
    }

    private void addValue() {
        TableViewerColumn name = createColumn("Value", 2);
        name.setLabelProvider(
                new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("value"), Arrays.asList(rowDecorator)) {
                    @Override
                    protected String valueFromRow(DisplayBlock setting) {
                        if (setting != null) {
                            return setting.getValue();
                        }
                        return "";
                    }
                });
    }

    private void addInRange() {
        TableViewerColumn name = createColumn("In Range", 2);
        name.setLabelProvider(
                new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("inRange"), Arrays.asList(rowDecorator)) {
                    @Override
                    protected String valueFromRow(DisplayBlock setting) {
                        if (setting != null) {
                            return setting.getInRange().toString();
                        }
                        return "";
                    }
                });
    }

    private void addEnabled() {
        TableViewerColumn enabled = createColumn("Enabled", 2);
        enabled.setLabelProvider(
                new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("enabled"), Arrays.asList(rowDecorator)) {
                    @Override
                    protected String valueFromRow(DisplayBlock setting) {
                        if (setting != null) {
                            return setting.getEnabled().toString();
                        }
                        return "";
                    }
                });
    }

    private void addLowLimit() {
        TableViewerColumn name = createColumn("Low Limit", 2);
        name.setLabelProvider(
                new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("lowLimit"), Arrays.asList(rowDecorator)) {
                    @Override
                    protected String valueFromRow(DisplayBlock setting) {
                        if (setting != null) {
                            return setting.getLowLimit();
                        }
                        return "";
                    }
                });
    }

    private void addHighLimit() {
        TableViewerColumn name = createColumn("High Limit", 2);
        name.setLabelProvider(new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("highLimit"),
                Arrays.asList(rowDecorator)) {
            @Override
            protected String valueFromRow(DisplayBlock setting) {
                if (setting != null) {
                    return setting.getHighLimit();
                }
                return "";
            }
        });
    }

}
