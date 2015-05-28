package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.ButtonCellLabelProvider;

public class BlockVisibilityLabelProvider extends ButtonCellLabelProvider {

	protected BlockVisibilityLabelProvider(IObservableMap[] attributeMaps) {
		super(attributeMaps);
	}

	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);

		cell.setText("");
		final EditableBlock block = (EditableBlock) cell.getElement();		
		final Button checkBox = getButton(cell, SWT.CHECK);
		
		checkBox.setSelection(block.getIsVisible());	
		checkBox.setText(isVisible(block));

		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				block.setIsVisible(checkBox.getSelection());
				checkBox.setText(isVisible(block));
			}
		});
		
		checkBox.setEnabled(block.isEditable());
	}
	
	private String isVisible(Block block) {
		return block.getIsVisible() ? "Yes" : "No";
	}
}
