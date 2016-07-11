
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.list;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * The Class DeviceScreensTable which is the list of possible device screens set
 * from a list of DeviceDescriptions.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DeviceScreensTable extends DataboundTable<DeviceDescription> {

    /**
     * Instantiates a new device screens table.
     *
     * @param parent the parent
     * @param style the style
     * @param tableStyle the table style
     */
    public DeviceScreensTable(Composite parent, int style, int tableStyle) {
        super(parent, style, DeviceDescription.class, tableStyle);
		
		initialise();
	}

    // @Override
    // public void setRows(Collection<DeviceDescription> rows) {
    // TODO sort rows dynamically List<EditableIocState> states = new
    // ArrayList<>(rows);
    // Collections.sort(states);
    // super.setRows(states);
    // refresh();
    // }

    @Override
    protected void configureTable() {
        super.configureTable();
        table().setLinesVisible(false);
    }
	
	@Override
	protected void addColumns() {
        type();
		name();
	}

	private void name() {
        TableViewerColumn name = createColumn("Name", 20);
        name.setLabelProvider(new DataboundCellLabelProvider<DeviceDescription>(observeProperty("name")) {
			@Override
            protected String valueFromRow(DeviceDescription row) {
				return row.getName();
			}
		});		
	}
	
    private void type() {
        TableViewerColumn name = createColumn("Type", 1);
        name.setLabelProvider(new DataboundCellLabelProvider<DeviceDescription>(observeProperty("componentType")) {
            @Override
            protected String valueFromRow(DeviceDescription row) {
                return null;
            }

            @Override
            protected Image imageFromRow(DeviceDescription row) {
                return ComponentIcons.thumbnailForType(row.getComponentType());
            }
        });
    }

}
