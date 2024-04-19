
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/**
 * The double list editor control.
 * 
 * @param <T> the type of item in the list
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
public class DoubleListEditor<T> extends Composite {
	private final Button select;
	private final Button unselect;
	private final Button btnUp;
	private final Button btnDown;
	private ListViewer selectedViewer;
	private ListViewer unselectedViewer;
	private List selectedList;
	private List unselectedList;
	
	private final IListChangeListener<T> clearSelectedItems;
	private final IListChangeListener<T> clearUnselectedItems;
		
	private final IObservableList<T> selectedItems;
	private final IObservableList<T> unselectedItems;
	private Label lblAvailable;
	private Label lblSelected;
	
    /**
     * Constructor.
     * 
     * @param parent the containing composite
     * @param style the SWT style
     * @param observedProperty the property to observe
     * @param orderable is it orderable
     */
	public DoubleListEditor(Composite parent, int style, String observedProperty, boolean orderable) {
		super(parent, style);
		setLayout(new GridLayout(4, false));		
		
		lblAvailable = new Label(this, SWT.NONE);
		lblAvailable.setText("Available:");
		new Label(this, SWT.NONE);
		
		lblSelected = new Label(this, SWT.NONE);
		lblSelected.setText("Selected:");
		new Label(this, SWT.NONE);
		
		unselectedViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		configureViewer(unselectedViewer, observedProperty);
		unselectedList = unselectedViewer.getList();
		unselectedList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		select = new IBEXButtonBuilder(this, SWT.NONE)
				.customLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1))
				.build();
		
		select.setEnabled(false);;
		select.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_right.png"));
		
		selectedViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		configureViewer(selectedViewer, observedProperty);
		selectedList = selectedViewer.getList();
		selectedList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		btnUp = new IBEXButtonBuilder(this, SWT.NONE)
				.build();
		
		btnUp.setEnabled(false);
		GridData gd_btnUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1);
		gd_btnUp.widthHint = 25;
		gd_btnUp.exclude = !orderable;
		btnUp.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"));
		btnUp.setEnabled(false);
		if (!orderable) {
			new Label(this, SWT.NONE);
		}
		
		unselect = new IBEXButtonBuilder(this, SWT.NONE)
				.customLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1))
				.build();
		
		unselect.setEnabled(false);
		unselect.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_left.png"));
		
		btnDown = new IBEXButtonBuilder(this, SWT.NONE)
				.build();
		
		GridData gd_btnDown = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_btnDown.widthHint = 25;
		gd_btnDown.exclude = !orderable;
		btnDown.setLayoutData(gd_btnDown);
		btnDown.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"));
		btnDown.setEnabled(false);
		
		selectedItems = ViewerProperties.<ISelectionProvider, T>multipleSelection().observe(selectedViewer);
		unselectedItems = ViewerProperties.<ISelectionProvider, T>multipleSelection().observe(unselectedViewer);

        selectedList.addListener(SWT.MouseDoubleClick, e -> listDoubleClick(unselect));
        unselectedList.addListener(SWT.MouseDoubleClick, e -> listDoubleClick(select));

		// Allow only one list to have a selection
		clearUnselectedItems = new IListChangeListener<T>() {	
			@Override
			public void handleListChange(ListChangeEvent<? extends T> arg0) {
				unselectedItems.removeListChangeListener(clearSelectedItems);
				unselectedItems.clear();
				unselectedItems.addListChangeListener(clearSelectedItems);
			}
		};
				
		selectedItems.addListChangeListener(e -> unselect.setEnabled(!selectedItems.isEmpty()));
		selectedItems.addListChangeListener(clearUnselectedItems);
		
		clearSelectedItems = new IListChangeListener<T>() {	
			@Override
			public void handleListChange(ListChangeEvent<? extends T> arg0) {
				selectedItems.removeListChangeListener(clearUnselectedItems);
				selectedItems.clear();
				selectedItems.addListChangeListener(clearUnselectedItems);
			}
		};
		
		unselectedItems.addListChangeListener(e -> select.setEnabled(!unselectedItems.isEmpty()));
		unselectedItems.addListChangeListener(clearSelectedItems);
		
		//Can not reorder if no selection or selected the top or bottom item
		selectedItems.addListChangeListener(e -> setUpDownEnabled(selectedList.getSelectionIndex()));
		
		btnUp.addListener(SWT.Selection, e -> swapSelectedItems(true));	
		btnDown.addListener(SWT.Selection, e -> swapSelectedItems(false));	
	}
	
	private void listDoubleClick(Button selectButton) {
        // Bit of a hack as actual selecting behaviour is done on the 
        // listener, This effectively just clicks the appropriate button 
		// when an item is double clicked, to improve use a DoubleListEditorViewModel
        unselect.setSelection(true);
        unselect.notifyListeners(SWT.Selection, new Event());
	}
	
	/**
	 * Swaps an item in the selected list with the item before or after it.
	 * @param withPrevious True to swap with previous item, false to swap with next.
	 */
	private void swapSelectedItems(boolean withPrevious) {
		int selectIndex = selectedList.getSelectionIndex();
		int swappingIndex = withPrevious ? selectIndex - 1 : selectIndex + 1;
		String selected = selectedList.getItem(selectIndex);
		String temp = selectedList.getItem(swappingIndex);
		selectedList.setItem(swappingIndex, selected);
		selectedList.setItem(selectIndex, temp);
		selectedList.setSelection(swappingIndex);
		setUpDownEnabled(swappingIndex);
	}
	
	private void setUpDownEnabled(int selectionIndex) {
		btnUp.setEnabled(selectionIndex != -1 && selectionIndex > 0);
		btnDown.setEnabled(selectionIndex != -1 && selectionIndex < selectedList.getItemCount() - 1);
	}
	
    /**
     * Binds data to the list-viewers.
     * 
     * @param unselected the contents of the unselected list-viewer
     * @param selected the contents of the unselected list-viewer
     */
	public void bind(IObservableList<T> unselected, IObservableList<T> selected) {		
		selectedViewer.setInput(selected);
		unselectedViewer.setInput(unselected);
	}
	
    /**
     * @return the selected items
     */
	public IObservableList<T> selectedItems() {
		return selectedItems;
	}
	
    /**
     * @return the unselected items
     */
	public IObservableList<T> unselectedItems() {
		return unselectedItems;
	}
	
    /**
     * @return the currently selected item
     */
	public String selectedItem() {
		return selectedList.getItem(selectedList.getSelectionIndex());
	}
	
    /**
     * Add a listener for items being selected.
     * 
     * @param listener the listener
     */
	public void addSelectionListenerForSelecting(SelectionListener listener) {
		select.addSelectionListener(listener);
	}
	
    /**
     * Add a listener for items being deselected.
     * 
     * @param listener the listener
     */
	public void addSelectionListenerForUnselecting(SelectionListener listener) {
		unselect.addSelectionListener(listener);
	}
	
    /**
     * Add a listener for items moved up in the list-viewer.
     * 
     * @param listener the listener
     */
	public void addSelectionListenerForMovingUp(SelectionListener listener) {
		btnUp.addSelectionListener(listener);
	}
	
    /**
     * Add a listener for items moved down in the list-viewer.
     * 
     * @param listener the listener
     */
	public void addSelectionListenerForMovingDown(SelectionListener listener) {
		btnDown.addSelectionListener(listener);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		selectedList.setEnabled(enabled);
		unselectedList.setEnabled(enabled);
	}
	
	private void configureViewer(ListViewer viewer, String observedProperty) {
		ObservableListContentProvider<T> contentProvider = new ObservableListContentProvider<>();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(
				new ObservableMapLabelProvider(BeanProperties.value(observedProperty).observeDetail(contentProvider.getKnownElements())));
	}
	
    /**
     * Refresh the selected list-viewer.
     */
	public void refreshViewer() {
		int selectIndex = selectedList.getSelectionIndex();
		String selected = selectedList.getItem(selectIndex);
		selectedViewer.refresh();
		selectIndex = selectedList.indexOf(selected);
		selectedList.setSelection(selectIndex);
	}
}
