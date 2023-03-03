
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.epics.pv.PvState;
import uk.ac.stfc.isis.ibex.synoptic.model.WritableComponentProperty;

/**
 * A component which allows the user to change a device property in the synoptic
 * perspective.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class WritableComponentView extends Composite {

    private DataBindingContext bindingContext = new DataBindingContext();

    private final Composite parent;

    private Label propertyName;
    private Text text;
    private Composite composite;

    private final WritableComponentProperty property;

    /**
     * Constructs a new instance of this class given its parent and a writable
     * property.
     * 
     * @param parent A widget which will be the parent of the new instance (cannot be null)
     * @param property The property of the component that is writable
     * @param displayName True if the property name should be displayed
     * @param isPreview False if the property text can be modified (ie if the view is created in a the synoptic preview).
     */
    public WritableComponentView(Composite parent, final WritableComponentProperty property, boolean displayName,
            final boolean isPreview) {
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
        composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
        GridLayout glComposite = new GridLayout(1, false);
        glComposite.horizontalSpacing = 1;
        glComposite.marginWidth = 2;
        glComposite.verticalSpacing = 0;
        glComposite.marginHeight = 2;
        composite.setLayout(glComposite);

        text = new Text(composite, SWT.BORDER | SWT.RIGHT);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        text.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));

        bindText(property);

        text.addListener(SWT.Traverse, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.TRAVERSE_RETURN && !isPreview) {
                    sendValue();
                }
            }
        });

    }

    private void bindText(final WritableComponentProperty property) {
        String value = property.value().getValue();
        if (value != null) {
            text.setText(value);
        }
        bindingContext.bindValue(WidgetProperties.text().observe(text),
                BeanProperties.value("value").observe(property.value()));

        UpdateValueStrategy<PvState, Color> borderStrategy = new UpdateValueStrategy<PvState, Color>();
        borderStrategy.setConverter(new PvStatusBorderColourConverter());

        bindingContext.bindValue(WidgetProperties.background().observe(composite),
                BeanProperties.value("pvState", PvState.class).observe(property.sourceReadableProperty()), null, borderStrategy);
    }

	private void sendValue() {
		try {
			property.writer().uncheckedWrite(text.getText());
		} catch (RuntimeException e) {
			MessageBox errorMessage = new MessageBox(getShell(), SWT.ICON_ERROR);
			errorMessage.setMessage("Could not write to PV - is it missing the setpoint suffix :SP?\n"
					+ "Check the PV address in the Synoptic setup dialog.");
	        errorMessage.setText("Error setting value");
	        errorMessage.open();
		}
		parent.setFocus();
	}
}
