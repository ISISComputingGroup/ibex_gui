package uk.ac.stfc.isis.ibex.ui.motor.views;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.motor.Motors;

/** Configurable settings for the table of motors. */
public class TableOfMotorsSettingsView {
	Text advancedMinimalMotorView;
	Button btnAdvancedMinimalMotorView ;
	
    private TableOfMotorsSettingsViewModel tableOfMotorsSettingsViewModel = new TableOfMotorsSettingsViewModel(Motors.getInstance().getMotorSettingsModel());

	private DataBindingContext bindingContext = new DataBindingContext();
	
    /**
     * Constructor. Creates a new instance of the TableOfMotorsSettingsView object.
     * 
     * @param parent
     *            the parent of this element
     * @param tableOfMotorsSettingsViewModel
     *            the view model to be used by this view.
     */
	@PostConstruct
	public void createPartControl(Composite parent) {
		
		GridLayout gridLayout = new GridLayout(1, false);
		parent.setLayout(gridLayout);
	
		/** The advance motor view provides more status and read back information for a minimal motor view **/
		btnAdvancedMinimalMotorView = new Button(parent, SWT.CHECK);
		btnAdvancedMinimalMotorView.setText("Enable advance table of motors");

		bind();
	}
	
    /**
     * Gets the TableOfMotorsSettingsViewModel used by the view.
     * 
     * @return the motor view model used by the cell.
     */
    public TableOfMotorsSettingsViewModel getViewModel() {
        return tableOfMotorsSettingsViewModel;
    }
	
    /**
     * Binds the view model to the view.
     */
    private void bind() {
    	bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnAdvancedMinimalMotorView),
                BeanProperties.value("advancedMinimalMotorView").observe(tableOfMotorsSettingsViewModel));
    }
}
