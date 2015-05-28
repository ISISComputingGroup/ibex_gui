package uk.ac.stfc.isis.ibex.ui.goniometer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.goniometer.views.SetpointsTableView;

public class GoniometerPanel extends ScrolledComposite {
	private SetpointsTableView goniometerSettings;
	
	public void setModel(GoniometerViewModel model) {
		goniometerSettings.setRows(model.settings());
	}
	
	public GoniometerPanel(Composite parent, int style) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		setExpandHorizontal(true);
		setExpandVertical(true);
		
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.horizontalSpacing = 20;
		parent.setLayout(gl_parent);
		
		goniometerSettings = new SetpointsTableView(parent, SWT.NONE);
		//goniometerSettings.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_goniometerSettings = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_goniometerSettings.heightHint = 210;
		gd_goniometerSettings.widthHint = 500;
		goniometerSettings.setLayoutData(gd_goniometerSettings);
		
		setModel(Activator.getDefault().viewModel());
	}
}
