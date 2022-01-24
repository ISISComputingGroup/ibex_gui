
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

package uk.ac.stfc.isis.ibex.ui.about;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

/**
 * A panel showing the application version and java information.
 */
public class BaseVersionPanel extends Composite {

	private Link applicationVersion;
    private Label javaVersion;
    private Label javaPathLabel;
    private String RELEASE_NOTES_BASE_LINK = "https://github.com/ISISComputingGroup/IBEX/blob/master/release_notes/ReleaseNotes_v10.0.0.md";

    /**
     * Construct a new version panel.
     * 
     * @param parent The parent component
     * @param style The style to apply to the panel
     * @param applicationName The application's name
     */
    @SuppressWarnings("checkstyle:magicnumber")
	public BaseVersionPanel(Composite parent, int style, String applicationName) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		// Application version
        Label lblClientVersion = new Label(this, SWT.NONE);
        lblClientVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblClientVersion.setText(applicationName + " Version:");
        

        applicationVersion = new Link(this, SWT.NONE);
        applicationVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        final String versionText = Platform.getProduct().getDefiningBundle().getVersion().toString();
        applicationVersion.setText("<a href=\"" + RELEASE_NOTES_BASE_LINK + "\">" + versionText  + "</a>");

        // Java version
        Label lblJavaVersion = new Label(this, SWT.NONE);
        lblJavaVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblJavaVersion.setText("Java Version:");

        javaVersion = new Label(this, SWT.NONE);
        GridData javaVersionGd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        javaVersion.setText(System.getProperty("java.version"));
        javaVersionGd.widthHint = AboutDialogBox.WIDTH;
        javaVersion.setLayoutData(javaVersionGd);

        // Java Location
        Label lblJavaPath = new Label(this, SWT.NONE);
        lblJavaPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblJavaPath.setText("Java Path:");

        javaPathLabel = new Label(this, SWT.NONE);
        GridData javaPathGd = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
        javaPathGd.widthHint = 250;
        javaPathLabel.setLayoutData(javaPathGd);

        String javaPath = System.getProperties().getProperty("java.home");
        Point pathSize = new GC(javaPathLabel).stringExtent(javaPath);
        if (pathSize.x > javaPathGd.widthHint) {
            javaPathLabel.setToolTipText(javaPath);
            // Assuming chars are the same width calculate how many can we fit
            // on one line
            int charsToPrint = javaPathGd.widthHint * javaPath.length() / pathSize.x;
            javaPathLabel.setText(javaPath.substring(0, charsToPrint - 3) + "...");
        } else {
            javaPathLabel.setText(javaPath);
        }
	}
}
