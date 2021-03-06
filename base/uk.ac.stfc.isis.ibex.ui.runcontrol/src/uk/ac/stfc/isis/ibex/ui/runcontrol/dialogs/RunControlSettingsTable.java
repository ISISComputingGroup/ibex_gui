
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

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * A table displaying the run control settings.
 */
@SuppressWarnings({ "checkstyle:magicnumber" })
public class RunControlSettingsTable extends DataboundTable<DisplayBlock> {

    private final CellDecorator<DisplayBlock> rowDecorator = new RunControlSettingCellDecorator();
    
    /**
     * A class that creates the run control settings table.
     * 
     * @param parent
     *              The parent to which the table belongs.
     * @param style
     *              The SWT style.
     * @param tableStyle
     *              The SWT table style.
     *                  
     */
    public RunControlSettingsTable(Composite parent, int style, int tableStyle) {
        super(parent, style, tableStyle);

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
        addSuspendIfInvalid();
    }

    private void addName() {
        createColumn("Name", 4, new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("name"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayBlock setting) {
                        if (setting != null) {
                        	if (setting.getName() != null) {
                        		return setting.getName();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addValue() {
        createColumn("Value", 2, new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("value"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayBlock setting) {
                        if (setting != null) {
                        	if (setting.getValue() != null) {
                        		return setting.getValue();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addInRange() {
        createColumn("In Range", 2, new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("inRange"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayBlock setting) {
                        if (setting != null) {
                        	if (setting.getInRange() != null) {
                        		return setting.getInRange().toString();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addEnabled() {
        createColumn("Enabled", 2, new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("runControlEnabled"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayBlock setting) {
                        if (setting != null) {
                        	if (setting.getRunControlEnabled() != null) {
                        		return setting.getRunControlEnabled().toString();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addLowLimit() {
        createColumn("Low Limit", 2, new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("runControlLowLimit"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayBlock setting) {
                        if (setting != null) {
                        	if (setting.getRunControlLowLimit() != null) {
                        		return setting.getRunControlLowLimit().toString();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addHighLimit() {
        createColumn("High Limit", 2, new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("runControlHighLimit"),
                Arrays.asList(rowDecorator)) {
            @Override
			public String stringFromRow(DisplayBlock setting) {
                if (setting != null) {
                	if (setting.getRunControlHighLimit() != null) {
                		 return setting.getRunControlHighLimit().toString();
                	}
                }
                return "";
            }
        });
    }
    
    private void addSuspendIfInvalid() {
        createColumn("Suspend if invalid", 2, new DecoratedCellLabelProvider<DisplayBlock>(observeProperty("suspendIfInvalid"),
                Arrays.asList(rowDecorator)) {
            @Override
			public String stringFromRow(DisplayBlock setting) {
                if (setting != null) {
                	if (setting.getSuspendIfInvalid() != null) {
                		return setting.getSuspendIfInvalid().toString();
                	}
                }
                return "";
            }
        });
    }

}
