/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
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

package uk.ac.stfc.isis.ibex.ui.alerts.dialogs;

import java.util.Arrays;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayAlerts;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * A table displaying the alerts control settings.
 */
public class AlertsSettingTable extends DataboundTable<DisplayAlerts> {
	private static final int WIDTH_WEIGHT = 4;
    private final CellDecorator<DisplayAlerts> rowDecorator = new AlertsControlSettingCellDecorator();
    
    /**
     * A class that creates the alert control settings table.
     * 
     * @param parent
     *              The parent to which the table belongs.
     * @param style
     *              The SWT style.
     * @param tableStyle
     *              The SWT table style.
     *                  
     */
    public AlertsSettingTable(Composite parent, int style, int tableStyle) {
        super(parent, style, tableStyle);

        initialise();
    }

    @Override
    protected void addColumns() {
        addName();
        addEnabled();
        addLowLimit();
        addHighLimit();
        addDelayIn();
        addDelayOut();
    }

    private void addName() {
        createColumn("Name", WIDTH_WEIGHT, new DecoratedCellLabelProvider<DisplayAlerts>(observeProperty("name"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayAlerts setting) {
                        if (setting != null) {
                        	if (setting.getName() != null) {
                        		return setting.getName();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addEnabled() {
        createColumn("Enabled", WIDTH_WEIGHT, new DecoratedCellLabelProvider<DisplayAlerts>(observeProperty("enabled"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayAlerts setting) {
                        if (setting != null) {
                        	if (setting.getEnabled() != null) {
                        		return setting.getEnabled().toString();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addLowLimit() {
        createColumn("Low Limit", WIDTH_WEIGHT, new DecoratedCellLabelProvider<DisplayAlerts>(observeProperty("lowLimit"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(DisplayAlerts setting) {
                        if (setting != null) {
                        	if (setting.getLowLimit() != null) {
                        		return setting.getLowLimit().toString();
                        	}
                        }
                        return "";
                    }
                });
    }

    private void addHighLimit() {
        createColumn("High Limit", WIDTH_WEIGHT, new DecoratedCellLabelProvider<DisplayAlerts>(observeProperty("highLimit"),
                Arrays.asList(rowDecorator)) {
            @Override
			public String stringFromRow(DisplayAlerts setting) {
                if (setting != null) {
                	if (setting.getHighLimit() != null) {
                		 return setting.getHighLimit().toString();
                	}
                }
                return "";
            }
        });
    }

    private void addDelayIn() {
        createColumn("Delay In", WIDTH_WEIGHT, new DecoratedCellLabelProvider<DisplayAlerts>(observeProperty("delayIn"),
                Arrays.asList(rowDecorator)) {
            @Override
			public String stringFromRow(DisplayAlerts setting) {
                if (setting != null) {
                	if (setting.getHighLimit() != null) {
                		 return setting.getDelayIn().toString();
                	}
                }
                return "";
            }
        });
    }
    
    private void addDelayOut() {
        createColumn("Delay Out	", WIDTH_WEIGHT, new DecoratedCellLabelProvider<DisplayAlerts>(observeProperty("delayOut"),
                Arrays.asList(rowDecorator)) {
            @Override
			public String stringFromRow(DisplayAlerts setting) {
                if (setting != null) {
                	if (setting.getHighLimit() != null) {
                		 return setting.getDelayOut().toString();
                	}
                }
                return "";
            }
        });
    }
}
