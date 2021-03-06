
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

package uk.ac.stfc.isis.ibex.ui.help;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.io.Resources;

/**
 * A dialog box that contains information about the IBEX client icon licences. 
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class IconLicencesDialogBox extends TitleAreaDialog {
	
	private static final String ICONLICENCES_FILE_PATH = "/resources/iconlicences.txt";
	/** Dialog width. */
    public static final int WIDTH = 600;
    /** Dialog height. */
    private static final int HEIGHT = 500;
    
    /**
     * Construct a new about Ibex dialog box.
     * 
     * @param parentShell The parent shell in which in the dialog will be loaded
     */
	public IconLicencesDialogBox(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		setTitle("IBEX Icon Licences");
		
	}
	
	@Override
	protected Point getInitialSize() {
        return new Point(WIDTH, HEIGHT);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("IBEX Icon Licences");
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
			
		Text txtIconLicences = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        GridData gdIconLicences = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdIconLicences.heightHint = 70;
        txtIconLicences.setLayoutData(gdIconLicences);
        
        Optional<String> textBoxContents = Optional.ofNullable(getClass().getResource(ICONLICENCES_FILE_PATH))
        		.map(IconLicencesDialogBox::stringFromUrl)
        		.flatMap(Function.identity());
        
		txtIconLicences.setText(textBoxContents.orElse(String.format("Can't read from file '%s'", ICONLICENCES_FILE_PATH)));

		return container;
	}	
	
	private static Optional<String> stringFromUrl(URL url) {
		try {
			return Optional.ofNullable(Resources.toString(url, StandardCharsets.UTF_8));
		} catch (IOException e) {
			return Optional.empty();
		}
	}

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (id == IDialogConstants.CANCEL_ID) {
			return null;
		}
		return super.createButton(parent, id, label, defaultButton);
	}

}
