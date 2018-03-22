
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ConnectionStatusConverter;
import uk.ac.stfc.isis.ibex.ui.nicos.models.OutputLogViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;

/**
 * The main view for the NICOS Script Output view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosOutputView {
	
	/**
	 * The public ID of this class.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.NicosView";
    
    private static final int FIXED_WIDTH = 750;
    private static final int FIXED_HEIGHT = 225;
	
    private DataBindingContext bindingContext = new DataBindingContext();
	private ScriptStatusViewModel scriptStatusViewModel;
	private OutputLogViewModel outputLogViewModel;
	
    private NicosModel model;
    
    StyledText txtOutput;


	/**
	 * The default constructor for the view.
	 */
	public NicosOutputView() {
        model = Nicos.getDefault().getModel();
        scriptStatusViewModel = new ScriptStatusViewModel(model);
        outputLogViewModel = new OutputLogViewModel(model);
	}

	@PostConstruct
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite nicosComposite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(nicosComposite);
        scrolledComposite.setMinSize(new Point(FIXED_WIDTH, FIXED_HEIGHT));
		
		GridLayout glParent = new GridLayout(1, false);
		glParent.marginHeight = 10;
		glParent.marginWidth = 10;
		nicosComposite.setLayout(glParent);

        Composite currentScriptInfoContainer = new Composite(nicosComposite, SWT.NONE);
        currentScriptInfoContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        GridLayout currScriptInfoLayout = new GridLayout(2, false);
        currScriptInfoLayout.marginHeight = 10;
        currScriptInfoLayout.marginWidth = 10;
        currentScriptInfoContainer.setLayout(currScriptInfoLayout);
        
        Label lblCurrentScriptStatus = new Label(currentScriptInfoContainer, SWT.NONE);
        lblCurrentScriptStatus.setText("Current script status: ");
        
        Label lineNumberIndicator = new Label(currentScriptInfoContainer, SWT.NONE);
        lineNumberIndicator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(lineNumberIndicator),
                BeanProperties.value("lineNumber").observe(scriptStatusViewModel));
        
		txtOutput = new StyledText(nicosComposite, SWT.BORDER);
		txtOutput.setEditable(false);
		txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        NicosControlButtonPanel controlPanel =
                new NicosControlButtonPanel(nicosComposite, SWT.NONE, scriptStatusViewModel);
        controlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        bind();
	}
	
	private void bind() {

        bindingContext.bindValue(WidgetProperties.text().observe(txtOutput),
                BeanProperties.value("log").observe(outputLogViewModel));
        
        txtOutput.addListener(SWT.Modify, new Listener() {
            @Override
            public void handleEvent(Event e) {
                txtOutput.setTopIndex(txtOutput.getLineCount() - 1);
            }
        });


	}

}
