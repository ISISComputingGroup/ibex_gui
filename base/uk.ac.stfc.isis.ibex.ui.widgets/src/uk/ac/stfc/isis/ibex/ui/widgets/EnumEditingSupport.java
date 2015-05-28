package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class EnumEditingSupport<T, E extends Enum<E>> extends EditingSupport {
	private ComboBoxViewerCellEditor cellEditor;	
	
	private final Class<T> rowType;
	private final Class<E> enumType;
	
	public EnumEditingSupport(ColumnViewer viewer, Class<T> rowType, Class<E> enumType) {
		super(viewer);
		this.rowType = rowType;
		this.enumType = enumType;
		
		cellEditor = new ComboBoxViewerCellEditor((Composite) viewer.getControl(), SWT.READ_ONLY);
        cellEditor.setLabelProvider(new LabelProvider());
        cellEditor.setContentProvider(new ArrayContentProvider());

        E[] options = enumType.getEnumConstants();
        cellEditor.setInput(options);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return cellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (enumType.isInstance(element)) {	
			E enumValue =  enumType.cast(element);
			return enumValue;
		}
		
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (rowType.isInstance(element) && enumType.isInstance(value)) {
			T row = rowType.cast(element);
			E newEnumValue = enumType.cast(value);
			
			if (getEnumValueForRow(row) != newEnumValue) {
				setEnumForRow(row, newEnumValue);
				this.getViewer().update(element, null);
			}
		}
	}

	protected abstract E getEnumValueForRow(T row);

	protected abstract void setEnumForRow(T row, E value);
}
