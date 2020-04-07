package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

/**
 * The abstract viewer for an individual motors info such as read back values.
 */
public abstract class MotorInfoView extends Composite {
	
	protected static final int MOTOR_COMPOSITE_VERTIAL_SPACING = 1; 
	protected static final int MOTOR_COMPOSITE_MARGIN_WIDTH = 1;
	protected static final int MOTOR_COMPOSITE_MARGIN_HEIGHT = 1;
	protected static final int MOTOR_NAME_MINIMUM_WIDTH = MotorsOverview.WIDTH_DIMENSION - 2 * MOTOR_COMPOSITE_MARGIN_WIDTH;
	protected static final int MOTOR_NAME_WIDTHHINT = MOTOR_NAME_MINIMUM_WIDTH;
	
	private MinimalMotorViewModel minimalMotorViewModel;
	protected DataBindingContext bindingContext = new DataBindingContext();
	protected MinimalMotionIndicator minimalMotionIndicator;
	
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
	
	protected MouseListener forwardDoubleClick = new MouseAdapter() {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			Event event = new Event();
			event.widget = MotorInfoView.this;
	
			MotorInfoView.this.notifyListeners(SWT.MouseDoubleClick, event);
		}
	};
}
