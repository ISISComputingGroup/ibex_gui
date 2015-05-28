package uk.ac.stfc.isis.ibex.ui.dae.run;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.dae.actions.DaeActions;
import uk.ac.stfc.isis.ibex.model.Action;

public class DaeActionButtonPanel extends Composite {

	public DaeActionButtonPanel(Composite parent, int style, DaeActions actions) {
		super(parent, style);
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 10;
		setLayout(gridLayout);
		
		addActionButton("BEGIN RUN", "play.png", actions.begin);
		addActionButton("END RUN", "stop.png", actions.end);
		addActionButton("PAUSE RUN", "pause.png", actions.pause);
		addActionButton("RESUME RUN", "resume.png", actions.resume);
		addActionButton("ABORT RUN", "abort.png", actions.abort);
		addActionButton("CANCEL ABORT", "undo.png", actions.cancelAbort);
		
		Label middleSpacer = new Label(this, SWT.NONE);
		middleSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		middleSpacer = new Label(this, SWT.NONE);
		middleSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		middleSpacer = new Label(this, SWT.NONE);
		middleSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		
		addActionButton("SAVE RUN", "save.png", actions.save);
		
		Label bottomSpacer = new Label(this, SWT.NONE);
		bottomSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
	}

	private void addActionButton(String text, String imageFileName, final Action action) {
		ActionButton button = new ActionButton(this, SWT.CENTER, action);	
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		button.setText(text);
		button.setImage(imageFromFile(imageFileName));
	}

	private Image imageFromFile(String imageFileName) {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/" + imageFileName);
	}
}
