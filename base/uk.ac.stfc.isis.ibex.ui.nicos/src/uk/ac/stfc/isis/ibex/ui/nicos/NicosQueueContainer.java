package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.EditScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.QueueScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * Nicos queue container.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosQueueContainer {
	
	private final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	private final NicosModel model = Nicos.getDefault().getModel();
	private final DataBindingContext bindingContext = new DataBindingContext();
	private ScriptStatusViewModel scriptStatusViewModel;
	private final QueueScriptViewModel queueScriptViewModel;
	
	/**
	 * Default constructor.
	 */
	public NicosQueueContainer() {
		queueScriptViewModel = new QueueScriptViewModel(model);
		scriptStatusViewModel = new ScriptStatusViewModel(model);
	}
	
	private void createQueuedScriptMenu(Viewer queuedScriptsViewer) {
        Menu queuedScriptMenu = new Menu(queuedScriptsViewer.getControl());
        MenuItem saveScript = new MenuItem(queuedScriptMenu, SWT.PUSH);
        saveScript.setText("Save to file...");
        saveScript.addSelectionListener(new SelectionAdapter() {
        	@Override
            public void widgetSelected(SelectionEvent e) {
        		queueScriptViewModel.saveSelected(shell);
        	}
		});
        queuedScriptsViewer.getControl().setMenu(queuedScriptMenu);
	}
	
	private Button createMoveScriptButton(Composite parent, String icon, String direction) {
        Button moveButton =  new Button(parent, SWT.NONE);
		GridData gdBtnScriptUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdBtnScriptUp.widthHint = 25;
		moveButton.setLayoutData(gdBtnScriptUp);
		moveButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/" + icon));
        moveButton.setToolTipText("Move selected script " + direction);
        return moveButton;
	}
	
	/**
	 * Create the view.
	 * @param parent the parent (injected by eclipse)
	 */
	@PostConstruct
	@SuppressWarnings("unchecked")
	public void createQueueContainer(Composite parent) {
		GridData gdQueueContainer = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdQueueContainer.heightHint = 300;
		parent.setLayoutData(gdQueueContainer);
		parent.setLayout(new GridLayout(2, false));
        
        Label lblQueuedScriptsSubLabel = new Label(parent, SWT.NONE);
        lblQueuedScriptsSubLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
        lblQueuedScriptsSubLabel.setText("(double-click name to see contents)");
        lblQueuedScriptsSubLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
        
        DataboundTable<QueuedScript> queuedScriptsViewer = new DataboundTable<QueuedScript>(parent, SWT.NONE, SWT.V_SCROLL) {
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
		queuedScriptsViewer.initialise();
		queuedScriptsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		queuedScriptsViewer.table().setHeaderVisible(false);
		queuedScriptsViewer.table().setLinesVisible(false);
		
        queuedScriptsViewer.viewer().setInput(BeanProperties.list("queuedScripts").observe(model));

        queuedScriptsViewer.viewer().addDoubleClickListener(e ->
				(new EditScriptDialog(shell, queueScriptViewModel)).open());
        
        queuedScriptsViewer.addSelectionChangedListener(e ->
                queueScriptViewModel.setSelectedScript(queuedScriptsViewer.firstSelectedRow()));

        createQueuedScriptMenu(queuedScriptsViewer.viewer());
        
        Composite moveComposite = new Composite(parent, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        
        Button btnScriptUp = createMoveScriptButton(moveComposite, "move_up.png", "up");
        Button btnScriptDown = createMoveScriptButton(moveComposite, "move_down.png", "down");
        
        Label lblQueueStatusReadback = new Label(parent, SWT.NONE);
        lblQueueStatusReadback.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        
        bindingContext.bindValue(WidgetProperties.text().observe(lblQueueStatusReadback),
                BeanProperties.value("statusReadback").observe(scriptStatusViewModel));
        
        Composite scriptSendGrp = new Composite(parent, SWT.NONE);
        scriptSendGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout ssgLayout = new GridLayout(2, false);
        ssgLayout.marginHeight = 10;
        ssgLayout.marginWidth = 10;
        scriptSendGrp.setLayout(ssgLayout);
		        
        final Button btnCreateScript = new Button(scriptSendGrp, SWT.NONE);
        btnCreateScript.setText("Create Script and Add to Queue");
        btnCreateScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        final Button btnDequeueScript = new Button(scriptSendGrp, SWT.NONE);
        btnDequeueScript.setText("Dequeue Selected Script");
        btnDequeueScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        btnCreateScript.addListener(SWT.Selection, e -> (new QueueScriptDialog(shell, queueScriptViewModel)).open());
        btnDequeueScript.addListener(SWT.Selection, e -> queueScriptViewModel.dequeueScript());
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnDequeueScript), 
        		BeanProperties.value("dequeueButtonEnabled").observe(queueScriptViewModel));

        btnScriptUp.addListener(SWT.Selection, e -> queueScriptViewModel.moveScript(true));
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptUp), 
        		BeanProperties.value("upButtonEnabled").observe(queueScriptViewModel));

        btnScriptDown.addListener(SWT.Selection, e -> queueScriptViewModel.moveScript(false));
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptDown), 
        		BeanProperties.value("downButtonEnabled").observe(queueScriptViewModel));
	}

}
