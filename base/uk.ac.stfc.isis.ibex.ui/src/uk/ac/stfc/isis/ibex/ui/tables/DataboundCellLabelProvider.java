
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

package uk.ac.stfc.isis.ibex.ui.tables;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

/**
 * The Class DataboundCellLabelProvider allows a data table to provide the
 * contents of a cell based on the row data. Contents can be text and/or an
 * image.
 * 
 * Implement valueFromRow to set text within a cell (null for no text) Override
 * imageFromRow to set the image on the cell
 * 
 *
 * @param <TRow> the type of the row data
 */
public abstract class DataboundCellLabelProvider<TRow> extends SortableObservableMapCellLabelProvider<TRow> {

    /**
     * Instantiates a new databound cell label provider that tracks changes to
     * one attribute.
     *
     * @param attributeMap the attribute map
     */
	public DataboundCellLabelProvider(IObservableMap attributeMap) {
		super(attributeMap);
	}

	@Override
	public void update(ViewerCell cell) {
		TRow row = getRow(cell);
		cell.setText(stringFromRow(row));
        cell.setImage(imageFromRow(row));
	}

    /**
     * Gets the row data for the cell.
     *
     * @param cell the cell to get data from
     * @return the row as the current type
     */
	@SuppressWarnings("unchecked")
	protected TRow getRow(ViewerCell cell) {
		return (TRow) cell.getElement();
	}

    /**
     * Set the text to be displayed in a cell.
     *
     * @param row the row
     * @return the string to set in the cell
     */
	public abstract String stringFromRow(TRow row);
	
    /**
     * Image from row for the cell; default to null.
     *
     * @param row the row
     * @return the image to set
     */
    protected Image imageFromRow(TRow row) {
        return null;
    }

    /**
     * Value or empty for a double.
     *
     * @param value the value
     * @return the value as a double
     */
	protected String valueOrEmpty(Double value) {
		return value == null ? "" : Double.toString(value);
	}
}
