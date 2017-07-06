 
package uk.ac.stfc.isis.ibex.e4.ui.dashboard.views;

import javax.inject.Inject;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.stfc.isis.ibex.e4.ui.mocks.dashboard.Banner;
import uk.stfc.isis.ibex.e4.ui.mocks.dashboard.MonitorPanel;
import uk.stfc.isis.ibex.e4.ui.mocks.dashboard.TimePanel;
import uk.stfc.isis.ibex.e4.ui.mocks.dashboard.TitlePanel;

public class DashboardView {
	
	private final Font bannerTitleFont = SWTResourceManager.getFont("Arial", 24, SWT.BOLD);
	private final Font bannerFont = SWTResourceManager.getFont("Arial", 14, SWT.NORMAL);
	private final Font textFont = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
    
	@Inject
	public DashboardView() {
		System.out.println("Initialising dashboard view");
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		GridLayout glParent = new GridLayout(3, false);
		glParent.marginHeight = 0;
		glParent.marginWidth = 0;
		glParent.horizontalSpacing = 1;
		glParent.verticalSpacing = 0;
		parent.setLayout(glParent);
		Banner banner = new Banner(parent, SWT.NONE, bannerTitleFont, bannerFont);
		banner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		Label separator1 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		TitlePanel title = new TitlePanel(parent, SWT.NONE, textFont);
		title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label separator2 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		MonitorPanel monitors = new MonitorPanel(parent, SWT.NONE, textFont);
		monitors.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label separator3 = new Label(parent, SWT.SEPARATOR | SWT.VERTICAL);
		separator3.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
		TimePanel times = new TimePanel(parent, SWT.NONE, textFont);
		times.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}
	
}