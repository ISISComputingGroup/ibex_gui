
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2016 Science &
 * Technology Facilities Council. All rights reserved.
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.list;

import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.CheckboxIcons;
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * The Class DeviceScreensTable which is the list of possible device screens set
 * from a list of DeviceDescriptions.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DeviceScreensTable extends DataboundTable<DeviceDescription> {

    private DeviceScreensComparator comparator;

    /**
     * Instantiates a new device screens table.
     *
     * @param parent the parent
     * @param style the style
     * @param tableStyle the table style
     */
    public DeviceScreensTable(Composite parent, int style, int tableStyle) {
        super(parent, style, DeviceDescription.class, tableStyle);
        comparator = new DeviceScreensComparator();
        
		initialise();
	}

    @Override
    protected void configureTable() {
        super.configureTable();
        table().setLinesVisible(false);
    }
	
	@Override
	protected void addColumns() {
        type();
		name();
        persist();
	}

	private void name() {
        createColumn("Name", 20, new DataboundCellLabelProvider<DeviceDescription>(observeProperty("name")) {
			@Override
			protected String stringFromRow(DeviceDescription row) {
				return row.getName();
			}
        });
    }

    private void persist() {
        createColumn("Saved on server", 4, new DataboundCellLabelProvider<DeviceDescription>(observeProperty("persist")) {
            @Override
            protected Image imageFromRow(DeviceDescription row) {
                return CheckboxIcons.getCheckboxImage(row.getPersist());
            }

            @Override
			protected String stringFromRow(DeviceDescription row) {
                return null;
            }
        });
    }

    private void type() {
        createColumn("Type", 1, new DataboundCellLabelProvider<DeviceDescription>(observeProperty("key")) {
            @Override
			protected String stringFromRow(DeviceDescription row) {
                return null;
            }

            @Override
            protected Image imageFromRow(DeviceDescription row) {
                Map<String, OpiDescription> opis = Opi.getDefault().descriptionsProvider().getDescriptions().getOpis();
                ComponentType component;
                try {
                    OpiDescription opiDescription = opis.get(row.getKey());
                    component = ComponentType.valueOf(opiDescription.getType());
                } catch (IllegalArgumentException | NullPointerException ex) {
                    // caught when the OPI does not hold the key, when the
                    // description has no type or it does not match an enum
                    component = ComponentType.UNKNOWN;
                }
                return ComponentIcons.thumbnailForType(component);
            }
        });
    }
    
    @Override
	protected ColumnComparator<DeviceDescription> comparator() {
		return comparator;
	}
}
