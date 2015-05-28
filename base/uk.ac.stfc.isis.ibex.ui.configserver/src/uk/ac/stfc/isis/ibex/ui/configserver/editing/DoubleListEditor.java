package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Label;

public class DoubleListEditor extends Composite {
	
	private final Button select;
	private final Button unselect;
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
	
	public DoubleListEditor(Composite parent, int style, String observedProperty) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		lblAvailable = new Label(this, SWT.NONE);
		lblAvailable.setText("Available:");
		new Label(this, SWT.NONE);
		
		lblSelected = new Label(this, SWT.NONE);
		lblSelected.setText("Selected:");
		
		unselectedViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		configureViewer(unselectedViewer, observedProperty);
		unselectedList = unselectedViewer.getList();
		unselectedList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		
		select = new Button(this, SWT.NONE);
		select.setEnabled(false);
		select.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		select.setText(">");
		
		selectedViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		configureViewer(selectedViewer, observedProperty);
		selectedList = selectedViewer.getList();
		selectedList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
				
		unselect = new Button(this, SWT.NONE);
		unselect.setEnabled(false);
		unselect.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
		unselect.setText("<");
		
		selectedItems = ViewerProperties.multipleSelection().observe(selectedViewer);
		unselectedItems = ViewerProperties.multipleSelection().observe(unselectedViewer);

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
	}
	 
	public void bind(IObservableList unselected, IObservableList selected) {		
		selectedViewer.setInput(selected);
		unselectedViewer.setInput(unselected);
	}
	
	public IObservableList selectedItems() {
		return selectedItems;
	}
	
	public IObservableList unselectedItems() {
		return unselectedItems;
	}
	
	public void addSelectionListenerForSelecting(SelectionListener listener) {
		select.addSelectionListener(listener);
	}
	
	public void addSelectionListenerForUnselecting(SelectionListener listener) {
		unselect.addSelectionListener(listener);
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
				new ObservableMapLabelProvider(BeansObservables.observeMaps(contentProvider.getKnownElements(), new String[] { observedProperty } )));
	}
}
