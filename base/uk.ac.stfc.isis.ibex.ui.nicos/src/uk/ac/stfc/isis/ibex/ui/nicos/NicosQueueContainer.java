package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
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
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;

/**
 * Nicos queue container.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosQueueContainer {
	
	private final QueueScriptViewModel queueScriptViewModel;
	private final Shell shell;
	private final NicosModel model;
	private final DataBindingContext bindingContext;
	private ScriptStatusViewModel scriptStatusViewModel;
	
	/**
	 * Default constructor.
	 */
	public NicosQueueContainer() {
		model = Nicos.getDefault().getModel();
		queueScriptViewModel = new QueueScriptViewModel(model);
		scriptStatusViewModel = new ScriptStatusViewModel(model);
		
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		bindingContext = new DataBindingContext();
	}
	
	private void createQueuedScriptMenu(ListViewer queuedScriptsViewer) {
        Menu queuedScriptMenu = new Menu(queuedScriptsViewer.getList());
        MenuItem saveScript = new MenuItem(queuedScriptMenu, SWT.PUSH);
        saveScript.setText("Save to file...");
        saveScript.addSelectionListener(new SelectionAdapter() {
        	@Override
            public void widgetSelected(SelectionEvent e) {
        		queueScriptViewModel.saveSelected(shell);
        	}
		});
        queuedScriptsViewer.getList().setMenu(queuedScriptMenu);
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
        
        final ListViewer queuedScriptsViewer = new ListViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
        List list = queuedScriptsViewer.getList();
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        queuedScriptsViewer.setContentProvider(new ObservableListContentProvider());
        queuedScriptsViewer.setLabelProvider(new LabelProvider() {
        	@Override
            public String getText(Object element) {
        		QueuedScript code = (QueuedScript) element;
        		return code.getName();
        	}
        });
        
        queuedScriptsViewer.setInput(BeanProperties.list("queuedScripts").observe(model));
        queuedScriptsViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				QueuedScript selection = (QueuedScript) ((IStructuredSelection) event.getSelection()).getFirstElement();
				ExistingScriptDialog dialog = new ExistingScriptDialog(shell, selection);
				dialog.open();
			}
		});
        
        queuedScriptsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) queuedScriptsViewer.getSelection();
                queueScriptViewModel.setSelectedScript((QueuedScript) selection.getFirstElement());
			}
		});

        createQueuedScriptMenu(queuedScriptsViewer);
        
        Composite moveComposite = new Composite(parent, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        
        Button btnScriptUp =  new Button(moveComposite, SWT.NONE);
		GridData gdBtnScriptUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdBtnScriptUp.widthHint = 25;
		btnScriptUp.setLayoutData(gdBtnScriptUp);
		btnScriptUp.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"));
        btnScriptUp.setToolTipText("Move selected script UP");
		
		Button btnScriptDown =  new Button(moveComposite, SWT.NONE);
		GridData gdBtnScriptDown = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gdBtnScriptDown.widthHint = 25;
		btnScriptDown.setLayoutData(gdBtnScriptDown);
		btnScriptDown.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"));
		btnScriptDown.setToolTipText("Move selected script DOWN");
        
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
        
        btnCreateScript.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
        	QueueScriptDialog dialog = new QueueScriptDialog(shell, queueScriptViewModel);
        	dialog.open();
        }
        });
                
        btnDequeueScript.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                queueScriptViewModel.dequeueScript();
            }
        });
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnDequeueScript), 
        		BeanProperties.value("dequeueButtonEnabled").observe(queueScriptViewModel));

        btnScriptUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                queueScriptViewModel.moveScript(true);
            }
        });
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptUp), 
        		BeanProperties.value("upButtonEnabled").observe(queueScriptViewModel));

        btnScriptDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                queueScriptViewModel.moveScript(false);
            }
        });
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptDown), 
        		BeanProperties.value("downButtonEnabled").observe(queueScriptViewModel));
        new Label(parent, SWT.NONE);
		
	}

}
