
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.scriptgenerator.Activator;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Provides settings to control the script generator.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TestView {

	private ScriptGeneratorSingleton toyModel;
	private Label lblOrder;
	private DataBindingContext bindingContext;
	private ActionsTable scriptGeneratorTable;
	private ActionsViewTable table;
	private static final Display DISPLAY = Display.getCurrent();
	
	private Button createMoveRowButton(Composite parent, String icon, String direction) {
        Button moveButton =  new Button(parent, SWT.NONE);
		GridData gdBtnMoveRow = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdBtnMoveRow.widthHint = 25;
		moveButton.setLayoutData(gdBtnMoveRow);
		moveButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/" + icon));
        moveButton.setToolTipText("Move selected row " + direction);
        return moveButton;
	}
	
	/**
	 * A basic framework to hold the toy interface.
	 * 
	 * @param parent the parent composite
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {

		this.toyModel = Activator.getModel();
		scriptGeneratorTable = this.toyModel.getScriptGeneratorTable();
		
		GridData gdQueueContainer = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdQueueContainer.heightHint = 300;
		parent.setLayoutData(gdQueueContainer);
		parent.setLayout(new GridLayout(2, false));
       
		table = new ActionsViewTable(parent, SWT.NONE, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION, scriptGeneratorTable);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//table.addSelectionChangedListener(e -> queueScriptViewModel.setSelectedScript(queuedScriptsViewer.firstSelectedRow()));
		//addDragAndDrop(queuedScriptsViewer.viewer());
        
		Composite moveComposite = new Composite(parent, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        
        Button btnScriptUp = createMoveRowButton(moveComposite, "move_up.png", "up");
        Button btnScriptDown = createMoveRowButton(moveComposite, "move_down.png", "down");
        
        
        Composite actionsSendGrp = new Composite(parent, SWT.NONE);
        actionsSendGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout ssgLayout = new GridLayout(3, false);
        ssgLayout.marginHeight = 10;
        ssgLayout.marginWidth = 10;
        actionsSendGrp.setLayout(ssgLayout);
		        
        final Button btnCreateScript = new Button(actionsSendGrp, SWT.NONE);
        btnCreateScript.setText("Add Action");
        btnCreateScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        final Button btnDequeueScript = new Button(actionsSendGrp, SWT.NONE);
        btnDequeueScript.setText("Delete Action");
        btnDequeueScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        final Button btnaDequeueScript = new Button(actionsSendGrp, SWT.NONE);
        btnaDequeueScript.setText("Duplicate Action");
        btnaDequeueScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        //extra under here
		
//		Group grpTable = new Group(parent, SWT.NULL);
//		grpTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		grpTable.setLayout(new RowLayout(SWT.VERTICAL));
//		grpTable.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));

//		this.lblOrder = new Label(parent, SWT.NONE);
//		this.lblOrder.setText("test");
//		
//		
//		Button button = new Button(grpTable, SWT.NONE);
//		button.setText("Press me!");
		
		//Button btnRowUp = createMoveRowButton(btnsComposite, "move_up.png", "up");
		
//		button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				toyModel.iterateNumber();
//			}
//		});
		

        bind();
		
        scriptGeneratorTable.set_actions();
	}

	private void bind() {
		bindingContext = new DataBindingContext();
//		bindingContext.bindValue(WidgetProperties.text().observe(lblOrder), 
//				BeanProperties.value("iteratedNumber").observe(toyModel));
		
		this.scriptGeneratorTable.addPropertyChangeListener("actions", e -> 
        DISPLAY.asyncExec(() -> {
                this.table.setRows(this.scriptGeneratorTable.getActions());
        }));
	}
	

}
