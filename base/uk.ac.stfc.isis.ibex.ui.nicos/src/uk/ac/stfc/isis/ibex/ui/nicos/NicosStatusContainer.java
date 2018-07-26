package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;

/**
 * The nicos status container.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosStatusContainer {
	
	private Label lblCurrentError;
	private final DataBindingContext bindingContext;
	private final NicosModel model;
	
	/**
	 * Constructor.
	 */
	public NicosStatusContainer() {
		bindingContext = new DataBindingContext();
		model = Nicos.getDefault().getModel();
	}
	
	/**
	 * Creates the view.
	 * @param parent injected by eclipse
	 */
	@PostConstruct
	public void createStatusContainer(Composite parent) {
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		parent.setLayout(new GridLayout(1, false));
        
        Composite nicosStatus = new Composite(parent, SWT.NONE);
        nicosStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        nicosStatus.setLayout(new GridLayout(2, false));
        
        lblCurrentError = new Label(nicosStatus, SWT.NONE);
        lblCurrentError.setText("NICOS status: ");
        
        Label errorIndicator = new Label(nicosStatus, SWT.NONE);
        errorIndicator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(errorIndicator),
                BeanProperties.value("error").observe(model));
    }
}
