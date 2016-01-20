
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

package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.synoptic.model.WritableComponentProperty;

@SuppressWarnings("checkstyle:magicnumber")
public class WritableComponentView extends Composite {
	
	private DataBindingContext bindingContext = new DataBindingContext();
	
	private final Composite parent;

	private Label propertyName;
	private Text text;
	private Composite composite;
	private Button setButton;
	
	private final WritableComponentProperty property;
	
	private final ModifyListener textModifyListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			setButton.setEnabled(true);
		}
	};
	
	/**
	 * @wbp.parser.constructor
	 */
	public WritableComponentView(Composite parent, final WritableComponentProperty property) {
		this(parent, property, true);
	}
	
	public WritableComponentView(Composite parent, final WritableComponentProperty property, boolean displayName) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		this.parent = parent;
		this.property = property;
		
		if (displayName) {
			propertyName = new Label(this, SWT.NONE);		
			propertyName.setAlignment(SWT.CENTER);
			propertyName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			propertyName.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
	
			propertyName.setText(property.displayName());
		}
		
		composite = new Composite(this, SWT.NONE);
		GridLayout glComposite = new GridLayout(2, false);
		glComposite.horizontalSpacing = 1;
		glComposite.marginWidth = 1;
		glComposite.marginRight = 1;
		glComposite.marginLeft = 1;
		glComposite.verticalSpacing = 0;
		glComposite.marginHeight = 0;
		composite.setLayout(glComposite);
		
		text = new Text(composite, SWT.BORDER | SWT.RIGHT);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		
		bindText(property);
		
		setButton = new Button(composite, SWT.CENTER);
		setButton.setEnabled(false);
		setButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/tick.png"));
		
		text.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				text.removeModifyListener(textModifyListener);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				text.addModifyListener(textModifyListener);
			}
		});
		
		text.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_RETURN) {
					sendValue();
					setButton.setFocus();
				}
			}
		});
		
		setButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sendValue();
			}
		});
		
	}

	private void bindText(final WritableComponentProperty property) {
		String value = property.value().getValue();
		if (value != null) {
			text.setText(value);
		}
		bindingContext.bindValue(WidgetProperties.text().observe(text), BeanProperties.value("value").observe(property.value()));
	}
	
	private void sendValue() {
		property.writer().write(text.getText());
		parent.setFocus();
		setButton.setEnabled(false);
	}
}
