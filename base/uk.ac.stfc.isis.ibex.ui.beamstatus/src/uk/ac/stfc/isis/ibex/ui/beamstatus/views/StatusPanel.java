package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.ExpandItem;

public class StatusPanel extends Composite {

	public StatusPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledComposite scrolled = new ScrolledComposite(this, SWT.V_SCROLL);
		
		ExpandBar expandBar = new ExpandBar(scrolled, SWT.NONE);
		scrolled.setContent(expandBar);
		
		ExpandItem xpndtmSynchrotron = new ExpandItem(expandBar, SWT.NONE);
		xpndtmSynchrotron.setExpanded(true);
		xpndtmSynchrotron.setText("Synchrotron");
		SynchrotronPanel sync = new SynchrotronPanel(expandBar, SWT.NONE);
		xpndtmSynchrotron.setControl(sync);
		xpndtmSynchrotron.setHeight(60);

		ExpandItem xpndtmTargetStation1 = new ExpandItem(expandBar, SWT.NONE);
		xpndtmTargetStation1.setExpanded(true);
		xpndtmTargetStation1.setText("Target Station 1");
		TargetStationOnePanel ts1 = new TargetStationOnePanel(expandBar, SWT.NONE);
		xpndtmTargetStation1.setControl(ts1);
		xpndtmTargetStation1.setHeight(220);

		ExpandItem xpndtmTargetStation2 = new ExpandItem(expandBar, SWT.NONE);
		xpndtmTargetStation2.setExpanded(true);
		xpndtmTargetStation2.setText("Target Station 2");
		TargetStationTwoPanel ts2 = new TargetStationTwoPanel(expandBar, SWT.NONE);
		xpndtmTargetStation2.setControl(ts2);
		xpndtmTargetStation2.setHeight(400);	
		
		expandBar.layout();
		expandBar.setSize(400, 660);
	}
}
