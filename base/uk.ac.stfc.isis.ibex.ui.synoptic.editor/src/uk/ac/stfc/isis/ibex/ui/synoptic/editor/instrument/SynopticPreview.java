package uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.InstrumentSynoptic;

public class SynopticPreview extends Dialog {
	private final SynopticModel model;

	public SynopticPreview(Shell parent,
			InstrumentDescription instrumentDescription) {
		super(parent);
		model = Synoptic.getInstance().getBlankModel();
		model.setInstrumentFromDescription(instrumentDescription);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		InstrumentSynoptic instrument = new InstrumentSynoptic(container,
				SWT.NONE);
		instrument.setComponents(model.instrument().components());

		return instrument;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Synoptic Preview");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(1200, 500);
	}
}
