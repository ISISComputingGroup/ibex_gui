package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.Arrays;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetDetailView extends Composite {

	private InstrumentViewModel instrument;
	
	private Composite labelComposite;
	private Composite fieldsComposite;
	private Composite addComposite;
	TargetNameWidget nameSelect;
	
	private ComboViewer cmboType;
	
	private static TargetType[] typeList = TargetType.values();
	
	public TargetDetailView(Composite parent, final InstrumentViewModel instrument) 
	{
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				if (updateType != UpdateTypes.EDIT_COMPONENT) {
					showTarget(instrument.getSelectedComponent());
				}
			}
		});
		
		instrument.addComponentSelectionListener(new IComponentSelectionListener() {			
			@Override
			public void selectionChanged(ComponentDescription oldSelection, ComponentDescription newSelection) {
				showTarget(newSelection);
			}
		});
		
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);
		showTarget(null);
	}
	
	public void createControls(Composite parent) {	
		labelComposite = new Composite(parent, SWT.NONE);
		labelComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		labelComposite.setLayout(new GridLayout(1, false));
		{
			Label lblNoSelection = new Label(labelComposite, SWT.NONE);
			lblNoSelection.setText("Select a Component to view/edit details");
		}
		
		fieldsComposite = new Composite(parent, SWT.NONE);
		fieldsComposite.setLayout(new GridLayout(2, false));
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		{
			Label lblName = new Label(fieldsComposite, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText("Name");
			
			nameSelect = new TargetNameWidget(fieldsComposite, instrument);
			nameSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label lblType = new Label(fieldsComposite, SWT.NONE);
			lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblType.setText("Type");
			
			cmboType = new ComboViewer(fieldsComposite, SWT.READ_ONLY);
			cmboType.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			cmboType.setContentProvider(new ArrayContentProvider());
			cmboType.setInput(typeList);
			cmboType.getCombo().select(0);
			cmboType.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					int typeIndex = cmboType.getCombo().getSelectionIndex();
					TargetType type = Arrays.asList(typeList).get(typeIndex);
					
					nameSelect.setTargetType(type);
				}
			});
			
			Label lblProperties = new Label(fieldsComposite, SWT.NONE);
			lblProperties.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
			lblProperties.setText("Properties");
			
			TargetPropertyList properties = new TargetPropertyList(fieldsComposite, instrument);
			properties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			properties.showPropertyList(instrument.getSelectedComponent());
		}
		
		addComposite = new Composite(parent, SWT.NONE);
		addComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		addComposite.setLayout(new GridLayout(1, false));
		{
			Label lblNoTarget = new Label(addComposite, SWT.NONE);
			lblNoTarget.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			lblNoTarget.setText("Component currently has no target.");
			
			Button btnAdd = new Button(addComposite, SWT.NONE);
		    btnAdd.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		    btnAdd.setText("Add Target");
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.addTargetToSelectedComponent();
				}
			});
		}
	}
	
	private void showTarget(ComponentDescription component) {		
		if (component == null) {
			fieldsComposite.setVisible(false);
			labelComposite.setVisible(true);
			addComposite.setVisible(false);
			
			nameSelect.setTarget(null);
			cmboType.getCombo().select(0);
		} else {
			TargetDescription target = component.target();
			
			if (target != null) {
				fieldsComposite.setVisible(true);
				labelComposite.setVisible(false);
				addComposite.setVisible(false);
				
				TargetType type = target.type();
				int typeIndex = Arrays.asList(typeList).indexOf(type);
				cmboType.getCombo().select(typeIndex);
				
				nameSelect.setTarget(target);
			} else {
				fieldsComposite.setVisible(false);
				labelComposite.setVisible(false);
				addComposite.setVisible(true);
				
				nameSelect.setTarget(null);
				cmboType.getCombo().select(0);
			}
		}
	}
}
