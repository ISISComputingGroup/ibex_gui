
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

package uk.ac.stfc.isis.ibex.ui.widgets;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

/**
 * A LabelProvider that adds a check box to a cell in a table. 
 *
 * @param <T> The model to get the/set the information for the check box.
 */
public abstract class CheckboxLabelProvider<T> extends ButtonCellLabelProvider<T> {
    
    /**A map between a model and a flag that says whether the selection listeners 
     * on the check box on the row of the given model need to be updated. The 
     * value of the flag is false, unless an item has just been added or removed
     * from the table.*/
    private Map<T, AtomicBoolean> checkboxListenerUpdateFlags = new WeakHashMap<>();
    
    /**
     * A selection listener that binds models to check boxes.
     */
    public class CheckboxSelectionAdapter extends SelectionAdapter {    
        final Button checkbox;
        final T model;
        
        /**
         * Makes a new check box selection adapter.
         * @param checkbox the check box a model will be bound to.
         * @param model the model in a table item the check box will be bound to.
         */
        public CheckboxSelectionAdapter(Button checkbox, T model) {
            super();
            this.checkbox = checkbox;
            this.model = model;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            setChecked(model, checkbox.getSelection());
            checkbox.setText(stringFromRow(model));
        }
    }
    
    
    /**
     * The default constructor for the CheckboxLabelProvider.
     * @param stateProperty The property that this label provider should be observing, 
     *                          this property should give the checked value for the box.
     */
    @SuppressWarnings("unchecked")
    public CheckboxLabelProvider(IObservableMap<T, ?> stateProperty) {
        this(new IObservableMap[] {stateProperty});
    }
    
	/**
	 * The default constructor for the CheckboxLabelProvider.
	 * @param stateProperties The properties that this label provider should be observing.
	 */
	public CheckboxLabelProvider(IObservableMap<T, ?>[] stateProperties) {
		super(stateProperties);
		
        for (IObservableMap<T, ?> attributeMap : stateProperties) {
            attributeMap.addMapChangeListener(event -> {
                updateCheckboxListenerUpdateFlags(attributeMap);
            });
        }
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
		
        /*if needed, clears all selection listeners on the check box and re-adds
         * new update listeners. Since check boxes remain in the same row after
         * the table is sorted, while the models do not, we need to remove old 
         * listeners that refer to models that are no longer in the row of that
         * check box.*/
        getOptionalUpdateFlag(model).ifPresent(updateFlag -> resetCheckBoxListeners(
                updateFlag, checkBox, model));
        
		checkBox.setEnabled(isEditable(model));
	}
	
	@Override
	protected String stringFromRow(T model) {
		return checked(model) ? "Yes" : "No";
	}
	
	/**
	 * Ensures newly added models have an associated update flag, that flags 
	 * associated with models no longer in the table are removed, and all update
	 * flags are set to true.
	 */
	private void updateCheckboxListenerUpdateFlags(IObservableMap<T, ?> stateProperties) {
	    checkboxListenerUpdateFlags.clear();
	    for (T model: stateProperties.keySet()) {
	        checkboxListenerUpdateFlags.put(model, new AtomicBoolean(true));
	    }
    }
	
	/**
	 * Sets the update flag of each model of the table to true. Meaning that next
	 * time the view updates listeners will be reset on all checkboxes (to ensure
	 * they are listening to the correct model).
	 */
	public void resetCheckBoxListenerUpdateFlags() {
        for (AtomicBoolean flag: checkboxListenerUpdateFlags.values()) {
            flag.set(true);
        }
    }
	
	/**
	 * Gets the check box listener update flag mapped to the given model, wrapped
	 * in an Optional. A side effect of this method is that flag corresponding 
	 * to the given model is set to false.
	 * @param model the model on the same table row as the check box.
	 * @return a boolean wrapped in an Optional, or an empty Optional if there is 
	 * no mapping for the given model.
	 */
	private Optional<Boolean> getOptionalUpdateFlag(T model) {
	    Optional<AtomicBoolean> optionalAtomicFlag = Optional.ofNullable(checkboxListenerUpdateFlags.get(model));
	    Optional<Boolean> optionalFlag = optionalAtomicFlag.map(atomicFlag -> atomicFlag.getAndSet(false));
	    return optionalFlag;
	}
	
	/**
	 * If the given flag is true, it removes all SelectionAdapter listeners of the given 
	 * check box, then adds one selection listener that will bind the given model 
	 * to the given check box.
	 * @param doUpdate a boolean telling whether to do the update or not.
	 * @param checkBox The check box where we want to reset the listeners.
	 * @param model The model that will be bound to the check box by the new listeners.
	 */
	private void resetCheckBoxListeners(boolean doUpdate, Button checkBox, T model) {
	    if (doUpdate) {
            clearCheckBoxSelectListeners(checkBox);
          
            checkBox.addSelectionListener(new CheckboxSelectionAdapter(checkBox, model));
        }
	}
	
	/**
	 * Removes all SelectionAdapter listeners from this check box's 
	 * listener list.
	 * @param checkBox the check box whose SelectionAdapter listeners we 
	 * will remove.
	 */
	public static void clearCheckBoxSelectListeners(Button checkBox) {
        for (var listener: checkBox.getTypedListeners(SWT.Selection, CheckboxLabelProvider.CheckboxSelectionAdapter.class).toList()) {
            checkBox.removeSelectionListener(listener);
        }
    }
	
	/**Says whether the check box should be checked or not.
	 * @param model the model.
	 * @return true if the check box should be checked, false if not.
	 */
	protected abstract boolean checked(T model);
	
	/**
	 * Called when the user checks a check box.
	 * 
	 * @param model the model.
	 * @param checked whether the check box is checked or unchecked.
	 */
	protected abstract void setChecked(T model, boolean checked);
	
	/** Says if the item is editable.
	 * @param model the model.
	 * @return true if the item is editable, false if not.
	 */
	protected abstract boolean isEditable(T model);
}
