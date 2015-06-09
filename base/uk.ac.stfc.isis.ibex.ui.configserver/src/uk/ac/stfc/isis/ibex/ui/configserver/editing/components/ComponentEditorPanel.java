// Edit which components are included in this config
package uk.ac.stfc.isis.ibex.ui.configserver.editing.components;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableComponents;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DoubleListEditor;

public class ComponentEditorPanel extends Composite {
	private MessageDisplayer messageDisplayer;
	private EditableConfiguration config;
	private DoubleListEditor editor;
	private EditableComponents components;

	public ComponentEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		this.messageDisplayer = messageDisplayer;
		setLayout(new GridLayout(1, false));
		
		editor = new DoubleListEditor(this, SWT.NONE, "name", false);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	public void setConfig(EditableConfiguration config) {
		this.config = config;
		
		components = config.getEditableComponents();
		IObservableList selected = BeanProperties.list("selected").observe(components);
		IObservableList unselected = BeanProperties.list("unselected").observe(components);
		editor.bind(unselected, selected);

		
		editor.addSelectionListenerForSelecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				components.toggleSelection(editor.unselectedItems());
			}
		});
		
		editor.addSelectionListenerForUnselecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				components.toggleSelection(editor.selectedItems());
			}
		});
	}
}
