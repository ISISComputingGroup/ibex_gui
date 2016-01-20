
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

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
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
	private IObservableMap[] stateProperties = {observeProperty("isVisible")};
	private BlockVisibilityLabelProvider visibilityLabelProvider;
	private boolean isBlockVisibilityShown;
	
	private BlockNameSearch search;
	
	private CellDecorator<EditableBlock> rowDecorator = new BlockRowCellDecorator();
	
	public BlocksTable(Composite parent, int style, int tableStyle, boolean isBlockVisibilityShown) {
		super(parent, style, EditableBlock.class, tableStyle | SWT.BORDER);
		
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
			visibilityLabelProvider.dispose();
			visibilityLabelProvider = new BlockVisibilityLabelProvider(stateProperties);
			enabled.setLabelProvider(visibilityLabelProvider);
		}
	}
	
	private void name() {
		TableViewerColumn name = createColumn("Name", 3);
		name.setLabelProvider(new DecoratedCellLabelProvider<EditableBlock>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableBlock row) {
				return row.getName();
			}
		});	
	}
	
	private void pv() {
		TableViewerColumn desc = createColumn("PV address", 6);
		desc.setLabelProvider(new DecoratedCellLabelProvider<EditableBlock>(
				observeProperty("PV"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableBlock row) {
				return row.getPV();
			}
		});	
	}
	
	private void blockIsVisible() {
		enabled = createColumn("Visible?", 2);
		visibilityLabelProvider = new BlockVisibilityLabelProvider(stateProperties);
		enabled.setLabelProvider(visibilityLabelProvider);		
	}
	
	public void setSearch(String searchText) {
		search.setSearchText(searchText);
		this.viewer().refresh();
	}
}
