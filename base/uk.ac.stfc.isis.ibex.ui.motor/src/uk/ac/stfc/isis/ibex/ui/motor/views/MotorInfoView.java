package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

public abstract class MotorInfoView extends Composite {
	
	protected static final Integer MOTOR_COMPOSITE_VERTIAL_SPACING = 2; 
	protected static final Integer MOTOR_COMPOSITE_MARGIN_WIDTH = 2;
	protected static final Integer MOTOR_COMPOSITE_MARGIN_HEIGHT = 2;
	protected static final Integer MOTOR_NAME_MINIMUM_WIDTH = 80;
	protected static final Integer MOTOR_NAME_WIDTHHINT = 80;
	
	private MinimalMotorViewModel minimalMotorViewModel;
	protected DataBindingContext bindingContext = new DataBindingContext();
	protected MinimalMotionIndicator indicator;
	
	protected Label motorName;
	protected Label value;
	protected Label setpoint;
	
	public MotorInfoView(Composite parent, int style, MinimalMotorViewModel minimalMotorViewModel) {
		super(parent, style);
		
		this.minimalMotorViewModel = minimalMotorViewModel;

	}
	
	/**
	* Gets the MinimalMotorViewModel used by the cell.
	* 
	* @return the motor view model used by the cell.
	*/
	public MinimalMotorViewModel getViewModel() {
	    return minimalMotorViewModel;
	}
	
	MouseListener forwardDoubleClick = new MouseAdapter() {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			Event event = new Event();
			event.widget = MotorInfoView.this;
	
			MotorInfoView.this.notifyListeners(SWT.MouseDoubleClick, event);
		}
	};
}
