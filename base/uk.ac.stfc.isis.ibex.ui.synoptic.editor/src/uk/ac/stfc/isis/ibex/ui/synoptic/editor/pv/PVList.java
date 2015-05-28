package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

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
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class PVList extends Composite {
	private ListViewer list;
	
	private Button btnDelete;
	private Button btnAdd;
	private Button btnPlus;
	private Button btnMinus;
	
	private InstrumentViewModel instrument;
	
	public PVList(Composite parent, final InstrumentViewModel instrument) {
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		
		GridLayout compositeLayout = new GridLayout(2, false);
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		
		setLayout(compositeLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		instrument.addComponentSelectionListener(new IComponentSelectionListener() {			
			@Override
			public void selectionChanged(ComponentDescription oldSelection, ComponentDescription newSelection) {
				showPvList(newSelection);
			}
		});
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {	
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				if (updateType == UpdateTypes.EDIT_PV) {
					list.refresh();
					setButtonStates();
				}
			}
		});
		
		createControls(this);
	}
	
	public void createControls(Composite parent) {
		list = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		list.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    list.setContentProvider(new PvContentProvider());
	    list.setLabelProvider(new PvLabelProvider());
	    
	    list.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateSelection();
			}
		});
	    
	    Composite moveComposite = new Composite(parent, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	    {
	    	btnPlus = new Button(moveComposite, SWT.NONE);
	    	btnPlus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	    	btnPlus.setText("+");
	    	btnPlus.setEnabled(false);
	    	btnPlus.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.promoteSelectedPV();
				}
			});
	    	
	    	btnMinus = new Button(moveComposite, SWT.NONE);
	    	btnMinus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	    	btnMinus.setText("-");
	    	btnMinus.setEnabled(false);
	    	btnMinus.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.demoteSelectedPV();
				}
			});
	    }
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
	    controlComposite.setLayout(new GridLayout(1, false));
	    controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    {
		    btnAdd = new Button(controlComposite, SWT.NONE);
		    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		    btnAdd.setText("Add New PV");
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = instrument.addNewPV();
					list.getList().select(index);
					updateSelection();
				}
			});
		    
		    btnDelete = new Button(controlComposite, SWT.NONE);
			btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			btnDelete.setText("Remove PV");
			btnDelete.setEnabled(false);
			btnDelete.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.removeSelectedPV();
				}
			});
	    }
	}
	
	public void showPvList(ComponentDescription component) {
		if (component != null) {
			list.setInput(component.pvs());
		}
	}
	
	public PV getSelectedPV() {
		IStructuredSelection selection = (IStructuredSelection) list.getSelection();
		return (PV) selection.getFirstElement();
	}
	
	private void setButtonStates() {
		btnDelete.setEnabled(getSelectedPV() != null);
		btnPlus.setEnabled(instrument.canPromotePV());
		btnMinus.setEnabled(instrument.canDemotePV());
	}

	private void updateSelection() {
		instrument.setSelectedPV(getSelectedPV());
		setButtonStates();
	}
}
