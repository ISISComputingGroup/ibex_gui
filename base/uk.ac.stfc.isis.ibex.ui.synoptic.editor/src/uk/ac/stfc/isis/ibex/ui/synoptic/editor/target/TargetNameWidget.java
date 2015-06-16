package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetNameWidget extends Composite {

	private Text txtName;
	private ComboViewer cmboOpiName;
	private boolean updateLock;
	private InstrumentViewModel instrument;
	private TargetType type;
	private StackLayout layout;
	private Collection<String> availableOPIs;
	
	public TargetNameWidget(Composite parent, final InstrumentViewModel instrument) {
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		
		availableOPIs = Opi.getDefault().provider().getOPIList();
		
		layout = new StackLayout();
		setLayout(layout);
		
		createControls(this);
		
		setTargetType(TargetType.OPI);
	}
	
	private void createControls(Composite parent) {
		txtName = new Text(parent, SWT.BORDER);
		
		txtName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateModel(txtName.getText());
			}
		});
		
		cmboOpiName = new ComboViewer(parent, SWT.READ_ONLY);
		cmboOpiName.setContentProvider(new ArrayContentProvider());
		cmboOpiName.setInput(availableOPIs);
		cmboOpiName.getCombo().select(-1);
		
		cmboOpiName.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent e) {
				IStructuredSelection selection = (IStructuredSelection) e.getSelection();	
				String target = (String) selection.getFirstElement();
				
				updateModel(target);
			}
		});
	}
	
	
	private void updateModel(String targetName) 	{
		if (!updateLock && instrument.getSelectedComponent() != null) {
			TargetDescription target = instrument.getSelectedComponent().target();
			
			if (target != null) {
				target.setName(targetName);
				target.setType(type);
				instrument.broadcastInstrumentUpdate(UpdateTypes.EDIT_COMPONENT);
			}
		}
	}
	
	public void setTargetType(TargetType type) {
		this.type = type;
		switch (type) {
			case OPI:
				layout.topControl = cmboOpiName.getCombo();
				IStructuredSelection selection = (IStructuredSelection) cmboOpiName.getSelection();	
				updateModel((String) selection.getFirstElement());
				break;
			case COMPONENT:
				layout.topControl = txtName;
				updateModel(txtName.getText());
				break;
		}
		layout();
	}
	
	public void setTarget(TargetDescription target) {
		updateLock = true;
		
		if (target == null) {
			txtName.setText("");
			cmboOpiName.getCombo().select(-1);
		} else {
			setTargetType(target.type());
			
			switch (target.type()) {
				case OPI:
					ISelection selection = new StructuredSelection(target.name());
					cmboOpiName.setSelection(selection);
					break;
				case COMPONENT:
					txtName.setText(target.name());
					break;
			}
		}
		
		updateLock = false;
	}
}
