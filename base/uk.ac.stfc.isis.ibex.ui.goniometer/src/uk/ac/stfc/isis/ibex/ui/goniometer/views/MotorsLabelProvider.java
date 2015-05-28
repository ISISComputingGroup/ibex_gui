package uk.ac.stfc.isis.ibex.ui.goniometer.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;
import uk.ac.stfc.isis.ibex.ui.motor.views.MotorOPIView;

public class MotorsLabelProvider extends ColumnLabelProvider {
	
	private Map<ViewerCell, Button> buttons = new HashMap<>();
	private Map<ViewerCell, TableEditor> editors = new HashMap<>();
		
	@Override
    public void update(ViewerCell cell) {

		if (buttons.containsKey(cell)) {
			return;
		}
		
        addButton(cell);                    
        addEditor(cell);
    }

	private void addEditor(ViewerCell cell) {		
		Button button = buttons.get(cell);
		TableItem item = (TableItem) cell.getItem();
        TableEditor editor = new TableEditor(item.getParent());
        editor.grabHorizontal  = true;
        editor.grabVertical = true;
        
        editor.setEditor(button, item, cell.getColumnIndex());
        
        editor.layout();
        editors.put(cell, editor);
	}

	private void addButton(ViewerCell cell) {		
		Button button = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);  
        button.setText("Motor...");
        button.setToolTipText("Show a more detailed view of the motor for this axis");
        button.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/cog.png"));
        
        buttons.put(cell, button);
        
        final MotorSetpoint row = (MotorSetpoint) cell.getElement();

        button.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent event) {
        		try {
            		// Display OPI motor view
    				IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(MotorOPIView.ID, row.getName(), IWorkbenchPage.VIEW_ACTIVATE);
    				
    				MotorOPIView motorView = (MotorOPIView) view;
    				motorView.setOPITitle(row.getName());
    				motorView.setMotorName(row.getName() + ":MTR");
    				motorView.initialiseOPI();
    				
				} catch (PartInitException e) {
					e.printStackTrace();
				}
        	}
        });
	}
    
	@Override
	public void dispose() {
		super.dispose();
		for (Button button : buttons.values()) {
			button.dispose();
		}
		
		for (TableEditor editor : editors.values()) {
			editor.dispose();
		}
	}
}
