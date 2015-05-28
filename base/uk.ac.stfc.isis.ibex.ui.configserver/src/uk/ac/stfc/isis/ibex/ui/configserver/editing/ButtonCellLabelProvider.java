package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

public abstract class ButtonCellLabelProvider extends ObservableMapCellLabelProvider {

	private final Map<ViewerCell, Button> cellButtons = new HashMap<>();
	private final Map<ViewerCell, TableEditor> cellEditors = new HashMap<>();
	
	protected ButtonCellLabelProvider(IObservableMap[] attributeMaps) {
		super(attributeMaps);
	}

	protected Button getButton(ViewerCell cell, int style) {		
		return cellButtons.containsKey(cell) ? 
				cellButtons.get(cell) : createButton(cell, style);
	}
		
	@Override
	public void dispose() {
		//super.dispose();
		for (Button button : cellButtons.values()) {
			button.dispose();
		}
		
		for(TableEditor editor : cellEditors.values()) {
			editor.dispose();
		}
		
		cellButtons.clear();
	}

	private Button createButton(final ViewerCell cell, int style) {
		Button button = new Button(composite(cell), style);
		button.setText("");
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setEditor(cell, button);
		cellButtons.put(cell, button);
		
		return button;
	}	
	
	private Composite composite(ViewerCell cell) {
		return (Composite) cell.getControl();
	}
	
	private void setEditor(ViewerCell cell, Button button) {		
	    TableItem item = (TableItem) cell.getItem();	    
		TableEditor editor = new TableEditor(item.getParent());
		editor.horizontalAlignment = SWT.FILL;
		editor.verticalAlignment = SWT.FILL;
	    editor.grabHorizontal  = true;
	    editor.grabVertical = true;
	    editor.setEditor(button, item, cell.getColumnIndex());
	    editor.layout();
	    
	    cellEditors.put(cell, editor);
	}
}