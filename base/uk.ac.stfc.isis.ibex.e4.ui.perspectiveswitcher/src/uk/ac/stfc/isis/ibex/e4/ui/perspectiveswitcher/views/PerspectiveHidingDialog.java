package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveInfo;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesVisibleModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectivesTable;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

/**
 * A dialog box for selecting which perspectives to show and hide.
 *
 */
@SuppressWarnings("magicnumber")
public class PerspectiveHidingDialog extends TitleAreaDialog {
    private PerspectivesTable table;
    private PerspectivesVisibleModel model;
    
    @Inject
    private MApplication app;
    
    @Inject
    EPartService partService;
    
    @Inject
    EModelService modelService;
    
    /**
     * Constructor for a dialog box to select which perspectives to show and hide.
     * @param parentShell The shell to launch the dialog in (injected by eclipse)
     */
    @Inject
    public PerspectiveHidingDialog(@Named (IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("Select which perspectives to show");
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));
        
        model = new PerspectivesVisibleModel(app, partService, modelService);
        
        model.addPropertyChangeListener("remoteErrors", event -> {
            setRemoteErrors(model.getRemoteErrors());
        });
        setRemoteErrors(model.getRemoteErrors());
        
        table = new PerspectivesTable(container, SWT.NONE, SWT.NONE);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        table.setRows(model.getPerspectiveInfo());
        
        Button btnCheckButton = new IBEXButtonBuilder(container, SWT.CHECK).text("Use Local Settings").build();
        
        DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnCheckButton),
                BeanProperties.value("useLocal").observe(model));
        
        return container;
    }
    
    private void setRemoteErrors(List<String> remoteErrors) {
        Display.getDefault().asyncExec(() -> {  
            if (model.getRemoteErrors().isEmpty()) {
                setMessage(null, IMessageProvider.NONE);
            } else {                
                String result = remoteErrors.stream()
                        .map(n -> String.valueOf(n))
                        .collect(Collectors.joining(",", "Cannot set remote perspectives: ", ""));
                setMessage(result, IMessageProvider.WARNING);
            }
        });
    }
    
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Select Perspectives to Show");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(400, 600);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "OK", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
    }
    
    @Override
    protected void okPressed() {
        boolean perspectiveSelectedLocal = false;
        boolean perspectiveSelectedRemote = false;
        for (PerspectiveInfo info : model.getPerspectiveInfo()) {
        	perspectiveSelectedLocal = info.getVisibleLocally() || perspectiveSelectedLocal;
        	perspectiveSelectedRemote = info.getVisibleRemotely() || perspectiveSelectedRemote;
        }
    	if (!(perspectiveSelectedLocal && perspectiveSelectedRemote)) {
    		setMessage("Cannot save - select at least one perspective to be shown for both settings", IMessageProvider.ERROR);
    	} else {
    		model.saveState();
    		super.okPressed();
    	}
    }
}
