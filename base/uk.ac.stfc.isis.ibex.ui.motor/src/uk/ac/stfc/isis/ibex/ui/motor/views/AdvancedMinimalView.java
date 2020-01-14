package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AdvancedMinimalView extends MotorInfoView {

	public AdvancedMinimalView(Composite parent, int style, MinimalMotorViewModel minimalMotorViewModel) {
		super(parent, style, minimalMotorViewModel); 
		
        GridLayout glMotorComposite = new GridLayout(2, false);
		glMotorComposite.verticalSpacing = MOTOR_COMPOSITE_VERTIAL_SPACING;
		glMotorComposite.marginWidth = MOTOR_COMPOSITE_MARGIN_WIDTH;
		glMotorComposite.marginHeight = MOTOR_COMPOSITE_MARGIN_HEIGHT;
        setLayout(glMotorComposite);
   
        motorName = new Label(this, SWT.NONE);
		motorName.setAlignment(SWT.CENTER);
		GridData gdMotorName = new GridData(SWT.TOP, SWT.TOP, false, false, 2, 1);
		gdMotorName.minimumWidth = MOTOR_NAME_MINIMUM_WIDTH;
		gdMotorName.widthHint = MOTOR_NAME_WIDTHHINT;
		motorName.setLayoutData(gdMotorName);
		motorName.setText("Motor name");
				
        value = new Label(this, SWT.NONE);
		value.setAlignment(SWT.CENTER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
        value.setText("");
		
        setpoint = new Label(this, SWT.NONE);
		setpoint.setAlignment(SWT.CENTER);
		setpoint.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
        setpoint.setText("");
		
        indicator = new MinimalMotionIndicator(this, SWT.NONE);
		indicator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
        indicator.setMotor(this.getViewModel().getMotor());

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

        bindingContext.bindValue(WidgetProperties.background().observe(indicator),
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

		motorName.addMouseListener(forwardDoubleClick);
		value.addMouseListener(forwardDoubleClick);
		setpoint.addMouseListener(forwardDoubleClick);
		indicator.addMouseListener(forwardDoubleClick);
		
	}	
    
}
