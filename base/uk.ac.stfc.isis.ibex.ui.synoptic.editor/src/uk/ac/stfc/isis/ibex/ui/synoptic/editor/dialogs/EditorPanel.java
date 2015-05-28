package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentDetailView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeControls;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvDetailView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.TargetDetailView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.TargetPropertyDetailView;

public class EditorPanel extends Composite {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.EditorView";
	
	private static Font titleFont = SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD);
	
	private InstrumentViewModel instrumentViewModel = new InstrumentViewModel();
	
	private Composite treeComposite;
	private Composite detailBarComposite;
	private Composite targetBarComposite;
	private Composite componentComposite;
	private Composite pvComposite;	
	private Composite targetComposite;
	private Composite propertyComposite;
	
	public EditorPanel (Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		GridData treeGridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData detailBarData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData targetBarData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		GridData componentGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData targetGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData propertyGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData pvGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		treeGridData.widthHint = 300;
		detailBarData.widthHint = 300;
		targetBarData.widthHint = 300;
		
		GridLayout detailBarLayout = new GridLayout(1, false);
		detailBarLayout.marginHeight = 0;
		detailBarLayout.marginWidth = 0;
		
		GridLayout targetBarLayout = new GridLayout(1, false);
		targetBarLayout.marginHeight = 0;
		targetBarLayout.marginWidth = 0;
		

		treeComposite = new Composite(this, SWT.BORDER);
		treeComposite.setLayout(new GridLayout(1, false));
		treeComposite.setLayoutData(treeGridData);
		{			
			Label lblInstrumentTree = new Label(treeComposite, SWT.NONE);
			lblInstrumentTree.setFont(titleFont);
			lblInstrumentTree.setText("Instrument Tree");
	
			new InstrumentTreeView(treeComposite, instrumentViewModel);
			new InstrumentTreeControls(treeComposite, instrumentViewModel);
		}
		
		detailBarComposite = new Composite(this, SWT.NONE);
		detailBarComposite.setLayout(detailBarLayout);
		detailBarComposite.setLayoutData(detailBarData);
		{
			componentComposite = new Composite(detailBarComposite, SWT.BORDER);
			componentComposite.setLayout(new GridLayout(1, false));
			componentComposite.setLayoutData(componentGridData);
			{
				Label lblComponentTitle = new Label(componentComposite, SWT.NONE);
				lblComponentTitle.setFont(titleFont);
				lblComponentTitle.setText("Component Details");
				
				new ComponentDetailView(componentComposite, instrumentViewModel);
			}
			
			pvComposite = new Composite(detailBarComposite, SWT.BORDER);
			pvComposite.setLayout(new GridLayout(1, false));
			pvComposite.setLayoutData(pvGridData);
			{
				Label lblPvTitle = new Label(pvComposite, SWT.NONE);
				lblPvTitle.setFont(titleFont);
				lblPvTitle.setText("PV Details");
				
				new PvDetailView(pvComposite, instrumentViewModel);
			}
		}
		
		targetBarComposite = new Composite(this, SWT.NONE);
		targetBarComposite.setLayout(targetBarLayout);
		targetBarComposite.setLayoutData(targetBarData);
		{
			targetComposite = new Composite(targetBarComposite, SWT.BORDER);
			targetComposite.setLayout(new GridLayout(1, false));
			targetComposite.setLayoutData(targetGridData);
			{
				Label lblTargetTitle = new Label(targetComposite, SWT.NONE);
				lblTargetTitle.setFont(titleFont);
				lblTargetTitle.setText("Component Target Details");
				
				new TargetDetailView(targetComposite, instrumentViewModel);
			}
			
			propertyComposite = new Composite(targetBarComposite, SWT.BORDER);
			propertyComposite.setLayout(new GridLayout(1, false));
			propertyComposite.setLayoutData(propertyGridData);
			{
				Label lblPropertyTitle = new Label(propertyComposite, SWT.NONE);
				lblPropertyTitle.setFont(titleFont);
				lblPropertyTitle.setText("Target Property Details");
				
				TargetPropertyDetailView propertyDetail = new TargetPropertyDetailView(propertyComposite, SWT.NONE);
				propertyDetail.setModel(instrumentViewModel);
				
				propertyDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			}
		}
	}
	
	public void setSynopticToEdit(InstrumentDescription instrument) {
		instrumentViewModel.loadInstrumentDescription(instrument);
	}
}
