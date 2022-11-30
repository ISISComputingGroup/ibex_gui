/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2022 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.IocControlViewModel.Item;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCConfigProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCContentProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCLabelProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCList;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCPatternFilter;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCRunModeProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCStatusProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCViewerComparator;

/**
 * The Start/Stop IOC viewer.
 */
public class IocControlView extends Composite {
	
	private final Button expandButton;
	private final Button  collapseButton;
	private FilteredTree availableIocsTree;
	private final Button start;
	private final Button stop;
	private final Button restart;
	
	private DataBindingContext bindingContext = new DataBindingContext();;
	
	private IocControlViewModel model;
	
	private static final int COLUMN_WIDTH = 70;
	private static final int LAYOUT = 3;
	private static final int MARGIN_WIDTH = 10;
	
	/**
	 * Constructor. Creates a new instance of the IocControlView object.
	 * 
	 * @param parent The parent of this element.
	 * @param style The base style to be applied to the view.
	 * @param model The model associated with this view.
	 */
	IocControlView(Composite parent, int style, IocControlViewModel model) {
		super(parent, style);
		this.model = model;
		setLayout(new GridLayout(1, false));
		
		// Add Expand and Collapse Tree buttons.
		Composite expansionComposite = new Composite(this, SWT.FILL);
		expansionComposite.setLayout(new GridLayout(2, true));
		expandButton = new Button(expansionComposite, SWT.NONE);
		expandButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		expandButton.setText("\u25BC Expand All");
		expandButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				availableIocsTree.getViewer().expandAll();
			}
		});
		
		collapseButton = new Button(expansionComposite, SWT.NONE);
		collapseButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		collapseButton.setText("\u25B2 Collapse All");
		collapseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				availableIocsTree.getViewer().collapseAll();
			}
		});
		
		// Add selection tree.
		Composite treeComposite = new Composite(this, SWT.FILL);
  		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
  		treeComposite.setLayout(new GridLayout(1, true));

        availableIocsTree = new FilteredTree(treeComposite, SWT.FULL_SELECTION, new IOCPatternFilter(), true, true);
        final var viewer = availableIocsTree.getViewer();
        viewer.setContentProvider(new IOCContentProvider());
        viewer.setComparator(new IOCViewerComparator(Comparator.naturalOrder()));
        
        TreeViewerColumn mainColumn = new TreeViewerColumn(viewer, SWT.NONE);
        mainColumn.getColumn().setText("Ioc");
        mainColumn.setLabelProvider(new IOCLabelProvider());
        
        TreeViewerColumn statusColumn = new TreeViewerColumn(viewer, SWT.NONE);
        statusColumn.getColumn().setText("Status");
        statusColumn.getColumn().setWidth(COLUMN_WIDTH);
        statusColumn.getColumn().setAlignment(SWT.CENTER);
        statusColumn.setLabelProvider(new IOCStatusProvider());
        
        TreeViewerColumn configColumn = new TreeViewerColumn(viewer, SWT.NONE);
        configColumn.getColumn().setText("In Config?");
        configColumn.getColumn().setWidth(COLUMN_WIDTH);
        configColumn.getColumn().setAlignment(SWT.CENTER);
        configColumn.setLabelProvider(new IOCConfigProvider());
        
        if (IocPanelHandler.getDisplayRunMode()) {
        	TreeViewerColumn runModeColumn = new TreeViewerColumn(viewer, SWT.NONE);
        	runModeColumn.getColumn().setText("Mode");
        	runModeColumn.getColumn().setWidth(COLUMN_WIDTH);
        	runModeColumn.getColumn().setAlignment(SWT.CENTER);
        	runModeColumn.setLabelProvider(new IOCRunModeProvider());
        }
        
        viewer.setInput(model.getAvailableIocs());
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
    	mainColumn.getColumn().pack();
    	

    	// Add control buttons.
    	Composite buttonsComposite = new Composite(this, SWT.NONE);
    	buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
    	GridLayout gridLayout = new GridLayout(LAYOUT, true);
		gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = MARGIN_WIDTH;
		gridLayout.marginHeight = 0;
		buttonsComposite.setLayout(gridLayout);
		
		start = new Button(buttonsComposite, SWT.NONE);
        start.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		start.setText("Start");
		start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.startIoc();
			}
		});
		bindingContext.bindValue(WidgetProperties.enabled().observe(start), BeanProperties.value("startEnabled", Boolean.class).observe(model));
		
		stop = new Button(buttonsComposite, SWT.NONE);
		stop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		stop.setText("Stop");
		stop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.stopIoc();
			}
		});
		bindingContext.bindValue(WidgetProperties.enabled().observe(stop), BeanProperties.value("stopEnabled", Boolean.class).observe(model));
		
		restart = new Button(buttonsComposite, SWT.NONE);
		restart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		restart.setText("Restart");
		restart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.restartIoc();
			}
		});
		bindingContext.bindValue(WidgetProperties.enabled().observe(restart), BeanProperties.value("restartEnabled", Boolean.class).observe(model));
    	
		
		addListeners();
    	model.refresh();
	}
	
	private void addListeners() {
		final var viewer = availableIocsTree.getViewer();
		final var tree = viewer.getTree();
		
		// Tree selection listener.
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				if (tree.getSelection().length != 0) {
					var selectedItem = tree.getSelection()[0];
					var parent = selectedItem.getParentItem();
					
					if (parent == null) {
						model.setSelected(selectedItem.getText(), null);
					} else {
						model.setSelected(parent.getText(), selectedItem.getText());
					}

					
					Object selection = selectedItem.getData();
					if (selection instanceof IocState) {
						model.setIoc(IocState.class.cast(selection));
						return;
					}
				}
				model.setIoc(null);	
			}
		});
		
		// Tree expand and collapse listener.
		tree.addTreeListener(new TreeListener() {
			@Override
			public void treeCollapsed(TreeEvent e) {
				TreeItem item = (TreeItem) e.item;
				model.removeExpanded(item.getText());
			}

			@Override
			public void treeExpanded(TreeEvent e) {
				TreeItem item = (TreeItem) e.item;
				model.addExpanded(item.getText());		
			}
    	});
		
		// Tree top item listener.
    	tree.getVerticalBar().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = viewer.getTree().getTopItem();
				TreeItem parent = item.getParentItem();
				if (parent == null) {
					model.setTop(item.getText(), null);
				} else {
					model.setTop(parent.getText(), item.getText());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
    	});
    	
    	// Tree input change listener.
    	model.addPropertyChangeListener("availableIocs", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				tree.setVisible(false);
				
				viewer.setInput(evt.getNewValue());
				model.refresh();

	            tree.setVisible(true);
			}
    	});

    	// Selection restore listener.
    	model.addPropertyChangeListener("selected", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Item) {
					var item = (Item) evt.getNewValue();
					
					if (item.description().isPresent()) {
						var descriptionTreeItem = getDescriptionItem(item.description().get());
						
						if (descriptionTreeItem.isPresent()) {
							// Set description as selected item.
							if (item.ioc().isEmpty()) {
								tree.setSelection(descriptionTreeItem.get());
							// Set IOC as selected item.
							} else {
								var iocTreeItem = getIocItem(descriptionTreeItem.get(), item.ioc().get());
								if (iocTreeItem.isPresent()) {
									tree.setSelection(iocTreeItem.get());
									model.setIoc((IocState) iocTreeItem.get().getData());
								}
							}
						}
					}
				}
			}
    	});
    	
    	// Expanded items restore listener.
    	model.addPropertyChangeListener("expanded", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof List<?>) {
					ArrayList<TreePath> paths = new ArrayList<TreePath>();

					List<?> test = (List<?>) evt.getNewValue();
					for (var each : test) {
						IOCList[] iocArray = {(IOCList) each};
						paths.add(new TreePath(iocArray));
					}
					
					availableIocsTree.getViewer().setExpandedTreePaths(paths.toArray(new TreePath[0]));
				}
				
				
			}
    	});

    	// Top item restore listener. This restores the scroll position.
    	model.addPropertyChangeListener("top", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Item) {
					var item = (Item) evt.getNewValue();
					
					if (item.description().isPresent()) {
						var descriptionTreeItem = getDescriptionItem(item.description().get());
						
						if (descriptionTreeItem.isPresent()) {
							// Set description as top item.
							if (item.ioc().isEmpty()) {
								tree.setTopItem(descriptionTreeItem.get());
							// Set IOC as top item.
							} else {
								var iocTreeItem = getIocItem(descriptionTreeItem.get(), item.ioc().get());
								if (iocTreeItem.isPresent()) {
									tree.setTopItem(iocTreeItem.get());
								}
							}
						}
					}
				}
			}
    	});
	}
	
	private Optional<TreeItem> getDescriptionItem(final String description) {
		final var tree = availableIocsTree.getViewer().getTree();
		
		for (var parent : tree.getItems()) {
			if (parent.getText().equals(description)) {
				return Optional.of(parent);
			}
		}
		
		return Optional.empty();
	}
	
	private Optional<TreeItem> getIocItem(final TreeItem parent, final String ioc) {
		for (var child : parent.getItems()) {
			if (child.getText().equals(ioc)) {
				return Optional.of(child);
			}
		}
		
		return Optional.empty();
	}
}
