package uk.ac.stfc.isis.ibex.ui.motor.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTable;

public class MotorsOverview extends Composite {
	private Composite motorComposite;
	
	private List<MinimalMotorView> minimalViews = new LinkedList<>();
	
	private final List<MouseListener> mouseListeners = new ArrayList<>();
	
	private final Color background = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	
	public MotorsOverview(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(192, 192, 192));
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		motorComposite = new Composite(this, SWT.NONE);
		motorComposite.setBackground(background);
	}
	
	public void setMotors(MotorsTable motorsTable) {
		motorComposite.setLayout(new GridLayout(motorsTable.getNumMotors() + 1, false));
		
		addSpacerLabel();		
		for (int i = 1; i <= motorsTable.getNumMotors(); i++) {
			addNumberLabel(i);
		}		
		
		resetViews();
		
		int i = 0;
		for (Motor motor : motorsTable.motors()) {
			if (i % motorsTable.getNumMotors() == 0) {
				addNumberLabel(1 + i / motorsTable.getNumMotors());
			}
			i++;
			
			addMinimalView(motor);
		}
	}

	@Override
	public void addMouseListener(MouseListener listener) {
		super.addMouseListener(listener);
		mouseListeners.add(listener);
	}
	
	@Override
	public void removeMouseListener(MouseListener listener) {
		super.removeMouseListener(listener);
		mouseListeners.remove(listener);
		
		for (MinimalMotorView view : minimalViews) {
			view.removeMouseListener(listener);
		}
	}
	
	private void addMinimalView(Motor motor) {
		MinimalMotorView view = new MinimalMotorView(motorComposite, SWT.NONE);
		view.setMotor(motor);
		view.setLayoutData(viewLayout());
		
		minimalViews.add(view);

		for (MouseListener listener : mouseListeners) {
			view.addMouseListener(listener);
		}
	}
	
	private void addSpacerLabel() {
		Label spacer = new Label(motorComposite, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		spacer.setBackground(background);
	}
	
	private void addNumberLabel(int columnNumber) {
		Label columnLabel = new Label(motorComposite, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd.minimumWidth = 10;
		columnLabel.setLayoutData(gd);
		columnLabel.setAlignment(SWT.CENTER);
		columnLabel.setText(Integer.toString(columnNumber));
		columnLabel.setBackground(background);
	}
	
	private void resetViews() {
		for (MinimalMotorView view : minimalViews) {
			view.dispose();
		}
		minimalViews.clear();
	}
	
	private static GridData viewLayout() {
		final GridData gd = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		int heightDimension = 74;
		int widthDimension = 85;
		gd.minimumHeight = heightDimension;
		gd.minimumWidth = widthDimension;
		gd.widthHint = widthDimension;
		gd.heightHint = heightDimension;
		
		return gd;
	}
}
