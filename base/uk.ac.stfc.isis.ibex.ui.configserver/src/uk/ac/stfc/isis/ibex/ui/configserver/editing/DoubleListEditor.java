
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

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.ResourceManager;

/**
 * The double list editor control.
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
public class DoubleListEditor extends Composite {
	private final Button select;
	private final Button unselect;
	private final Button btnUp;
	private final Button btnDown;
	private ListViewer selectedViewer;
	private ListViewer unselectedViewer;
	private List selectedList;
	private List unselectedList;
	
	private final IListChangeListener clearSelectedItems;
	private final IListChangeListener clearUnselectedItems;
		
	private final IObservableList selectedItems;
	private final IObservableList unselectedItems;
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
		unselectedList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		
		select = new Button(this, SWT.NONE);
		select.setEnabled(false);
		select.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		select.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_right.png"));
		
		selectedViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		configureViewer(selectedViewer, observedProperty);
		selectedList = selectedViewer.getList();
		selectedList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		
		btnUp =  new Button(this, SWT.NONE);
		btnUp.setEnabled(false);
		GridData gd_btnUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1);
		gd_btnUp.widthHint = 25;
		gd_btnUp.exclude = !orderable;
		btnUp.setLayoutData(gd_btnUp);
		btnUp.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"));
		btnUp.setEnabled(true);
		if (!orderable) {
			new Label(this, SWT.NONE);
		}
		
		unselect = new Button(this, SWT.NONE);
		unselect.setEnabled(false);
		unselect.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
		unselect.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_left.png"));
		
		btnDown =  new Button(this, SWT.NONE);
		GridData gd_btnDown = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_btnDown.widthHint = 25;
		gd_btnDown.exclude = !orderable;
		btnDown.setLayoutData(gd_btnDown);
		btnDown.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"));
		btnDown.setEnabled(true);
		
		selectedItems = ViewerProperties.multipleSelection().observe(selectedViewer);
		unselectedItems = ViewerProperties.multipleSelection().observe(unselectedViewer);

        selectedList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                // Bit of a hack as actual selecting behaviour is done on
                // listener, to improve use a DoubleListEditorViewModel
                unselect.setSelection(true);
                unselect.notifyListeners(SWT.Selection, new Event());
            }
        });

        unselectedList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                // Bit of a hack as actual selecting behaviour is done on
                // listener, to improve use a DoubleListEditorViewModel
                select.setSelection(true);
                select.notifyListeners(SWT.Selection, new Event());
            }
        });

		// Allow only one list to have a selection
		clearSelectedItems = new IListChangeListener() {	
			@Override
			public void handleListChange(ListChangeEvent arg0) {
				selectedItems.removeListChangeListener(clearUnselectedItems);
				selectedItems.clear();
				selectedItems.addListChangeListener(clearUnselectedItems);
			}
		};
		
		clearUnselectedItems = new IListChangeListener() {	
			@Override
			public void handleListChange(ListChangeEvent arg0) {
				unselectedItems.removeListChangeListener(clearSelectedItems);
				unselectedItems.clear();
				unselectedItems.addListChangeListener(clearSelectedItems);
			}
		};
				
		selectedItems.addListChangeListener(new IListChangeListener() {
			@Override
			public void handleListChange(ListChangeEvent arg0) {
				unselect.setEnabled(!selectedItems.isEmpty());
			}
		});
		
		selectedItems.addListChangeListener(clearUnselectedItems);
		
		unselectedItems.addListChangeListener(new IListChangeListener() {
			@Override
			public void handleListChange(ListChangeEvent arg0) {
				select.setEnabled(!unselectedItems.isEmpty());
			}
		});
		
		unselectedItems.addListChangeListener(clearSelectedItems);
		
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedList.getSelectionIndex() > 0) {
					int selectIndex = selectedList.getSelectionIndex();
					String selected = selectedList.getItem(selectIndex);
					String temp = selectedList.getItem(selectIndex - 1);
					selectedList.setItem(selectIndex, temp);
					selectedList.setItem(selectIndex - 1, selected);
					selectedList.setSelection(selectIndex - 1);
				}
			}
		});
		
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedList.getSelectionIndex() < selectedList.getItemCount() - 1) {
					int selectIndex = selectedList.getSelectionIndex();
					String selected = selectedList.getItem(selectIndex);
					String temp = selectedList.getItem(selectIndex + 1);
					selectedList.setItem(selectIndex + 1, selected);
					selectedList.setItem(selectIndex, temp);
					selectedList.setSelection(selectIndex + 1);
				}
			}
		});
	}
	 
    /**
     * Binds data to the list-viewers.
     * 
     * @param unselected the contents of the unselected list-viewer
     * @param selected the contents of the unselected list-viewer
     */
	public void bind(IObservableList unselected, IObservableList selected) {		
		selectedViewer.setInput(selected);
		unselectedViewer.setInput(unselected);
	}
	
    /**
     * @return the selected items
     */
	public IObservableList selectedItems() {
		return selectedItems;
	}
	
    /**
     * @return the unselected items
     */
	public IObservableList unselectedItems() {
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
	};
	
	private void configureViewer(ListViewer viewer, String observedProperty) {
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(
				new ObservableMapLabelProvider(BeansObservables.observeMaps(contentProvider.getKnownElements(), new String[] {observedProperty})));
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
