
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;

/**
 * A widget for showing an OPI's description.
 */
public class TargetDescriptionWidget extends Composite {

    /** The view model. **/
    private DeviceScreensDescriptionViewModel viewModel;
    /** The text box for holding the description. **/
	private Text txtDescription;

    /**
     * An anonymous Converter which is used as a short-cut to get the OPI
     * description for the currently selected OPI.
     */
    private final Converter toDescriptionConverter = new Converter(DeviceDescription.class, String.class) {
        @Override
        public Object convert(Object fromObject) {
            if (fromObject == null) {
                return "";
            }

            // Get the description
            OpiDescription desc = viewModel.getOpi(fromObject.toString());

            return desc.getDescription();
        }

    };

    /**
     * The constructor.
     * 
     * @param parent the parent composite
     * @param viewModel the view model for the device screen editor
     */
    public TargetDescriptionWidget(Composite parent, DeviceScreensDescriptionViewModel viewModel) {
		super(parent, SWT.NONE);
        this.viewModel = viewModel;

        setLayout(new FillLayout());
        txtDescription = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
	}

    /**
     * Binds the widget to an external ListViewer.
     * 
     * @param devicesViewer the ListViewer to bind against
     */
    public void bindToSelected(ListViewer devicesViewer) {
        DataBindingContext bindingContext = new DataBindingContext();

        // A converter is used to extract the corresponding OPI description
        // based on the currently selected screen.
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setConverter(toDescriptionConverter);

        bindingContext.bindValue(
                WidgetProperties.text(SWT.Modify).observe(txtDescription), ViewerProperties.singleSelection()
                        .value(BeanProperties.value("key", DeviceDescription.class)).observe(devicesViewer),
                null, strategy);
    }
}
