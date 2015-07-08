
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

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class BlocksTable extends DataboundTable<DisplayBlock> {

	private BlockVisibilityFilter visibilityFilter = new BlockVisibilityFilter();
		
	public BlocksTable(Composite parent, int style, int tableStyle) {
		super(parent, style, DisplayBlock.class, tableStyle | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.NO_SCROLL);
		
		initialise();
		table().setHeaderVisible(false);
		table().setLinesVisible(false);
		table().setMenu(new BlocksMenu(this).createContextMenu());
		viewer().addFilter(visibilityFilter);
		
		ColumnViewerToolTipSupport.enableFor(viewer(), ToolTip.NO_RECREATE); 
	}
	
	public void showHiddenBlocks(boolean showHidden) {
		if (showHidden) {
			viewer().removeFilter(visibilityFilter);
		} else {
			viewer().addFilter(visibilityFilter);
		}		
	}
	
	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		table().setBackground(color);
	}
	
	@Override
	public void addFocusListener(FocusListener listener) {
		super.addFocusListener(listener);
		table().addFocusListener(listener);
	}
	
	@Override
	protected void addColumns() {
		name();
		value();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 4);
		name.setLabelProvider(new DataboundCellLabelProvider<DisplayBlock>(observeProperty("name")) {
			@Override
			protected String valueFromRow(DisplayBlock row) {
				return row.getName();
			}
			
			@Override
			public String getToolTipText(Object element) {
				return toolTipText(element);
			}
		});	
	}
	
	private void value() {
		TableViewerColumn value = createColumn("Value", 4);
		value.getColumn().setAlignment(SWT.RIGHT);
		value.setLabelProvider(new DataboundCellLabelProvider<DisplayBlock>(observeProperty("value")) {
			@Override
			protected String valueFromRow(DisplayBlock row) {
				String value = row.getValue();
				return value ==  null ? "not connected" :  value;
			}
			
			@Override
			public String getToolTipText(Object element) {
				return toolTipText(element);
			}
		});	
	}
	
	private String toolTipText(Object element) {
		if (!(element instanceof DisplayBlock)) {
			return "";
		}
		
		DisplayBlock block = (DisplayBlock) element;
		String desc = block.getDescription();
		return desc == null ? "" : desc;
	}
}
