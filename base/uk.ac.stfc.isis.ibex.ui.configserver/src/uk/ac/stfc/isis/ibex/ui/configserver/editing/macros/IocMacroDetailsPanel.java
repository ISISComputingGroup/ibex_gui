package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class IocMacroDetailsPanel extends Composite {
	private final MessageDisplayer messageDisplayer;
	private Text name;
	private Text value;
	private DataBindingContext bindingContext;
	private AddMacroTable availableMacrosTable;
	private UpdateValueStrategy nameStrategy = new UpdateValueStrategy();
	private UpdateValueStrategy valueStrategy = new UpdateValueStrategy();
	private AvailableMacroSearcher macroSearcher;
	private MacroValueValidator valueValidator;
	
	public IocMacroDetailsPanel(Composite parent, int style, MessageDisplayer messageDisplayer) {
		super(parent, style);
		this.messageDisplayer = messageDisplayer;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpSelectedPv = new Group(this, SWT.NONE);
		grpSelectedPv.setText("Selected Macro");
		grpSelectedPv.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(grpSelectedPv, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		name = new Text(grpSelectedPv, SWT.BORDER);
		name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		name.setEnabled(false);
		
		Label lblValue = new Label(grpSelectedPv, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValue.setText("Value");
		
		value = new Text(grpSelectedPv, SWT.BORDER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		value.setEnabled(false);
		
		availableMacrosTable = new AddMacroTable(grpSelectedPv, SWT.NONE, 0);
		availableMacrosTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		new Label(grpSelectedPv, SWT.NONE);
		new Label(grpSelectedPv, SWT.NONE);
		availableMacrosTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size()>0) {
					Macro macro = (Macro)selection.getFirstElement();
					name.setText(macro.getName());
				}
			}
		});
	}
	
	public void setMacro(Macro macro, Collection<Macro> macros, Collection<Macro> availabeMacros, boolean canEdit) {
		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		if (macro == null) {
			setEnabled(false);
			setTextEnabled(false);
	
			return;
		}
		
		setEnabled(canEdit);
		setTextEnabled(canEdit);
		
		macroSearcher = new AvailableMacroSearcher(availabeMacros);
		
		bindingContext = new DataBindingContext();
		nameStrategy.setBeforeSetValidator(new MacroNameValidator(macros, macro, messageDisplayer));
		valueValidator = new MacroValueValidator(macro, messageDisplayer);
		valueStrategy.setBeforeSetValidator(valueValidator);
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("name").observe(macroSearcher), 
				nameStrategy, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER)); 
		bindingContext.bindValue(BeanProperties.value("pattern").observe(macro), BeanProperties.value("pattern").observe(macroSearcher));	
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("name").observe(macro), nameStrategy, null); 
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(value), BeanProperties.value("value").observe(macro), valueStrategy, null);
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("filter").observe(availableMacrosTable), 
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
		
		macroSearcher.addPropertyChangeListener("pattern", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				valueValidator.validate(value.getText());
			}
		});
		
		availableMacrosTable.setRows(availabeMacros);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		availableMacrosTable.setEnabled(enabled);
	}
	
	private void setTextEnabled(boolean enabled) {
		name.setEnabled(enabled);
		value.setEnabled(enabled);
		if ( !enabled ) {
			name.setText("");
			value.setText("");
		}
	}
}
