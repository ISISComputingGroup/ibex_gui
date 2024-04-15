/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2024
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any kind,
 * either expressed or implied, including but not limited to the implied
 * warranties of merchantability and/or fitness for a particular purpose.
 */

package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;
import static java.util.stream.Collectors.joining;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;


import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;

/**
 * A panel showing port mappings of networked Moxa switches set up for this
 * machine.
 */
@SuppressWarnings({"checkstyle:magicnumber" })
public class MoxaInfoPanel extends Composite {
	private final Button expandButton;
	private final Button collapseButton;

	private static final int COLUMN_WIDTH = 500;
	private static final int COLUMN_WIDTH_NARROW = 150;
	
	private static final String UP_STATUS = "up";
	private static final String NOT_CONNECTED = "[INFO UNAVAILABLE]";

	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 * @param style  the SWT style
	 * @param model  the viewmodel for Moxa port mappings
	 */
	public MoxaInfoPanel(Composite parent, int style, MoxasViewModel model) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		final var viewer = new TreeViewer(this, SWT.FULL_SELECTION);
		makePhysicalPortColumn(viewer);
		makeComPortColumn(viewer);
		makeConnectedIOCColumn(viewer);	
		makeStatusColumn(viewer);
		makeAdditionInfoColumn(viewer);
		
		viewer.setContentProvider(new MoxaTableContentProvider());

		viewer.setInput(model.getMoxaPorts());

		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 21, 1));
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);

		// Tree expand and collapse listener.
		viewer.getTree().addTreeListener(new TreeListener() {
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

		Composite expansionComposite = new Composite(this, SWT.FILL);
		expansionComposite.setLayout(new GridLayout(5, true));
		expandButton = new Button(expansionComposite, SWT.NONE);
		expandButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		expandButton.setText("\u25BC Expand All");
		expandButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.expandAll();
			}
		});

		collapseButton = new Button(expansionComposite, SWT.NONE);
		collapseButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		collapseButton.setText("\u25B2 Collapse All");
		collapseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.collapseAll();
			}
		});

		Label refreshLabel = new Label(expansionComposite, SWT.NONE);
		refreshLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1));
		refreshLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		refreshLabel.setText("Items will refresh every 30 seconds");

		model.addUiThreadPropertyChangeListener("moxaMappings", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				viewer.getTree().setVisible(false);
				viewer.setInput(evt.getNewValue());
				viewer.getTree().setVisible(true);
				model.refresh();
			}
		});

		model.addUiThreadPropertyChangeListener("expanded", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// Re-expand previously expanded items
				if (evt.getNewValue() instanceof List<?>) {
					ArrayList<TreePath> paths = new ArrayList<TreePath>();

					List<?> test = (List<?>) evt.getNewValue();
					for (var each : test) {
						MoxaList[] iocArray = {(MoxaList) each};
						paths.add(new TreePath(iocArray));
					}
					
					viewer.setExpandedTreePaths(paths.toArray(new TreePath[0]));
				}
				
				
			}
    	});
	}

	/**
	 * @param viewer
	 */
	private void makeComPortColumn(final TreeViewer viewer) {
		TreeViewerColumn comPortColumn = new TreeViewerColumn(viewer, SWT.NONE);
		comPortColumn.getColumn().setText("COM port");
		comPortColumn.getColumn().setWidth(COLUMN_WIDTH_NARROW);

		comPortColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ArrayList<?>) {
					return null;
				}
				MoxaModelObject p = (MoxaModelObject) element;
				return p.getComPort();
			}
		});
	}

	/**
	 * @param viewer
	 */
	private void makePhysicalPortColumn(final TreeViewer viewer) {
		TreeViewerColumn moxaPortColumn = new TreeViewerColumn(viewer, SWT.NONE);
		moxaPortColumn.getColumn().setText("Physical MOXA port");
		moxaPortColumn.getColumn().setWidth(COLUMN_WIDTH);

		moxaPortColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof MoxaList) {
					MoxaList list = MoxaList.class.cast(element);
					String name = list.name;
					
					// extracting uptime for conversion and display
					String[] parts = name.split("\\)\\(");
					if (1 < parts.length) {
						String uptime = parts[1].substring(0, parts[1].length() - 1);
						name = parts[0] + ") [Up " + MoxasViewModel.toDaysHoursMinutes(Long.valueOf(uptime) * 10) + "]";
					} else {
						name = parts[0] + NOT_CONNECTED;
					}
					return name;
				} else {
					MoxaModelObject p = (MoxaModelObject) element;
					return p.getPhysPort();
				}
			}
		});
	}

	/**
	 * @param viewer
	 */
	private void makeConnectedIOCColumn(final TreeViewer viewer) {
		TreeViewerColumn iocColumn = new TreeViewerColumn(viewer, SWT.NONE);
		iocColumn.getColumn().setText("Connected IOC");
		iocColumn.getColumn().setWidth(COLUMN_WIDTH_NARROW);

		iocColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				// Change the text colour of cells where a port is connected to multiple IOCs
				if (element instanceof MoxaModelObject) {
					MoxaModelObject p = (MoxaModelObject) element;
					List<Ioc> iocs = p.getIocs();
					
					if (iocs.size() > 1) {
						return new Color(255, 0, 0);
					}
				}
				return super.getForeground(element);
			}
			
			@Override
			public String getText(Object element) {
				if (element instanceof ArrayList<?>) {
					return null;
				}
				MoxaModelObject p = (MoxaModelObject) element;
				List<Ioc> iocs = p.getIocs();
				
				return iocs.stream().map(Ioc::getName).collect(joining(", "));
			}
		});
	}

	/**
	 * @param viewer
	 */
	private void makeStatusColumn(final TreeViewer viewer) {
		TreeViewerColumn infoColumn = new TreeViewerColumn(viewer, SWT.NONE);
		infoColumn.getColumn().setText("Status");
		infoColumn.getColumn().setWidth(COLUMN_WIDTH_NARROW);

		infoColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ArrayList<?>) {
					return null;
				}
				MoxaModelObject field = (MoxaModelObject) element;
				return field.getStatus();
			}
			
			@Override
			public Color getForeground(Object element) {
				// Change the text colour of cells where a port is physically connected
				if (element instanceof MoxaModelObject) {
					MoxaModelObject field = (MoxaModelObject) element;
					
					if (field.getStatus().equalsIgnoreCase(UP_STATUS)) {
						return new Color(0, 0, 255);
					}
				}
				return super.getForeground(element);
			}
		});
	}

	/**
	 * @param viewer
	 */
	private void makeAdditionInfoColumn(final TreeViewer viewer) {
		TreeViewerColumn infoColumn = new TreeViewerColumn(viewer, SWT.NONE);
		infoColumn.getColumn().setText("Additional Info");
		infoColumn.getColumn().setWidth(COLUMN_WIDTH);

		infoColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ArrayList<?>) {
					return null;
				}
				MoxaModelObject field = (MoxaModelObject) element;
				return field.getAdditionalInfo();
			}
		});
	}

}