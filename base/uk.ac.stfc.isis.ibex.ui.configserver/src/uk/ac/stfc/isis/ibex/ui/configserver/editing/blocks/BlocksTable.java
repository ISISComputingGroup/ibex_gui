
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.BlockNameSearch;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * Provides a table to display blocks. Can be shown with or without block
 * visibility. Also allows filtering/searching by block name.
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlocksTable extends DataboundTable<EditableBlock> {
	
	private TableViewerColumn enabled;
	private boolean isBlockVisibilityShown;
	
	private BlockNameSearch search;
	
	private CellDecorator<EditableBlock> rowDecorator = new BlockRowCellDecorator();
	
	private CheckboxLabelProvider<EditableBlock> visibilityLabelProvider = 
			new CheckboxLabelProvider<EditableBlock>(observeProperty("isVisible")) {
		@Override
		protected boolean checked(EditableBlock block) {
			return block.getIsVisible();
		}

		@Override
		protected void setChecked(EditableBlock block, boolean checked) {
			block.setIsVisible(checked);
		}

		@Override
		protected boolean isEditable(EditableBlock block) {
			return block.isEditable();
		}
	};
	
	/**
	 * Constructor for the blocks table.
	 * @param parent The parent composite that this table lives in.
	 * @param style The style of the viewer.
	 * @param tableStyle The style of the table.
	 * @param isBlockVisibilityShown Whether the block visibility column should be shown.
	 */
	public BlocksTable(Composite parent, int style, int tableStyle, boolean isBlockVisibilityShown) {
		super(parent, style, tableStyle | SWT.BORDER);
		
		setSortAction(() -> visibilityLabelProvider.resetCheckBoxListenerUpdateFlags());
		
		this.isBlockVisibilityShown = isBlockVisibilityShown;
		initialise();
		
		search = new BlockNameSearch();
		this.viewer().addFilter(search);
	}

	@Override
	protected void addColumns() {
		name();
		pv();
		if (isBlockVisibilityShown) {
			blockIsVisible();
		}
	}
	
	@Override
	public void setRows(Collection<EditableBlock> rows) {
		clear();
		super.setRows(rows);
	}
	
	private void clear() {
		if (isBlockVisibilityShown) {
			enabled.setLabelProvider(visibilityLabelProvider);
		}
	}
	
	private void name() {
		createColumn("Name", 3, new DecoratedCellLabelProvider<EditableBlock>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator)) {
			@Override
			public String stringFromRow(EditableBlock row) {
				return row.getName();
			}
		});	
	}
	
	private void pv() {
		createColumn("PV address", 6, new DecoratedCellLabelProvider<EditableBlock>(
				observeProperty("PV"), 
				Arrays.asList(rowDecorator)) {
			@Override
			public String stringFromRow(EditableBlock row) {
				return row.getPV();
			}
		});	
	}
	
	private void blockIsVisible() {
		enabled = createColumn("Visible?", 2, visibilityLabelProvider);
	}
	
	/**
	 * Sets the PV string to search by.
	 * @param searchText The string to search by.
	 */
	public void setSearch(String searchText) {
		search.setSearchText(searchText);
		this.viewer().refresh();
	}
}
