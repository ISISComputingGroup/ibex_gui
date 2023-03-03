
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

package uk.ac.stfc.isis.ibex.ui.nicos;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.EditScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.QueueScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * Nicos queue container.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosQueueContainer {
	
	private final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	private final NicosModel model = Nicos.getDefault().getModel();
	private final DataBindingContext bindingContext = new DataBindingContext();
	private ScriptStatusViewModel scriptStatusViewModel;
	private final QueueScriptViewModel queueScriptViewModel;
	private DataboundTable<QueuedScript> queuedScriptsViewer;
	
	/**
	 * Default constructor.
	 */
	public NicosQueueContainer() {
		queueScriptViewModel = new QueueScriptViewModel(model);
		scriptStatusViewModel = new ScriptStatusViewModel(model);
	}
	
	private void createQueuedScriptMenu(Viewer queuedScriptsViewer) {
        Menu queuedScriptMenu = new Menu(queuedScriptsViewer.getControl());
        MenuItem saveScript = new MenuItem(queuedScriptMenu, SWT.PUSH);
        saveScript.setText("Save to file...");
        saveScript.addSelectionListener(new SelectionAdapter() {
        	@Override
            public void widgetSelected(SelectionEvent e) {
        		queueScriptViewModel.saveSelected(shell);
        	}
		});
        queuedScriptsViewer.getControl().setMenu(queuedScriptMenu);
	}
	
	private Button createMoveScriptButton(Composite parent, String icon, String direction) {
        Button moveButton =  new Button(parent, SWT.NONE);
		GridData gdBtnScriptUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdBtnScriptUp.widthHint = 25;
		moveButton.setLayoutData(gdBtnScriptUp);
		moveButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/" + icon));
        moveButton.setToolTipText("Move selected script " + direction);
        return moveButton;
	}
	
	/**
	 * Create the view.
	 * @param parent the parent (injected by eclipse)
	 */
	@PostConstruct
	public void createQueueContainer(Composite parent) {
		GridData gdQueueContainer = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdQueueContainer.heightHint = 300;
		parent.setLayoutData(gdQueueContainer);
		parent.setLayout(new GridLayout(2, false));
        
        Label lblQueuedScriptsSubLabel = new Label(parent, SWT.NONE);
        lblQueuedScriptsSubLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
        lblQueuedScriptsSubLabel.setText("(double-click name to view / edit contents)");
        lblQueuedScriptsSubLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
        
        queuedScriptsViewer = new DataboundTable<QueuedScript>(parent, SWT.NONE, SWT.V_SCROLL) {
			@Override
			protected void addColumns() {
				createColumn("Name", 100, new DataboundCellLabelProvider<QueuedScript>(observeProperty("name")) {
		            @Override
					protected String stringFromRow(QueuedScript row) {
		                return row.getName();
		            }
		        });
			}
			
			@Override
			protected ColumnComparator<QueuedScript> comparator() {
				return null;
			}
		};
		queuedScriptsViewer.initialise();
		queuedScriptsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		queuedScriptsViewer.table().setHeaderVisible(false);
		queuedScriptsViewer.table().setLinesVisible(false);
		
        queuedScriptsViewer.viewer().setInput(BeanProperties.list("queuedScripts").observe(model));

        queuedScriptsViewer.viewer().addDoubleClickListener(e ->
				(new EditScriptDialog(shell, queueScriptViewModel)).open());
        
        queuedScriptsViewer.addSelectionChangedListener(e ->
                queueScriptViewModel.setSelectedScript(queuedScriptsViewer.firstSelectedRow()));

        createQueuedScriptMenu(queuedScriptsViewer.viewer());
        addDragAndDrop(queuedScriptsViewer.viewer());
        
        Composite moveComposite = new Composite(parent, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        
        Button btnScriptUp = createMoveScriptButton(moveComposite, "move_up.png", "up");
        Button btnScriptDown = createMoveScriptButton(moveComposite, "move_down.png", "down");
        
        Label lblQueueStatusReadback = new Label(parent, SWT.NONE);
        lblQueueStatusReadback.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        
        bindingContext.bindValue(WidgetProperties.text().observe(lblQueueStatusReadback),
                BeanProperties.value("statusReadback").observe(scriptStatusViewModel));
        
        Composite scriptSendGrp = new Composite(parent, SWT.NONE);
        scriptSendGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout ssgLayout = new GridLayout(2, false);
        ssgLayout.marginHeight = 10;
        ssgLayout.marginWidth = 10;
        scriptSendGrp.setLayout(ssgLayout);
		        
        final Button btnCreateScript = new Button(scriptSendGrp, SWT.NONE);
        btnCreateScript.setText("Create Script and Add to Queue");
        btnCreateScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        final Button btnDequeueScript = new Button(scriptSendGrp, SWT.NONE);
        btnDequeueScript.setText("Dequeue Selected Script");
        btnDequeueScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        btnCreateScript.addListener(SWT.Selection, e -> (new QueueScriptDialog(shell, queueScriptViewModel)).open());
        btnDequeueScript.addListener(SWT.Selection, e -> queueScriptViewModel.dequeueScript());
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnDequeueScript), 
        		BeanProperties.value("dequeueButtonEnabled").observe(queueScriptViewModel));

        btnScriptUp.addListener(SWT.Selection, e -> queueScriptViewModel.moveScript(true));
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptUp), 
        		BeanProperties.value("upButtonEnabled").observe(queueScriptViewModel));

        btnScriptDown.addListener(SWT.Selection, e -> queueScriptViewModel.moveScript(false));
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptDown), 
        		BeanProperties.value("downButtonEnabled").observe(queueScriptViewModel));
	}
	
	private void addDragAndDrop(TableViewer viewer) {
	    // support drag and drop
        int operations = DND.DROP_MOVE;
        Transfer[] transferTypes = new Transfer[] {LocalSelectionTransfer.getTransfer()};
        viewer.addDragSupport(operations, transferTypes, new DragSourceAdapter());
                
        viewer.addDropSupport(operations, transferTypes, new ViewerDropAdapter(viewer) {
            
            @Override
            public boolean validateDrop(Object target, int operation, TransferData transferType) {
                // Cannot drop a script onto itself
                return queuedScriptsViewer.firstSelectedRow() != (QueuedScript) determineTarget(getCurrentEvent());
            }
            
            @Override
            public boolean performDrop(Object data) {
                QueuedScript sourceScript = queuedScriptsViewer.firstSelectedRow();
                List<QueuedScript> queue = new ArrayList<>(queueScriptViewModel.getQueuedScripts());
                
                QueuedScript targetScript = (QueuedScript) determineTarget(getCurrentEvent());
                int location = determineLocation(getCurrentEvent());
                
                // If not dropped on to anything, put on the end of the list
                if (targetScript == null) {
                    targetScript = queue.get(queue.size() - 1);
                    location = LOCATION_AFTER;
                }
                
                if (location == LOCATION_BEFORE || location == LOCATION_AFTER || location == LOCATION_ON) {
                    queue.remove(sourceScript);
                }
                
                int targetPosition = queue.indexOf(targetScript);
                
                if (location == LOCATION_AFTER) {
                    targetPosition++;
                }
                
                queue.add(targetPosition, sourceScript);
                
                queueScriptViewModel.sendReorderedList(queue);
                return true;
            }
        });
	}

}
