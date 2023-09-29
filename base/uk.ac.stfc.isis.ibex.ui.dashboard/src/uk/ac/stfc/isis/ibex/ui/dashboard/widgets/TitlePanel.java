
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

package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.ui.Utils;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.TitlePanelModel;

/**
 * The panel within the dashboard that contains the DAE title and the users.
 */
public class TitlePanel extends Composite {

	private final Label title;
	private final Label users;
	private Button btnDisplayTitle;

    /**
     * Default constructor, creates the panel.
     * 
     * @param parent
     *            The composite that this panel is part of.
     * @param style
     *            The SWT style of the panel.
     * @param model
     *            The viewmodel that this panel is based on.
     * @param font
     *            The font for the labels on the panel.
     */
	public TitlePanel(Composite parent, int style, TitlePanelModel model, Font font) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label lblTitle = new Label(this, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setFont(font);
		lblTitle.setText("Title:");
		
		title = new Label(this, SWT.NONE);
		title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		title.setFont(font);
		title.setText("Experiment title");
		title.setToolTipText("Experiment title");
		
		Label lblUsers = new Label(this, SWT.NONE);
		lblUsers.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblUsers.setFont(font);
		lblUsers.setText("Users:");
		
		users = new Label(this, SWT.NONE);
		users.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		users.setFont(font);
		users.setText("Experiment users");
		users.setToolTipText("Experiment users");

		btnDisplayTitle = new Button(this, SWT.CHECK);
		btnDisplayTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnDisplayTitle.setFont(font);
		btnDisplayTitle.setText("Show Title and Users in Dataweb Dashboard Page");
		btnDisplayTitle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				model.displayTitle().uncheckedSetValue(btnDisplayTitle.getSelection());
			}
		});
		
		if (model != null) {
			bind(model);
		}
	}

	private void bind(TitlePanelModel model) {
        UpdateValueStrategy<String, String> literalAmpersands =
                new UpdateValueStrategy<String, String>().setConverter(new Converter<String, String>(String.class, String.class) {
            @Override
            public String convert(String fromObject) {
                return fromObject.replaceAll("&", "&&");
            }
        });

		DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(
        		WidgetProperties.text().observe(title), 
        		BeanProperties.<Object, String>value("value").observe(model.title()), 
        		null, literalAmpersands);
        bindingContext.bindValue(
        		WidgetProperties.tooltipText().observe(title), 
        		BeanProperties.<Object, String>value("value").observe(model.title()), 
        		null, literalAmpersands);
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnDisplayTitle),
				BeanProperties.value("value").observe(model.displayTitle().value()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnDisplayTitle), 
		        BeanProperties.value("value").observe(model.displayTitle().canSetValue()));

        if (Utils.SHOULD_HIDE_USER_INFORMATION) {
        	users.setText("<Users unavailable>");
        	users.setToolTipText("<Users unavailable>");
        } else {
	        UsersConverter deJsoner = new UsersConverter();
			bindingContext.bindValue(
					WidgetProperties.text().observe(users), 
					BeanProperties.<Object, String>value("value").observe(model.users()), 
					null, new UpdateValueStrategy<String, String>().setConverter(deJsoner));	
			bindingContext.bindValue(
					WidgetProperties.tooltipText().observe(users), 
					BeanProperties.<Object, String>value("value").observe(model.users()), 
					null, new UpdateValueStrategy<String, String>().setConverter(deJsoner));	
        }
	}
}