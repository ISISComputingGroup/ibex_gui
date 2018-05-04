
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

// Table showing filtered, interesting PVs for use in a block
package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.util.Collection;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.PVAddressSearch;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * Table for selecting a PV address to associate to a block.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockPVTable extends DataboundTable<PV> {
	private PVAddressSearch search;
	
	private ViewerFilter sourceFilter;
	private ViewerFilter interestFilter;	
	
    /**
     * Standard constructor.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     * @param tableStyle
     *            The SWT table style.
     */
	public BlockPVTable(Composite parent, int style, int tableStyle) {
		super(parent, style, PV.class, tableStyle | SWT.BORDER);

		initialise();
	
		search = new PVAddressSearch();
		this.viewer().addFilter(search);
	}
	
	@Override
	public void setRows(Collection<PV> rows) {
		super.setRows(rows);
	}

	@Override
	protected void addColumns() {
		address();
		description();
	}
	
	private void address() {
		createColumn("PV address", 8, new DataboundCellLabelProvider<PV>(observeProperty("address")) {
			@Override
			protected String stringFromRow(PV row) {
				return row.getAddress();
			}
		});	
	}
	
	private void description() {
		createColumn("Description", 6, new DataboundCellLabelProvider<PV>(observeProperty("description")) {
			@Override
			protected String stringFromRow(PV row) {
				return row.getDescription();
			}
		});	
	}
	
    /**
     * Sets the search text to the paramter specified.
     * 
     * @param searchText
     *            The text to search for.
     */
	public void setSearch(String searchText) {
		search.setSearchText(searchText);
		this.viewer().refresh();
	}
	
	/**
     * Filters the list of PVs by source.
     * 
     * @param filter
     *            The source filter.
     */
	public void setSourceFilter(ViewerFilter filter) {
		sourceFilter = filter;
		removeFilters();
		this.viewer().addFilter(filter);
		addFilterIfNotNull(interestFilter);
	}
	
    /**
     * Filters the list of PVs by interest level.
     * 
     * @param filter
     *            The interest level filter.
     */
	public void setInterestFilter(ViewerFilter filter) {
		interestFilter = filter;
		removeFilters();
		this.viewer().addFilter(filter);
		addFilterIfNotNull(sourceFilter);
	}	
	
	private void addFilterIfNotNull(ViewerFilter filter) {
		if (filter != null) {
			this.viewer().addFilter(filter);
		}
	}
	
	private void removeFilters() {
		this.viewer().resetFilters();
		this.viewer().addFilter(search);
	}
}
