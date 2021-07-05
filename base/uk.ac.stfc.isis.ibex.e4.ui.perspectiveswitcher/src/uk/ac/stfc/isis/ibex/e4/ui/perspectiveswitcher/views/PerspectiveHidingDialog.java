package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.GridLayout;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesVisibleModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectivesTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.e4.ui.services.IServiceConstants;

public class PerspectiveHidingDialog extends TitleAreaDialog {
    private PerspectivesTable table;
    private PerspectivesVisibleModel model;
    
    @Inject
    private MApplication app;
    
    @Inject
    EPartService partService;
    
    @Inject
    EModelService modelService;
    
    @Inject
    public PerspectiveHidingDialog(@Named (IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("Select which perspectives to show");
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));
        
        table = new PerspectivesTable(container, SWT.NONE, SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        model = new PerspectivesVisibleModel(app, partService, modelService);
        table.setRows(model.getPerspectiveInfo());
        
        Button btnCheckButton = new Button(container, SWT.CHECK);
        btnCheckButton.setText("Check Button");
        
        return container;
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
        model.saveState();
        super.okPressed();
    }
}
