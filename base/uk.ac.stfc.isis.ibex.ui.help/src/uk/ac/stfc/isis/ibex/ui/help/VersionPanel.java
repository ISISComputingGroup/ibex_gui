
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

package uk.ac.stfc.isis.ibex.ui.help;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.help.Help;

/**
 * A panel showing the Ibex client and server version numbers.
 */
public class VersionPanel extends Composite {

    /** The version of the client. */
	private Label clientVersion;
    /** The version of the server. */
	private Label serverVersion;
    /** The ID of the bundle which owns the client version number. */
    private final String versionBundleId = "uk.ac.stfc.isis.ibex.product";
    /** The version of Java that the client is using */
    private Label javaVersion;
    /** The path to the Java that the client is using */
    private Label javaPath;

    /**
     * Construct a new version panel.
     * 
     * @param parent The parent component
     * @param style The style to apply to the panel
     */
	public VersionPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblClientVersion = new Label(this, SWT.NONE);
		lblClientVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblClientVersion.setText("Client Version:");
		
		clientVersion = new Label(this, SWT.NONE);
		clientVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        // Not bound as fixed
        final String versionText = Platform.getBundle(versionBundleId).getVersion().toString();
        clientVersion.setText(versionText);
		
		Label lblServerVersion = new Label(this, SWT.NONE);
		lblServerVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerVersion.setText("Server Version:");
		
		serverVersion = new Label(this, SWT.NONE);
		GridData serverVersionGd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		serverVersionGd.widthHint = AboutDialogBox.WIDTH;
		serverVersion.setLayoutData(serverVersionGd);
		
        Label lblJavaVersion = new Label(this, SWT.NONE);
        lblJavaVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblJavaVersion.setText("Java Version:");

        javaVersion = new Label(this, SWT.NONE);
        GridData javaVersionGd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        // Not bound as fixed
        javaVersion.setText(System.getProperty("java.version"));
        javaVersionGd.widthHint = AboutDialogBox.WIDTH;
        javaVersion.setLayoutData(javaVersionGd);

        Label lblJavaPath = new Label(this, SWT.NONE);
        lblJavaPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblJavaPath.setText("Java Path:");

        javaPath = new Label(this, SWT.NONE);
        GridData javaPathGd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        // Not bound as fixed
        javaPath.setText(System.getProperties().getProperty("java.home"));
        javaPathGd.widthHint = AboutDialogBox.WIDTH;
        javaPath.setLayoutData(javaPathGd);

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