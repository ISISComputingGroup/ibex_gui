package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.ExistingScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.QueueScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.models.LoadScriptAction;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * Nicos container for available scripts to add to the queue.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosAvailableScriptsContainer {
	
	private static final  Font SUBTITLE = SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD);
	private final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	private final NicosModel model = Nicos.getDefault().getModel();
	private final DataBindingContext bindingContext = new DataBindingContext();
	private final QueueScriptViewModel queueScriptViewModel;
	
	/**
	 * Default constructor.
	 */
	public NicosAvailableScriptsContainer() {
		queueScriptViewModel = new QueueScriptViewModel(model);
	}
	
	/**
	 * Create the view.
	 * @param parent the parent (injected by eclipse)
	 */
	@PostConstruct
	@SuppressWarnings("unchecked")
	public void createAvailableScriptsContainer(Composite parent) {
		GridData gdAvailableScriptsContainer = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdAvailableScriptsContainer.heightHint = 300;
		parent.setLayoutData(gdAvailableScriptsContainer);
		GridLayout glAvailableScriptsContainer = new GridLayout(2, false);
		glAvailableScriptsContainer.horizontalSpacing = 20;
		parent.setLayout(glAvailableScriptsContainer);

        Label lblLoadedScriptsSubLabel = new Label(parent, SWT.NONE);
        lblLoadedScriptsSubLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
        lblLoadedScriptsSubLabel.setText("(double-click name to see contents)");
        lblLoadedScriptsSubLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
        
        DataboundTable<QueuedScript> loadedScriptsViewer = new DataboundTable<QueuedScript>(parent, SWT.NONE, SWT.V_SCROLL) {
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
		loadedScriptsViewer.initialise();
		loadedScriptsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		loadedScriptsViewer.table().setHeaderVisible(false);
		loadedScriptsViewer.table().setLinesVisible(false);
		
        loadedScriptsViewer.viewer().setInput(BeanProperties.list("queuedScripts").observe(model));

        loadedScriptsViewer.viewer().addDoubleClickListener(e ->
				(new ExistingScriptDialog(shell, loadedScriptsViewer.firstSelectedRow())).open());
        
        loadedScriptsViewer.addSelectionChangedListener(e ->
                queueScriptViewModel.setSelectedScript(loadedScriptsViewer.firstSelectedRow()));

        final Button btnLoadScript = new Button(parent, SWT.NONE);
        btnLoadScript.setText("Load Script");
        btnLoadScript.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.nicos", "icons/open_folder.png"));
        btnLoadScript.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        
        btnLoadScript.addListener(SWT.Selection, e -> (new LoadScriptAction(parent.getShell(), new QueuedScript())).execute());
        
        final Button btnQueueSelectedScript = new Button(parent, SWT.NONE);
        btnQueueSelectedScript.setText("Queue selected Script");
        btnQueueSelectedScript.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        btnQueueSelectedScript.addListener(SWT.Selection, e -> queueScriptViewModel.queueScript());

        Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        GridData gdSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gdSeparator.heightHint = 30;
        separator.setLayoutData(gdSeparator);
		
		Label lblNewScript = new Label(parent, SWT.NONE);
		lblNewScript.setFont(SUBTITLE);
		lblNewScript.setText("New Script:");
		lblNewScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
        final Button btnCreateScript = new Button(parent, SWT.NONE);
        btnCreateScript.setText("Create and Queue Script");
        btnCreateScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        btnCreateScript.addListener(SWT.Selection, e -> (new QueueScriptDialog(shell, queueScriptViewModel)).open());
	}

}
