
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;

import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;

/**
 * Provides decorated Cell Labels.
 *
 * @param <TRow> the type of the row data
 */
public abstract class DecoratedCellLabelProvider<TRow> extends DataboundCellLabelProvider<TRow> {

	private final List<CellDecorator<TRow>> decorators = new ArrayList<>();
			
	 /**
     * Instantiates a new decorated cell label provider that tracks changes to
     * one attribute.
     *
     * @param attributeMap the attribute map
     * @param decorators - decorators to add to the cell
     */
	public DecoratedCellLabelProvider(
			IObservableMap<TRow, ?> attributeMap, 
			Collection<? extends CellDecorator<TRow>> decorators) {
		super(attributeMap);
		
		this.decorators.addAll(decorators);
	}
	
    @Override
    public void update(ViewerCell cell) {
    	super.update(cell);
    	
    	for (CellDecorator<TRow> decorator : decorators) {
    		decorator.applyDecoration(cell);
    	}
    }
}
