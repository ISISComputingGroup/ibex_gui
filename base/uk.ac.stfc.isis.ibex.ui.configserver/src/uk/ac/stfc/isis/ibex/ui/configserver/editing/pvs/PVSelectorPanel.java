package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlockPVTable;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.InterestFilters;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.PVFilter;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.PVFilterFactory;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters.SourceFilters;


/**
 * A composite for selecting a PV
 *
 */
public class PVSelectorPanel extends Composite {

	private final Text pvAddress;
	private ComboViewer interestLevel;
	private ComboViewer pvSource;
	private final BlockPVTable blockPVTable;
	private PVFilterFactory filterFactory;
	private PVFilter sourceFilter;
	private PVFilter interestFilter;
	private DataBindingContext bindingContext;
	
	public PVSelectorPanel(Composite parent, int style) {
		super(parent, style);

		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpPV = new Group(this, SWT.NONE);
		grpPV.setText("PV Selector");
		grpPV.setLayout(new GridLayout(2, false));
		
		Label lblViewPVs = new Label(grpPV, SWT.NONE);
		lblViewPVs.setAlignment(SWT.RIGHT);
		lblViewPVs.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblViewPVs.setText("PVs From:");		
		
		pvSource = new ComboViewer(grpPV, SWT.READ_ONLY);
		GridData gd_pvSource = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_pvSource.widthHint = 100;
		pvSource.getCombo().setLayoutData(gd_pvSource);
		pvSource.setContentProvider(new ArrayContentProvider());
		pvSource.setInput(SourceFilters.values());	

		
		Label lblInterestLevel = new Label(grpPV, SWT.NONE);
		lblInterestLevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInterestLevel.setText("Interest Level:");
		
		interestLevel = new ComboViewer(grpPV, SWT.READ_ONLY);
		GridData gd_interestLevel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_interestLevel.widthHint = 100;
		interestLevel.getCombo().setLayoutData(gd_interestLevel);
		interestLevel.setContentProvider(new ArrayContentProvider());
		interestLevel.setInput(InterestFilters.values());	
		
		Label lblPvAddress = new Label(grpPV, SWT.NONE);
		lblPvAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPvAddress.setText("PV address:");
		
		pvAddress = new Text(grpPV, SWT.BORDER);
		GridData gd_pvAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_pvAddress.widthHint = 250;
		pvAddress.setLayoutData(gd_pvAddress);
		
		blockPVTable = new BlockPVTable(grpPV, SWT.NONE, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION);
		blockPVTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		blockPVTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size() > 0) {
					PV pv = (PV)selection.getFirstElement();
					pvAddress.setText(pv.getAddress());
				}
			}
		});


	}
	
	public void setConfig(EditableConfiguration config, PV pv) {
		setPVs(config.pvs());
		
		filterFactory = new PVFilterFactory(config.getEditableIocs());
		
		//respond to changes in combo box
		pvSource.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				StructuredSelection selection = (StructuredSelection)arg0.getSelection();
				SourceFilters PVfilter = (SourceFilters)selection.getFirstElement();
				changeSourceFilter(PVfilter);
			}
		});
		
		interestLevel.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				StructuredSelection selection = (StructuredSelection)arg0.getSelection();
				InterestFilters interestFilter = (InterestFilters)selection.getFirstElement();
				changeInterestFilter(interestFilter);
			}
		});
		
		//Provide a default selection
		pvSource.setSelection(new StructuredSelection(SourceFilters.ACTIVE));
		interestLevel.setSelection(new StructuredSelection(InterestFilters.HIGH));
		
		//Set up the binding here
		bindingContext = new DataBindingContext();		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(pvAddress), BeanProperties.value("address").observe(pv), null, null);
	}
	
	private void changeSourceFilter(SourceFilters pvFilter) {
		sourceFilter = filterFactory.getFilter(pvFilter);
		blockPVTable.setSourceFilter(sourceFilter.getFilter());
		addFilterListener(sourceFilter);
	}
	
	private void changeInterestFilter(InterestFilters pvFilter) {
		interestFilter = filterFactory.getFilter(pvFilter);
		blockPVTable.setInterestFilter(interestFilter.getFilter());
		addFilterListener(interestFilter);
	}	
	
	private void addFilterListener(PVFilter filter) {
		sourceFilter.addPropertyChangeListener("refresh", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				blockPVTable.refresh();
			}
		});		
	}
	
	private void setPVs(Collection<PV> allPVs) {    
	   	blockPVTable.setRows(allPVs);
	}

}
