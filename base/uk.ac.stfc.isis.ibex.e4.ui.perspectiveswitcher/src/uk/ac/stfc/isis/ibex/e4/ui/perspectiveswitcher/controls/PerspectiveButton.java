package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class PerspectiveButton extends Composite {
	
    private static final Font LABEL_FONT = SWTResourceManager.getFont("Arial", 16, SWT.NONE);
    private final Button button;

	public PerspectiveButton(Composite parent, MPerspective perspective, PerspectivesProvider perspectivesProvider) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		button = new Button(this, SWT.LEFT);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		button.setFont(LABEL_FONT);
		button.setText(perspective.getLabel());
		button.setToolTipText(perspective.getTooltip());
		button.setImage(ResourceManager.getPluginImageFromUri(perspective.getIconURI()));
		
		button.setSelection(perspectivesProvider.isSelected(perspective));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				perspectivesProvider.getPartService().switchPerspective(perspective);
			}
		});
	}
	
	public void setSelection(boolean isSelected) {
		button.setSelection(isSelected);
	}

}
