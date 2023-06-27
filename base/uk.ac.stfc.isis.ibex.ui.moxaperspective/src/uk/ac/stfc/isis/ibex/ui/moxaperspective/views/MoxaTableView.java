package uk.ac.stfc.isis.ibex.ui.moxaperspective.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;

public class MoxaTableView  {

    public void createPartControl(Composite parent) {
    	parent.setLayout(new GridLayout(10, true));

		ExpandBar expandBar = new ExpandBar(parent, SWT.FILL | SWT.V_SCROLL);
		expandBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

		ExpandItem xpndtmMoxas = new ExpandItem(expandBar, SWT.NONE);
		xpndtmMoxas.setExpanded(true);
		xpndtmMoxas.setText("Moxas");

		xpndtmMoxas.setHeight(130);
		expandBar.layout();
    }

}
