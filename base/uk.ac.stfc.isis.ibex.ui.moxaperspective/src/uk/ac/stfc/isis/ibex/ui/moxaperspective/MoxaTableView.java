package uk.ac.stfc.isis.ibex.ui.moxaperspective;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;

public class MoxaTableView extends ViewPart {
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.moxaperspective.moxatableview";
    
    public MoxaTableView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        Browser browser = new Browser(parent, SWT.NONE);
    }

    @Override
    public void setFocus() {
    }
}
