package uk.ac.stfc.isis.ibex.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Generic selection dialog class.
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class SelectionDialog extends Dialog {

	private String title;
    public Table items;
	
	/**
	 * @param parentShell The shell to open the dialog from.
	 * @param title The title of the dialog box.
	 **/
	protected SelectionDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		createSelection(container);
		
        items.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				okPressed();
			}
		});
		
        items.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
                getButton(IDialogConstants.OK_ID).setEnabled(items.getSelection().length != 0);
			}
			
			@Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
		});
		
		return container;
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		Control control = super.createButtonBar(parent);
		
		//Selection starts as null so disable ok button
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		
		return control;
	}
	
    /**
     * Sets a list of names as items in the selection table.
     * 
     * @param names The names
     */
    protected void setItems(String[] names) {
        this.items.clearAll();
        for (String name : names) {
            TableItem item = new TableItem(this.items, SWT.NONE);
            item.setText(name);
        }
    }

    /**
     * Converts the items in a table into a list of strings.
     * 
     * @param items The table items
     * @return The items as a list of strings
     */
    protected Collection<String> asString(TableItem[] items) {
        Collection<String> converted = new ArrayList<String>();
        for (TableItem item : items) {
            converted.add(item.getText());
        }
        return converted;
    }

    /**
     * Creates and returns a table pre-configured with single column layout.
     * 
     * @param parent The parent composite
     * @param style The style settings
     * @return The table object
     */
    protected Table createTable(Composite parent, int style) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Table table = new Table(tableComposite, style);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        TableColumn singleColumn = new TableColumn(table, SWT.NONE);
        TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableColumnLayout.setColumnData(singleColumn, new ColumnWeightData(1));
        tableComposite.setLayout(tableColumnLayout);

        return table;
    }

    /**
     * Abstract method for creating the selection dialog.
     * 
     * @param container The parent container
     */
	protected abstract void createSelection(Composite container);

}
