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

    /** vertical spacing between motor composites.*/
    protected static final int MOTOR_COMPOSITE_VERTIAL_SPACING = 1;
    
    /** horizontal margin between motor composites.*/
    protected static final int MOTOR_COMPOSITE_MARGIN_WIDTH = 1;
    
    /** vertical margin between motor composites.*/
    protected static final int MOTOR_COMPOSITE_MARGIN_HEIGHT = 1;
    
    /** Size of motor name label  in motor composites.*/
    protected static final int MOTOR_NAME_MINIMUM_WIDTH = MotorsOverview.WIDTH_DIMENSION - 2 * MOTOR_COMPOSITE_MARGIN_WIDTH;
    
    /** Size of label  in motor composites.*/
    protected static final int MOTOR_NAME_WIDTHHINT = MOTOR_NAME_MINIMUM_WIDTH;

    private MinimalMotorViewModel minimalMotorViewModel;
    
    /** Binding context. */
    protected DataBindingContext bindingContext = new DataBindingContext();
    
    /** model for view. */
    protected MinimalMotionIndicator minimalMotionIndicator;

    /** label displaying the motors name. */
    protected Label motorName;
    
    /** Label for motors value (position). */
    protected Label value;
    
    /** Label for motor setpoint. */
    protected Label setpoint;

    /**
     * Constructor.
     * @param parent parent of view
     * @param style SWT style
     * @param minimalMotorViewModel model with data for this view
     */
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

    /**
     * Click forwarder for selecting the motor OPI to show. 
     */
    protected MouseListener forwardClickOnMotorView = new MouseAdapter() {
	@Override
	public void mouseDown(MouseEvent e) {
	    Event event = new Event();
	    event.widget = MotorInfoView.this;

	    MotorInfoView.this.notifyListeners(SWT.MouseDown, event);
	}
    };
}
