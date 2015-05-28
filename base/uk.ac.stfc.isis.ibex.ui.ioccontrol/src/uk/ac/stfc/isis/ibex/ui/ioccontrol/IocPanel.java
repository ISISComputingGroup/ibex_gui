package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IocTable;

public class IocPanel extends Composite {
	
	private final Display display = Display.getDefault();
	
	private IocTable table;
	private IocEditorPanel editor;
	private IocControl control;
	
	private PropertyChangeListener updateTable = new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					setIocs();
				}
			});
		}
	};
	
	public IocPanel(Composite parent, final IocControl control, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
				
		Composite container = new Composite(this, SWT.NONE);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.marginHeight = 0;
		gl_container.horizontalSpacing = 0;
		gl_container.marginWidth = 0;
		container.setLayout(gl_container);
		
		table = new IocTable(container, SWT.BORDER, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.control = control;
		control.iocs().addPropertyChangeListener(updateTable, true);
		
		table.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				editor.setIoc(table.firstSelectedRow());
			}
		});

		editor = new IocEditorPanel(container, SWT.NONE, control);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		control.iocs().removePropertyChangeListener(updateTable);
	}

	private void setIocs() {		
		if (control.iocs().isSet()) {
			EditableIocState selected = table.firstSelectedRow();
			
			Collection<EditableIocState> rows = control.iocs().getValue();
			if (rows != null) {
				table.setRows(rows);
				resetLastSelectedIoc(selected, rows);
			}			
		}
	}

	private void resetLastSelectedIoc(EditableIocState lastSelected, Collection<EditableIocState> rows) {
		if (lastSelected == null) {
			return;
		}
		
		// Preserve selection if possible
		for (EditableIocState row : rows) {
			if (row.getName().equals(lastSelected.getName())) {
				editor.setIoc(row);
				return;
			}
		}
	}
}
