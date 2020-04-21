package uk.ac.stfc.isis.ibex.ui.motor.views;

import java.util.Set;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * The viewer for an individual motors standard information.
 */
public class MotorInfoStdView extends MotorInfoView {

    /**
     * Constructor.
     * @param parent parent of this view
     * @param style SWT style for the view
     * @param minimalMotorViewModel model for the view information
     */
    public MotorInfoStdView(Composite parent, int style, MinimalMotorViewModel minimalMotorViewModel) {
		super(parent, style, minimalMotorViewModel);
		
        GridLayout glMotorComposite = new GridLayout(1, false);
		glMotorComposite.verticalSpacing = MOTOR_COMPOSITE_VERTIAL_SPACING;
		glMotorComposite.marginWidth = MOTOR_COMPOSITE_MARGIN_WIDTH;
		glMotorComposite.marginHeight = MOTOR_COMPOSITE_MARGIN_HEIGHT;
        setLayout(glMotorComposite);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        
        motorName = new Label(this, SWT.NONE);
		motorName.setAlignment(SWT.CENTER);
		GridData gdMotorName = new GridData(SWT.FILL, SWT.TOP, true, false);
		gdMotorName.minimumWidth = MOTOR_NAME_MINIMUM_WIDTH;
		gdMotorName.widthHint = MOTOR_NAME_WIDTHHINT;
		motorName.setLayoutData(gdMotorName);
		motorName.setText("Motor name");
		
        value = new Label(this, SWT.NONE);
		value.setAlignment(SWT.CENTER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        value.setText("");
        value.setToolTipText("The actual position of this motor");
		
        setpoint = new Label(this, SWT.NONE);
		setpoint.setAlignment(SWT.CENTER);
		setpoint.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        setpoint.setText("");
        setpoint.setToolTipText("The requested position of this motor");
		
        minimalMotionIndicator = new MinimalMotionIndicator(this, SWT.NONE);
		minimalMotionIndicator.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
        minimalMotionIndicator.setMotor(this.getViewModel().getMotor());
		
        setMouseListeners();
        
        bind();
	}

    /**
     * Binds the model to the view.
     */
    private void bind() {
  	
        bindingContext.bindValue(WidgetProperties.text().observe(setpoint),
                BeanProperties.value("setpoint").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.text().observe(value),
                BeanProperties.value("value").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.text().observe(motorName),
                BeanProperties.value("motorName").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.font().observe(motorName),
                BeanProperties.value("font").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.background().observe(motorName),
                BeanProperties.value("color").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.background().observe(minimalMotionIndicator),
                BeanProperties.value("color").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.background().observe(value),
                BeanProperties.value("color").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.background().observe(setpoint),
                BeanProperties.value("color").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.background().observe(this),
                BeanProperties.value("color").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.tooltipText().observe(this),
                BeanProperties.value("tooltip").observe(this.getViewModel()));
	}
    
	private void setMouseListeners() {
		final Set<Control> controls = Set.of(motorName, value, setpoint, minimalMotionIndicator);
		
		for (var control : controls) {
			control.addMouseListener(forwardClickOnMotorView);
		}
	}
}
