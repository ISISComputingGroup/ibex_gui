
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

package uk.ac.stfc.isis.ibex.ui.help;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.help.Help;
import uk.ac.stfc.isis.ibex.ui.about.AboutDialogBox;
import uk.ac.stfc.isis.ibex.ui.about.BaseVersionPanel;


/**
 * A panel showing the Ibex client and server version numbers.
 */
public class ClientVersionPanel extends BaseVersionPanel {

    /** The version of the server. */
	private Label serverVersion;
    /** The PV prefix the client is using */
    private Label clientPvPrefix;

    /**
     * Construct a new version panel.
     * 
     * @param parent The parent component
     * @param style The style to apply to the panel
     */
    @SuppressWarnings("checkstyle:magicnumber")
	public ClientVersionPanel(Composite parent, int style) {
		super(parent, style, "IBEX");

		Label lblPvPrefix = new Label(this, SWT.NONE);
        lblPvPrefix.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPvPrefix.setText("Client PV Prefix:");
        
        clientPvPrefix = new Label(this, SWT.NONE);
        clientPvPrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        // Not bound as fixed
        final String pvPrefix = Help.getInstance().getPvPrefix();
        clientPvPrefix.setText(pvPrefix);

		Label lblServerVersion = new Label(this, SWT.NONE);
		lblServerVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerVersion.setText("Server Version:");

		serverVersion = new Label(this, SWT.NONE);
		GridData serverVersionGd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		serverVersionGd.widthHint = AboutDialogBox.WIDTH;
		serverVersion.setLayoutData(serverVersionGd);
        
        bind(Help.getInstance());
	}

    /**
     * Bind the server version as read from the target instrument to the local
     * server version so we can see changes.
     * 
     * @param help The help model which monitors, amongst other things, the
     *            server version
     */
	private void bind(Help help) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(serverVersion), BeanProperties.value("value").observe(help.revision()));	
	}
}
