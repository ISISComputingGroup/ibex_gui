package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;


public class MoxaInfoPanel  extends Composite {
	private final Button expandButton;
	private final Button  collapseButton;
	private final Button refreshButton;
	private FilteredTree moxasTree;


	private DataBindingContext bindingContext = new DataBindingContext();
	private static final int COLUMN_WIDTH = 200;
	private static final int LAYOUT = 3;
	private static final int MARGIN_WIDTH = 10;
	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 * @param style  the SWT style
	 */
	public MoxaInfoPanel(Composite parent, int style, MoxasViewModel model) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		// Add selection tree.
		Composite treeComposite = new Composite(this, SWT.FILL);
  		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
  		treeComposite.setLayout(new GridLayout(1, true));
  		
  		moxasTree = new FilteredTree(treeComposite, SWT.FULL_SELECTION, new PatternFilter(), true, true);		
  		final var viewer = moxasTree.getViewer();
  		
  		 TreeViewerColumn moxaPortColumn = new TreeViewerColumn(viewer, SWT.NONE);
         moxaPortColumn.getColumn().setText("MOXA port");
         moxaPortColumn.getColumn().setWidth(COLUMN_WIDTH);
         moxaPortColumn.getColumn().setAlignment(SWT.CENTER);

         moxaPortColumn.setLabelProvider(new ColumnLabelProvider() {
      	    @Override
      	    public String getText(Object element) {
      	    if (element instanceof MoxaList) {
     				MoxaList list = MoxaList.class.cast(element);
     				String name = list.name;
     				return name;
     			}
     	    	else {
      	        MoxaModelObject p = (MoxaModelObject) element;
      	        return p.getPhysPort();
     	    	}
      	    }
      	});

         
         TreeViewerColumn comPortColumn = new TreeViewerColumn(viewer, SWT.NONE);
         comPortColumn.getColumn().setText("COM port");
         comPortColumn.getColumn().setWidth(COLUMN_WIDTH);
         comPortColumn.getColumn().setAlignment(SWT.CENTER);
         
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
         
        viewer.setContentProvider(new MoxaTableContentProvider());
      	
      	viewer.setInput(model.getMoxaPorts());
      	
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
     	moxaPortColumn.getColumn().pack();

		
		Composite expansionComposite = new Composite(this, SWT.FILL);
		expansionComposite.setLayout(new GridLayout(3, true));
		expandButton = new Button(expansionComposite, SWT.NONE);
		expandButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		expandButton.setText("\u25BC Expand All");
		expandButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moxasTree.getViewer().expandAll();
			}
		});
		
		collapseButton = new Button(expansionComposite, SWT.NONE);
		collapseButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		collapseButton.setText("\u25B2 Collapse All");
		collapseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moxasTree.getViewer().collapseAll();
			}
		});
		
  		refreshButton = new Button(expansionComposite, SWT.NONE);
		refreshButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		refreshButton.setText("\u27F3 Refresh mappings");
		refreshButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				model.refreshMappings();
				viewer.setInput(model.getMoxaPorts());;
			}
		});

	}

}