package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class MoxaInfoPanel  extends Composite {
	private final Button expandButton;
	private final Button  collapseButton;

	private static final int COLUMN_WIDTH = 500;
	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 * @param style  the SWT style
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
      	
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 21, 1));
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
     	
		
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
    		}
    	});

	}

}