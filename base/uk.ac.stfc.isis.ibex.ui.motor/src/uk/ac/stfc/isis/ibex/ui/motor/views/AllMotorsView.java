package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.Motors;

import com.google.common.base.Strings;

public class AllMotorsView extends ViewPart {
	
	private ScrolledComposite scrolledComposite;
	private MotorsOverview overview;
	
	private static final int SIZE = 680;
	private static final int TABLE_MARGIN = 20;
	
	private MouseListener motorSelection = new MouseAdapter() {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			if (e.widget instanceof MinimalMotorView) {
				MinimalMotorView minimal = (MinimalMotorView) e.widget;
				openMotorView(minimal.motor());			
			}
		}
	};
	
	public AllMotorsView() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 0;
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		gl_parent.horizontalSpacing = 0;
		parent.setLayout(gl_parent);
				
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		scrolledComposite.setMinWidth(SIZE + TABLE_MARGIN);
		
		int numCrates = Motors.getInstance().getMotorsTable().getNumCrates();
		int motorHeight = 95;
		
		scrolledComposite.setMinHeight(numCrates * motorHeight + TABLE_MARGIN);
		scrolledComposite.setExpandHorizontal(true);

		int numMotors = Motors.getInstance().getMotorsTable().getNumMotors();
		int motorWidth = 90;
		
		scrolledComposite.setMinWidth(numMotors * motorWidth + TABLE_MARGIN);
		scrolledComposite.setExpandVertical(true);
		
		overview = new MotorsOverview(scrolledComposite, SWT.NONE);		
		GridData gd_overview = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_overview.minimumWidth = SIZE;
		gd_overview.heightHint = SIZE;
		gd_overview.minimumHeight = SIZE;
		gd_overview.widthHint = SIZE;
		overview.setLayoutData(gd_overview);
		
		scrolledComposite.setContent(overview);
		
		overview.addMouseListener(motorSelection);
		
		Label spacer = new Label(parent, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		overview.setMotors(Motors.getInstance().getMotorsTable());
	}

	@Override
	public void setFocus() {	
	}

	
	private static void openMotorView(Motor motor) {
		try {
    		// Display OPI motor view
			String description = motor.getDescription();
			String secondaryID = Strings.isNullOrEmpty(description) ? motor.name() : description;
			secondaryID = secondaryID.replace(":", "");
			
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart view = page.showView(MotorOPIView.ID, secondaryID, IWorkbenchPage.VIEW_ACTIVATE);
			
			MotorOPIView motorView = (MotorOPIView) view;
			motorView.setOPITitle(secondaryID);
			motorView.setMotorName(motor.motorAddress());
			motorView.initialiseOPI();		
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}			
	}
}
