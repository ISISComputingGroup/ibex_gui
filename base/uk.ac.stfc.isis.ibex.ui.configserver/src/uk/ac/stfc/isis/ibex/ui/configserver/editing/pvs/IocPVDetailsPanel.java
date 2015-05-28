package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class IocPVDetailsPanel extends Composite {
	private final MessageDisplayer messageDisplayer;
	private Text name;
	private Text value;
	private DataBindingContext bindingContext;
	private IocAvailablePVsTable availablePVTable;
	private UpdateValueStrategy strategy = new UpdateValueStrategy();
	private Collection<AvailablePV> pvs;

	public IocPVDetailsPanel(Composite parent, int style, MessageDisplayer messageDisplayer) {
		super(parent, style);
		this.messageDisplayer = messageDisplayer;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpSelectedPv = new Group(this, SWT.NONE);
		grpSelectedPv.setText("Selected PV");
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
		
		availablePVTable = new IocAvailablePVsTable(grpSelectedPv, SWT.NONE, 0);
		availablePVTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		new Label(grpSelectedPv, SWT.NONE);
		new Label(grpSelectedPv, SWT.NONE);
		availablePVTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size()>0) {
					AvailablePV pv = (AvailablePV)selection.getFirstElement();
					name.setText(pv.getName());
				}
			}
		});
	}
	
	public void setPV(PVDefaultValue pv, EditableIoc ioc) {
		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		if (pv == null) {
			setEnabled(false);
			setTextEnabled(false);
	
			return;
		}
		
		setEnabled(ioc.isEditable());
		setTextEnabled(ioc.isEditable());
		
		bindingContext = new DataBindingContext();
		strategy.setBeforeSetValidator(new PVNameValidator(ioc, pv, messageDisplayer));
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("name").observe(pv), strategy, null); 
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(value), BeanProperties.value("value").observe(pv));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("filter").observe(availablePVTable), 
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
		
		updateAvailablePVs();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		availablePVTable.setEnabled(enabled);
	}
	
	private void setTextEnabled(boolean enabled) {
		name.setEnabled(enabled);
		value.setEnabled(enabled);
		if ( !enabled ) {
			name.setText("");
			value.setText("");
		}
	}
	
	public void setPVs(Collection<AvailablePV> pvs) { 
		this.pvs = pvs;
		updateAvailablePVs();
	}
	
	private void updateAvailablePVs() {
		if ( pvs!=null) {
		   	availablePVTable.setRows(pvs);
		}
	}
}
