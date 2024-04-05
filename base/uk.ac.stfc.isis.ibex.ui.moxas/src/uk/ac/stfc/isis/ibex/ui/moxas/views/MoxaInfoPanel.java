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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

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
		TreeViewerColumn moxaPortColumn = new TreeViewerColumn(viewer, SWT.NONE);
		moxaPortColumn.getColumn().setText("Physical MOXA port");
		moxaPortColumn.getColumn().setWidth(COLUMN_WIDTH);

		moxaPortColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof MoxaList) {
					MoxaList list = MoxaList.class.cast(element);
					String name = list.name;
					return name;
				} else {
					MoxaModelObject p = (MoxaModelObject) element;
					return p.getPhysPort();
				}
			}
		});
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
		TreeViewerColumn iocColumn = new TreeViewerColumn(viewer, SWT.NONE);
		iocColumn.getColumn().setText("Connected IOC");
		iocColumn.getColumn().setWidth(COLUMN_WIDTH);

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
		expansionComposite.setLayout(new GridLayout(3, true));
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

}