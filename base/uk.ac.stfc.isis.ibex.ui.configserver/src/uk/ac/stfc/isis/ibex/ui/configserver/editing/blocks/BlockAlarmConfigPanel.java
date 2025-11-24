/**
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;

/**
 * A panel in the edit block dialog for the block's alarm configuration settings.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockAlarmConfigPanel extends Composite {
    private Text lowLimit;
    private Text highLimit;
    private Text delay;
    private Text guidance;
    private Button enabled;
	private Button latched;
    private Text lowLimitSeverity;
    private Text highLimitSeverity;

    /**
     * Standard constructor.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param viewModel The viewModel for the block alarm configuration.
     */
    public BlockAlarmConfigPanel(Composite parent, int style, BlockAlarmConfigViewModel viewModel) {
        super(parent, style);
        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group alarmConfigGroup = new Group(this, SWT.NONE);
        alarmConfigGroup.setText("Alarm Configurations");
        alarmConfigGroup.setLayout(new GridLayout(8, false));

        enabled = new IBEXButton(alarmConfigGroup, SWT.CHECK)
        		.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1))
        		.text("Enabled").tooltip("Enable or disable the alarm").enabled(true)
        		.get();

        latched = new IBEXButton(alarmConfigGroup, SWT.CHECK)
        		.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1))
        		.text("Latched").tooltip("Enable or disable latched alarm behavior").enabled(true)
        		.get();
        addLabel(alarmConfigGroup, "Delay:");
        delay = new Text(alarmConfigGroup, SWT.BORDER);
        delay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        delay.setToolTipText("Delay before the alarm is triggered");

        addLabel(alarmConfigGroup, "Guidance:");
        guidance = new Text(alarmConfigGroup, SWT.BORDER);
        GridData grid = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        grid.widthHint = 200;
        guidance.setLayoutData(grid);
        guidance.setToolTipText("Guidance text for the alarm configuration");

        addLabel(alarmConfigGroup, "Low Limit:");
        lowLimit = new Text(alarmConfigGroup, SWT.BOLD);
        lowLimit.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lowLimit.setToolTipText("Alarm Low limit - not managed at block level");
        lowLimit.setEnabled(false); // Low limit is currently not editable.

        addLabel(alarmConfigGroup, "Low Limit Severity:");
        lowLimitSeverity = new Text(alarmConfigGroup, SWT.BOLD);
        lowLimitSeverity.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lowLimitSeverity.setToolTipText("Low Limit Severity - Alarm severity when the low limit is breached");
        lowLimitSeverity.setEnabled(false); // currently not editable.

        addLabel(alarmConfigGroup, "High Limit:");
        highLimit = new Text(alarmConfigGroup, SWT.BOLD);
        highLimit.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        highLimit.setToolTipText("Alarm High limit - not managed at block level");
        highLimit.setEnabled(false); // High limit is currently not editable.

        addLabel(alarmConfigGroup, "High Limit Severity:");
        highLimitSeverity = new Text(alarmConfigGroup, SWT.BOLD);
        highLimitSeverity.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        highLimitSeverity.setToolTipText("High Limit Severity - Alarm severity when the high limit is breached");
        highLimitSeverity.setEnabled(false); // currently not editable.
		if (viewModel.isNoAlarmSeveritySet()) {
			Label label = new Label(alarmConfigGroup, SWT.BALLOON);
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 8, 1));
			label.setText("Note: If no severity is set then the corresponding alarm will not trigger.");
		}
        setModel(viewModel);
    }

	/**
	 * @param alarmConfigGroup the group to which the label is added
	 * @param labelText the text for the label
	 */
	private void addLabel(Group alarmConfigGroup, String labelText) {
		Label label = new Label(alarmConfigGroup, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        label.setText(labelText);
	}
	
    /**
     * Sets the view model and observers for run control settings.
     * @param viewModel The view model for run control settings. 
     */
	private void setModel(BlockAlarmConfigViewModel viewModel) {
		DataBindingContext bindingContext = new DataBindingContext();
		
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(enabled),
                BeanProperties.value(BlockAlarmConfigViewModel.ENABLED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(latched),
                BeanProperties.value(BlockAlarmConfigViewModel.LATCHED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(delay),
                BeanProperties.value(BlockAlarmConfigViewModel.DELAY_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(guidance),
                BeanProperties.value(BlockAlarmConfigViewModel.GUIDANCE_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(lowLimit),
                BeanProperties.value(BlockAlarmConfigViewModel.LOWLIMIT_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(highLimit),
                BeanProperties.value(BlockAlarmConfigViewModel.HIGHLIMIT_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(lowLimitSeverity),
                BeanProperties.value(BlockAlarmConfigViewModel.LOWLIMIT_SEVERITY_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(highLimitSeverity),
                BeanProperties.value(BlockAlarmConfigViewModel.HIGHLIMIT_SEVERITY_BINDING_NAME).observe(viewModel));
	}
}
