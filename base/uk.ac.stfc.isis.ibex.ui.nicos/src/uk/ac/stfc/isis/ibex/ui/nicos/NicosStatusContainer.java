package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/**
 * The nicos status container.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosStatusContainer {

	private Label lblCurrentError;
	private final DataBindingContext bindingContext = new DataBindingContext();
	private final NicosModel model = Nicos.getDefault().getModel();

	private static final String HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Script-Server.rest";
	private static final String DESCRIPTION = "Script Server";

	/**
	 * Creates the view.
	 * 
	 * @param parent injected by eclipse
	 */
	@PostConstruct
	public void createStatusContainer(Composite parent) {
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		parent.setLayout(new GridLayout(1, false));

		Composite nicosStatus = new Composite(parent, SWT.NONE);
		nicosStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		nicosStatus.setLayout(new GridLayout(2, false));

		Button helpButton = new IBEXButtonBuilder(nicosStatus, SWT.PUSH).setHelpButton(true).setLink(HELP_LINK)
				.setDescription(DESCRIPTION).build();

		Composite textComposite = new Composite(nicosStatus, SWT.NONE);
		textComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		textComposite.setLayout(new GridLayout(1, false));

		lblCurrentError = new Label(textComposite, SWT.NONE);
		lblCurrentError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblCurrentError.setText("Server status: ");

		Label errorIndicator = new Label(textComposite, SWT.NONE);
		errorIndicator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bindingContext.bindValue(WidgetProperties.text().observe(errorIndicator),
				BeanProperties.value("error").observe(model));
	}
}
