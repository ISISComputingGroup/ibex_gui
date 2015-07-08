
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.groups;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DoubleListEditor;

public class GroupsEditorPanel extends Composite {

	private final DoubleListEditor blocksEditor;
	
	private EditableConfiguration config;
	private Text name;
	private Label componentDetails;
	private ListViewer groupsViewer;
	private List groupList;
	
	private DataBindingContext bindingContext = new DataBindingContext();
	
	public GroupsEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		
		setLayout(new GridLayout(2, false));
		
		Group grpGroups = new Group(this, SWT.NONE);
		grpGroups.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1));
		grpGroups.setText("Groups");
		grpGroups.setLayout(new GridLayout(3, false));
	
		groupsViewer = new ListViewer(grpGroups, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		groupList = groupsViewer.getList();
		GridData gd_viewer = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 6);
		gd_viewer.widthHint = 100;
		gd_viewer.heightHint = 350;
		groupList.setLayoutData(gd_viewer);
		
		Button btnAdd = new Button(grpGroups, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.widthHint = 80;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add");
		
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (config != null) {
					config.addNewGroup();
				}
			}
		});
		
		final Button btnRemove = new Button(grpGroups, SWT.NONE);
		btnRemove.setEnabled(false);
		GridData gd_btnRemove = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_btnRemove.widthHint = 80;
		btnRemove.setLayoutData(gd_btnRemove);
		btnRemove.setText("Remove");
		
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (config != null) {
					if (groupList.getSelectionIndex() != -1){					
						EditableGroup group = getSelectedGroup();
						if (group != null){
					        config.removeGroup((EditableGroup) group);
						}
					}
				}
			}
		});
		new Label(grpGroups, SWT.NONE);
				
		Group grpSelectedGroup = new Group(this, SWT.NONE);
		grpSelectedGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		grpSelectedGroup.setText("Selected group");
		grpSelectedGroup.setLayout(new GridLayout(3, false));
		
		Label lblEditName = new Label(grpSelectedGroup, SWT.NONE);
		GridData gd_lblEditName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblEditName.horizontalIndent = 10;
		lblEditName.setLayoutData(gd_lblEditName);
		lblEditName.setText("Name:");
		
		name = new Text(grpSelectedGroup, SWT.BORDER);
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_name.widthHint = 100;
		name.setLayoutData(gd_name);
		name.setEnabled(false);
		
		componentDetails = new Label(grpSelectedGroup, SWT.NONE);
		componentDetails.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		componentDetails.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Group grpBlocks = new Group(grpSelectedGroup, SWT.NONE);
		grpBlocks.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true, 3, 1));
		grpBlocks.setLayout(new GridLayout(1, false));
		grpBlocks.setText("Blocks");
		
		blocksEditor = new DoubleListEditor(grpBlocks, SWT.NONE, "name", true);
		GridData gd_blocksEditor = new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1);
		gd_blocksEditor.heightHint = 300;
		gd_blocksEditor.widthHint = 300;
		blocksEditor.setLayoutData(gd_blocksEditor);
		
		blocksEditor.addSelectionListenerForSelecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableGroup group = getSelectedGroup();
				if (group != null) {
			        group.toggleSelection(blocksEditor.unselectedItems());
				}
			}
		});
		
		blocksEditor.addSelectionListenerForUnselecting(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableGroup group = getSelectedGroup();
				if (group != null) {
			        group.toggleSelection(blocksEditor.selectedItems());
				}
			}
		});
		
		blocksEditor.addSelectionListenerForMovingUp(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableGroup group = getSelectedGroup();
				if (group != null) {
					group.moveBlockUp(blocksEditor.selectedItem());
					blocksEditor.refreshViewer();
				}
			}
		});
		
		blocksEditor.addSelectionListenerForMovingDown(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableGroup group = getSelectedGroup();
				if (group != null) {
					group.moveBlockDown(blocksEditor.selectedItem());
					blocksEditor.refreshViewer();
				}
			}
		});
		
		groupsViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				EditableGroup group = getSelectedGroup();
				if (group != null) {
			        boolean groupIsSelected = group != null;
					boolean canEdit = groupIsSelected && group.isEditable();
					
					btnRemove.setEnabled(canEdit);
					name.setEnabled(canEdit);
					blocksEditor.setEnabled(canEdit);
					
					componentDetails.setText(componentDetail(group, canEdit));
				}
			}	
			
			private String componentDetail(EditableGroup group, boolean canEdit) {
				if (canEdit) {
					return "";
				}
				
				String componentName = group.subconfig() != null ? group.subconfig() : "unknown";
				return "contributed by " + componentName;
			}
		});
				
		bindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observe(name), 
				ViewerProperties.singleSelection().value(BeanProperties.value("name", Group.class)).observe(groupsViewer)
        );
		
		name.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				groupsViewer.refresh();
			}
		});
		
		IObservableList selectedBlocks = ViewerProperties.singleSelection().list(BeanProperties.list("selectedBlocks", Group.class)).observe(groupsViewer);
		IObservableList unselectedBlocks = ViewerProperties.singleSelection().list(BeanProperties.list("unselectedBlocks", Group.class)).observe(groupsViewer);
		new Label(grpGroups, SWT.NONE);
		
		Button btnUp =  new Button(grpGroups, SWT.NONE);
		GridData gd_btnUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_btnUp.widthHint = 80;
		btnUp.setLayoutData(gd_btnUp);
		btnUp.setText("Up");
		
		btnUp.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (config != null) {
					
					if (groupList.getSelectionIndex() > 0){
						//Move up
						EditableGroup group1 = getSelectedGroup();
						EditableGroup group2 = (EditableGroup) groupsViewer.getElementAt(groupList.getSelectionIndex() - 1);
						
						if (group1 != null && group2 != null){
							config.swapGroups(group1, group2);
						}
					}

				}
			}
		});
		
		Button btnDown =  new Button(grpGroups, SWT.NONE);
		GridData gd_btnDown = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnDown.widthHint = 80;
		btnDown.setLayoutData(gd_btnDown);
		btnDown.setText("Down");
		
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (config != null) {
					
					if (groupList.getSelectionIndex() < groupList.getItemCount() - 1) {
						//Move down
						EditableGroup group1 = getSelectedGroup();
						EditableGroup group2 = (EditableGroup) groupsViewer.getElementAt(groupList.getSelectionIndex() + 1);
						
						if (group1 != null && group2 != null) {
							config.swapGroups(group1, group2);
						}
					}

				}
			}
		});
		
		blocksEditor.bind(unselectedBlocks, selectedBlocks);
	}
	
	private EditableGroup getSelectedGroup() {
		ISelection selection = groupsViewer.getSelection();
		if (selection != null && (selection instanceof IStructuredSelection)) {
			IStructuredSelection ss = (IStructuredSelection) groupsViewer.getSelection();
	        EditableGroup group = (EditableGroup) ss.getFirstElement();
	        return group;
		}
		
		return null;
	}

	public void setConfig(EditableConfiguration config) {
		this.config = config;
		
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
	    groupsViewer.setContentProvider(contentProvider);
	    groupsViewer.setInput(BeanProperties.list("editableGroups").observe(config));
	}
}
