
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

package uk.ac.stfc.isis.ibex.ui.configserver;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TypedListener;

import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.ButtonCellLabelProvider;

/**
 * A LabelProvider that adds a checkbox to a cell in a table. 
 *
 * @param <T> The model to get the/set the information for the checkbox
 */
public abstract class CheckboxLabelProvider<T> extends ButtonCellLabelProvider<T> {
    
    private Map<T, AtomicBoolean> checkboxListenerUpdateFlags = new WeakHashMap<>();
    
    private DataboundTable<T> databoundTable;
	
	/**
	 * The default constructor for the CheckboxLabelProvider.
	 * @param stateProperties The properties that this label provider should be observing
	 */
	public CheckboxLabelProvider(IObservableMap stateProperties, DataboundTable<T> table) {
		super(stateProperties);
		this.databoundTable = table;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);

		cell.setText("");
		final T model = (T) cell.getElement();		
		final Button checkBox = getControl(cell, SWT.CHECK);
		
		checkBox.setSelection(checked(model));	
		checkBox.setText(stringFromRow(model));
		
        Optional<Boolean> optionalBoolean = Optional.ofNullable(
                checkboxListenerUpdateFlags.get(model)).map(
                atomicFlag -> atomicFlag.getAndSet(false));
        
        optionalBoolean.ifPresent(updateFlag -> {
            if(updateFlag) {
                
                for(Listener listener: checkBox.getListeners(SWT.Selection)) {
                    if (listener instanceof TypedListener) {
                        TypedListener typedListener = (TypedListener) listener;
                        
                        if(typedListener.getEventListener() instanceof SelectionAdapter) {
                            checkBox.removeSelectionListener((SelectionListener)
                            typedListener.getEventListener());
                        }
                    }
                }
              
                checkBox.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        setChecked(model, checkBox.getSelection());
                        checkBox.setText(stringFromRow(model));
                    }
                });
            }
        });
		
		checkBox.setEnabled(isEditable(model));
	}
	
	@Override
	protected String stringFromRow(T model) {
		return checked(model) ? "Yes" : "No";
	}
	
	public void updateCheckboxListenerUpdateFlagsMap() { 
        for(TableItem item: databoundTable.table().getItems()) {
            T model = (T) item.getData();
            
            if(!checkboxListenerUpdateFlags.containsKey(model)) {
                checkboxListenerUpdateFlags.put(model, new AtomicBoolean(true));
            } else {
                checkboxListenerUpdateFlags.get(model).set(true);
            }  
        }
    }
	
	public void resetCheckBoxListenerUpdateFlags() {
        for(AtomicBoolean flag: checkboxListenerUpdateFlags.values()) {
            flag.set(true);
        }
    }
	
	/**
	 * @param model the model
	 * @return whether the checkbox should be checked or not
	 */
	protected abstract boolean checked(T model);
	
	/**
	 * Called when the user checks a checkbox.
	 * 
	 * @param model the model
	 * @param checked whether the checkbox is checked ot unchecked
	 */
	protected abstract void setChecked(T model, boolean checked);
	
	/**
	 * @param model the model
	 * @return whether the item is editable or not
	 */
	protected abstract boolean isEditable(T model);
}
