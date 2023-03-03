package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.ui.nicos.models.OutputLogViewModel;

/**
 * Nicos script output view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosOutputContainer {
	
	private final DataBindingContext bindingContext = new DataBindingContext();
	private OutputLogViewModel outputLogViewModel = new OutputLogViewModel(Nicos.getDefault().getModel());
	
	/**
	 * Creates the view.
	 * @param parent the parent injected by eclipse
	 */
	@PostConstruct
	public void createOutputContainer(Composite parent) {
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		parent.setLayout(new GridLayout(1, false));
        
        final StyledText txtOutput = new StyledText(parent, SWT.V_SCROLL | SWT.BORDER);
        txtOutput.setEditable(false);
        txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        bindingContext.bindValue(WidgetProperties.text().observe(txtOutput),
                BeanProperties.value("log").observe(outputLogViewModel));
        
        txtOutput.addListener(SWT.Modify, e -> txtOutput.setTopIndex(txtOutput.getLineCount() - 1));
    }
}
