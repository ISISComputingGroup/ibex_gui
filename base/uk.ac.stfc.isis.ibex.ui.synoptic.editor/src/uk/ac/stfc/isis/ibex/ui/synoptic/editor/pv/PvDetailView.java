package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

import java.util.Arrays;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IPVSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;

public class PvDetailView extends Composite {
	private Composite labelComposite;
	private Composite fieldsComposite;
	
	private InstrumentViewModel instrument;
	
	private PV selectedPv;
	
	private boolean updateLock;
	
	private Text txtName;
	private Text txtAddress;
	private ComboViewer cmboMode;
	
	private static IO[] modeList = IO.values();

	public PvDetailView(Composite parent, InstrumentViewModel instrument) {
		super(parent, SWT.NONE);
		
		this.instrument = instrument;

		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		if (instrument != null) {
			instrument.addPVSelectionListener(new IPVSelectionListener() {			
				@Override
				public void selectionChanged(PV oldSelection, PV newSelection) {
					showPV(newSelection);
				}
			});
		}
		createControls(this);
		showPV(null);
	}
	
	public void createControls(Composite parent) {	
		labelComposite = new Composite(parent, SWT.NONE);
		labelComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		labelComposite.setLayout(new FillLayout());
		{
			Label lblNoSelection = new Label(labelComposite, SWT.NONE);
			lblNoSelection.setText("Select a PV to view/edit details");
		}
		
		fieldsComposite = new Composite(parent, SWT.NONE);
		fieldsComposite.setLayout(new GridLayout(2, false));
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		{
			Label lblName = new Label(fieldsComposite, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText("Name");
			
			txtName = new Text(fieldsComposite, SWT.BORDER);
			txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			txtName.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					updateModel();
				}
			});
			
			Label lblAddress = new Label(fieldsComposite, SWT.NONE);
			lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblAddress.setText("Address");
			
			txtAddress = new Text(fieldsComposite, SWT.BORDER);
			txtAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			txtAddress.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					updateModel();
				}
			});
			
			Label lblMode = new Label(fieldsComposite, SWT.NONE);
			lblMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblMode.setText("Mode");
			
			cmboMode = new ComboViewer(fieldsComposite, SWT.READ_ONLY);
			cmboMode.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			cmboMode.setContentProvider(ArrayContentProvider.getInstance());
			cmboMode.setInput(modeList);
			cmboMode.getCombo().select(0);
			cmboMode.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					updateModel();
				}
			});
		}
	}

	private void showPV(PV componentPv) {
		updateLock = true;
		
		selectedPv = componentPv;
		
		if (selectedPv != null) {
			fieldsComposite.setVisible(true);
			labelComposite.setVisible(false);
			
			txtName.setText(selectedPv.displayName());
			txtAddress.setText(selectedPv.address());
			
			IO mode = selectedPv.recordType().io();
			int typeIndex = Arrays.asList(modeList).indexOf(mode);
			cmboMode.getCombo().select(typeIndex);
		} else {
			fieldsComposite.setVisible(false);
			labelComposite.setVisible(true);
			
			txtName.setText("");
			txtAddress.setText("");
			cmboMode.getCombo().select(0);
		}
		
		updateLock = false;
	}
	
	private void updateModel() {
		if (!updateLock && selectedPv != null) {
			int typeIndex = cmboMode.getCombo().getSelectionIndex();
			IO mode = Arrays.asList(modeList).get(typeIndex);
			String name = txtName.getText();
			String address = txtAddress.getText();
			
			instrument.updateSelectedPV(name, address, mode);
		}
	}
}
