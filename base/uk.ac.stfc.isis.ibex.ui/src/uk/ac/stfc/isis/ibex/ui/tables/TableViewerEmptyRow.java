package uk.ac.stfc.isis.ibex.ui.tables;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Table viewer class which will always append an empty row at the end (non-editable). This is so that
 * copy-pasting after final (editable) line is possible.
 */
public class TableViewerEmptyRow extends TableViewer {
	private boolean addEmptyRow = false;
	private static final Logger LOG = IsisLog.getLogger(TableViewerEmptyRow.class);
	/**
	 * Constructor for creating table using default style bits used by TableViewer.
	 * @param parent the parent control
	 */
    public TableViewerEmptyRow(Composite parent) {
        super(parent);
    }
    	
    /**
     * Constructor for creating table using style bits.
     * @param composite the parent control
     * @param style SWT style bits
     * @param addEmptyRow to add empty row or not
     */
    public TableViewerEmptyRow(Composite composite, int style, boolean addEmptyRow) {
        super(composite, style);
        this.addEmptyRow = addEmptyRow;
    }

    @Override
    public void refresh(Object element) {
    	if (!addEmptyRow) {
    		super.refresh(element);
    	} else {
	        deleteEmptyRow();
	        super.refresh(element);
    	}

    }
    
    @Override
    protected void inputChanged(Object input, Object oldInput) {
    	if (!addEmptyRow) {
    		super.inputChanged(input, oldInput);
    	} else {
            deleteEmptyRow();
            super.inputChanged(input, oldInput);
            @SuppressWarnings("unused")
            TableItem emptyRow = new TableItem(getTable(), SWT.NO_BACKGROUND | SWT.NO_FOCUS);
    	}
    }
    
    /**
     * Deletes empty row which is present in the table.
     */
    public void deleteEmptyRow() {
        try {
            for (TableItem tableItem : getTable().getItems()) {
                if ("".equals(tableItem.getText())) {
                    tableItem.dispose();
                }
            }
        } catch (Exception e) {
        	LOG.error(e);
        }
    }

    @Override
    public void refresh() {
    	if (!addEmptyRow) {
    		super.refresh();
    	} else {
	        deleteEmptyRow();
	        super.refresh();
	        @SuppressWarnings("unused")
	        TableItem tableItem = new TableItem(getTable(), SWT.NO_BACKGROUND | SWT.NO_FOCUS);
    	}
    
    }

}
