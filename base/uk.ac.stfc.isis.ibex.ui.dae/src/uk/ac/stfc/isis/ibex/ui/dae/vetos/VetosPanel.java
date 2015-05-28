package uk.ac.stfc.isis.ibex.ui.dae.vetos;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.ui.dae.DaeViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.widgets.MessageBox;

public class VetosPanel extends Composite {

	private MessageBox vetos;
	
	public VetosPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		vetos = new MessageBox(this, SWT.NONE);
		vetos.setTitle("Vetos");
	}
	
	public void setModel(DaeViewModel viewModel) {
		vetos.setModel(viewModel.vetos());
	}
}
