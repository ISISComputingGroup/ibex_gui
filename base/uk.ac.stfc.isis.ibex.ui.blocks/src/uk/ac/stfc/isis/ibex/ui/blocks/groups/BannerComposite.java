package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.wb.swt.SWTResourceManager;

public class BannerComposite extends Composite {

	public BannerComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CLabel lblText = new CLabel(this, SWT.CENTER);
		lblText.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		// Leave text blank
		lblText.setText("");
	}

}
