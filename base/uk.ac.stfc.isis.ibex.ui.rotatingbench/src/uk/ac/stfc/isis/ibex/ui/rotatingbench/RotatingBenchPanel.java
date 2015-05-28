package uk.ac.stfc.isis.ibex.ui.rotatingbench;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.rotatingbench.IRotatingBench;

/**
 * Class to control the rotating bench
 * @author sjb99183
 *
 */
public class RotatingBenchPanel extends ScrolledComposite {

	public static final Font LABEL_FONT = SWTResourceManager.getFont("Arial", 9, SWT.NORMAL);
	public static final Font GROUP_FONT = SWTResourceManager.getFont("Arial", 9, SWT.NORMAL);
	
	private Label rotationAngle;
	private Label status;	
	private Label liftStatus;	
	private Label motion;
	private Button btnPowerDownHv;
	private Text angleSPtext;
	private Button angleSPsetButton;
	private Composite composite;
	
	public RotatingBenchPanel(Composite parent, int style) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		setExpandHorizontal(true);
		setExpandVertical(true);
		
		composite = new Composite(this, style);
		composite.setLayout(new GridLayout(1, false));
		
		Composite statusComposite = new Composite(composite, SWT.BORDER);
		statusComposite.setLayout(new GridLayout(4, false));
		statusComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStatus = new Label(statusComposite, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatus.setText("Status:");
		
		status = new Label(statusComposite, SWT.NONE);
		GridData gd_status = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_status.minimumWidth = 70;
		gd_status.widthHint = 100;
		status.setLayoutData(gd_status);
		new Label(statusComposite, SWT.NONE);
		
		motion = new Label(statusComposite, SWT.NONE);
		motion.setAlignment(SWT.RIGHT);
		motion.setText("Bench status is unknown");
		GridData gd_motion = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_motion.widthHint = 120;
		gd_motion.minimumWidth = 120;
		motion.setLayoutData(gd_motion);
		
		Group grpBench = new Group(composite, SWT.NONE);
		grpBench.setFont(GROUP_FONT);
		grpBench.setLayout(new GridLayout(3, false));
		grpBench.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpBench.setText("Bench");
		
		Label lblRotationAngle = new Label(grpBench, SWT.NONE);
		lblRotationAngle.setFont(LABEL_FONT);
		lblRotationAngle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRotationAngle.setBounds(0, 0, 55, 15);
		lblRotationAngle.setText("Rotation angle:");
		
		angleSPtext = new Text(grpBench, SWT.BORDER | SWT.RIGHT);
		angleSPtext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		angleSPtext.setFont(LABEL_FONT);
		
		
		angleSPsetButton = new Button(grpBench, SWT.CENTER);
		angleSPsetButton.setEnabled(false);
		angleSPsetButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/tick.png"));
		
		angleSPtext.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				angleSPtext.removeModifyListener(textModifyListener);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				angleSPtext.addModifyListener(textModifyListener);
			}
		});

		Label lblCurrentAngle = new Label(grpBench, SWT.NONE);
		lblCurrentAngle.setFont(LABEL_FONT);
		lblCurrentAngle.setText("Current angle:");
		
		rotationAngle = new Label(grpBench, SWT.NONE);
		rotationAngle.setFont(LABEL_FONT);
		GridData gd_rotationAngle = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_rotationAngle.minimumWidth = 70;
		gd_rotationAngle.widthHint = 100;
		rotationAngle.setLayoutData(gd_rotationAngle);
		new Label(grpBench, SWT.NONE);
		
		Group grpAir = new Group(composite, SWT.NONE);
		grpAir.setFont(GROUP_FONT);
		grpAir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpAir.setText("Compressed Air");
		
		Label lblLiftStatus = new Label(grpAir, SWT.NONE);
		lblLiftStatus.setFont(LABEL_FONT);
		lblLiftStatus.setBounds(10, 28, 56, 13);
		lblLiftStatus.setText("Lift status:");
		
		liftStatus = new Label(grpAir, SWT.NONE);
		liftStatus.setFont(LABEL_FONT);
		liftStatus.setBounds(102, 28, 297, 13);
		liftStatus.setText("raised");
		
		Group grpPower = new Group(composite, SWT.NONE);
		grpPower.setFont(GROUP_FONT);
		grpPower.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpPower.setText("Power");
		
		btnPowerDownHv = new Button(grpPower, SWT.CHECK);
		btnPowerDownHv.setFont(LABEL_FONT);
		btnPowerDownHv.setBounds(10, 23, 196, 16);
		btnPowerDownHv.setText("Power down HV before moving");
		setContent(composite);
		setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		IRotatingBench model = uk.ac.stfc.isis.ibex.rotatingbench.RotatingBench.getDefault().model();
		setModel(new ViewModel(model));
	}

	private void setModel(final ViewModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(rotationAngle), BeanProperties.value("angle").observe(model));
		bindingContext.bindValue(WidgetProperties.text().observe(status), BeanProperties.value("status").observe(model));
		bindingContext.bindValue(WidgetProperties.text().observe(liftStatus), BeanProperties.value("liftStatus").observe(model));
		bindingContext.bindValue(WidgetProperties.text().observe(motion), BeanProperties.value("movingStatus").observe(model));
		bindingContext.bindValue(WidgetProperties.background().observe(motion), BeanProperties.value("movingColour").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnPowerDownHv), BeanProperties.value("hvCheck").observe(model));

		bindingContext.bindValue(WidgetProperties.text().observe(angleSPtext), BeanProperties.value("angleSP").observe(model));
		
		angleSPsetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setAngleSP(angleSPtext.getText());
				composite.setFocus();
				angleSPsetButton.setEnabled(false);
			}
		});
	}
	
	private final ModifyListener textModifyListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			angleSPsetButton.setEnabled(true);
		}
	};
}
