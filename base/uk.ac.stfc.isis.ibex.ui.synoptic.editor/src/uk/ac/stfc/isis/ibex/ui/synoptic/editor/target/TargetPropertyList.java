package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetPropertyList extends Composite {
	private ListViewer list;
	private Button btnDelete;
	private Button btnAdd;
	
	private InstrumentViewModel instrument;
	
	public TargetPropertyList(Composite parent, final InstrumentViewModel instrument) 
	{
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		
		instrument.addComponentSelectionListener(new IComponentSelectionListener() {			
			@Override
			public void selectionChanged(ComponentDescription oldSelection, ComponentDescription newSelection) {
				showPropertyList(newSelection);
			}
		});
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {	
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				switch (updateType) {
					case EDIT_PROPERTY:
						int selected = list.getList().getSelectionIndex();
						showPropertyList(instrument.getSelectedComponent());
						list.refresh();
						list.getList().setSelection(selected);
						setButtonStates();
						break;
					case NEW_PROPERTY: 
						int lastItem = list.getList().getItemCount();
						showPropertyList(instrument.getSelectedComponent());
						list.getList().setSelection(lastItem);
						list.refresh();
						break;
					case DELETE_PROPERTY:
						showPropertyList(instrument.getSelectedComponent());
						list.getList().setSelection(-1);
						list.refresh();
						break;
					default:
						break;
				}
				setButtonStates();
			}
		});
		
		GridLayout compositeLayout = new GridLayout(1, false);
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		
		setLayout(compositeLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);
	}
	
	public void createControls(Composite parent) {
		list = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		list.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    list.setContentProvider(new PropertyContentProvider());
	    list.setLabelProvider(new PropertyLabelProvider());
	    
	    list.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				instrument.setSelectedProperty(getSelectedProperty());
				setButtonStates();
			}
		});
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
	    controlComposite.setLayout(new GridLayout(1, false));
	    controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    {
		    btnAdd = new Button(controlComposite, SWT.NONE);
		    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		    btnAdd.setText("Add New Property");
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.addNewProperty();
				}
			});
		    
		    btnDelete = new Button(controlComposite, SWT.NONE);
			btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			btnDelete.setText("Remove Property");
			btnDelete.setEnabled(false);
			btnDelete.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.removeSelectedProperty();
				}
			});
	    } 
	}
	
	public void showPropertyList(ComponentDescription component) {
		if (component != null && component.target() != null) {
			list.setInput(component.target().properties());
		} else {
			list.setInput(null);
		}
	}
	
	public Property getSelectedProperty() {
		IStructuredSelection selection = (IStructuredSelection) list.getSelection();
		return (Property) selection.getFirstElement();
	}
	
	private void setButtonStates() {
		btnDelete.setEnabled(getSelectedProperty() != null);
	}
}
