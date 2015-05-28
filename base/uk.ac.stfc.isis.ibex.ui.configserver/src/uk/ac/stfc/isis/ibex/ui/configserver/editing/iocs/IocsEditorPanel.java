package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocsTable;

public class IocsEditorPanel extends Composite {

	private IocsTable table;
	
	private final Display display = Display.getCurrent();
	private EditableConfiguration config;
	
	private final PropertyChangeListener updateIocs = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (config != null) {
				updateIocs(config.getEditableIocs());
			}
		}
	};
	
	public IocsEditorPanel(Composite parent, int style, MessageDisplayer msgDisp) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		table = new IocsTable(this, SWT.NONE, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 200;
		table.setLayoutData(gd_table);
	}

	public void setConfig(EditableConfiguration config) {
		this.config = config;
		updateIocs(config.getEditableIocs());	
		config.addPropertyChangeListener(updateIocs);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		table.setEnabled(enabled);
	}
	
	private void updateIocs(final Collection<EditableIoc> iocs) {
		display.asyncExec(new Runnable() {	
			@Override
			public void run() {
				if (!table.isDisposed()) {
					table.setRows(iocs);
					table.refresh();	
				}
			}
		});
	}
}
