package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ActionsTableView extends Composite {
	
	ScriptGeneratorViewModel viewModel;
	Table table;
	
	public ActionsTableView(ScriptGeneratorViewModel viewModel, Composite parent, int style) {
		super(parent, style);
		this.viewModel = viewModel;
		GridLayout layout = new GridLayout(1, false);
        parent.setLayout(layout);
		createViewer(parent);
	}

	private TableViewer viewer;
	
	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL 
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        ActionProvider provider = new ActionProvider(viewer);
        viewModel.getScriptGeneratorTable().addPropertyChangeListener("actions", actions -> {
        	provider.updateActions((List<ScriptGeneratorAction>) actions.getNewValue());
        });
        viewer.setContentProvider(provider);
        viewer.setUseHashlookup(true);

        viewer.setInput(viewModel.getActions());
        
        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
	}
	
	private void createColumns(Composite parent, TableViewer viewer) {
        TableViewerColumn col = createTableViewerColumn("Hello", 100, 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return "Hello 1";
            }
        });
	}
	
	public TableViewer getViewer() {
        return viewer;
    }
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
                SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;

    }
	
	public void refresh() {
		viewer.refresh();
	}

}
