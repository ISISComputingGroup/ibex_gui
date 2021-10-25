
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.ui.about.BaseVersionPanel;

/**
 * A panel showing the Ibex client and server version numbers.
 */
public class ScriptGeneratorVersionPanel extends BaseVersionPanel {

	private Label scriptDefinitionsLabel;
    private Label scriptDefinitionsLocationsLabel;

    /**
     * Construct a new version panel.
     * 
     * @param parent The parent component
     * @param style The style to apply to the panel
     */
    @SuppressWarnings("checkstyle:magicnumber")
	public ScriptGeneratorVersionPanel(Composite parent, int style, Optional<Path> scriptDefinitionsLocation) {
		super(parent, style, "Script Generator");

		scriptDefinitionsLabel = new Label(this, SWT.NONE);
		scriptDefinitionsLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		scriptDefinitionsLabel.setText("Script Definitions:");
        
		scriptDefinitionsLocationsLabel = new Label(this, SWT.NONE);
		scriptDefinitionsLocationsLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		scriptDefinitionsLocation.ifPresent(scriptDefinitionsLocationPath -> {
			scriptDefinitionsLocationsLabel.setText(scriptDefinitionsLocationPath.toString());
		});
	}
}



