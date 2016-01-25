
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.widgets;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.widgets.models.SetterModel;
import uk.ac.stfc.isis.ibex.ui.widgets.styles.RecordSetterStyle;

/**
 * Widget that allows write-only access to a model record
 */
public class RecordSetter extends Composite {
	/** The fixed width (in pixels) of the 'Set' button */
	private static final int SET_BUTTON_WIDTH = 80; 
	
	/** The fixed width (in pixels) of the status label that shows an icon */
	private static final int ICON_WIDTH = 30;
	
	/** The font to be used in the text box */
	private static final Font TEXT_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
	
	/** The background colour of the widget */
	private static final Color BACKGROUND_COLOR = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	
	private static final RecordSetterStyle[] DEFAULT_STYLES = { 
		RecordSetterStyle.BUTTON,  
		RecordSetterStyle.ICON };
	
	// UI controls
	private Text textbox;
	private Button setButton;
	private Label statusLabel;
	
	// Flags indicating which of the possible features should be used
	private boolean useFocus = false;
	private boolean useButton = false;
	private boolean useIcon = false;
	private boolean useHighlighting = false;
	
	private DataBindingContext bindingContext;
	
	private SetterModel model;

	/**
	 * Constructs a new instance of the RecordSetter class.
	 * @param parent The widget's parent element
	 * @param styles List of style enumeration flags that determine which features the control has:
	 * <br>- BUTTON - provides a 'Set' Button to send the value to the record.
	 * <br>- FOCUS - automatically send the value to record when the control loses focus.
	 * <br>- ICON - provides an icon that indicates the current status of the control (disconnected, in/valid data, data sent).
	 * <br>- HIGHLIGHTING - provides a prominent colour highlight indicating status (disconnected, in/valid data).
	 * <br>- FULL - provides all of the above.
	 * <br>- DEFAULT - provides the features of BUTTON and ICON.
	 */
	public RecordSetter(Composite parent, RecordSetterStyle... styles) {
		super(parent, SWT.NONE);
		
		parseStyles(Arrays.asList(styles));
		initializeLayout();
		initializeControls();
	}
	
	/** Specify the SetterModel to be used and bind this object to its updates. */
	public void setModel(SetterModel model) {
		if (model != null) {
			this.model = model;
			bindModel(model);
		}
	}
	
	/** Gets the SetterModel currently associated with this object. */
	public SetterModel getModel() {
		return model;
	}
	
	private void parseStyles(List<RecordSetterStyle> styles) {
		// Use the default styling if none specified in constructor
		if (styles.contains(RecordSetterStyle.DEFAULT) || styles.size() == 0) {
			styles = Arrays.asList(DEFAULT_STYLES);
		}
		
		if (styles.contains(RecordSetterStyle.FULL)) {
			useFocus = true;
			useButton = true;
			useIcon = true;
			useHighlighting = true;
		}
		
		if (styles.contains(RecordSetterStyle.FOCUS)) {
			useFocus = true;
		}
		
		if (styles.contains(RecordSetterStyle.BUTTON)) {
			useButton = true;
		}
		
		if (styles.contains(RecordSetterStyle.HIGHLIGHTING)) {
			useHighlighting = true;
		}
		
		if (styles.contains(RecordSetterStyle.ICON)) {
			useIcon = true;
		}
	}
	
	/**
	 * Create a layout for the widget
	 */
	private void initializeLayout() {
		int numColumns = 1;
		if (useButton) {
			++numColumns;
		}
		if (useIcon) {
			++numColumns;
		}
		
		GridLayout gridLayout = new GridLayout(numColumns, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		setLayoutData(gridData);
	}
	
	/**
	 * Create the text box, status label (and 'Set' button).
	 */
	private void initializeControls() {
		// Clear old controls if present
		if (textbox != null) {
			textbox.dispose();
		}
		if (setButton != null) {
			setButton.dispose();
		}
		if (statusLabel != null) {
			statusLabel.dispose();
		}
		
		setBackground(BACKGROUND_COLOR);
				
		// Create text box and set the model (not record) value whenever the text is modified
		textbox = new Text(this, SWT.BORDER);
		textbox.setFont(TEXT_FONT);
		textbox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setModelValue(textbox.getText());
			}
		});
		textbox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		// Set the record value if the enter key is pressed when this text box has focus
		textbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					setRecordValue();
				}
			}
		});
		
		// Create the status label that shows an icon indicating the model's current status
		if (useIcon) {
			statusLabel = new Label(this, SWT.NONE);
			statusLabel.setText("Status");
			statusLabel.setBackground(BACKGROUND_COLOR);
			statusLabel.setAlignment(SWT.CENTER);
			
			GridData iconGridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			iconGridData.widthHint = ICON_WIDTH;
			statusLabel.setLayoutData(iconGridData);
		}
		
		// Create the 'Set' button if required (based on the selected style).
		if (useButton) {
			// Set the record value if the set button is clicked.
			setButton = new Button(this, SWT.NONE);
			setButton.setText("Set");
			setButton.setToolTipText("Set the record to the entered value.");
			setButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					setRecordValue();
				}
			});
			
			GridData buttonGridData = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
			buttonGridData.widthHint = SET_BUTTON_WIDTH;
			setButton.setLayoutData(buttonGridData);
		} 
		
		// Set the record value if focus is lost.
		if (useFocus) {
			textbox.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					setRecordValue();
				}
			});
		}
	}
		
	/**
	 * Bind this widget to updates made by the model
	 */
	private void bindModel(SetterModel model) {
		bindingContext = new DataBindingContext();	
		
		// Enable text box if record is connected
		bindingContext.bindValue(WidgetProperties.enabled().observe(textbox), BeanProperties.value("isConnected").observe(model));
		
		// Show the Record description as the text box tool-tip
		bindingContext.bindValue(WidgetProperties.tooltipText().observe(textbox), BeanProperties.value("description").observe(model));

		// Change box colour depending on current state
		if (useHighlighting) {
			bindingContext.bindValue(WidgetProperties.background().observe(textbox), BeanProperties.value("color").observe(model));
		}
		
		// Set the icon and tooltip of the status label
		if (useIcon && statusLabel != null) {
			bindingContext.bindValue(WidgetProperties.image().observe(statusLabel), BeanProperties.value("image").observe(model));
			bindingContext.bindValue(WidgetProperties.tooltipText().observe(statusLabel), BeanProperties.value("statusMessage").observe(model));
		}
		
		// Enable set button if current value can be sent (valid and connected)
		if (useButton && setButton != null) {
			bindingContext.bindValue(WidgetProperties.enabled().observe(setButton), BeanProperties.value("isSendable").observe(model));
		}
	}
	
	/**
	 * Set the value currently stored in the UI model (but do not update the record value).
	 */
	private void setModelValue(String text) {
		if (model != null) {
			model.setCurrentValue(text);
		}
	}
	
	/**
	 * Send a request to update the record value with the value currently stored in the model
	 */
	private void setRecordValue() {
		if (model != null) {
			model.setRecordValue();
		}
	}
	
}
