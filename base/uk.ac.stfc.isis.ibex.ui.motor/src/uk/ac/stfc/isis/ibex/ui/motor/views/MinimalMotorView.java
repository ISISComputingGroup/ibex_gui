package uk.ac.stfc.isis.ibex.ui.motor.views;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;

public class MinimalMotorView extends Composite {

	private DataBindingContext bindingContext = new DataBindingContext();

	private Composite motorComposite;
	private MinimalMotionIndicator indicator;
	private Label status;
	
	private final Display display = Display.getDefault();
	
	private static final Font ENABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.BOLD);
	private static final Font DISABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.ITALIC);
		
	private Motor motor;
	private Label motorName;
		
	private static final Color MOVINGCOLOR = SWTResourceManager.getColor(160, 250, 170);
	private static final Color STOPPEDCOLOR = SWTResourceManager.getColor(255, 200, 200);
	private static final Color DISABLEDCOLOR = SWTResourceManager.getColor(220, 220, 220);

	private Label value;
	private Label setpoint;
	
	public MinimalMotorView(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		motorComposite = new Composite(this, SWT.BORDER);
		motorComposite.setFont(ENABLEDFONT);
		GridLayout gl_motorComposite = new GridLayout(1, false);
		gl_motorComposite.verticalSpacing = 2;
		gl_motorComposite.marginWidth = 2;
		gl_motorComposite.marginHeight = 1;
		motorComposite.setLayout(gl_motorComposite);
		
		motorName = new Label(motorComposite, SWT.NONE);
		motorName.setAlignment(SWT.CENTER);
		GridData gd_motorName = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_motorName.minimumWidth = 80;
		gd_motorName.widthHint = 80;
		motorName.setLayoutData(gd_motorName);
		motorName.setText("Motor name");
		
		status = new Label(motorComposite, SWT.NONE);
		status.setAlignment(SWT.CENTER);
		status.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		status.setText("Status");
		
		value = new Label(motorComposite, SWT.NONE);
		value.setAlignment(SWT.CENTER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		value.setText("Val: 2.12");
		
		setpoint = new Label(motorComposite, SWT.NONE);
		setpoint.setAlignment(SWT.CENTER);
		setpoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		setpoint.setText("SP: 1.12");
		
		indicator = new MinimalMotionIndicator(motorComposite, SWT.NONE);
		indicator.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		
		setMouseListeners();
	}
		
	public Motor motor() {
		return motor;
	}
	
	public void setMotor(final Motor motor) {
		this.motor = motor;
		
		bindingContext.bindValue(WidgetProperties.text().observe(status), BeanProperties.value("status").observe(motor));	
		bindingContext.bindValue(WidgetProperties.text().observe(motorName), BeanProperties.value("description").observe(motor));	
		
		indicator.setMotor(motor);
		
		setEnabled(motor.getEnabled());
		motor.addPropertyChangeListener("enabled", new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setEnabled(motor.getEnabled());
			}

		});
		
		setMoving(motor);
		motor.addPropertyChangeListener("moving", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setMoving(motor);
			}
		});
		
		setValue(motor);
		setSetpoint(motor);
		motor.getSetpoint().addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setValue(motor);
			}
		});
		motor.getSetpoint().addPropertyChangeListener("setpoint", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setSetpoint(motor);
			}
		});	
	}
	
	private void setMouseListeners() {
		final MinimalMotorView self = this;
		MouseListener forwardDoubleClick = new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Event event = new Event();
				event.widget = self;
		
				self.notifyListeners(SWT.MouseDoubleClick, event);
			}
		};
		
		motorComposite.addMouseListener(forwardDoubleClick);
		motorName.addMouseListener(forwardDoubleClick);
		status.addMouseListener(forwardDoubleClick);
		value.addMouseListener(forwardDoubleClick);
		setpoint.addMouseListener(forwardDoubleClick);
		indicator.addMouseListener(forwardDoubleClick);
	}
	
	private void setSetpoint(final Motor motor) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				Double setpt = motor.getSetpoint().getSetpoint();
				String text = setpt != null ? 
						String.format("SP: %.2f", setpt) : "";
				setpoint.setText(text);
			}
		});
	}

	private void setValue(final Motor motor) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				Double setpointValue = motor.getSetpoint().getValue();
				String text = setpointValue != null ? 
						String.format("Val: %.2f", setpointValue) : "";
				value.setText(text);
			}
		});
	}
	
	private void setEnabled(final MotorEnable enabled) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				boolean isEnabled = enabled == MotorEnable.ENABLE;
				motorComposite.setEnabled(isEnabled);
				motorName.setFont(isEnabled ? ENABLEDFONT : DISABLEDFONT);
				
				setColor(motor);
			}
		});
	}
	
	private void setMoving(final Motor motor) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				setColor(motor);
			}
		});
	}

	private void setColor(Motor motor) {
		Boolean movingValue = motor.getMoving();
		boolean isMoving = movingValue != null && movingValue;
		boolean isEnabled = motor.getEnabled() == MotorEnable.ENABLE;

		setColor(isEnabled ? (isMoving ? MOVINGCOLOR : STOPPEDCOLOR) : DISABLEDCOLOR);
		
	}
	
	private void setColor(Color color) {
		motorComposite.setBackground(color);
		indicator.setBackground(color);
		status.setBackground(color);
		motorName.setBackground(color);
		value.setBackground(color);
		setpoint.setBackground(color);
	}
}
