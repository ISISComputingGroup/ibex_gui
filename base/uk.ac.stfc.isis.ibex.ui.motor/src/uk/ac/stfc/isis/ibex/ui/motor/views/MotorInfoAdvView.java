package uk.ac.stfc.isis.ibex.ui.motor.views;

import java.util.Set;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * The viewer for an individual motors advanced information.
 */
public class MotorInfoAdvView extends MotorInfoView {
	
	private Composite iconContainer;
	
	private Composite setpointAndReadbackContainer;
	private Composite limitsContainer;
	private Composite errorAndOffsetContainer;
	
	private Label highLimit;
	private Label lowLimit;
	private Label offset;
	private Label error;
	
	private static final Font ARIAL_7PT = new Font(Display.getCurrent(), new FontData("Arial", 7, SWT.NONE));

	public MotorInfoAdvView(Composite parent, int style, MinimalMotorViewModel minimalMotorViewModel) {
		super(parent, style, minimalMotorViewModel); 
		
        GridLayout glMotorComposite = new GridLayout(1, false);
		glMotorComposite.verticalSpacing = MOTOR_COMPOSITE_VERTIAL_SPACING;
		glMotorComposite.marginWidth = MOTOR_COMPOSITE_MARGIN_WIDTH;
		glMotorComposite.marginHeight = MOTOR_COMPOSITE_MARGIN_HEIGHT;
        setLayout(glMotorComposite);
   
        motorName = new Label(this, SWT.NONE);
		motorName.setAlignment(SWT.CENTER);
		GridData gdMotorName = new GridData(SWT.TOP, SWT.TOP, false, false);
		gdMotorName.minimumWidth = MOTOR_NAME_MINIMUM_WIDTH;
		gdMotorName.widthHint = MOTOR_NAME_WIDTHHINT;
		motorName.setLayoutData(gdMotorName);
		motorName.setText("Motor name");
		
		createSetpointAndReadback();
        
        createLimits();
        
        createErrorAndOffset();
        
        iconContainer = new Composite(this, SWT.NONE);
        
        iconContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        
        GridLayout iconContainerLayout = new GridLayout(3, false);
        iconContainerLayout.marginHeight = 0;
        iconContainerLayout.marginWidth = 0;
        iconContainer.setLayout(iconContainerLayout);
		
        minimalMotionIndicator = new MinimalMotionIndicator(iconContainer, SWT.NONE);
		minimalMotionIndicator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        minimalMotionIndicator.setMotor(this.getViewModel().getMotor());

        setMouseListeners();
        
        bind();
	}
	
	private Label createMotorValueLabel(final Composite container) {
		final var label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		label.setText("");
		label.setFont(ARIAL_7PT);
		return label;
	}
	
	private Composite createTwoColumnContainer() {
        var container = new Composite(this, SWT.NONE);
		
		GridLayout containerLayout = new GridLayout(2, true);
		containerLayout.marginHeight = 0;
		containerLayout.marginWidth = 0;
		containerLayout.horizontalSpacing = 0;
		containerLayout.verticalSpacing = 0;
		
		container.setLayout(containerLayout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		return container;
	}

	private void createErrorAndOffset() {
		errorAndOffsetContainer = createTwoColumnContainer();
        offset = createMotorValueLabel(errorAndOffsetContainer);
        error = createMotorValueLabel(errorAndOffsetContainer);
	}

	private void createLimits() {
		limitsContainer = createTwoColumnContainer();
		lowLimit = createMotorValueLabel(limitsContainer);
		highLimit = createMotorValueLabel(limitsContainer);
	}

	private void createSetpointAndReadback() {
		setpointAndReadbackContainer = createTwoColumnContainer();
        value = createMotorValueLabel(setpointAndReadbackContainer);
        setpoint = createMotorValueLabel(setpointAndReadbackContainer);
	}

    /**
     * Binds the model to the view.
     */
    private void bind() {
  	
        bindingContext.bindValue(WidgetProperties.text().observe(setpoint),
                BeanProperties.value("setpoint").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.text().observe(value),
                BeanProperties.value("value").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.text().observe(lowLimit),
                BeanProperties.value("lowLimit").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.text().observe(highLimit),
                BeanProperties.value("highLimit").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.text().observe(offset),
                BeanProperties.value("offset").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.text().observe(error),
                BeanProperties.value("error").observe(this.getViewModel()));

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
        
        bindingContext.bindValue(WidgetProperties.background().observe(setpointAndReadbackContainer),
                BeanProperties.value("color").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.background().observe(limitsContainer),
                BeanProperties.value("color").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.background().observe(errorAndOffsetContainer),
                BeanProperties.value("color").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.background().observe(lowLimit),
                BeanProperties.value("color").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.background().observe(highLimit),
                BeanProperties.value("color").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.background().observe(offset),
                BeanProperties.value("color").observe(this.getViewModel()));
        
        bindingContext.bindValue(WidgetProperties.background().observe(error),
                BeanProperties.value("color").observe(this.getViewModel()));

        bindingContext.bindValue(WidgetProperties.tooltipText().observe(this),
                BeanProperties.value("tooltip").observe(this.getViewModel()));
	}
    
	private void setMouseListeners() {
		final Set<Control> controls = Set.of(motorName, value, setpoint, lowLimit, highLimit, offset, error, iconContainer, minimalMotionIndicator);
		
		for (var control : controls) {
			control.addMouseListener(forwardDoubleClick);
		}
	}
    
}
